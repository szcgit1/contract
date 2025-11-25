package com.xtm.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.log.model.dto.BusinessChangeLogDto;
import com.xtm.common.log.model.dto.ChangeLogDto;
import com.xtm.common.log.service.IBusinessHitoryService;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.common.service.impl.SuperServiceImpl;
import com.xtm.company.feign.CompanyFeign;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.DisableEnum;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.SalesContractHistoryOperationTypeEnum;
import com.xtm.contract.feign.*;
import com.xtm.contract.mapper.FrameAgreementMapper;
import com.xtm.contract.mapper.FrameAgreementSubMapper;
import com.xtm.contract.mapper.SalesContractMapper;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.*;
import com.xtm.contract.model.dto.ChangesDto;
import com.xtm.contract.model.dto.FrameAgreementHistoryDto;
import com.xtm.contract.model.dto.HistoryDTO;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import com.xtm.contract.model.enums.FrameAgreementBusiSourceEnum;
import com.xtm.contract.model.enums.FrameAgreementHistoryOperationTypeEnum;
import com.xtm.contract.model.enums.FrameAgreementSystemSourceEnum;
import com.xtm.contract.model.param.*;
import com.xtm.contract.model.param.frameAgreement.*;
import com.xtm.contract.model.vo.*;
import com.xtm.contract.model.vo.frameAgreement.*;
import com.xtm.contract.service.FrameAgreementService;
import com.xtm.contract.service.FrameAgreementSubService;
import com.xtm.contract.service.InternalMatchingApiLogService;
import com.xtm.contract.service.SalesContractHistoryService;
import com.xtm.contract.service.SalesContractService;
import com.xtm.lock.Lock;
import com.xtm.setting.feign.SettingFeign;
import com.xtm.setting.model.vo.CargoOwnerInfoResult;
import com.xtm.thirdparty.data.feign.IThirdPartDataFeign;
import com.xtm.thirdparty.data.model.vo.ProductLineVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  19:56
 *@Description: 框架合同协议接口实现类
 */
@Service
@Slf4j
public class FrameAgreementServiceImpl extends SuperServiceImpl<FrameAgreementMapper, FrameAgreement>
        implements FrameAgreementService {

    @Autowired
    private FrameAgreementMapper frameAgreementMapper;
    @Autowired
    private FrameAgreementSubMapper frameAgreementSubMapper;
    @Autowired
    private SalesContractMapper logisticsContractMapper;
    @Autowired
    private TmsFileService fileService;
    @Resource
    private SettingFeign settingFeign;
    @Resource
    private IThirdPartDataFeign thirdPartDataFeign;
    @Resource
    private CompanyFeign companyFeign;
    @Autowired
    private FrameAgreementSubService frameAgreementSubService;
    @Autowired
    private FileFeignAdapter fileFeignAdapter;
    @Autowired
    private IBusinessHitoryService businessHitoryService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private InternalMatchingApiLogService internalMatchingApiLogService;

    @Resource
    private SalesContractService salesContractService;

    @Resource
    private SalesContractHistoryService salesContractHistoryService;

    /**
     * 分页查询框架合同协议
     * @param queryParam 查询参数
     * @return
     */
    @Override
    public ApiPageResult<FrameAgreementVo> queryPageList(FrameAgreementParam queryParam) {
        // 当查询框都是精准搜索时,不加生效/失效日期的过滤条件
        if(StrUtil.isNotEmpty(queryParam.getCode()) || StrUtil.isNotEmpty(queryParam.getShippingUscc()) ||
           StrUtil.isNotEmpty(queryParam.getCustomerUscc()) || StrUtil.isNotEmpty(queryParam.getSaleOrgUscc()) || StrUtil.isNotEmpty(queryParam.getProductLineName()) ||
                queryParam.getDisabled() != null || queryParam.getVirtualTag() != null || CollUtil.isNotEmpty(queryParam.getBusiSource())
        ){
            queryParam.setStartTime(null);
            queryParam.setEndTime(null);
        }

        Page<FrameAgreementVo> pageParam = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        IPage<FrameAgreementVo> frameAgreementPage = frameAgreementMapper.queryPageList(pageParam, queryParam);
        if (frameAgreementPage == null || CollectionUtil.isEmpty(frameAgreementPage.getRecords())) {
            return new ApiPageResult<FrameAgreementVo>();
        }

        List<FrameAgreementVo> agreementList = frameAgreementPage.getRecords();
        // 查询框架合同的关联销售合同
        List<Long> agreementIds = agreementList.stream().distinct().map(FrameAgreementVo::getId).collect(Collectors.toList());
        List<SalesContractVo> logisticsContractList = logisticsContractMapper.queryByAgreementIds(agreementIds);
        if(CollUtil.isNotEmpty(logisticsContractList)){
            for (FrameAgreementVo agreementVo : agreementList) {
                List<SalesContractVo> contractVoList = logisticsContractList.stream().filter(x -> x.getAgreementId().equals(agreementVo.getId())).collect(Collectors.toList());
                String contractCodes = contractVoList.stream().map(p -> p.getContractCode()).collect(Collectors.joining(","));
                // 显示关联的销售合同
                agreementVo.setSaleContractCode(contractCodes);
            }
        }

        ApiPageResult<FrameAgreementVo> resultApiPageResult = ApiPageResult.<FrameAgreementVo>builder()
                .currentPage((int)frameAgreementPage.getCurrent())
                .pageSize((int)frameAgreementPage.getSize())
                .totalPage((int)frameAgreementPage.getPages())
                .total((int)frameAgreementPage.getTotal())
                .build();

        resultApiPageResult.setList(agreementList);
        return resultApiPageResult;
    }

    /**
     * 框架合同协议详情
     * @param id
     * @return
     */
    @Override
    public FrameAgreementDetailVo queryDetailById(Long id) {
        FrameAgreementDetailVo agreementDetailVo = new FrameAgreementDetailVo();
        FrameAgreement agreement = frameAgreementMapper.selectById(id);
        if(agreement == null){
            return agreementDetailVo;
        }
        BeanUtil.copyProperties(agreement,agreementDetailVo);

        // 框架合同 1：N 虚拟年度协议
        // 虚拟年度协议 ：是 ,直接取parentId作为关联的框架合同,查询出1个
        if(agreement.getVirtualTag().equals(DicConstant.NumEnums.NUM_ONE.getIntegerValue()) && agreement.getParentId() != null){
            FrameAgreement virtualAgreement = frameAgreementMapper.selectById(agreement.getParentId());
            agreementDetailVo.setReVirtualCode(virtualAgreement.getCode());
            // 是:并且存在虚拟年度协议关联框架合同(协议)显示“是-关联框架合同(协议)的合同编号”;
            agreementDetailVo.setReVirtualName("是-" + virtualAgreement.getCode());
        } else {
            // 填写为是,没有关联关系时,显示"是"
            agreementDetailVo.setReVirtualName("是");
        }

        List<Long> virtualAgreementIdList = Lists.newArrayList();
        // 虚拟年度协议 ：否 ,使用id作为parentId查询关联的虚拟合同协议,查询出1或多个
        if(agreement.getVirtualTag().equals(DicConstant.NumEnums.NUM_ZERO.getIntegerValue())){
            LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FrameAgreement::getParentId , id);
            wrapper.eq(FrameAgreement::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
            List<FrameAgreement> virtualAgreementList = frameAgreementMapper.selectList(wrapper);
            if(CollUtil.isNotEmpty(virtualAgreementList)){
                agreementDetailVo.setReVirtualCode(virtualAgreementList.stream().map(p -> p.getCode()).collect(Collectors.joining(",")));
                // 否:并且存在虚拟年度协议关联框架合同(协议)显示“否-关联框架合同(协议)+关联框架合同(协议).”;
                agreementDetailVo.setReVirtualName("否-" + agreementDetailVo.getReVirtualCode());
                virtualAgreementIdList = virtualAgreementList.stream().map(p -> p.getId()).collect(Collectors.toList());
            } else {
                agreementDetailVo.setReVirtualName("否");
            }
        }

        // 获取全部框架合同id
        // 1、框架合同-虚拟为否  需查询显示自己和绑定的虚拟协议的关联销售合同
        // 2、框架合同-虚拟为是  需显示自己关联的销售合同
        List<Long> agreementIds = Lists.newArrayList();
        agreementIds.addAll(virtualAgreementIdList);
        agreementIds.add(id);
        // 根据框架合同ids,查询关联销售合同
        List<FrameAgreementRelateSaleContractVo> contractRelateVoList = logisticsContractMapper.queryRelateContractByAgreementId(agreementIds);

        // 系统来源：nc,框架合同子表有数据,也要展示的是没有销售合同编号的数据,等待关联框架合同
        if(DicConstant.NumEnums.NUM_ONE.getIntegerValue().equals(agreement.getSystemSource())){
            LambdaQueryWrapper<FrameAgreementSub> subWrapper = new LambdaQueryWrapper<>();
            subWrapper.eq(FrameAgreementSub::getAgreementId, id);
            subWrapper.eq(FrameAgreementSub::getRelate, 0);
            List<FrameAgreementSub> agreementSubs = frameAgreementSubMapper.selectList(subWrapper);
            if(CollUtil.isNotEmpty(agreementSubs)){
                for (FrameAgreementSub sub : agreementSubs) {
                    FrameAgreementRelateSaleContractVo relateContractVo = new FrameAgreementRelateSaleContractVo();
                    relateContractVo.setAgreementId(sub.getAgreementId());
                    relateContractVo.setReMonth(sub.getMonth());
                    relateContractVo.setTotalNumber(sub.getAmount());
                    relateContractVo.setReMergeState(sub.getMergeState());
                    relateContractVo.setReRemark(sub.getRemark());
                    contractRelateVoList.add(relateContractVo);
                }
            }
        }

        // 按照月份正序排列
        contractRelateVoList = contractRelateVoList.stream().sorted(Comparator.comparing(FrameAgreementRelateSaleContractVo::getReMonth, Comparator.nullsFirst(String::compareTo))).collect(Collectors.toList());
        agreementDetailVo.setRelateContractVoList(contractRelateVoList);
        return agreementDetailVo;
    }

    /**
     * 查询业务来源
     * @param shippingMain
     */
    public String getBusiSource(String shippingMain){
        String busiSource = "";
        // 系统来源：纵横,中铁  ,查询系统来源    根据发运组织查询货主平台简称接口,不存在,系统来源为空,报错,生成禁用状态的协议合同
        log.info("根据NcId(货主唯一标识)查询货主平台配置开始,入参:{}", shippingMain);
        Result<CargoOwnerInfoResult> settingResult = settingFeign.getCargoOwnerByOwnerCode(shippingMain);
        log.info("根据NcId(货主唯一标识)查询货主平台配置结束:{}", JSONObject.toJSON(settingResult));
        // 停用,提示报错
        if(!settingResult.isSuccess() || settingResult.getData() == null || settingResult.getData().getEnableStatus() == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(settingResult.getData().getEnableStatus()) || StrUtil.isEmpty(settingResult.getData().getBusiSource())){

        } else {
            busiSource = settingResult.getData().getBusiSource();
        }
        return busiSource;
    }

    /**
     * 修改业务来源
     * @param shippingMain
     */
    private void updateBusiSource(String shippingMain, String busi) {
        // 查询
        LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FrameAgreement::getShippingMain , shippingMain);
        wrapper.eq(FrameAgreement::getSystemSource, DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.NC);
        wrapper.eq(FrameAgreement::getBusiSource , DicConstant.LOGIS_CONTRACT_AGREEMENT_BUSI_SOURCE.NC);
        wrapper.eq(FrameAgreement::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
        List<FrameAgreement> shippingAgreementList = frameAgreementMapper.selectList(wrapper);
        if(CollUtil.isNotEmpty(shippingAgreementList)){
            List<Long> ids = shippingAgreementList.stream().map(FrameAgreement::getId).collect(Collectors.toList());
            // 查询业务来源
            String busiSource = busi;
            if(StrUtil.isEmpty(busiSource)){
                busiSource = getBusiSource(shippingMain);
            }
            if(StrUtil.isNotEmpty(busiSource)){
                // 更新
                LambdaUpdateWrapper<FrameAgreement> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(FrameAgreement::getBusiSource, busiSource);
                updateWrapper.in(FrameAgreement::getId, ids);
                frameAgreementMapper.update(null, updateWrapper);
            }
        }
    }

    /**
     * nc创建编辑框架合同协议
     * @param param
     */
    @Lock(key = "'frameAgreement:nc:saveOrUpdate:mainId:' + #param.mainId", fail = "正在处理请求，请不要重复请求")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result ncCreateOrUpdateAgreement(NcFrameAgreementSaveParam param) {
        LocalDateTime now = LocalDateTime.now();
        String userId = LoginUserContextHolder.getUser().getId();
        String sysUserName = LoginUserContextHolder.getUser().getName();
        FrameAgreement agreement = new FrameAgreement();
        BeanUtil.copyProperties(param, agreement);

        // 单据日期
        agreement.setBillDate(Date.from(param.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));

        // 根据发运组织查询货主平台简称接口,不存在,业务来源为空
        log.info("根据NcId(货主唯一标识)查询货主平台配置开始,入参:{}", param.getShippingMain());
        Result<CargoOwnerInfoResult> settingResult = settingFeign.getCargoOwnerByOwnerCode(param.getShippingMain());
        log.info("根据NcId(货主唯一标识)查询货主平台配置结束:{}", JSONObject.toJSON(settingResult));
        // 停用,提示报错
        if(!settingResult.isSuccess() || settingResult.getData() == null || settingResult.getData().getEnableStatus() == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(settingResult.getData().getEnableStatus()) || StrUtil.isEmpty(settingResult.getData().getBusiSource())){
            agreement.setBusiSource(null);
        } else {
            // 业务来源 0:丰南 1:中铁 2:中重 99:本平台
            agreement.setBusiSource(Integer.valueOf(settingResult.getData().getBusiSource()));
        }
        // 判断是创建还是编辑
        Boolean isNew = true;

        // 1.判断主键是否存在
        LambdaQueryWrapper<FrameAgreement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FrameAgreement::getMainId, param.getMainId());
        queryWrapper.eq(FrameAgreement::getSystemSource, param.getSystemSource());
        queryWrapper.eq(FrameAgreement::getDeleted,0);
        List<FrameAgreement> oldAgreementList = frameAgreementMapper.selectList(queryWrapper);
        FrameAgreement oldAgreement = null;
        if(CollUtil.isNotEmpty(oldAgreementList)){
            oldAgreement = oldAgreementList.get(0);
            isNew = false;
        }
        // 统一信用码与框架合同协议对应基础名称对应关系
        Map<String, String> usccVsBasicNameMap = new HashMap<>();
        usccVsBasicNameMap.put(param.getShippingUscc(), Constant.Frame_Agreement_Basic_Name.SHIPPING);
        usccVsBasicNameMap.put(param.getCustomerUscc(), Constant.Frame_Agreement_Basic_Name.CUSTOMER);
        usccVsBasicNameMap.put(param.getSaleOrgUscc(), Constant.Frame_Agreement_Basic_Name.SALE_ORG);

        // 统一信用码与公司名称映射
        Map<String, String> usccNameMap = new HashMap<>();
        usccNameMap.put(param.getShippingUscc(), param.getShipping());
        usccNameMap.put(param.getCustomerUscc(), param.getCustomer());
        usccNameMap.put(param.getSaleOrgUscc(), param.getSaleOrg());

        // 发运组织,客户,收发货单组织,产品线,不存在,则创建失败
        List<String> usccList = Lists.newArrayList();
        usccList.add(param.getShippingUscc());
        usccList.add(param.getCustomerUscc());
        usccList.add(param.getSaleOrgUscc());
        // 统一社会信用码 去重
        usccList = usccList.stream().distinct().collect(Collectors.toList());
        // 记录内部匹配日志
        StringBuffer errorMsgSb = new StringBuffer();

        // 1.1.产品线,不存在,则记录内部匹配日志
        log.info("根据产品线编码查询产品线详情开始:{}",param.getProductLineCode());
        Result<ProductLineVo> lineResult = thirdPartDataFeign.getProductLineByCode(param.getProductLineCode());
        log.info("根据产品线编码查询产品线详情结束:{}",JSONObject.toJSONString(lineResult));
        if(!lineResult.isSuccess() || lineResult.getData() == null){
            errorMsgSb = errorMsgSb.append(param.getProductLineCode() + "，产品线不存在").append("；");
        } else {
            // 产品线名称
            agreement.setProductLineName(lineResult.getData().getProductLineName());
            agreement.setProductLineId(Long.valueOf(lineResult.getData().getId()));
            // 产品线未启用，记录内部匹配日志
            if(lineResult.getData().getStatus() == null || lineResult.getData().getStatus().equals(DisableEnum.STOP.getCode())){
                errorMsgSb = errorMsgSb.append(agreement.getProductLineName() + "(" + param.getProductLineCode() + ")，产品线未启用").append("；");
            }
        }

        // 1.2.发运组织,客户,收发货单组织，不存在，未启用或者未认证，记录内部匹配日志
        log.info("根据统一信用码批量查询公司信息开始,入参:{}", JSONObject.toJSON(usccList));
        Result<Map<String, CompanyVo>> companyResult = companyFeign.listCompanyByIdentifiers(usccList);
        log.info("根据统一信用码批量查询公司信息结束:{}", JSONObject.toJSON(companyResult));
        if(!companyResult.isSuccess() || companyResult.getData() == null){
            errorMsgSb = errorMsgSb.append("发运组织、客户、销售组织，公司不存在").append("；");
        } else {
            // 公司信息是否存在标识
            Boolean existsCompanyInfo = false;
            StringBuffer sbExist = new StringBuffer();
            for (String uscc : usccList) {
                if (!companyResult.getData().containsKey(uscc) || companyResult.getData().get(uscc) == null) {
                    sbExist = sbExist.append(usccVsBasicNameMap.get(uscc)).append("、");
                    existsCompanyInfo = true;
                }
            }
            if(existsCompanyInfo){
                errorMsgSb = errorMsgSb.append(sbExist.substring(0,sbExist.length()-1) + "，公司不存在").append("；");
            }

            // 记录公司信息未启用或未认证
            Boolean enableCompanyInfo = false;
            StringBuffer companyEnableSb = new StringBuffer();
            for (String uscc : usccList) {
                CompanyVo data = companyResult.getData().get(uscc);
                if(data == null){
                    continue;
                }
                // 1.3.未启用或者未认证，记录内部匹配日志，程序继续执行。 启用状态 0禁用 1启用    认证状态: 0未认证 1已认证 2认证中
                if(data.getEnableStatus() == null || !data.getEnableStatus().equals(1L) || data.getCertificationState() == null || !data.getCertificationState().equals(1L)){
                    if(StrUtil.isNotEmpty(usccNameMap.get(uscc))){
                        enableCompanyInfo = true;
                        companyEnableSb = companyEnableSb.append(usccNameMap.get(uscc) + "(" + uscc + ")").append("；");
                    }
                }
            }
            if(enableCompanyInfo){
                errorMsgSb = errorMsgSb.append(companyEnableSb.substring(0, companyEnableSb.length()-1) + "，公司信息未启用或未认证").append("；");
            }
        }

        // 备注去除空格
        agreement.setRemark(StrUtil.isEmpty(agreement.getRemark()) ? null : agreement.getRemark().trim());
        // 系统来源
        agreement.setSystemSource(param.getSystemSource());
        // 虚拟：是 ,根据“合同编号”移除合同编号字段后“-XN”后根据剩余协议号自动匹配虚拟年度协议为“否”的协议
        Long parentId = null;
        if(DicConstant.NumEnums.NUM_ONE.getIntegerValue().equals(param.getVirtualTag()) && agreement.getCode().contains("-XN")){
            String code = agreement.getCode().substring(0, agreement.getCode().indexOf("-XN"));
            LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FrameAgreement::getCode , code);
            wrapper.eq(FrameAgreement::getBusiSource, agreement.getBusiSource());
            wrapper.eq(FrameAgreement::getVirtualTag , 0);
            wrapper.eq(FrameAgreement::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
            List<FrameAgreement> frameAgreementList = frameAgreementMapper.selectList(wrapper);
            if(CollUtil.isNotEmpty(frameAgreementList)){
                // 匹配上,则绑定虚拟关系,如匹配不上则不做绑定关系,不影响正常生成单据
                parentId = frameAgreementList.get(0).getId();
            }
        }
        // 记录操作历史记录
        FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
        FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();

        if(StrUtil.isNotEmpty(errorMsgSb.toString())){
            // 记录内部匹配日志
            InternalMatchingApiLogDto logDto = new InternalMatchingApiLogDto(TmsContractConstant.InternalMatchingType.FRAMEWORK_CONTRACT,
                    TmsContractConstant.InternalMatchingOperation.BASIC_INFO, agreement.getCode(), agreement.getCode(), Result.error(errorMsgSb.toString()));
            internalMatchingApiLogService.create(logDto);
        }
        log.info("nc创建合同协议,主键id：{},isNew：{}", param.getMainId() , isNew);
        if (isNew) {
            // 创建
            LocalDateTime createTime = now;
            LocalTime timeNow = LocalTime.now();
            createTime = LocalDateTime.of(LocalDate.from(param.getCreateTime()), timeNow);
            agreement.setCreateTime(createTime);

            agreement.setId(IdWorker.getId());
            agreement.setCreateId(userId);
            agreement.setCreateTime(createTime);
            agreement.setCreateName(sysUserName);
            agreement.setModifyId(userId);
            agreement.setModifyTime(createTime);
            agreement.setModifyName(sysUserName);

            agreement.setParentId(parentId);

            frameAgreementMapper.insert(agreement);

            // 批量保存子表数据
            List<NcFrameAgreementSubParam> agreementSubList = param.getAgreementSubList();
            if(CollUtil.isNotEmpty(agreementSubList)){
                List<FrameAgreementSubHistoryVo> subHistoryList = new ArrayList<>();
                List<FrameAgreementSub> list = Lists.newArrayList();
                for (NcFrameAgreementSubParam vo : agreementSubList) {
                    FrameAgreementSub agreementSub = new FrameAgreementSub();
                    BeanUtil.copyProperties(vo,agreementSub);

                    agreementSub.setAgreementId(agreement.getId());
                    agreementSub.setCreateId(userId);
                    agreementSub.setCreateTime(createTime);
                    agreementSub.setCreateName(sysUserName);
                    agreementSub.setModifyId(userId);
                    agreementSub.setModifyTime(createTime);
                    agreementSub.setModifyName(sysUserName);
                    list.add(agreementSub);

                    // 子表历史记录
                    FrameAgreementSubHistoryVo subHistoryVo = new FrameAgreementSubHistoryVo();
                    subHistoryVo.setSubId(agreementSub.getSubId());
                    subHistoryVo.setSubMonth(agreementSub.getMonth());
                    subHistoryVo.setSubAmount(agreementSub.getAmount());
                    subHistoryVo.setSubMergeState(agreementSub.getMergeState());
                    subHistoryVo.setSubRemark(agreementSub.getRemark());
                    subHistoryVo.setSubRelate(agreementSub.getRelate());
                    subHistoryVo.setSubSaleContractId(agreementSub.getSaleContractId());
                    subHistoryList.add(subHistoryVo);
                }
                frameAgreementSubService.saveBatch(list);
                // 子表
                afterAgreement.setSubList(subHistoryList);
            }

            // 记录操作历史记录
            BeanUtil.copyProperties(agreement,afterAgreement);
            saveHistoryRecord(sysUserName, now, null , afterAgreement, FrameAgreementHistoryOperationTypeEnum.CREATE.getType());

        } else {
            agreement.setId(oldAgreement.getId());
            agreement.setModifyId(userId);
            agreement.setModifyTime(now);
            agreement.setModifyName(sysUserName);
            // 虚拟协议
            Integer newVirtualTag = param.getVirtualTag();
            Integer oldVirtualTag = oldAgreement.getVirtualTag();
            // 如果原来是【否】,现在改成【否】 则不处理
            // 如果原来是【是】,现在还是【是】 则不处理
            if((DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(oldVirtualTag) &&
                    DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(newVirtualTag)) ||
                    (DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(oldVirtualTag) &&
                            DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(newVirtualTag)) ){
                // 不处理
            } else {
                agreement.setVirtualTag(newVirtualTag);
                agreement.setParentId(parentId);
            }
            // 更新框架合同协议
            updateFrameAgreement(agreement);

            // 更新子表数据  先删除老表,再新增数据
            // 先查出老的数据,与新的做比对,相同的子表数据,替换绑定关系
            LambdaQueryWrapper<FrameAgreementSub> subWrapper = new LambdaQueryWrapper<>();
            subWrapper.eq(FrameAgreementSub::getAgreementId, oldAgreement.getId());
            subWrapper.eq(FrameAgreementSub::getDeleted, 0);
            List<FrameAgreementSub> oldSubs = frameAgreementSubMapper.selectList(subWrapper);

            // 删除子表老数据
            List<Long> subIds = oldSubs.stream().map(FrameAgreementSub::getId).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(subIds)){
                frameAgreementSubMapper.deleteBatchIds(subIds);
            }

            Map<String, FrameAgreementSub> relateOldSubMap = new HashMap<>();

            // 子表老数据
            if(CollUtil.isNotEmpty(oldSubs)){
                // 过滤出已绑定框架合同的数据
                List<FrameAgreementSub> relateOldSubList = oldSubs.stream().filter(x -> StrUtil.isNotEmpty(x.getSaleContractId())).collect(Collectors.toList());
                if(CollUtil.isNotEmpty(relateOldSubList)){
                    // 对比月份是否相同,如果相同月份,则新数据绑定框架合同id和标记已删除,
                    relateOldSubMap = relateOldSubList.stream().collect(Collectors.toMap(FrameAgreementSub::getSubId, x -> x));
                }
            }

            // 保存子表新数据
            List<NcFrameAgreementSubParam> newSubs = param.getAgreementSubList();
            if(CollUtil.isNotEmpty(newSubs)){
                List<FrameAgreementSubHistoryVo> subHistoryList = new ArrayList<>();
                List<FrameAgreementSub> newList = Lists.newArrayList();
                for (NcFrameAgreementSubParam vo : newSubs) {
                    FrameAgreementSub agreementSub = new FrameAgreementSub();
                    BeanUtil.copyProperties(vo,agreementSub);

                    agreementSub.setAgreementId(agreement.getId());
                    agreementSub.setCreateId(userId);
                    agreementSub.setCreateTime(agreement.getCreateTime());
                    agreementSub.setCreateName(sysUserName);
                    agreementSub.setModifyId(userId);
                    agreementSub.setModifyTime(now);
                    agreementSub.setModifyName(sysUserName);

                    if(relateOldSubMap.containsKey(vo.getSubId())){
                        FrameAgreementSub sub = relateOldSubMap.get(vo.getSubId());
                        String month = sub.getMonth();
                        if(month.equals(vo.getMonth())){
                            // 相同子表id,相同月份,则绑定合同id且删除掉
                            agreementSub.setRelate(1);
                            agreementSub.setSaleContractId(sub.getSaleContractId());
                        }
                    }
                    newList.add(agreementSub);

                    // 子表历史记录
                    FrameAgreementSubHistoryVo subHistoryVo = new FrameAgreementSubHistoryVo();
                    subHistoryVo.setSubId(agreementSub.getSubId());
                    subHistoryVo.setSubMonth(agreementSub.getMonth());
                    subHistoryVo.setSubAmount(agreementSub.getAmount());
                    subHistoryVo.setSubMergeState(agreementSub.getMergeState());
                    subHistoryVo.setSubRemark(agreementSub.getRemark());
                    subHistoryVo.setSubRelate(agreementSub.getRelate());
                    subHistoryVo.setSubSaleContractId(agreementSub.getSaleContractId());
                    subHistoryList.add(subHistoryVo);
                }
                frameAgreementSubService.saveBatch(newList);
                // 子表
                afterAgreement.setSubList(subHistoryList);
            }

            // 记录操作历史记录
            BeanUtil.copyProperties(oldAgreement,beforeAgreement);
            BeanUtil.copyProperties(agreement,afterAgreement);
            saveHistoryRecord(sysUserName, now, null , afterAgreement, FrameAgreementHistoryOperationTypeEnum.NC_CREATE.getType());
        }
        return Result.ok();
    }

    /**
     * nc批量修改合同状态为禁用
     * @param params
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock(key = "'frameAgreement:nc:updateState:mainIds:' + #params.mainIds", fail = "正在处理请求，请不要重复请求")
    public Result ncBatchUpdateState(NcFrameAgreementUpdateStateParam params) {
        if(CollUtil.isEmpty(params.getMainIds()) || params.getDisabled() == null){
            throw new BusinessException("批量修改合同状态的入参不能为空");
        }
        LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FrameAgreement::getMainId, params.getMainIds());
        wrapper.eq(FrameAgreement::getSystemSource, DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.NC);
        List<FrameAgreement> agreementList = frameAgreementMapper.selectList(wrapper);
        if(CollUtil.isEmpty(agreementList)){
            throw new BusinessException("框架合同主键不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = LoginUserContextHolder.getUser().getId();
        String sysUserName = LoginUserContextHolder.getUser().getName();

        // nc数据,根据nc主键id修改合同状态
        LambdaUpdateWrapper<FrameAgreement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FrameAgreement::getDisabled, params.getDisabled());
        updateWrapper.set(FrameAgreement::getModifyTime, now);
        updateWrapper.set(FrameAgreement::getModifyId, userId);
        updateWrapper.set(FrameAgreement::getModifyName, sysUserName);

        updateWrapper.in(FrameAgreement::getMainId, params.getMainIds());
        frameAgreementMapper.update(null, updateWrapper);

        for (FrameAgreement agreement : agreementList) {
            FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();

            afterAgreement.setDisabled(agreement.getDisabled());
            afterAgreement.setCode(agreement.getCode());

            FrameAgreementHistoryFieldVo newAgreement = new FrameAgreementHistoryFieldVo();
            newAgreement.setDisabled(!params.getDisabled().equals(0));
            newAgreement.setCode(agreement.getCode());

            // 记录操作历史记录
            saveHistoryRecord(sysUserName, now, newAgreement , afterAgreement, FrameAgreementHistoryOperationTypeEnum.NC_UPDATE_STATE.getType());
        }
        return Result.ok();
    }

    /**
     * 手动创建编辑框架合同协议
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createOrUpdate(FrameAgreementSaveParam param) {
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        LocalDateTime now = LocalDateTime.now();
        boolean isNew = param.getId() == null;

        // 校验创建参数
        Result<String> checkParam = checkManualParam(param);
        if (!checkParam.isSuccess()) {
            return checkParam;
        }

        FrameAgreement agreement = new FrameAgreement();
        BeanUtil.copyProperties(param, agreement);

        // 生效日期 默认时分秒为00:00:00
        agreement.setEffectiveDate(agreement.getEffectiveDate().withHour(0).withMinute(0).withSecond(0).withNano(0));
        // 失效日期 默认时分秒为23:59:59
        agreement.setExpiryDate(agreement.getExpiryDate().withHour(23).withMinute(59).withSecond(59).withNano(0));
        // 备注去除空格
        agreement.setRemark(StrUtil.isEmpty(agreement.getRemark()) ? null : agreement.getRemark().trim());

        if(isNew){
            agreement.setId(IdWorker.getId());
            agreement.setCreateTime(now);
            agreement.setCreateId(sessionInfo.getId());
            agreement.setCreateName(sessionInfo.getName());
            agreement.setModifyTime(now);
            agreement.setModifyId(sessionInfo.getId());
            agreement.setModifyName(sessionInfo.getName());
            agreement.setDeleted(false);
            agreement.setDisabled(true);
            agreement.setSystemSource(DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.TMS);
            agreement.setBusiSource(DicConstant.LOGIS_CONTRACT_AGREEMENT_BUSI_SOURCE.tms);
            frameAgreementMapper.insert(agreement);
            // 记录操作历史记录
            FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
            BeanUtil.copyProperties(agreement,afterAgreement);
            saveHistoryRecord(sessionInfo.getName(), now, null , afterAgreement, FrameAgreementHistoryOperationTypeEnum.CREATE.getType());
            return Result.ok();
        } else {
            FrameAgreement oldAgreement = frameAgreementMapper.selectById(param.getId());
            agreement.setId(param.getId());
            agreement.setMainId(oldAgreement.getMainId());
            agreement.setSystemSource(oldAgreement.getSystemSource());
            agreement.setBusiSource(oldAgreement.getBusiSource());
            agreement.setShippingMain(oldAgreement.getShippingMain());
            agreement.setDisabled(oldAgreement.getDisabled());
            agreement.setModifyTime(now);
            agreement.setModifyId(sessionInfo.getId());
            agreement.setModifyName(sessionInfo.getName());

            // 虚拟协议
            Integer newVirtualTag = param.getVirtualTag();
            Long newParentId = param.getParentId();
            Integer oldVirtualTag = oldAgreement.getVirtualTag();
            Long oldParentId = oldAgreement.getParentId();
            // 如果原来是【否】,现在改成【否】 则不处理
            if(DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(oldVirtualTag) &&
                    DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(newVirtualTag)){
                // 不处理
            }
            // 原来是【否】 且关联协议号, 现在无法修改状态为“是”,并且提示“当前无法修改” 三方创建,以nc为准,  三方推送,已存在,不可编辑
            if(DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(oldVirtualTag) && oldParentId != null && DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(newVirtualTag)){
                throw new BusinessException("虚拟年度协议为【否】,且关联协议号,不可修改为【是】,当前无法修改");
            }
            // 原来是【否】 且没有关联协议号,现在【是】 ,先查询绑定关系,有则解绑,然后再绑定
            if(DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(oldVirtualTag) &&
                    oldAgreement.getParentId() == null &&
                    DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(newVirtualTag)){
                agreement.setVirtualTag(newVirtualTag);
                agreement.setParentId(newParentId);
            }
            // 如果原来是【是】,现在改成【否】 需先查询作为虚拟id是否有绑定关系,有则解绑
            if(DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(oldVirtualTag) &&
                    DicConstant.LOGIS_VIRTUAL_TAG.NO.equals(newVirtualTag)){
                agreement.setVirtualTag(newVirtualTag);
                agreement.setParentId(null);
            }
            // 原来是【是】,现在还是【是】,但是变更协议号,需先查询作为虚拟id是否有绑定关系,有则解绑,然后再绑定
            if(DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(oldVirtualTag) &&
                    DicConstant.LOGIS_VIRTUAL_TAG.YES.equals(newVirtualTag)){
                agreement.setVirtualTag(newVirtualTag);
                agreement.setParentId(newParentId);
            }

            // 更新框架合同协议
            updateFrameAgreement(agreement);

            // 记录操作历史记录
            FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
            BeanUtil.copyProperties(oldAgreement,beforeAgreement);
            FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
            BeanUtil.copyProperties(agreement,afterAgreement);
            saveHistoryRecord(sessionInfo.getName(), now, beforeAgreement, afterAgreement, FrameAgreementHistoryOperationTypeEnum.UPDATE.getType());

            return Result.ok();
        }
    }

    /**
     * 记录操作历史记录
     * @param sysUserName
     * @param now
     * @param before
     * @param after
     * @param operationType
     */
    @Override
    public void saveHistoryRecord(String sysUserName, LocalDateTime now , FrameAgreementHistoryFieldVo before , FrameAgreementHistoryFieldVo after , String operationType) {
        // 记录操作历史记录
        BusinessChangeLogDto businessChangeLogDto = new BusinessChangeLogDto();

        businessChangeLogDto.setCollectionName(Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        List<ChangeLogDto> changeLogDtos = new ArrayList<>();
        ChangeLogDto changeLogDto = new ChangeLogDto();
        FrameAgreementFixedMetadataVo fixedMetadataVo = new FrameAgreementFixedMetadataVo();//业务固定属性;
        fixedMetadataVo.setCode(after.getCode());

        if(operationType.equals(FrameAgreementHistoryOperationTypeEnum.CREATE.getType())){
            changeLogDto.setAfter(JSONUtil.toJsonStr(after));

        } else if (operationType.equals(FrameAgreementHistoryOperationTypeEnum.UPDATE.getType()) ||
                operationType.equals(FrameAgreementHistoryOperationTypeEnum.RE_LOGIS_CONTRACT.getType()) ||
                operationType.equals(FrameAgreementHistoryOperationTypeEnum.CANCEL_RE_LOGIS_CONTRACT.getType())
        ){
            changeLogDto.setAfter(JSONUtil.toJsonStr(after));
            changeLogDto.setBefore(JSONUtil.toJsonStr(before));
            fixedMetadataVo.setReSource(after.getReSource());

        } else if (operationType.equals(FrameAgreementHistoryOperationTypeEnum.UPDATE_STATE.getType())){
            fixedMetadataVo.setBeforeDisabled(before.getDisabled());
            fixedMetadataVo.setAfterDisabled(after.getDisabled());
        }

        changeLogDto.setId(String.valueOf(IdWorker.getId()));
        changeLogDto.setOperator(sysUserName);
        changeLogDto.setOperationType(operationType);
        changeLogDto.setBusinessModule(Constant.TMS_CONTRACT_FRAME_AGREEMENT_MODULE);
        changeLogDto.setOperationTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        changeLogDto.setFixedMetadata(fixedMetadataVo);

        changeLogDtos.add(changeLogDto);
        businessChangeLogDto.setChangeLogDtos(changeLogDtos);
        try {
            businessHitoryService.save(businessChangeLogDto);
        } catch (BusinessException e) {
            log.error("保存历史记录失败,参数:{}", e);
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            log.error("保存历史记录失败,参数:{}", e);
            throw new BusinessException("保存历史记录失败：{}", e);
        }

    }

    /**
     * 字符转换
     * @param jsonObject
     */
    private String changeDto(JSONObject jsonObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        if (jsonObject.containsKey("virtualTag")) {
            Integer virtualTag = (Integer) jsonObject.get("virtualTag");
            jsonObject.put("virtualTag", virtualTag.equals(0) ? "否" : "是");
        }
        if (jsonObject.containsKey("busiSource")) {
            Integer busiSource = (Integer) jsonObject.get("busiSource");
            jsonObject.put("busiSource", FrameAgreementBusiSourceEnum.getOperationDesc(busiSource));
        }
        if (jsonObject.containsKey("systemSource")) {
            Integer systemSource = (Integer) jsonObject.get("systemSource");
            jsonObject.put("systemSource", FrameAgreementSystemSourceEnum.getOperationDesc(systemSource));
        }
        if (jsonObject.containsKey("effectiveDate")){
            jsonObject.put("effectiveDate", formatter.format(Instant.ofEpochMilli((long) jsonObject.get("effectiveDate"))));
        }
        if (jsonObject.containsKey("expiryDate")){
            jsonObject.put("expiryDate", formatter.format(Instant.ofEpochMilli((long) jsonObject.get("expiryDate"))));
        }
        if (jsonObject.containsKey("billDate")){
            jsonObject.put("billDate", formatter.format(Instant.ofEpochMilli((long) jsonObject.get("billDate"))));
        }
        if (jsonObject.containsKey("createTime")){
            jsonObject.put("createTime", formatter.format(Instant.ofEpochMilli((long) jsonObject.get("createTime"))));
        }
        if (jsonObject.containsKey("modifyTime")){
            jsonObject.put("modifyTime", formatter.format(Instant.ofEpochMilli((long) jsonObject.get("modifyTime"))));
        }
        return JSONObject.toJSONString(jsonObject);
    }

    /**
     * 校验手动修改合同状态
     * @param params
     */
    @Override
    public void checkUpdateContractState(FrameAgreementUpdateStateParam params) {
        if(CollUtil.isEmpty(params.getAgreementIds()) || params.getDisabled() == null){
            throw new BusinessException("批量修改合同状态的入参不能为空");
        }
        LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FrameAgreement::getId, params.getAgreementIds());
        List<FrameAgreement> agreementList = frameAgreementMapper.selectList(wrapper);
        if(CollUtil.isEmpty(agreementList)){
            throw new BusinessException("框架合同协议不存在");
        }

        // 2.系统来源为【本平台】且都是禁用数据,二次确认提示“ 当前数据包含禁用数据,确认是否启用”
        List<FrameAgreement> tmsAgreemetList = agreementList.stream().filter(x -> DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.TMS.equals(x.getSystemSource()) && x.getDisabled()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(tmsAgreemetList)){
            throw new BusinessException("当前数据包含禁用数据,确认是否启用");
        }
        // 3.系统来源为【非本平台】且都是禁用数据,二次确认提示“当前数据非“本平台”数据,修改后与三方数据不符会导致三方推送数据报错确认修改？”
        List<FrameAgreement> unTmsAgreemetList = agreementList.stream().filter(x -> !DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.TMS.equals(x.getSystemSource()) && x.getDisabled()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(unTmsAgreemetList)){
            throw new BusinessException("当前数据非“本平台”数据,修改后与三方数据不符会导致三方推送数据报错确认修改？");
        }

        // 1.全选合同状态包含已启用或已禁用的时候,要提示用户
        List<Boolean> contractStatusList = agreementList.stream().distinct().map(FrameAgreement::getDisabled).collect(Collectors.toList());
        List<Boolean> vaildList = contractStatusList.stream().distinct().collect(Collectors.toList());
        if(vaildList.size() != 1){
            throw new BusinessException("请选择状态一致的数据进行操作");
        }

    }

    /**
     * 手动批量修改合同状态
     * @param params
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateContractState(FrameAgreementUpdateStateParam params) {
        LocalDateTime now = LocalDateTime.now();
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        if(CollUtil.isEmpty(params.getAgreementIds()) || params.getDisabled() == null){
            throw new BusinessException("批量修改合同状态的入参不能为空");
        }
        LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(FrameAgreement::getId, params.getAgreementIds());
        List<FrameAgreement> agreementList = frameAgreementMapper.selectList(wrapper);
        // 修改合同状态,校验三方来源且启用的数据
        List<FrameAgreement> thirdAgreementList = agreementList.stream().filter(x -> DicConstant.LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE.NC.equals(x.getSystemSource())).collect(Collectors.toList());
        updateStateCheckThirdSourceAndEnableData(thirdAgreementList, params.getDisabled());

        // tms数据,根据协议id修改合同状态
        LambdaUpdateWrapper<FrameAgreement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FrameAgreement::getDisabled, params.getDisabled());
        updateWrapper.in(FrameAgreement::getId, params.getAgreementIds());
        updateWrapper.set(FrameAgreement::getModifyTime, LocalDateTime.now());
        updateWrapper.set(FrameAgreement::getModifyId, sessionInfo.getId());
        updateWrapper.set(FrameAgreement::getModifyName, sessionInfo.getName());
        frameAgreementMapper.update(null, updateWrapper);

        for (FrameAgreement agreement : agreementList) {
            FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
            afterAgreement.setDisabled(agreement.getDisabled());
            afterAgreement.setCode(agreement.getCode());
            FrameAgreementHistoryFieldVo newAgreement = new FrameAgreementHistoryFieldVo();
            newAgreement.setDisabled(!params.getDisabled().equals(0));
            newAgreement.setCode(agreement.getCode());
            // 记录操作历史记录
            saveHistoryRecord(sessionInfo.getName(), now, newAgreement , afterAgreement, FrameAgreementHistoryOperationTypeEnum.UPDATE_STATE.getType());
        }
    }

    /**
     * 修改合同状态,校验三方来源且启用的数据
     * @param agreementList
     * @param disabled
     */
    private void updateStateCheckThirdSourceAndEnableData(List<FrameAgreement> agreementList, Integer disabled) {
        // 点击【启用】的数据
        if(DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(disabled)){
            // 「系统来源」为三方推送时,点击「启用」,需要校验字段「发运组织,客户,收/发货单组织」是否存在且有实际数据,没有数据提示“启用失败,公司信息未启用或无存在,请调整后重新启用”
            if(CollUtil.isNotEmpty(agreementList)){
                StringBuffer sb = new StringBuffer();
                for (FrameAgreement agreement : agreementList) {
                    if(StrUtil.isEmpty(agreement.getShippingUscc()) || StrUtil.isEmpty(agreement.getCustomerUscc()) || StrUtil.isEmpty(agreement.getSaleOrgUscc())){
                        String code = agreement.getCode();
                        sb.append(code + ",");
                    }
                }
                if(StrUtil.isNotEmpty(sb)){
                    String substring = sb.substring(0,sb.length()-1);
                    log.info("请检查合同编号：{}的发运组织,客户,收/发货单组织存在空的数据", substring);
                    throw new BusinessException("请检查当前所选数据的发运组织,客户,收/发货单组织存在为空的数据");
                }

                // 系统来源：三方数据,查询发运组织,客户,收/发货单组织是否真是存在
                for (FrameAgreement thirdAgreement : agreementList) {
                    // 1.发运组织根据发运组织主键查询是否启用
                    log.info("根据NcId(货主唯一标识)查询货主平台配置开始,入参:{}", thirdAgreement.getShippingMain());
                    Result<CargoOwnerInfoResult> settingResult = settingFeign.getCargoOwnerByOwnerCode(thirdAgreement.getShippingMain());
                    log.info("根据NcId(货主唯一标识)查询货主平台配置结束:{}", JSONObject.toJSON(settingResult));
                    // 停用,提示报错  enableStatus 停启用状态(0 停用; 1启用)
                    if(!settingResult.isSuccess() || settingResult.getData() == null || settingResult.getData().getEnableStatus() == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(settingResult.getData().getEnableStatus())){
                        throw new BusinessException("启用失败,公司信息未启用或不存在,请调整后重新启用");
                    }

                    // 2.发运组织,客户,收发货单组织根据统一信用码查询是否存在
                    List<String> usccList = Lists.newArrayList();
                    usccList.add(thirdAgreement.getShippingUscc());
                    usccList.add(thirdAgreement.getCustomerUscc());
                    usccList.add(thirdAgreement.getSaleOrgUscc());
                    // 统一社会信用码 去重
                    usccList = usccList.stream().distinct().collect(Collectors.toList());

                    log.info("根据统一信用码批量查询公司信息开始,入参:{}", JSONObject.toJSON(usccList));
                    Result<Map<String, CompanyVo>> companyResult = companyFeign.listCompanyByIdentifiers(usccList);
                    log.info("根据统一信用码批量查询公司信息结束:{}", JSONObject.toJSON(companyResult));
                    // 基础信息,发运组织,客户,收发货单组织 未启用或者未认证,合同状态设置为禁用
                    if(!companyResult.isSuccess() || companyResult.getData() == null){
                        throw new BusinessException("公司信息未启用未认证或不存在");
                    }
                    for (String uscc : usccList) {
                        if(!companyResult.getData().containsKey(uscc) || companyResult.getData().get(uscc) == null){
                            throw new BusinessException("公司信息未启用未认证或不存在");
                        }
                        CompanyVo data = companyResult.getData().get(uscc);
                        // 1.3.未启用或者未认证，记录日志，程序继续执行。 启用状态 0禁用 1启用    认证状态: 0未认证 1已认证 2认证中
                        if(data.getEnableStatus() == null || !data.getEnableStatus().equals(1L) || data.getCertificationState() == null || !data.getCertificationState().equals(1L)){
                            throw new BusinessException("公司信息未启用未认证或不存在");
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据合同编号查询虚拟协议为否且已启用的框架合同协议
     * @param contractCode
     * @return
     */
    @Override
    public List<FrameAgreementVo> queryVirtualEnableByCode(String contractCode) {
        List<FrameAgreementVo> agreementList = frameAgreementMapper.queryVirtualEnableByCode(contractCode);
        return agreementList;
    }

    /**
     * 关联销售合同
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void relateContract(FrameAgreementRelateSaleContractParam param) {
        LocalDateTime now = LocalDateTime.now();
        SysUser sysUser = LoginUserContextHolder.getUser();
        Long agreementId = param.getAgreementId();
        Long saleContractId = param.getContractId();

        // a.需要校验当前销售合同,被绑定或被禁用,存在这两个状态提示"当前销售合同不存在,请重新查询"
        SalesContract logisticsContract = logisticsContractMapper.selectById(saleContractId);
        if(logisticsContract == null){
            throw new BusinessException("当前销售合同不存在,请重新查询");
        }
        // 已绑定 或者 已禁用 ,提示"当前销售合同不存在,请重新查询"
        if(logisticsContract.getFrameAgreementId() != null || logisticsContract.getDisabled()){
            throw new BusinessException("当前销售合同不存在,请重新查询");
        }
        // 禁用的框架合同无法在进行关联其他的销售合同
        FrameAgreement agreement = frameAgreementMapper.selectById(agreementId);
        if(agreement.getDisabled()){
            throw new BusinessException("禁用的框架合同无法在进行关联其他的销售合同");
        }
        // 关联月份
        String reMonth = param.getReMonth();
        Boolean reMergeState = false;

        //查询是否存在可以关联的子表
        LambdaQueryWrapper<FrameAgreementSub> subWrapper = new LambdaQueryWrapper<>();
        subWrapper.eq(FrameAgreementSub::getAgreementId, agreementId);
        subWrapper.eq(FrameAgreementSub::getRelate, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
        subWrapper.eq(FrameAgreementSub::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
        subWrapper.last("limit 1");
        if (reMonth==null){
            subWrapper.isNull(FrameAgreementSub::getMonth);
        }else{
            subWrapper.eq(FrameAgreementSub::getMonth, reMonth);
        }
        FrameAgreementSub agreementSub = frameAgreementSubMapper.selectOne(subWrapper);
        if (agreementSub != null) {
            LambdaUpdateWrapper<FrameAgreementSub> subUpdateWrapper = new LambdaUpdateWrapper<>();
            subUpdateWrapper.set(FrameAgreementSub::getRelate, 1);
            subUpdateWrapper.set(FrameAgreementSub::getSaleContractId, saleContractId);
            subUpdateWrapper.set(FrameAgreementSub::getModifyId, sysUser.getId());
            subUpdateWrapper.set(FrameAgreementSub::getModifyTime, now);
            subUpdateWrapper.set(FrameAgreementSub::getModifyName, sysUser.getName());
            subUpdateWrapper.eq(FrameAgreementSub::getId, agreementSub.getId());
            frameAgreementSubMapper.update(null, subUpdateWrapper);
            // 合并状态 0：未合并 1：已合并
            reMergeState = agreementSub.getMergeState().equals(0) ? false : true;
        }
        // 销售合同绑定协议id
        LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SalesContract::getFrameAgreementId, agreementId);
        updateWrapper.set(SalesContract::getFrameAgreementCode, agreement.getCode());
        updateWrapper.set(SalesContract::getReMonth, reMonth);
        updateWrapper.set(SalesContract::getReMergeState, reMergeState);
        updateWrapper.set(SalesContract::getReRemark, param.getReRemark().trim());
        updateWrapper.set(SalesContract::getModifyId, sysUser.getId());
        updateWrapper.set(SalesContract::getModifyTime, now);
        updateWrapper.set(SalesContract::getModifyName, sysUser.getName());
        updateWrapper.eq(SalesContract::getId, saleContractId);
        logisticsContractMapper.update(null, updateWrapper);

        FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
        beforeAgreement.setReContractCode(Constant.DEFAULT_VALUE);
        beforeAgreement.setCode(agreement.getCode());

        FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
        afterAgreement.setReContractCode(logisticsContract.getCode());
        afterAgreement.setCode(agreement.getCode());
        afterAgreement.setReSource(Constant.Frame_Agreement_Relate_Sales_Contract_Source.MANUAL);
        saveHistoryRecord(sysUser.getName(), now, beforeAgreement, afterAgreement, FrameAgreementHistoryOperationTypeEnum.RE_LOGIS_CONTRACT.getType());

        //销售合同记录关联框架合同日志
        SalesContract beforeSalesContract = new SalesContract();
        beforeSalesContract.setId(logisticsContract.getId());
        beforeSalesContract.setCode(logisticsContract.getCode());
        beforeSalesContract.setName(logisticsContract.getName());
        beforeSalesContract.setFrameAgreementCode(Constant.DEFAULT_VALUE);
        SalesContract afterSalesContract = new SalesContract();
        afterSalesContract.setId(logisticsContract.getId());
        afterSalesContract.setCode(logisticsContract.getCode());
        afterSalesContract.setName(logisticsContract.getName());
        afterSalesContract.setFrameAgreementCode(agreement.getCode());
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setBeforeSalesContract(beforeSalesContract);
        historyDTO.setAfterSalesContract(afterSalesContract);
        historyDTO.setOperationType(SalesContractHistoryOperationTypeEnum.RE_FRAME_CONTRACT.getType());
        salesContractHistoryService.saveHistoryRecord(sysUser.getName(), now, historyDTO);

    }

    /**
     * 取消关联销售合同
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelRelateContract(FrameAgreementCancelRelateSaleContractParam param) {
        LocalDateTime now = LocalDateTime.now();
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        Long agreementId = param.getAgreementId();
        List<Long> contractIds = param.getContractIdList();
        FrameAgreement agreement = frameAgreementMapper.selectById(agreementId);

        List<SalesContract> logisticsContractList = logisticsContractMapper.selectBatchIds(contractIds);
        if(CollUtil.isEmpty(logisticsContractList) || contractIds.size() != logisticsContractList.size()){
            throw new BusinessException("未查询到销售合同信息");
        }

        LambdaQueryWrapper<FrameAgreementSub> subWrapper = new LambdaQueryWrapper<>();
        subWrapper.eq(FrameAgreementSub::getAgreementId, agreementId);
        subWrapper.eq(FrameAgreementSub::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
        List<FrameAgreementSub> agreementSubs = frameAgreementSubMapper.selectList(subWrapper);

        List<String> relateMonths = logisticsContractList.stream().map(SalesContract::getReMonth).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(relateMonths)){
            for (String relateMonth : relateMonths) {
                // 根据协议id查询框架合同,判断框架合同的关联月份是否有重复的数据,如果有,则不标记为未关联,且不清除关联的合同id
                List<Long> agreementIds = new ArrayList<>();
                agreementIds.add(agreementId);
                List<FrameAgreementRelateSaleContractVo> contractVoList = logisticsContractMapper.queryRelateContractByAgreementId(agreementIds);
                long count = contractVoList.stream().filter(x -> StrUtil.isNotEmpty(x.getReMonth()) && x.getReMonth().equals(relateMonth)).count();
                if(count > 1){
                    continue;
                }

                if(CollUtil.isNotEmpty(agreementSubs)){
                    // 根据框架合同月份,筛选出子表id
                    for (FrameAgreementSub sub : agreementSubs) {
                        if(sub.getMonth().equals(relateMonth)){
                            // 标记为未关联,且清除关联的合同id
                            LambdaUpdateWrapper<FrameAgreementSub> updateWrapper = new LambdaUpdateWrapper<>();
                            updateWrapper.set(FrameAgreementSub::getRelate, 0);
                            updateWrapper.set(FrameAgreementSub::getSaleContractId, null);
                            updateWrapper.set(FrameAgreementSub::getModifyId, sessionInfo.getId());
                            updateWrapper.set(FrameAgreementSub::getModifyTime, now);
                            updateWrapper.set(FrameAgreementSub::getModifyName, sessionInfo.getName());
                            updateWrapper.eq(FrameAgreementSub::getId, sub.getId());
                            frameAgreementSubMapper.update(null,updateWrapper);
                        }
                    }
                }
            }
        }

        LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SalesContract::getFrameAgreementId, null);
        updateWrapper.set(SalesContract::getFrameAgreementCode, null);
        updateWrapper.set(SalesContract::getReMonth, null);
        updateWrapper.set(SalesContract::getReMergeState, null);
        updateWrapper.set(SalesContract::getReRemark, null);
        updateWrapper.set(SalesContract::getModifyId, sessionInfo.getId());
        updateWrapper.set(SalesContract::getModifyName, sessionInfo.getName());
        updateWrapper.set(SalesContract::getModifyTime, now);
        updateWrapper.in(SalesContract::getId, contractIds);
        logisticsContractMapper.update(null, updateWrapper);

        // 记录历史记录
        StringBuffer historySb = new StringBuffer();
        for (SalesContract contract : logisticsContractList) {
            historySb.append(contract.getCode() +"，");
        }
        FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
        beforeAgreement.setReContractCode(historySb.substring(0, historySb.length()-1));
        beforeAgreement.setCode(agreement.getCode());

        FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
        afterAgreement.setReContractCode(Constant.DEFAULT_VALUE);
        afterAgreement.setCode(agreement.getCode());
        afterAgreement.setReSource(Constant.Frame_Agreement_Relate_Sales_Contract_Source.MANUAL);
        saveHistoryRecord(sessionInfo.getName(), now, beforeAgreement, afterAgreement, FrameAgreementHistoryOperationTypeEnum.CANCEL_RE_LOGIS_CONTRACT.getType());

        //记录销售合同变更历史记录
        FrameAgreement frameAgreement = getById(agreementId);
        for (SalesContract salesContract : logisticsContractList) {
            SalesContract beforeSalesContract = new SalesContract();
            beforeSalesContract.setId(salesContract.getId());
            beforeSalesContract.setCode(salesContract.getCode());
            beforeSalesContract.setName(salesContract.getName());
            beforeSalesContract.setFrameAgreementCode(frameAgreement.getCode());
            SalesContract afterSalesContract = new SalesContract();
            afterSalesContract.setId(salesContract.getId());
            afterSalesContract.setCode(salesContract.getCode());
            afterSalesContract.setName(salesContract.getName());
            afterSalesContract.setFrameAgreementCode(Constant.DEFAULT_VALUE);
            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setBeforeSalesContract(beforeSalesContract);
            historyDTO.setAfterSalesContract(afterSalesContract);
            historyDTO.setOperationType(SalesContractHistoryOperationTypeEnum.CANCEL_RE_FRAME_CONTRACT.getType());
            salesContractHistoryService.saveHistoryRecord(sessionInfo.getName(), now, historyDTO);
        }
    }

    /**
     * 分页查询框架合同协议历史记录
     * @param param
     * @return
     */
    @Override
    public ApiPageResult<FrameAgreementHistoryListVo> getHistoryList(FrameAgreementHistoryListParam param) {
        ApiPageResult result = new ApiPageResult();
        // 历史记录默认显示为空
        if(StrUtil.isEmpty(param.getOperator()) && StrUtil.isEmpty(param.getAgreeNameCode()) && param.getDisabled() == null){
            return result;
        }
        // 动态构建查询条件
        Query query = this.buildQuery(param);
        // 总条数
        long total = mongoTemplate.count(query, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);

        // 设置分页参数
        long offset = (param.getPageNum() - 1) * param.getPageSize();
        // 分页
        query.skip(offset).limit(param.getPageSize().intValue());

        // 执行查询
        List<FrameAgreementHistoryDto> datas = mongoTemplate.find(query, FrameAgreementHistoryDto.class, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        List<FrameAgreementHistoryListVo> responseVoList = new ArrayList<>();

        //转换数据
        if (CollectionUtils.isNotEmpty(datas)) {
            for (int i = 0; i < datas.size(); i++) {
                FrameAgreementHistoryDto historyDto = datas.get(i);
                FrameAgreementFixedMetadataVo fixedMetadataVo = historyDto.getFixedMetadata();
                FrameAgreementHistoryListVo responseVo = new FrameAgreementHistoryListVo();
                responseVo.setId(historyDto.getId());

                if (fixedMetadataVo != null) {
                    responseVo.setName(fixedMetadataVo.getName());
                    responseVo.setCode(fixedMetadataVo.getCode());

                    if (historyDto.getOperation_type().equals(FrameAgreementHistoryOperationTypeEnum.UPDATE_STATE.getType())){
                        // 修改前后不同,显示修改后的状态  停用标记 0: 未停用 1: 已停用
                        responseVo.setDisabled(fixedMetadataVo.getAfterDisabled() ? "1" : "0");
                        responseVo.setAdjustContent("");
                    } else {
                        // 修改前后相同,说明没有修改合同状态
                        responseVo.setDisabled("");
                        responseVo.setAdjustContent("1");
                    }
                }

                responseVo.setOperateTime(DateUtil.format(historyDto.getOperation_time(), "yyyy-MM-dd HH:mm:ss"));//修改操作时间;
                responseVo.setOperateUser(historyDto.getOperator());//操作人姓名
                responseVo.setOperateType(FrameAgreementHistoryOperationTypeEnum.getOperationDesc(historyDto.getOperation_type()));//操作类型;

                responseVoList.add(responseVo);
            }
        }

        result.setList(responseVoList);
        result.setTotal((int) total);
        result.setPageSize(param.getPageSize());
        result.setCurrentPage(param.getPageNum());

        return result;
    }

    /**
     * 查询合同协议历史记录的详情
     * @param recordId
     * @return
     */
    @Override
    public FrameAgreementHistoryDetailVo getHistoryDetailById(String recordId) {
        FrameAgreementHistoryDetailVo detailResponse = new FrameAgreementHistoryDetailVo();
        FrameAgreementHistoryDto businessHistory = mongoTemplate.findById(recordId, FrameAgreementHistoryDto.class, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        if (businessHistory != null && businessHistory.getChanges() != null) {
            FrameAgreementFixedMetadataVo fixedMetadataVo = businessHistory.getFixedMetadata();
            ChangesDto changes = businessHistory.getChanges();//变更前后的内容;
            String beforeJson = changes.getBefore();
            String afterJson = changes.getAfter();

            // 字符转换
            if(StrUtil.isNotEmpty(beforeJson)){
                JSONObject beforeJsonObject = JSONObject.parseObject(beforeJson, JSONObject.class);
                beforeJson = changeDto(beforeJsonObject);
            }

            if(StrUtil.isNotEmpty(afterJson)){
                JSONObject afterJsonObject = JSONObject.parseObject(afterJson, JSONObject.class);
                afterJson = changeDto(afterJsonObject);
            }

            detailResponse.setCode(fixedMetadataVo.getCode());
            detailResponse.setBeforeContent(beforeJson);
            detailResponse.setAfterContent(afterJson);
        }
        return detailResponse;
    }

    /**
     * 判断框架合同是否启用（供前端日志详情展示【重新创建】按钮使用）
     * @param mainId
     * @return Boolean true:启用 false:禁用或者无单据
     */
    @Override
    public Boolean queryDisabledByMainId(String mainId) {
        // 查询启用的框架合同数
        LambdaQueryWrapper<FrameAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FrameAgreement::getMainId, mainId);
        wrapper.eq(FrameAgreement::getDisabled, 0);
        wrapper.eq(FrameAgreement::getDeleted,0);
        Long count = frameAgreementMapper.selectCount(wrapper);
        if(count > 0){
            // 启用
            return true;
        }
        return false;
    }

    //组装查询参数;
    private Query buildQuery(FrameAgreementHistoryListParam param) {

        // 动态构建查询条件
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("business_type").is(Constant.TMS_CONTRACT_FRAME_AGREEMENT_MODULE);

        //框架合同编号
        if (StrUtil.isNotEmpty(param.getAgreeNameCode())) {
            criteria.and("fixed_metadata.code").is(param.getAgreeNameCode());
        }

        //操作人
        if (StrUtil.isNotEmpty(param.getOperator())) {
            criteria.and("operator").is(param.getOperator());
        }

        //合同状态 停用标记 0: 未停用 1: 已停用
        if (param.getDisabled() != null) {
            criteria.and("fixed_metadata.afterDisabled").is(param.getDisabled());
        }

        // 设置条件及排序规则
        query.addCriteria(criteria).with(Sort.by(Sort.Direction.DESC, "operation_time"));

        return query;
    }

    /**
     * 更新框架合同协议
     * @param agreement
     */
    private void updateFrameAgreement(FrameAgreement agreement) {
        LambdaUpdateWrapper<FrameAgreement> updateAgreementWrapper = new LambdaUpdateWrapper<>();
        updateAgreementWrapper.set(FrameAgreement::getCode, agreement.getCode());
        updateAgreementWrapper.set(FrameAgreement::getShipping, agreement.getShipping());
        updateAgreementWrapper.set(FrameAgreement::getShippingUscc, agreement.getShippingUscc());
        updateAgreementWrapper.set(FrameAgreement::getShippingMain, agreement.getShippingMain());
        updateAgreementWrapper.set(FrameAgreement::getCustomer, agreement.getCustomer());
        updateAgreementWrapper.set(FrameAgreement::getCustomerUscc, agreement.getCustomerUscc());
        updateAgreementWrapper.set(FrameAgreement::getSaleOrg, agreement.getSaleOrg());
        updateAgreementWrapper.set(FrameAgreement::getSaleOrgUscc, agreement.getSaleOrgUscc());
        updateAgreementWrapper.set(FrameAgreement::getYear, agreement.getYear());
        updateAgreementWrapper.set(FrameAgreement::getTotalVolume, agreement.getTotalVolume());
        updateAgreementWrapper.set(FrameAgreement::getProductLineId, agreement.getProductLineId());
        updateAgreementWrapper.set(FrameAgreement::getProductLineCode, agreement.getProductLineCode());
        updateAgreementWrapper.set(FrameAgreement::getProductLineName, agreement.getProductLineName());
        updateAgreementWrapper.set(FrameAgreement::getEffectiveDate, agreement.getEffectiveDate());
        updateAgreementWrapper.set(FrameAgreement::getExpiryDate, agreement.getExpiryDate());
        updateAgreementWrapper.set(FrameAgreement::getVirtualTag, agreement.getVirtualTag());
        updateAgreementWrapper.set(FrameAgreement::getParentId, agreement.getParentId());
        updateAgreementWrapper.set(FrameAgreement::getNum, agreement.getNum());
        updateAgreementWrapper.set(FrameAgreement::getRemark, agreement.getRemark());
        updateAgreementWrapper.set(FrameAgreement::getBusiSource, agreement.getBusiSource());
        updateAgreementWrapper.set(FrameAgreement::getSystemSource, agreement.getSystemSource());
        updateAgreementWrapper.set(FrameAgreement::getMainId, agreement.getMainId());
        updateAgreementWrapper.set(FrameAgreement::getBillDate, agreement.getBillDate());
        updateAgreementWrapper.set(FrameAgreement::getDisabled, agreement.getDisabled());

        updateAgreementWrapper.set(FrameAgreement::getModifyId, agreement.getModifyId());
        updateAgreementWrapper.set(FrameAgreement::getModifyTime, agreement.getModifyTime());
        updateAgreementWrapper.set(FrameAgreement::getModifyName, agreement.getModifyName());

        updateAgreementWrapper.eq(FrameAgreement::getId, agreement.getId());
        frameAgreementMapper.update(null, updateAgreementWrapper);
    }

    /**
     * 手动创建订单参数校验
     */
    private Result<String> checkManualParam(FrameAgreementSaveParam param) {
        // 新建
        if(param.getId() == null){
            // 手动创建的合同编号不能重复,「合同编号」系统唯一,不可重复,校验是否存在,存在,不可保存
            LambdaQueryWrapper<FrameAgreement> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FrameAgreement::getCode, param.getCode());
            queryWrapper.eq(FrameAgreement::getDeleted,0);
            Long count = frameAgreementMapper.selectCount(queryWrapper);
            if(count > 0){
                throw new BusinessException(ResultCode.VALIDATOR.getCode(), "合同编号不能重复!");
            }
        }

        // 「生效日期」要早于「失效日期」
        if(param.getEffectiveDate() != null && param.getExpiryDate() != null && param.getEffectiveDate().isAfter(param.getExpiryDate())){
            throw new BusinessException(ResultCode.VALIDATOR.getCode(), "生效日期必须大于失效日期");
        }

        return Result.ok();
    }
}
