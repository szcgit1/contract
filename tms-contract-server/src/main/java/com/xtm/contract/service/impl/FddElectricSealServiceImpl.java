package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.company.constant.CompanyConstant;
import com.xtm.company.feign.CompanyFeign;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.ContractConstant;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.StatusCode;
import com.xtm.contract.feign.MotorcadeServiceFeign;
import com.xtm.contract.feign.TmsCompanyService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.mapper.ContractMapper;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.FddCompanyStatusVo;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.contract.service.ContractFddSignService;
import com.xtm.contract.service.FddElectricSealService;
import com.xtm.contract.service.FddFeignService;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.motorcade.model.vo.DriverVo;
import com.xtm.thirdparty.auth.feign.FddElectricSealFeign;
import com.xtm.thirdparty.auth.feign.TmsFddElectricSealFeign;
import com.xtm.thirdparty.auth.model.param.FddUserBO;
import com.xtm.thirdparty.auth.model.param.QueryFddSealListParam;
import com.xtm.thirdparty.auth.model.req.DriverInfoReq;
import com.xtm.thirdparty.auth.model.resp.AuthAutoSignResponse;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.GetVerifyUrlResponse;
import com.xtm.thirdparty.auth.model.resp.QuerySignResultResponse;
import com.xtm.thirdparty.auth.model.vo.FddVerifyUrlInfoVo;
import com.xtm.user.feign.UserFeign;
import com.xtm.user.model.vo.UserInfoVo;
import com.xtm.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  13:44
 *@Description: 法大大电子签章表接口实现类
 *@title: FddElectricSealServiceImpl
 */
@Slf4j
@Service(value = "FddElectricSealService")
public class FddElectricSealServiceImpl implements FddElectricSealService {

    @Autowired
    ContractFddSignService contractFddSignService;
    @Autowired
    private TmsUserService userService;
    @Autowired
    private EqbHelper eqbHelper;
    @Autowired
    private CompanyFeign companyFeign;
    @Autowired
    private NacosValueConfig nacosValueConfig;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private MotorcadeServiceFeign motorcadeService;

    @Resource
    private FddFeignService fddFeignService;

    @Resource
    private TmsFddElectricSealFeign tmsFddElectricSealFeign;

    @Resource
    private FddElectricSealFeign fddElectricSealFeign;

    @Resource
    private TmsCompanyService companyService;

    /**
     * 根据当前账号查询法大大认证授权状态（本地数据库，不调用法大大接口）
     * @param userId
     * @return
     */
    @Override
    public FddElectricSealResp queryFddVerifyAuthStatusByOpenId(String userId) {

        UserInfoVo userBO = userService.getUserById(userId);
        String userName = userBO.getName();
        String openId = userBO.getIdcardNo(); // 身份证

        FddElectricSealResp fddElectricSeal = null;
        if(StrUtil.isNotEmpty(openId)){
            // 根据openId查询签章信息
            fddElectricSeal = fddFeignService.getFddElectricSealByOpenId(openId);
        }

        // 法大大账户-未注册
        if(fddElectricSeal == null){
            // 保存注册信息
            FddElectricSealResp fddIntert = new FddElectricSealResp();
            fddIntert.setId(UUID.randomUUID().toString().replace("-", ""));
            fddIntert.setUserName(userName);
            fddIntert.setOpenId(openId);
            fddIntert.setVerifyStatus(0); // 未认证
            fddIntert.setAuthAutoSignStatus(0); // 未授权
            return fddIntert;
        } else { // 法大大账户-已注册
            return fddElectricSeal;
        }

    }

    /**
     * 根据当前账号查询法大大认证授权状态（本地数据库，不调用法大大接口）
     * @param openIds
     * @return
     */
    @Override
    public List<FddElectricSealVo> queryFddVerifyAuthStatusByOpenIds(List<String> openIds) {
        log.info("queryFddVerifyAuthStatusByOpenIds,{}", openIds);
        List<UserInfoVo> userList = userService.getUserByIds(openIds);
        List<FddElectricSealVo> fddElectricSealList = new ArrayList<>();
        List<FddElectricSealVo> fddElectricSealCompanyList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userList)){
            List<String> userOpenIds = userList.stream().map(UserInfoVo::getIdcardNo).collect(Collectors.toList());
            List<FddElectricSealResp> fddList = fddFeignService.getFddElectricSealByOpenIds(userOpenIds, null);
            for (UserInfoVo userInfoVo : userList) {
                FddElectricSealVo fddElectricSealVo = new FddElectricSealVo();
                fddElectricSealVo.setUserId(userInfoVo.getId());
                fddElectricSealVo.setUserName(userInfoVo.getName());
                String idcardNo = userInfoVo.getIdcardNo();
                if(StringUtils.isNotBlank(idcardNo)){
                    for (FddElectricSealResp fddElectricSealResp : fddList) {
                        if (idcardNo.equals(fddElectricSealResp.getOpenId())) {
                            fddElectricSealVo.setVerifyStatus(fddElectricSealResp.getVerifyStatus());
                            fddElectricSealVo.setAuthAutoSignStatus(fddElectricSealResp.getAuthAutoSignStatus());
                            break;
                        }
                    }
                }
                fddElectricSealList.add(fddElectricSealVo);
            }
            List<String> companyIds = userList.stream().map(UserInfoVo::getCompanyId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(companyIds)){
                List<CompanyVo> companys = companyService.getCompanyByIds(companyIds);
                if(CollectionUtil.isNotEmpty(companys)){
                    List<String> companyOpenIds = companys.stream().map(CompanyVo::getUnifiedSocialCreditIdentifier).collect(Collectors.toList());
                    List<FddElectricSealResp> fddCompanyList = fddFeignService.getFddElectricSealByOpenIds(companyOpenIds, null);
                    for (CompanyVo companyVo : companys) {
                        FddElectricSealVo fddElectricSealVo = new FddElectricSealVo();
                        fddElectricSealVo.setUserId(companyVo.getId());
                        fddElectricSealVo.setUserName(companyVo.getName());
                        String unifiedSocialCreditIdentifier = companyVo.getUnifiedSocialCreditIdentifier();
                        if(StringUtils.isNotBlank(unifiedSocialCreditIdentifier)){
                            for (FddElectricSealResp fddElectricSealResp : fddCompanyList) {
                                if (unifiedSocialCreditIdentifier.equals(fddElectricSealResp.getOpenId())) {
                                    fddElectricSealVo.setVerifyStatus(fddElectricSealResp.getVerifyStatus());
                                    fddElectricSealVo.setAuthAutoSignStatus( fddElectricSealResp.getAuthAutoSignStatus());
                                    break;
                                }
                            }
                        }
                        fddElectricSealList.add(fddElectricSealVo);
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(fddElectricSealList)) {
            Map<String, FddElectricSealVo> collect = new HashMap<>();
            if (CollUtil.isNotEmpty(fddElectricSealCompanyList)) {
                collect = fddElectricSealCompanyList.stream().collect(Collectors.toMap(FddElectricSealVo::getUserId, fddElectricSealVo -> fddElectricSealVo, (old, now) -> now));
            }
            for (FddElectricSealVo fddElectricSealVo : fddElectricSealList) {
                if (collect.get(fddElectricSealVo.getUserId()) != null) {
                    if (fddElectricSealVo.getVerifyStatus() == null) {
                        fddElectricSealVo.setVerifyStatus(collect.get(fddElectricSealVo.getUserId()).getVerifyStatus());
                    }
                    if (fddElectricSealVo.getAuthAutoSignStatus() == null) {
                        fddElectricSealVo.setAuthAutoSignStatus(collect.get(fddElectricSealVo.getUserId()).getAuthAutoSignStatus());
                    }
                    if (fddElectricSealVo.getAuthAutoSignContractId() == null) {
                        fddElectricSealVo.setAuthAutoSignContractId(collect.get(fddElectricSealVo.getUserId()).getAuthAutoSignContractId());
                    }
                }
            }
        }else {
            fddElectricSealList = fddElectricSealCompanyList;
        }
        log.info("queryFddVerifyAuthStatusByOpenIds,fddElectricSealList:{}", fddElectricSealList);
        return fddElectricSealList;
    }

    /**
     * 根据当前账号查询法大大签章信息
     * @param sessionInfo
     * @return
     */
    @Override
    public FddElectricSealResp selectFddSealByOpenId(SysUser sessionInfo) {
        LocalDateTime nowDateTime = LocalDateTime.now();

        UserInfoVo userBO = userService.getUserById(sessionInfo.getId());
        CompanyVo companyVo=companyFeign.findCompanyById(userBO.getCompanyId()).getData();
        String userId = userBO.getId();
        String userName = userBO.getName();
        String openId = userBO.getIdcardNo(); // 身份证
        if(StrUtil.isEmpty(openId)){
            log.info("wanglei-根据当前账号查询法大大签章信息-身份证号码为空");
            FddElectricSealResp fddIntert = new FddElectricSealResp();
            fddIntert.setId(UUID.randomUUID().toString().replace("-", ""));
            fddIntert.setUserName(userName);
            fddIntert.setOpenId(openId);
            fddIntert.setVerifyStatus(0); // 未认证
            fddIntert.setAuthAutoSignStatus(0); // 未授权
            fddIntert.setCreateTime(nowDateTime);
            fddIntert.setUpdateTime(nowDateTime);
            return fddIntert;
        }

        Result<UserInfoVo> driverCaptain = userFeign.getDriverCaptainByUserId(userId);
        boolean isDriverCaptain = (driverCaptain.isSuccess() && driverCaptain.getData() != null);
        // 根据openId查询签章信息
        FddUserBO fddUserBO= new FddUserBO();
        fddUserBO.setOpenId(openId);
        fddUserBO.setUserName(userName);
        // 账号类型 1个人 2企业
        Integer accountType =null;
        // 司机/车辆经营人个人/车队长进行个人实名认证;  托运人、物流商、代理商进行企业实名认证
        if (companyVo.getOrganTypeIds().contains(CompanyConstant.OrganTypeEnum.DRIVER.getType())
                || companyVo.getOrganTypeIds().contains(CompanyConstant.OrganTypeEnum.VEHICLE_OPERATOR_PERSONAL.getType())
                || isDriverCaptain) {
            accountType = 1;
        } else {
            accountType = 2;
        }
        fddUserBO.setAccountType(accountType);
        log.info("查询法大大签章信息,参数:{}", JsonUtils.toJSONString(fddUserBO));
        Result<FddElectricSealResp> fddResult = fddElectricSealFeign.selectFddSealByOpenId(fddUserBO);
        log.info("查询法大大签章信息结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if (fddResult.isSuccess()) {
            return fddResult.getData();
        }
        throw new BusinessException("查询法大大签章信息失败:"+fddResult.getMsg());
    }

    /**
     * 根据当前账号获取法大大认证信息
     * @param sessionInfo
     * @return
     */
    @Override
    public GetVerifyUrlResponse getVerifyUrlByOpenId(SysUser sessionInfo) {
        UserInfoVo userBO = userService.getUserById(sessionInfo.getId());
        String idcardNo = userBO.getIdcardNo(); // 身份证号
        String userName = userBO.getName(); // 用户姓名
        String openId = userBO.getIdcardNo(); // 身份证
        // 身份证为空，前端提示用户填写身份证号码
        if(StrUtil.isEmpty(openId)){
            GetVerifyUrlResponse getVerifyUrlResponse = new GetVerifyUrlResponse();
            getVerifyUrlResponse.setIdcardNo("0");
            return getVerifyUrlResponse;
        }
        FddUserBO fddUserBO = new FddUserBO();
        fddUserBO.setOpenId(openId);
        fddUserBO.setUserName(userName);
        fddUserBO.setIdcardNo(idcardNo);
        Result<FddVerifyUrlInfoVo> fddResult = tmsFddElectricSealFeign.getVerifyUrl(fddUserBO);
        GetVerifyUrlResponse urlResponse = new GetVerifyUrlResponse();
        if(fddResult.isSuccess()){
            FddVerifyUrlInfoVo resultData = fddResult.getData();
            urlResponse.setUrl(resultData.getVerifyUrl());
            urlResponse.setTransactionNo(resultData.getVerifyStatus());
            return urlResponse;
        }
        throw new BusinessException(fddResult.getMsg());
    }

    /**
     * 根据当前账号获取法大大授权地址
     * @param sessionInfo
     * @return
     */
    @Override
    public AuthAutoSignResponse beforeAuthsignByOpenId(SysUser sessionInfo) {
        UserInfoVo userBO = userService.getUserById(sessionInfo.getId());
        String openId = userBO.getIdcardNo(); // 身份证
        FddUserBO fddUserBO = new FddUserBO();
        fddUserBO.setOpenId(openId);
        fddUserBO.setFirstRegister(true);
        Result<AuthAutoSignResponse> fddResult = fddElectricSealFeign.beforeAuthsignByOpenId(fddUserBO);
        if (fddResult.isSuccess()){
            AuthAutoSignResponse response = fddResult.getData();
            if (response != null){
                return response;
            }
            throw new BusinessException("未查询到该用户, 请先注册法大大账号");
        }
        throw new BusinessException(fddResult.getMsg());
    }

    /**
     * 根据当前驾驶员id查询法大大认证授权状态
     * @param driverId
     * @return
     */
    @Override
    public FddElectricSealResp queryFddVerifyAuthStatusByDriverId(String driverId) {
        // 查询司机的userId
        DriverVo driverBo = motorcadeService.getDriverById(driverId);
        // 根据userId查询认证、授权状态
        String userId = driverBo.getUserId();
        FddElectricSealResp fddElectricSeal = queryFddVerifyAuthStatusByOpenId(userId);
        return fddElectricSeal;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void updateAutoSignStatusByWeb(String idCard) {
        DriverInfoReq req = new DriverInfoReq();
        req.setIdCard(idCard);
        log.info(">>> web数据调整-更新法大大自动授权状态，司机身份证：{}",  idCard);
        Result<?> fddResult = tmsFddElectricSealFeign.updateAutoSignStatusByWeb(req);
        log.info(">>> web数据调整-更新法大大自动授权状态v，结果：{}",fddResult);
        if(!fddResult.isSuccess()){
            log.error("更新法大大自动授权状态失败，code:[{}]，msg:[{}]",fddResult.getCode(),fddResult.getMsg());
            throw new BusinessException(fddResult.getMsg());
        }
    }

    /**
     * 查询所有服务商以及认证信息
     * @return
     */
    @Override
    public List<CompanyFddVO> queryLogisticsCompanyAndFddVerifyAuthStatus() {
        String companyAgentId = LoginUserContextHolder.getUser().getCompanyAgentId();
        //查询所有物流商和管理员信息
        List<CompanyVo> companys = companyService.getLogisticsCompanys(companyAgentId);
        List<String> userIds = companys.stream().map(CompanyVo::getCompanyAdmin).filter(Objects::nonNull).collect(Collectors.toList());
        List<UserInfoVo> userList = userService.getUserByIds(userIds);
        //转换为map
        Map<String, UserInfoVo> userMap = userList.stream().collect(Collectors.toMap(UserInfoVo::getId, x -> x));
        List<String> openIds = new ArrayList<>();
        List<CompanyFddVO> companyFddVOList = new ArrayList<>();
        for (CompanyVo companyFddVO : companys) {
            openIds.add(companyFddVO.getUnifiedSocialCreditIdentifier());
            CompanyFddVO companyVO = new CompanyFddVO();
            companyVO.setUnifiedSocialCreditIdentifier(companyFddVO.getUnifiedSocialCreditIdentifier());
            companyVO.setCompanyId(companyFddVO.getId());
            companyVO.setCompanyName(companyFddVO.getName());
            String companyAdmin = companyFddVO.getCompanyAdmin();
            if(StringUtils.isNotBlank(companyAdmin)){
                UserInfoVo userInfoVo = userMap.get(companyAdmin);
                if (userInfoVo != null){
                    companyVO.setIdcardNo(userInfoVo.getIdcardNo());
                }
            }
            companyFddVOList.add(companyVO);
        }
        for (UserInfoVo userInfoVo : userList) {
            openIds.add(userInfoVo.getIdcardNo());
        }
        log.info("查询所有物流商以及认证信息请求参数：{}",JSON.toJSONString(openIds));
        List<FddElectricSealResp> fddResult = fddFeignService.getFddElectricSealByOpenIds(openIds, 2);
        log.info("查询所有物流商以及认证信息返回结果：{}",JSON.toJSONString(fddResult));
        //转换为map key openId
        Map<String, FddElectricSealResp> fddResultMap = fddResult.stream().collect(Collectors.toMap(FddElectricSealResp::getOpenId, fddElectricSealResp -> fddElectricSealResp));
        for (CompanyFddVO companyFddVO : companyFddVOList) {
            String unifiedSocialCreditIdentifier = companyFddVO.getUnifiedSocialCreditIdentifier();
            //设置默认值
            companyFddVO.setCertificationStatus(0);
            companyFddVO.setAuthorizationStatus(0);
            if(StringUtils.isNotBlank(unifiedSocialCreditIdentifier)&&fddResultMap.containsKey(unifiedSocialCreditIdentifier)){
                FddElectricSealResp fddElectricSealResp = fddResultMap.get(unifiedSocialCreditIdentifier);
                if(fddElectricSealResp!=null){
                    Integer verifyStatus = fddElectricSealResp.getVerifyStatus();
                    if(verifyStatus==null){
                        verifyStatus = 0;
                    }
                    companyFddVO.setCertificationStatus(verifyStatus);
                    Integer authAutoSignStatus = fddElectricSealResp.getAuthAutoSignStatus();
                    if(authAutoSignStatus==null){
                        authAutoSignStatus = 0;
                    }
                    companyFddVO.setAuthorizationStatus(authAutoSignStatus);
                }
            }else{
                String idcardNo = companyFddVO.getIdcardNo();
                FddElectricSealResp fddElectricSealResp = fddResultMap.get(idcardNo);
                if(fddElectricSealResp!=null){
                    Integer verifyStatus = fddElectricSealResp.getVerifyStatus();
                    if(verifyStatus==null){
                        verifyStatus = 0;
                    }
                    companyFddVO.setCertificationStatus(verifyStatus);
                    Integer authAutoSignStatus = fddElectricSealResp.getAuthAutoSignStatus();
                    if(authAutoSignStatus==null){
                        authAutoSignStatus = 0;
                    }
                    companyFddVO.setAuthorizationStatus(authAutoSignStatus);
                }
            }
        }
        //小铁马相关公司需要根据nacos配置的身份证号来查询认证信息
        String xtmTjCompanyId = nacosValueConfig.getXtmTjCompanyId();
        String xtmGsCompanyId = nacosValueConfig.getXtmGsCompanyId();
        String xtmTjCardNo = nacosValueConfig.getXtmTjCardNo();
        String xtmGsCardNo = nacosValueConfig.getXtmGsCardNo();

        List<String> cardNoList = Arrays.asList(xtmTjCardNo, xtmGsCardNo);
        List<FddElectricSealResp> xtmFddList = fddFeignService.getFddElectricSealByOpenIds(cardNoList, 2);
        Map<String,FddElectricSealResp> xtmMap = new HashMap<>();
        for (FddElectricSealResp fddElectricSeal : xtmFddList) {
            String openId = fddElectricSeal.getOpenId();
            if(xtmTjCardNo.equals(openId)){
                xtmMap.put(xtmTjCompanyId,fddElectricSeal);
            }else if (xtmGsCardNo.equals(openId)){
                xtmMap.put(xtmGsCompanyId,fddElectricSeal);
            }
        }
        for (CompanyFddVO companyFddVO : companyFddVOList) {
            String companyId = companyFddVO.getCompanyId();
            if(xtmMap.containsKey(companyId)){
                FddElectricSealResp fddElectricSeal = xtmMap.get(companyId);
                companyFddVO.setAuthorizationStatus(fddElectricSeal.getAuthAutoSignStatus());
                companyFddVO.setCertificationStatus(fddElectricSeal.getVerifyStatus());
            }
        }
        return companyFddVOList;
    }

    @Override
    @Async("asyncExecutor")
    public void contractDelete(String param) {
        // 1. 获取开始时间  首次 可以 指定个开始日期
        String startTime = redisTemplate.opsForValue().get(this.nacosValueConfig.getPrefix() + ContractConstant.CONTRACT_DATE_PARAM);
        // 3个月前的时间
        DateTime targetTime = DateUtil.offsetMonth(DateUtil.beginOfDay(new Date()), -3);
        LocalDateTime targetLocalTime = targetTime.toLocalDateTime();
        if (StrUtil.isBlank(startTime)){
            if (StrUtil.isBlank(param)){
                // 默认 从三个月前一天开始
                startTime = DateUtil.format(DateUtil.offsetDay(DateUtil.beginOfDay(targetTime), -1),"yyyy-MM-dd") + " 00:00:00";
            }else {
                startTime = param + " 00:00:00";
            }
        }else{
            //去掉多余的双引号
            startTime = StrUtil.removeAll(startTime, "\"");
        }
        //将 startTime 转换为localdatetime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTimeLocalDateTime = LocalDateTime.parse(startTime, dateTimeFormatter);

        // 如果开始时间不在 3个月前 不处理
        if (!startTimeLocalDateTime.isBefore(targetLocalTime)){
            log.info("法大大调用合同删除接口入参时间:{},当前截止时间:{}", startTimeLocalDateTime,targetLocalTime);

            return;
        }

        // 计算结束时间
        LocalDateTime endTimeDate = startTimeLocalDateTime.plusDays(1);

        //  查询时间范围内的运单类型合同id(一天)
        List<String> contractIds =  contractMapper.getContractIdsByParam(startTimeLocalDateTime,endTimeDate,DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH);

        if (CollUtil.isEmpty(contractIds)){
            return;
        }


        // 保存更新条件到redis
        redisTemplate.opsForValue().set(this.nacosValueConfig.getPrefix() + ContractConstant.CONTRACT_DATE_PARAM,DateUtil.format(endTimeDate, "yyyy-MM-dd HH:mm:ss"));


        // 2. 查询法大大配置
        setSysUser();

        // 3.循环调用法大大删除接口, 错误信息保存入库

        for (String contractId : contractIds) {
            // 调用法大大删除接口 返回调用结果
            Result<String> response = tmsFddElectricSealFeign.deleteFddContract(contractId);
            if (!response.isSuccess()){
                log.error("法大大调用合同删除错误:{},{}",contractId, response.getMsg());
            }
        }
        // 递归直到三个月前
        this.contractDelete(null);
    }



    private void setSysUser() {
        if (ObjectUtil.isEmpty(LoginUserContextHolder.getUser())) {
            SysUser sessionInfo = new SysUser();
            sessionInfo.setCompanyName("xtm");
            sessionInfo.setName("xtm");
            LoginUserContextHolder.setUser(sessionInfo);
        }
    }

    /**
     * 法大大解绑
     * @param openId
     */
    @Override
    public void fddUnbind(String openId){
        FddUserBO fddUserBO = new FddUserBO();
        fddUserBO.setOpenIds(Collections.singletonList(openId));
        log.info("解绑法大大开始，参数：{}", JsonUtils.toJSONString( fddUserBO));
        Result<?> fddResult = tmsFddElectricSealFeign.fddUnbindOperator(fddUserBO);
        log.info("解绑法大大结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if (!fddResult.isSuccess()){
            throw new BusinessException("解绑法大大异常:"+fddResult.getMsg());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAccountNew(Integer accountType, String operatorId) throws BusinessException {
        List<String> openIds = contractFddSignService.getOpenIds(operatorId, accountType);
        if(CollectionUtil.isEmpty(openIds)){
            throw new BusinessException("请先完善公司和管理员信息");
        }
        QueryFddSealListParam param = new QueryFddSealListParam();
        param.setOpenIds(openIds);
        param.setAccountType(accountType);
        //accountType=2 openid表示企业的社会信用代码 否者openid表示个人身份证号
        param.setOpenId(openIds.get(0));
        log.info("创建法大大账户请求参数{}", JsonUtils.toJSONString(param));
        Result<String> fddResult = tmsFddElectricSealFeign.fddCreateAccountByOpenId(param);
        log.info("创建法大大账户返回结果{}", JsonUtils.toJSONString(fddResult));
        if(!fddResult.isSuccess()){
            throw new BusinessException("创建法大大账户异常:"+fddResult.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FddVerifyUrlInfoVo getVerifyUrl(Integer accountType , String operatorId) {
        FddUserBO fddUserBO = new FddUserBO();
        contractFddSignService.queryOpenIdsAndUserInfo(operatorId, accountType,fddUserBO);
        log.info("获取法大大认证url请求参数{}，", JsonUtils.toJSONString(fddUserBO));
        Result<FddVerifyUrlInfoVo> verifyUrlResult = tmsFddElectricSealFeign.getVerifyUrl(fddUserBO);
        log.info("获取法大大认证url返回结果{}", JsonUtils.toJSONString(verifyUrlResult));
        if(verifyUrlResult.isSuccess()){
            return verifyUrlResult.getData();
        }
        throw new BusinessException(StatusCode.ERROR.getCode(), "获取法大大认证url失败:"+verifyUrlResult.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FddElectricSealResp findFddSealResult(Integer accountType, String operatorId) {
        List<String> openIds = contractFddSignService.getOpenIds(operatorId, accountType);
        if(CollectionUtil.isEmpty(openIds)){
            return null;
        }
        QueryFddSealListParam queryFddSealListParam = new QueryFddSealListParam();
        queryFddSealListParam.setOpenIds(openIds);
        queryFddSealListParam.setAccountType(accountType);
        log.info("获取法大大认证结果开始，参数：{}", JsonUtils.toJSONString( queryFddSealListParam));
        Result<FddElectricSealResp> fddResult = tmsFddElectricSealFeign.findFddSealResult(queryFddSealListParam);
        log.info("获取法大大认证结果结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if(fddResult.isSuccess()){
            return fddResult.getData();
        }
        throw new BusinessException("获取法大大认证结果失败:"+fddResult.getMsg());
    }

    @Override
    public Boolean fddUnbindOperator(Integer accountType, String operatorId) throws Exception {
        List<String> openIds = contractFddSignService.getOpenIds(operatorId, accountType);
        FddUserBO fddUserBO = new FddUserBO();
        fddUserBO.setOpenIds(openIds);
        fddUserBO.setAccountType(accountType);
        log.info("解绑法大大开始，参数：{}", JsonUtils.toJSONString( fddUserBO));
        Result<?> fddResult = tmsFddElectricSealFeign.fddUnbindOperator(fddUserBO);
        log.info("解绑法大大结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if (fddResult.isSuccess()){
            return true;
        }
        throw new BusinessException("解绑法大大异常:"+fddResult.getMsg());
    }

    @Override
    public FddVerifyUrlInfoVo fddAutoSignUrl(Integer accountType, String operatorId) {
        List<String> openIds = contractFddSignService.getOpenIds(operatorId, accountType);
        QueryFddSealListParam param = new QueryFddSealListParam();
        param.setOpenIds(openIds);
        param.setAccountType(accountType);
        log.info("查询法大大电子签章开始，参数：{}", JsonUtils.toJSONString( param));
        Result<FddVerifyUrlInfoVo> fddVerifyUrlInfoVoResult = tmsFddElectricSealFeign.fddAutoSignUrl(param);
        log.info("查询法大大电子签章结束，结果：{}",fddVerifyUrlInfoVoResult);
        if(fddVerifyUrlInfoVoResult.isSuccess()){
            return fddVerifyUrlInfoVoResult.getData();
        }
        throw new BusinessException(fddVerifyUrlInfoVoResult.getMsg());
    }

    /**
     * 根据当前账号获取自动签授权记录
     * @return
     */
    @Override
    public QuerySignResultResponse selectAuthSignResult(Integer accountType , String operatorId) {
        List<String> openIds = contractFddSignService.getOpenIds(operatorId, accountType);
        QueryFddSealListParam param = new QueryFddSealListParam();
        param.setOpenIds(openIds);
        param.setAccountType(accountType);
        log.info("根据当前账号获取自动签授权记录开始，参数：{}", JsonUtils.toJSONString( param));
        Result<QuerySignResultResponse> fddResult = tmsFddElectricSealFeign.selectAuthSignResult(param);
        log.info("根据当前账号获取自动签授权记录结束，结果：{}",fddResult);
        if(fddResult.isSuccess()){
            return fddResult.getData();
        }
        throw new BusinessException("获取自动签授权记录失败:"+fddResult.getMsg());
    }

    /**
     * 查当前法大大企业认证是否是用身份证号注册
     * @param accountType
     * @param operatorId
     * @return
     */
    @Override
    public Boolean findIsIdCardCreateCompany(Integer accountType, String operatorId) {
        // 查当前法大大企业认证是否是用身份证号注册  true : 是  false ： 否
        Boolean findIsIdCard = false;
        log.info("查当前法大大企业认证是否是用身份证号注册，查询公司信息，入参operatorId：{}",operatorId );
        Result<CompanyVo> companyInfo = companyFeign.findCompanyById(operatorId);
        log.info("查当前法大大企业认证是否是用身份证号注册，查询公司信息，返回报文：{}", JSONObject.toJSONString(companyInfo));
        if (companyInfo.isSuccess() && companyInfo.getData() != null) {
            UserInfoVo user = userService.getUserById(companyInfo.getData().getCompanyAdmin());
            if (ObjectUtil.isNotEmpty(user) && StrUtil.isNotEmpty(user.getIdcardNo())) {
                log.info("查询法大大信息开始,参数:{},{}", user.getIdcardNo(), accountType);
                List<FddElectricSealResp> fddElectricSeals = fddFeignService.getFddElectricSealByOpenIds(Lists.newArrayList(user.getIdcardNo()), accountType);
                log.info("查询法大大信息结束,结果:{}", JSONObject.toJSONString(fddElectricSeals));
                if (CollUtil.isNotEmpty(fddElectricSeals)){
                    // 说明是用身份证注册法大大企业
                    findIsIdCard = true;
                }
            }
        }
        return findIsIdCard;
    }

    /**
     * @Param: []
     * @return: void
     * @Author: wwh
     * @Date: 2025/4/11 15:33
     * @Description: 批量修改法大大认证信息定时任务
     */
    @Override
    public void findFddSealResultBatch() {
        log.info("批量修改法大大认证信息定时任务开始");
        Result<?> fddResult = tmsFddElectricSealFeign.findFddSealResultBatch();
        log.info("批量修改法大大认证信息定时任务结束，结果：{}",fddResult);
    }

    @Override
    public List<FddCompanyStatusVo> queryFddCompanyStatus(List<String> ids) {
        Result<List<CompanyVo>> companyResult = companyFeign.findCompanysByIds(ids);
        List<FddCompanyStatusVo> fddCompanyStatusVos = new ArrayList<>();
        //小铁马相关公司需要根据nacos配置的身份证号来查询认证信息
        String xtmTjCompanyId = nacosValueConfig.getXtmTjCompanyId();
        String xtmGsCompanyId = nacosValueConfig.getXtmGsCompanyId();
        String xtmTjCardNo = nacosValueConfig.getXtmTjCardNo();
        String xtmGsCardNo = nacosValueConfig.getXtmGsCardNo();
        if (companyResult.isSuccess()){
            List<String> openIds = new ArrayList<>();
            Map<String,String> companyMap = new HashMap<>();
            List<String> userIds = new ArrayList<>();
            List<CompanyVo> companyVos = companyResult.getData();
            companyVos.forEach(vo ->{
                String companyId = vo.getId();
                if(companyId.equals(xtmTjCompanyId)){
                    openIds.add(xtmTjCardNo);
                }else if (companyId.equals(xtmGsCompanyId)){
                    openIds.add(xtmGsCardNo);
                }else{
                    if (StringUtils.isNotBlank(vo.getUnifiedSocialCreditIdentifier())){
                        openIds.add(vo.getUnifiedSocialCreditIdentifier());
                    }
                    if (StringUtils.isNotBlank(vo.getCompanyAdmin())){
                        userIds.add(vo.getCompanyAdmin());
                    }
                }
            });
            if(CollectionUtil.isNotEmpty(userIds)){
                Result<List<UserInfoVo>> userResult = userFeign.loadIdCardNoByUserIds(userIds);
                if (userResult.isSuccess() && userResult.getData() != null){
                    userResult.getData().forEach(userInfoVo -> {
                        if (StringUtils.isNotBlank(userInfoVo.getIdcardNo())){
                            companyMap.put(userInfoVo.getId(),userInfoVo.getIdcardNo());
                            openIds.add(userInfoVo.getIdcardNo());
                        }
                    });
                }
            }
            if (openIds.isEmpty()){
                return fddCompanyStatusVos;
            }
            QueryFddSealListParam param = new QueryFddSealListParam();
            param.setOpenIds(openIds);
            List<FddElectricSealResp> fddElectricSeals = fddFeignService.getFddElectricSealByOpenIds(openIds, 2);
            Map<String,FddElectricSealResp> map = new HashMap<>();
            fddElectricSeals.forEach(vo ->map.put(vo.getOpenId(),vo));
            companyVos.forEach(vo ->{
                FddCompanyStatusVo fddCompanyStatusVo = new FddCompanyStatusVo();
                fddCompanyStatusVo.setCompanyId(vo.getId());
                fddCompanyStatusVo.setCompanyName(vo.getName());
                if (map.get(vo.getUnifiedSocialCreditIdentifier()) != null || map.get(companyMap.get(vo.getCompanyAdmin())) != null){
                    FddElectricSealResp seal = map.get(vo.getUnifiedSocialCreditIdentifier()) != null ? map.get(vo.getUnifiedSocialCreditIdentifier()):map.get(companyMap.get(vo.getCompanyAdmin()));
                    fddCompanyStatusVo.setCustomerId(seal.getCustomerId());
                    fddCompanyStatusVo.setVerifyStatus(seal.getVerifyStatus());
                    fddCompanyStatusVo.setAuthAutoSignStatus(seal.getAuthAutoSignStatus());
                }else {
                    fddCompanyStatusVo.setVerifyStatus(0);
                    fddCompanyStatusVo.setAuthAutoSignStatus(0);
                }
                fddCompanyStatusVos.add(fddCompanyStatusVo);
            });
        }
        return fddCompanyStatusVos;
    }
}
