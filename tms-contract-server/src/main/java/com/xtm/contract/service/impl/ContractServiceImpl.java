package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.constant.ContractConstant;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.constant.FddSignResultConstant;
import com.xtm.contract.constant.FindCarConstantResult;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.feign.DocumentFeginClient;
import com.xtm.contract.feign.FileFeignAdapter;
import com.xtm.contract.feign.MotorcadeServiceFeign;
import com.xtm.contract.feign.OrderFeign;
import com.xtm.contract.feign.SettingServiceFeign;
import com.xtm.contract.feign.TmsCompanyService;
import com.xtm.contract.feign.TmsFileService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.feign.UserActionExtFeginClient;
import com.xtm.contract.mapper.ContractAttachMapper;
import com.xtm.contract.mapper.ContractChargeMapper;
import com.xtm.contract.mapper.ContractExtMapper;
import com.xtm.contract.mapper.ContractGoodsMapper;
import com.xtm.contract.mapper.ContractMapper;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.bo.AddressInfo;
import com.xtm.contract.model.cargo.GoodsMeasuring;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.domain.ContractAttach;
import com.xtm.contract.model.domain.ContractCharge;
import com.xtm.contract.model.domain.ContractExt;
import com.xtm.contract.model.domain.ContractGoods;
import com.xtm.contract.model.domain.ContractMigrate;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.ContractPreviewEnum;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.file.DownloadIn;
import com.xtm.contract.model.organization.Address;
import com.xtm.contract.model.param.ContactParam;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.FindCarChargeSummaryPdfParam;
import com.xtm.contract.model.param.UpdateContractDataParam;
import com.xtm.contract.model.query.contract.ContractGoodsJsonReq;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.query.contract.ContractPreviewReq;
import com.xtm.contract.model.query.contractOther.DocumentInfoQryIn;
import com.xtm.contract.model.query.contractOther.PaymentSchedule;
import com.xtm.contract.model.query.contractOther.TransportCharge;
import com.xtm.contract.model.query.contractOther.TransportChargeDetail;
import com.xtm.contract.model.query.contractOther.TransportChargeItem;
import com.xtm.contract.model.query.eqbDto.ESignAccountDTO;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.query.eqbReq.EFileInfoReq;
import com.xtm.contract.model.req.BusinessExpansionFieldReq;
import com.xtm.contract.model.req.CommonGoodsReq;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.FindCarChargeSummaryPdfVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import com.xtm.contract.model.vo.contract.ContractChargeVO;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.contract.ContractDispatchBatchVo;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.model.vo.contract.ContractPaymentVO;
import com.xtm.contract.model.vo.contract.DispatchBatchContractVo;
import com.xtm.contract.model.vo.contract.DispatchGoodsInfoVo;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.contractOther.ContractChargeSubjectsInfo;
import com.xtm.contract.model.vo.contractOther.ContractPaymentInfo;
import com.xtm.contract.model.vo.contractOther.SupplementContractInfo;
import com.xtm.contract.model.vo.eqb.ContractSignInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.model.vo.fdd.ContractReq;
import com.xtm.contract.model.vo.fdd.FddConfigInfo;
import com.xtm.contract.model.vo.fdd.Response.ContractVoRes;
import com.xtm.contract.service.ChargeService;
import com.xtm.contract.service.ContractEqbSignService;
import com.xtm.contract.service.ContractMigrateService;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.ContractTemplateService;
import com.xtm.contract.service.ElectricSealSwitchService;
import com.xtm.contract.service.FddFeignService;
import com.xtm.contract.utils.ContractSessionUtil;
import com.xtm.contract.utils.DateUtil;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.FileHelper;
import com.xtm.contract.utils.OrganizationOrSettingHelper;
import com.xtm.contract.utils.PdfHelper;
import com.xtm.file.model.vo.FileInfoVo;
import com.xtm.motorcade.model.vo.DriverVo;
import com.xtm.setting.model.dto.AddressDto;
import com.xtm.setting.model.vo.AddressDetailsVo;
import com.xtm.setting.model.vo.DictionaryVo;
import com.xtm.thirdparty.auth.feign.ContractFddSignFeign;
import com.xtm.thirdparty.auth.feign.TmsFddElectricSealFeign;
import com.xtm.thirdparty.auth.model.req.SignIntegrationExtReq;
import com.xtm.thirdparty.auth.model.resp.ElectricSealResponse;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.FddExtsignAutoResponse;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.thirdparty.auth.model.resp.QuerySignStatusRes;
import com.xtm.thirdparty.auth.model.vo.ContractSignResVo;
import com.xtm.user.feign.UserFeign;
import com.xtm.user.model.dto.UserInfoDto;
import com.xtm.user.model.vo.ContactVo;
import com.xtm.user.model.vo.UserDataAuthVo;
import com.xtm.user.model.vo.UserInfoVo;
import com.xtm.utils.collection.CollectionUtils;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author: zt
 * @Desc: 合同业务SERVICE
 * @date: 2021/6/25 14:43
 * @version: 1.0
 */
@Slf4j
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private SettingServiceFeign settingService;
    @Autowired
    private ContractGoodsMapper contractGoodsMapper;
    @Autowired
    private ContractChargeMapper contractChargeMapper;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private TmsUserService tmsUserService;
    @Resource
    private TmsFileService tmsFileService;

    @Autowired
    private TmsCompanyService tmsCompanyService;
    @Autowired
    private ContractEqbSignService eqbSignService;
    @Autowired
    private DocumentFeginClient documentFeginClient;

    @Autowired
    private OrganizationOrSettingHelper organizationOrSettingHelper;

    @Resource
    private MotorcadeServiceFeign motorcadeServiceFeign;
    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private EqbHelper eqbHelper;

    @Autowired
    private ContractExtMapper contractExtMapper;

    @Autowired
    private ElectricSealSwitchService electricSealSwitchService;

    @Autowired
    private ContractAttachMapper attachMapper;

    @Autowired
    private NacosValueConfig nacosValueConfig;
    @Lazy
    @Autowired
    private ContractAsyncServiceImpl contractAsyncService;
    @Autowired
    private OrderFeign orderFeign;

    @Resource
    private UserActionExtFeginClient userActionExtFeginClient;

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractMigrateService contractMigrateService;

    @Resource
    UserFeign userFeign;
    /**
     *交易模块菜单id
     */
    private final String CONTRACT_MODEL_MENUID = "IqMvskipRBOCbuFd4zYhLw";

    @Autowired
    private FileFeignAdapter fileFeignAdapter3;

    @Resource
    private ContractFddSignFeign contractFddSignFeign;

    @Resource
    private FddFeignService fddFeignService;

    @Resource
    private TmsFddElectricSealFeign tmsFddElectricSealFeign;

    @Resource
    private ChargeService chargeService;


    @Override
    public Result  createContract(ContractCreUpdReq contractCreUpdParam) {
        // 查询签章开关标识（0：e签宝；1：法大大）
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        Integer signSwitchTag = electricSealSwitch.getSignSwitchTag();

        log.info("开始生成合同  signSwitchTag={},contractCreUpdParam={}",signSwitchTag,JSON.toJSONString(contractCreUpdParam));
        boolean flag = checkContractCreParam(contractCreUpdParam);
        if (!flag) {
            log.error("单据合同生成失败，原因：参数校验失败！");
            return Result.error("单据合同生成失败，原因：参数校验失败！");
        }
        boolean createFlag = contractCreUpdParam.isCreateFlag();// 是否是新合同
        log.info("生成合同documentId={}，createFlag={}", contractCreUpdParam.getDocumentId(), createFlag);
        String parentId = ""; // 批量/联合运输父编码id
        Contract pContract = null;
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);
        String currentUserId = ContractSessionUtil.getCurrentUserID(sessionInfo);
        if (contractCreUpdParam.getParentContractReq() != null){
            pContract = selectContractByDocumentId(contractCreUpdParam.getParentContractReq().getDocumentId());
            boolean pCheckFlag = false;
            if (pContract == null) {
                pContract = new Contract();
                parentId = IdWorker.getIdStr();
                pContract.setId(parentId);
                pCheckFlag = true;
            }
            assemblyContractCreBaseInfo(contractCreUpdParam,currentCompanyId,pContract);
            /** 重置父订单信息 **/
            //单据ID
            pContract.setDocumentId(contractCreUpdParam.getParentContractReq().getDocumentId());
            //单据编号
            pContract.setDocumentCode(contractCreUpdParam.getParentContractReq().getDocumentCode());
            //合同编号
            pContract.setContractCode(contractCreUpdParam.getParentContractReq().getDocumentCode());
            if (pCheckFlag){
                Integer ver = 1;
                pContract.setCreater(contractCreUpdParam.getCreater());
                pContract.setCreateTime(LocalDateTime.now());
                pContract.setVer(ver);
                pContract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
                pContract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
                contractMapper.insert(pContract);
                saveContractExt(pContract.getId(),Objects.equals(signSwitchTag, ContractConstant.ContractType.FDD) ? ContractConstant.SignType.FDD : ContractConstant.SignType.ECB,contractCreUpdParam.getCarrierContractIdCardNo());
            } else {
                parentId = pContract.getId();
                Date date = new Date();
                //单据合同签署时间即交易时间
                pContract.setContractDate(date);
                // 创建时间
                pContract.setModifyTime(LocalDateTime.now());
                //分表修改时不能修改创建时间
                pContract.setCreateTime(null);
                contractMapper.updateById(pContract);
            }
        }
        //是否新增
        Contract contract = new Contract();
        String id = "";
        if (createFlag) {
            id = IdWorker.getIdStr();
            contract.setId(id);
        } else {
            contract = selectContractByDocumentId(contractCreUpdParam.getDocumentId());
            if (contract == null) {
                contract = new Contract();
                id = IdWorker.getIdStr();
                contract.setId(id);
                createFlag = true;
            } else {
                id = contract.getId();
            }
        }
        //组装合同基本信息
        assemblyContractCreBaseInfo(contractCreUpdParam,currentCompanyId,contract);
        if (StringUtils.isNotBlank(parentId)){
            contract.setParentContractId(parentId);
        }
        if (StrUtil.isBlank(contract.getContractTemplateId())) {
            return Result.error(CommonLang.NOSUCHMETHOD_FAIL.getCode(), "未配置该类型业务的合同模板，请至合同中心配置");
        }
        //新增
        if (createFlag) {
            Integer ver = 1;
            contract.setCreater(contractCreUpdParam.getCreater());
            contract.setCreateTime(LocalDateTime.now());
            contract.setVer(ver);
            contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            contractMapper.insert(contract);
            saveContractExt(contract.getId(),Objects.equals(signSwitchTag, ContractConstant.ContractType.FDD) ? ContractConstant.SignType.FDD : ContractConstant.SignType.ECB,contractCreUpdParam.getCarrierContractIdCardNo());
            //新增费用信息
            assemblyAddContractCharge(contractCreUpdParam,id,ver,createFlag);
        } else {
            contract.setModifier(currentUserId);
            contract.setModifyTime(LocalDateTime.now());
            contract.setCreateTime(null);
            contractMapper.updateById(contract);
            //修改费用信息
            assemblyAddContractCharge(contractCreUpdParam,id,null,createFlag);
        }
        //删除当前合同下所有相关货物
        deleteByContractId(id);
        //添加合同货物信息
        assemblyAddContractGoods(contractCreUpdParam, id);
        //异步后置处理方法
        // 是否静默签署
        Boolean eqbSignFlg = contractCreUpdParam.getEqbSignFlg();
        log.info("====> 创建合同，是否静默签署：{} <====", eqbSignFlg);
        if (eqbSignFlg) {
            String contractId = id;
            if (signSwitchTag.equals(ContractConstant.ContractType.FDD)) {
                contractAsyncService.fddAfterContractSave(contractCreUpdParam, contractId, sessionInfo);
            } else {
                contractAsyncService.afterContractSave(contractCreUpdParam, contractId, sessionInfo);
            }
        }
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    public String updateFddPdfId(String contractId, SysUser sessionInfo) {
        log.info("从法大大下载电子签章并上传文件服务器开始, contractId={}", contractId);
        Result<FileInfoOut> fddResult = contractFddSignFeign.downLoadEcPdfId(contractId,sessionInfo);
        log.info("从法大大下载电子签章并上传文件服务器结束, fddResult={}", fddResult);
        if(!fddResult.isSuccess()){
            log.error("从法大大下载电子签章并上传文件服务器失败, message={}", fddResult.getMessage());
            Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
            throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
        }
        FileInfoOut fileInfo = fddResult.getData();
        updateFddPdfToContract(fileInfo.getFileID(), contractId);
        return fileInfo.getFileUrl();
    }

    @Override
    public void batchCreateContract(List<ContractCreUpdReq> contractCreUpdReqs) {
        log.info("Batch create contract start!");
        if(CollUtil.isEmpty(contractCreUpdReqs)){
            log.error("批量生成合同的参数不能为空！");
            return;
        }
        log.info("The number of create contracts: "+contractCreUpdReqs.size());
        contractCreUpdReqs.forEach(contractCreUpdReq -> this.createContract(contractCreUpdReq));
        log.info("Batch create contract end!");
    }

    /**
     * 单独统计货物和计量
     * @param commonGoodsReqList
     * @param id
     */
    private void statisticalGoodsList(List<CommonGoodsReq> commonGoodsReqList,String id) {
        if (commonGoodsReqList.isEmpty()) {
            log.info("创建组装的货物参数是空的");
            return;
        }
        String totalGood = getListGooodDesc(commonGoodsReqList);//获取货物汇总描述
        String totalGoodMeasuring = getTotalGoodMeasuring(commonGoodsReqList);//获取货物计量汇总

        Contract contract = contractMapper.selectById(id);
        contract.setContractGoodsCollection(totalGood);
        contract.setContractMeasuringCollection(totalGoodMeasuring);
        contract.setCreateTime(null);
        contractMapper.updateById(contract);
    }

    @Override
    public void createSupplementContract(ContractCreUpdReq contractCreUpdParam) throws Exception {
        log.info("创建补充合同-请求参数为:{}",JSON.toJSONString(contractCreUpdParam));
        boolean flag = checkContractCreParam(contractCreUpdParam);
        if (!flag) {
            log.error("参数不能为空！");
            throw new BusinessException(ResultCode.VALIDATOR.getCode(), "参数不能为空!");
        }

        Contract contract = new Contract();
        String id = IdWorker.getIdStr();
        contract.setId(id);
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);
        String documentId = contractCreUpdParam.getDocumentId();

        //当前版本 = 最新版本+1
        Integer ver = selectLastestVer(documentId)+1;
        //当前父合同
        Contract parentContract = selectParentContract(documentId);
        String parentId = parentContract.getId();//父合同id
        String parentCode = parentContract.getContractCode()+"_"+ver;//当前合同CODE = 父合同CODE+VER
        contractCreUpdParam.setDocumentCode(parentCode);
        //组装合同基本信息
        assemblyContractCreBaseInfo(contractCreUpdParam, currentCompanyId, contract);
        log.info("修改结算价格，组装合同基本信息通过，contract信息:{}",contract);
        if (StrUtil.isBlank(contract.getContractTemplateId())) {
            throw new BusinessException(Constant.ERROR_CODE, "未配置该类型业务的合同模板，请至合同中心配置");
        }
        contract.setCreater(contractCreUpdParam.getCreater());
        contract.setParentContractId(parentId);
        contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
        contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
        contract.setCreateTime(LocalDateTime.now());
        contract.setVer(ver);

        // 查询签章开关标识（0：e签宝；1：法大大）
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        Integer signSwitchTag = electricSealSwitch.getSignSwitchTag();
        saveContractExt(contract.getId(),Objects.equals(signSwitchTag, ContractConstant.ContractType.FDD) ? ContractConstant.SignType.FDD : ContractConstant.SignType.ECB,contractCreUpdParam.getCarrierContractIdCardNo());
        contractMapper.insert(contract);
        //新增费用信息
        assemblyAddContractCharge(contractCreUpdParam,id,ver,true);
        //新增合同货物信息
        assemblyAddContractGoods(contractCreUpdParam,id);
        //生成本地合同
        ContractInfoQryVO contractInfo = selectContractDetail(id);

        createFDDContractPdf(contractInfo);
    }

    /**
     * 查询最新版本
     * @param documentId
     * @return Long
     */
    @Override
    public Integer selectLastestVer(String documentId) {
        Contract contract = new Contract();
        contract.setDocumentId(documentId);
        contract.setIsDelete(DicConstant.IS_DELETE.NO);
        Contract lastestCon = contractMapper.selectOne(new QueryWrapper<>(contract).orderByDesc("ver").last("limit 1"));
        if (lastestCon == null) {
            return 0;
        }
        return lastestCon.getVer();
    }

    @Override
    public Contract selectParentContract(String documentId) {
        Contract contract = new Contract();
        contract.setDocumentId(documentId);
        contract.setParentContractId("0");
        contract.setIsDelete(DicConstant.IS_DELETE.NO);
        List<Contract> parentCon = contractMapper.selectList(new QueryWrapper<>(contract).orderByDesc("ver"));
        if (CollUtil.isEmpty(parentCon)) {
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.INVALID_CONTRACT_NO_SUPPLEMENT.getCode());
            String msg = ContractErrorCode.INVALID_CONTRACT_NO_SUPPLEMENT.getMessage();
            throw new BusinessException(code,msg);
        }
        if(parentCon.size() > 1){
            log.info("当前合同存在多个父合同！size:"+parentCon.size());
            throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode()+""),"当前合同存在多个父合同！size:"+parentCon.size());
        }
        return parentCon.get(0);
    }

    private String getCustomerId(String openId,Map<String, FddElectricSealResp> openIdMap){
        log.info("getCustomerId: openId={}", openId);
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        FddElectricSealResp fddElectricSealResp = openIdMap.get(openId);
        if(fddElectricSealResp==null){
            log.info("没有认证 openId={}",openId);
            return null;
        }
        log.info("查询的认证信息 openId={} CustomerId={}",openId,JSON.toJSONString(fddElectricSealResp));
        return fddElectricSealResp.getCustomerId();
    }

    @Override
    public ContractResult fDDCreateEcContractPdf(ContractInfoQryVO contractInfo) {
        log.info("====> 法大大签署合同入参: contractInfo: {} <====", contractInfo);
        ContractResult result = new ContractResult();
        String contractId = contractInfo.getId();
        List<String> openIds = new ArrayList<>();
        try {
            SignIntegrationExtReq signIntegrationReq = new SignIntegrationExtReq();
            signIntegrationReq.setSource(1);
            signIntegrationReq.setContractId(contractId);

            String cardNo = "";
            // 判断使用哪个承运人公司的身份号
            String xtmCompanyId = "";
            Integer documentType = contractInfo.getDocumentType();
            if(DicConstant.DOCUMENT_TYPE.ORDER.equals(documentType)){
                xtmCompanyId = contractInfo.getCarrierCompanyId();
            } else if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(documentType)){
                xtmCompanyId = contractInfo.getTrustorCompanyId();
            }
            if(nacosValueConfig.getXtmTjCompanyId().equals(xtmCompanyId)){
                cardNo = nacosValueConfig.getXtmTjCardNo();
            } else if (nacosValueConfig.getXtmGsCompanyId().equals(xtmCompanyId)) {
                cardNo = nacosValueConfig.getXtmGsCardNo();
            }
            openIds.add(cardNo);
            openIds.add(contractInfo.getCardNo());
            Map<String, FddElectricSealResp> openIdMap = getOpenIdMap(openIds);
            signIntegrationReq.setTrustorCustomerId(getCustomerId(cardNo,openIdMap));
            signIntegrationReq.setCarryCustomerId(getCustomerId(contractInfo.getCardNo(),openIdMap));
            signIntegrationReq.setDocTitle(contractInfo.getTitle());
            signIntegrationReq.setPdfUrl(contractInfo.getEcContractPathUrl());
            signIntegrationReq.setCarrySignKeyword(ContractConstant.signKeyword.CYR);
            signIntegrationReq.setTrustorSignKeyword(ContractConstant.signKeyword.TYR);
            log.info(" 法大大签署合同开始，contractId：{}，signIntegrationReq={}", contractId, signIntegrationReq);
            Result<ContractSignResVo> fddResult = contractFddSignFeign.signIntegration(signIntegrationReq);
            log.info(" 法大大签署合同结束，fddResult:{}", fddResult);
            if (!fddResult.isSuccess()){
                throw new BusinessException(" 法大大签署合同异常:"+fddResult.getMsg());
            }
            //签署成功后，保存状态
            saveEQBFlowIdToContract(contractId, contractId, ContractConstant.SignType.FDD,contractInfo.getCardNo());
        } catch (BusinessException e) {
            log.info("电子签章失败，contractId：{}", contractId);
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                result.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
            log.error("电子签章生成失败", e);
            //错误信息落库
            saveErrorInfoToContract(result, contractId);
        }
        return result;
    }

    @Override
    public void createFDDContractPdf(ContractInfoQryVO contractInfo) {
        ContractInfoQryVO batchCon = selectContractByDocumentId(contractInfo.getDocumentId());
        fDDCreateEcContractPdf(batchCon);
    }


    @Override
    public ContractResult createEcContractPdf(ContractInfoQryVO contractInfo, SysUser sessionInfo) {
        if (contractInfo == null) {
            log.error("创建电子签署的合同不能为空！！！");
            return null;
        }
        ContractResult result = null;
        String contractId = contractInfo.getId();
        try {
            CompanyVasInfo vasInfo = eqbHelper.vasAuth();
            log.info("增值服务授权信息vasInfo：" + JSON.toJSONString(vasInfo));
            //校验订阅服务授权信息
            result = eqbHelper.checkVasInfo(vasInfo);
            log.info("校验订阅服务授权信息result：" + JSON.toJSONString(result));
            if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() != result.getCode()) {
                return result;
            }
            //已归档的也不需要再生成
            String flowId = contractInfo.getEcContractEsignFlowId();
            if (StrUtil.isNotBlank(flowId)) {
                log.info("已归档的合同等待下载，不需要再生成电子合同,归档ID" + flowId);
                return result;
            }
            EContractEcSignReq ecSignReq = new EContractEcSignReq();
            BeanUtils.copyProperties(contractInfo,ecSignReq);
            ecSignReq.setContractId(contractId);
            ecSignReq.setLocalPdfUrl(contractInfo.getEcContractPathUrl());
            EqbConfigInfo configInfo = eqbHelper.getEqbConfigInfo();
            //签署e签宝签章
            flowId = signEqbElectronicSeal(configInfo,ecSignReq,vasInfo);
            contractInfo.setEcContractEsignFlowId(flowId);
        } catch (BusinessException e) {
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                result.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
            log.error("电子签章生成失败",e);
            //错误信息落库
            saveErrorInfoToContract(result,contractId);
        }
        return result;
    }

    /**
     * 签署e签宝印章
     * @param eqbConfigInfo
     * @param ecSignReq
     * @return
     * @throws BusinessException
     */
    @Override
    public String signEqbElectronicSeal(EqbConfigInfo eqbConfigInfo,EContractEcSignReq ecSignReq,CompanyVasInfo vasInfo) throws BusinessException {
        log.info("-----------------START SIGN E-SIGNATURE ELECTRONIC CONTRACT------------------");
        CompanyBO trustorCompanyInfo = ecSignReq.getTrustorCompany();
        CompanyBO carrierCompanyInfo = ecSignReq.getCarryCompany();
        CompanyVo compileSideCompanyInfo = ecSignReq.getCompileSideCompany();

        String contractId = ecSignReq.getContractId();
        log.info("-------------创建托运方E签宝账户-------------");
        String trustorAccountId = eqbSignService.createAccount(eqbConfigInfo,trustorCompanyInfo);

        log.info("-------------创建承运方E签宝账户-------------");
        String carrierAccountId =  eqbSignService.createAccount(eqbConfigInfo,carrierCompanyInfo);

        log.info("-------------上传本地PDF到E签宝-------------");
        EFileInfoReq eFileInfo = eqbSignService.uploadPdfToYQB(eqbConfigInfo,ecSignReq);

        log.info("-------------双方发起签署-------------");
        ESignAccountDTO trustorEsignAccountInfo = assemblyESignAccountInfo(trustorCompanyInfo,eFileInfo.getFileUrl(),trustorAccountId);
        ESignAccountDTO carrierEsignAccountInfo = assemblyESignAccountInfo(carrierCompanyInfo,eFileInfo.getFileUrl(),carrierAccountId);
        ContractSignInfo contractSignInfo = new ContractSignInfo();
        contractSignInfo.setEqbFileId(eFileInfo.getFileId());
        contractSignInfo.setFileName(eFileInfo.getFileName());
        contractSignInfo.setTitle(ecSignReq.getTitle());

        //暂时注释掉意愿签署的代码，二期上
        /*
        boolean createrAuthFlag = getCompileSideAuthStatus(compileSideCompanyInfo);//编制方的是否实名
        boolean trustorSignFlag = getSignedWay(trustorCompanyInfo);//托运方是否意愿签
        if (trustorSignFlag == true) {
            //校验托运方是否是发起方
            realNameAuthentication(eqbConfigInfo,trustorCompanyInfo,compileSideCompanyInfo,createrAuthFlag,trustorEsignAccountInfo,contractSignInfo);
        }
        boolean carrierSignFlag = getSignedWay(carrierCompanyInfo);//承运方是否意愿签
        if (carrierSignFlag == true) {
            //校验承运方是否是发起方
            realNameAuthentication(eqbConfigInfo,carrierCompanyInfo,compileSideCompanyInfo,createrAuthFlag,trustorEsignAccountInfo,contractSignInfo);
        }*/

        contractSignInfo.setTrustorEsignAccountInfo(trustorEsignAccountInfo);
        contractSignInfo.setCarrierEsignAccountInfo(carrierEsignAccountInfo);
        contractSignInfo.setBusinessType(ecSignReq.getBusinessType());
        if (Objects.nonNull(ecSignReq.getPlatCompany())
                && Objects.equals(ecSignReq.getBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
            log.info("-------------创建平台方E签宝账户-------------");
            String platAccountId = eqbSignService.createAccount(eqbConfigInfo, ecSignReq.getPlatCompany());
            log.info("-------------平台方发起签署-------------");
            ESignAccountDTO platEsignAccountInfo = assemblyESignAccountInfo(ecSignReq.getPlatCompany(), eFileInfo.getFileUrl(), platAccountId);
            contractSignInfo.setPlatEsignAccountInfo(platEsignAccountInfo);
        }

        //调用签署
        String flowId = eqbSignService.bothInitiationSign(eqbConfigInfo,contractSignInfo);

        log.info("合同签署---->签署成功，得到EQB的FLOWID为："+flowId);

        Integer result = saveEQBFlowIdToContract(flowId,contractId,ContractConstant.SignType.ECB,"");
        //扣除资金流水
        if(result != null && vasInfo != null){
            eqbHelper.deductionCapitalFlow(vasInfo,contractId);
        }
        log.info("-----------------SIGNING E-SIGNATURE ELECTRONIC CONTRACT END----------------");
        return flowId;
    }

    /**
     * 删除/批量删除合同
     * @param contractIds
     */
    @Override
    public int deleteContract(List<String> contractIds) throws Exception {
        int count = 0;
        try{
            List<Contract> contractList = this.listByIds(contractIds);
            if (CollUtil.isEmpty(contractList)) {
                int code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
                String msg = "要删除的合同不存在！";
                throw new BusinessException(code,msg);
            }
            for (Contract contract : contractList) {
                contract.setIsDelete(DicConstant.IS_DELETE.YES);
                contract.setCreateTime(null);
                int res = contractMapper.updateById(contract);
                // 为保证app的pdf文件和web的页面显示一致，且为最新的合同，此处作废时重新生成合同
                if (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contract.getContractType())
                        || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contract.getContractType())){
                    log.info("重新生成运单的电子签章,作废运单编号为：{}", contract.getContractCode());
                    LambdaQueryWrapper<Contract> query = new LambdaQueryWrapper<>();
                    query.eq(Contract::getParentContractId,contract.getParentContractId());
                    query.eq(Contract::getIsDelete,DicConstant.IS_DELETE.NO);
                    query.orderByDesc(Contract::getCreateTime);
                    List<Contract> list = this.list(query);
                    if (list != null && !list.isEmpty()){
                        ContractInfoQryVO batchCon = contractService.selectContractDetail(list.get(0).getId());
                        contractService.fDDCreateEcContractPdf(batchCon);
                        try {
                            log.info("重新生成运单的电子签章,重新获取的合同编码为：{}", list.get(0).getContractCode());
                            contractService.updateFddPdfId(list.get(0).getId(), LoginUserContextHolder.getUser());
                        } catch (Exception e) {
                            log.error("重新生成运单的电子签章,重新获取合同编码失败:",e);
                            throw new BusinessException(e.getMessage());
                        }
                    }
                }
                count = count + res;
            }
        } catch (Exception e) {
            log.error("合同删除异常",e);
            int code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
            String msg = ContractErrorCode.DELETE_CONTRACT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }
        return count;
    }

    @Override
    public void deleteByDocument(List<String> documentIds) throws Exception {
        if(CollUtil.isEmpty(documentIds)){
            log.error("要删除的单据ID不能为空！！！");
            return;
        }
        try{
            for (String docId : documentIds) {
                 List<Contract> contractList = this.list(new QueryWrapper<Contract>().eq("document_id",docId));
                if (CollUtil.isEmpty(contractList)) {
                    int code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
                    String msg = "要删除的合同不存在！";
                    throw new BusinessException(code,msg);
                }
                for (Contract contract : contractList) {
                    contract.setIsDelete(DicConstant.IS_DELETE.YES);
                    contract.setCreateTime(null);
                    contractMapper.updateById(contract);
                }
            }
        } catch (Exception e) {
            log.error("合同删除异常:",e);
            int code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
            String msg = ContractErrorCode.DELETE_CONTRACT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }
    }

    @Override
    public ContractInfoQryVO selectContractDetail(String contractId){
        return selectContractDetail(contractId,false);
    }

    /**
     * 查询合同详情
     * @param contractId
     * @param forCreatePdf true表示用于签章查询,针对联合运单有特殊处理逻辑
     */
    @Override
    public ContractInfoQryVO selectContractDetail(String contractId,boolean forCreatePdf) {
        log.info("====> 查询合同详情 - contractId: {} <====", contractId);
        // 查询签章开关标识（0：e签宝；1：法大大）
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        Integer signSwitchTag = electricSealSwitch.getSignSwitchTag();

        ContractInfoQryVO contractInfo = new ContractInfoQryVO();
        ContractMigrate contractMigrate = contractMigrateService.getById(contractId);
        if (contractMigrate == null) {
            contractInfo = contractMapper.findContractById(contractId);
        } else {
            contractInfo = contractMapper.findMigrateContractById(contractMigrate.getId(),contractMigrate.getTableSuffix());
        }
        log.info("====> 查询合同详情 - contractInfo: {} <====", contractInfo);
        if (contractInfo == null) {
            int code = ServerCode.getServerCode(ServerCode.TMS, ModuleCode.DETAIL.getCode(), FunctionCode.CON_QUERY.getCode(), ContractErrorCode.CONTRACT_DATA_ISNULL.getCode());
            String msg = ContractErrorCode.CONTRACT_DATA_ISNULL.getMessage();
            throw new BusinessException(code, msg);
        }
        String documentId = contractInfo.getDocumentId();
        Integer documentType = contractInfo.getDocumentType();
        //货物
        List<ContractGoods> contractGoods = new ArrayList<>();
        if (contractMigrate == null) {
            contractGoods = getContractGoods(contractId);
        } else {
            contractGoods = contractGoodsMapper.getMigrateContractGoods(contractId,contractMigrate.getTableSuffix());
        }
        contractInfo.setContractGoodsInfos(contractGoods);
        //组装费用科目
        contractInfo.setContractChargeInfos(getContractChargeVO(documentId, documentType, contractInfo));
        //组装付款方式
        ContractPaymentVO contractPaymentDetail = getContractPaymentDetail(documentId, documentType, contractInfo.getPaymentTypeVer());
        contractInfo.setContractPaymentDetail(contractPaymentDetail);
        //托运人
        CompanyBO trustorCompany = tmsCompanyService.getCompanyById(contractInfo.getTrustorCompany().getId());
        ContactVo trustContact = new ContactVo();
        trustContact.setName(contractInfo.getTrustorContactName());
        trustContact.setMobile(contractInfo.getTrustorContactMobile());
        trustorCompany.setContact(trustContact);
        contractInfo.setTrustorCompany(trustorCompany);

        // 托运人注册地址
        String trustorCompanyRegisteredAddress = "";
        AddressInfo addressInfo = new AddressInfo();
        String registeredAddressId = trustorCompany.getRegisteredAddressId();
        if (registeredAddressId!= null) {
            addressInfo.setAddressID(registeredAddressId);
            AddressDetailsVo addressExtInfo = settingService.getAddressInfoById(registeredAddressId);
            if (!Objects.isNull(addressExtInfo)) {
                String firstCityName = addressExtInfo.getProvinceName();
                String secondCityName = addressExtInfo.getCityName();
                String cityName = addressExtInfo.getCityName();
                String address = addressExtInfo.getStreetName();
                trustorCompanyRegisteredAddress = firstCityName + secondCityName + cityName + address;
                contractInfo.setTrustorCompanyRegisteredAddress(trustorCompanyRegisteredAddress);
            }
        }else{
            log.warn("未查询到托运人注册地址{}",contractInfo);
        }

        //承运人/结算对象
        CompanyBO carryCompany = tmsCompanyService.getCompanyById(contractInfo.getCarryCompany().getId());
        if (ObjectUtil.isNull(carryCompany)) {
            carryCompany = getCarrierCompanyInfo(contractInfo.getCarryCompany().getId(), contractInfo.getCarryCompany().getName());
        }
        ContactVo contact = new ContactVo();
        contact.setName(contractInfo.getCarrierContactName());
        contact.setMobile(contractInfo.getCarrierContactMobile());
        if(carryCompany.getContact() != null && carryCompany.getContact().getUserId() != null){
            UserInfoVo user = tmsUserService.getUserById(carryCompany.getContact().getUserId());
            contact.setIdcardNo(user.getIdcardNo());
        }
        carryCompany.setContact(contact);
        contractInfo.setCarryCompany(carryCompany);
        String platCompanyId = contractInfo.getPlatCompanyId();
        if (StrUtil.isNotBlank(platCompanyId)) {
            CompanyBO platCompany = tmsCompanyService.getCompanyById(platCompanyId);
            contractInfo.setPlatCompany(platCompany);
        }
        if (!contractInfo.getCarrierCompanyName().equals(carryCompany.getName())) {
            //取合同中保存的结算对象
            contractInfo.getCarryCompany().setName(contractInfo.getCarrierCompanyName());
        }

        //本地pdf
        if (StrUtil.isNotBlank(contractInfo.getEcContractPath())) {
            FileInfoVo file = tmsFileService.getFileById(contractInfo.getEcContractPath());
            if (file != null) {
                contractInfo.setEcContractPathUrl(file.getFileServerUrl() + file.getUrl());
                log.info("====> 查询合同详情 - 本地PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
            }
        }

        //电子签章pdf
        if (StrUtil.isNotBlank(contractInfo.getEcContractPdfId())) {
            FileInfoVo file = tmsFileService.getFileById(contractInfo.getEcContractPdfId());
            if (file != null) {
                contractInfo.setEcContractPdfUrl(file.getFileServerUrl() + file.getUrl());
                log.info("====> 查询合同详情 - 电子签章PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
            }
        }

        //查询补充合同
//        List<SupplementContractInfo> supplementContractInfos = supplementContractInfoList(contractId);
//        contractInfo.setArrSupContract(supplementContractInfos);
        //按钮权限
        Map<String, String> buttonResult = new HashMap<>();
        buttonResult.put("btnEcSigned", "hide");
        ContractExt contractExt = new ContractExt();
        contractExt.setContractId(contractInfo.getId());
        List<ContractExt> contractExtList = new ArrayList<>();
        if (contractMigrate == null) {
            contractExtList = contractExtMapper.selectList(new QueryWrapper<>(contractExt));
        } else {
            contractExtList = contractExtMapper.getMigrateContractExt(contractInfo.getId(),contractMigrate.getTableSuffix());
        }
        if (CollectionUtil.isNotEmpty(contractExtList)) {
            contractInfo.setCardNo(contractExtList.get(0).getCardNo());
            String idcardNo = contact.getIdcardNo();
            if(StringUtils.isBlank(idcardNo)){
                log.info("未查询到承运人联系人用户身份证信息，使用合同扩展表身份证信息:{}",documentId);
                contact.setIdcardNo(contractExtList.get(0).getCardNo());
            }
        }
        if (StrUtil.isNotBlank(contractInfo.getEcContractPdfUrl())) {
            buttonResult.put("btnEcSigned", "show");
        } else {
            if (signSwitchTag.equals(ContractConstant.ContractType.FDD)) {
                //法大大 是否认证
                if (CollectionUtil.isNotEmpty(contractExtList)) {
                    String xtmCompanyId = "";
                    if(DicConstant.DOCUMENT_TYPE.ORDER.equals(documentType)){
                        xtmCompanyId = contractInfo.getCarrierCompanyId();
                    } else if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(documentType)){
                        xtmCompanyId = contractInfo.getTrustorCompanyId();
                    }
                    if (isFFVasAuth(contractExtList.get(0).getCardNo(),xtmCompanyId)) {
                        buttonResult.put("btnEcSigned", "show");
                    }
                }
            } else {
                //是否授权
                try {
                    CompanyVasInfo vasInfo = eqbHelper.vasAuth();
                    if (vasInfo != null) {
                        if (vasInfo.getAuthorizedStatus() && vasInfo.getEnabledStatus()) {
                            buttonResult.put("btnEcSigned", "show");
                        }
                    }
                } catch (Exception e) {
                    log.error("订阅服务调用异常:" ,e);
                }
            }
        }
        contractInfo.setButtonPermission(buttonResult);
        if (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractInfo.getContractType())
            || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contractInfo.getContractType())){
            DispatchBatchContractVo dispatchBatchContractVo = new DispatchBatchContractVo();
            dispatchBatchContractVo.setVehicleCode(contractInfo.getVehicleCode());
            String pId = "";
            if (StringUtils.isNotBlank(contractInfo.getParentContractId()) && !contractInfo.getParentContractId().equals("0")){ // 子合同查询
                pId = contractInfo.getParentContractId();
                ContractMigrate pcontractMigrate = contractMigrateService.getById(pId);
                Contract contract = new Contract();
                if (pcontractMigrate == null) {
                    contract = getById(pId);
                } else {
                    contract = contractMapper.getMigrateContract(pcontractMigrate.getId(),pcontractMigrate.getTableSuffix());
                }
                dispatchBatchContractVo.setDispatchBatchContractNo(contract.getContractCode());
                dispatchBatchContractVo.setDispatchBatchContractTime(cn.hutool.core.date.DateUtil.format(contract.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                contractInfo.setContractCode(contract.getContractCode());
                //本地pdf
                if (StrUtil.isNotBlank(contract.getEcContractPath())) {
                    FileInfoVo file = tmsFileService.getFileById(contract.getEcContractPath());
                    if (file != null) {
                        contractInfo.setEcContractPathUrl(file.getFileServerUrl() + file.getUrl());
                        log.info("====> 批量/联合运输查询合同详情 - 本地PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
                    }
                }

                //电子签章pdf
                if (StrUtil.isNotBlank(contract.getEcContractPdfId())) {
                    FileInfoVo file = tmsFileService.getFileById(contract.getEcContractPdfId());
                    if (file != null) {
                        contractInfo.setEcContractPdfUrl(file.getFileServerUrl() + file.getUrl());
                        log.info("====> 批量/联合运输查询合同详情 - 电子签章PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
                    }
                }
            }else { // 父合同查询
                pId = contractInfo.getId();
                dispatchBatchContractVo.setDispatchBatchContractNo(contractInfo.getContractCode());
                dispatchBatchContractVo.setDispatchBatchContractTime(cn.hutool.core.date.DateUtil.format(contractInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            List<ContractInfoQryVO> contracts = contractMapper.findContractByParentId(pId);
            List<ContractMigrate> contractMigrateList = contractMigrateService.list(
                    Wrappers.<ContractMigrate>lambdaQuery().eq(ContractMigrate::getParentContractId, pId));
            if (contractMigrateList.size() > 0) {
                List<String> contractIds = new ArrayList<>();
                if (contracts != null && !contracts.isEmpty()){
                    for(ContractInfoQryVO contract: contracts) {
                        contractIds.add(contract.getId());
                    }
                }
                for (ContractMigrate migrate : contractMigrateList) {
                    if (!contractIds.contains(migrate.getId())) {
                        contracts.add(contractMapper.findMigrateContractById(migrate.getId(),migrate.getTableSuffix()));
                    }
                }
            }
            if (contracts != null && !contracts.isEmpty()){
                List<ContractDispatchBatchVo> contractGoodsInfoVos = new ArrayList<>();
                Integer dispatchNum = 0;
                Iterator<ContractInfoQryVO> iterator = contracts.iterator();
                while (iterator.hasNext()) {
                    ContractInfoQryVO contract = iterator.next();
                    if(forCreatePdf){
                        log.info("判断是否是中转卸货:{},{}",contract.getDocumentId(),contract.getDocumentCode());
                        Result  transferUnload = orderFeign.isTransferUnload(contract.getDocumentId());
                        log.info("判断是否是中转卸货:{},{},{}",contract.getDocumentId(),contract.getDocumentCode(),transferUnload);
                        boolean isTransferUnload = (boolean) transferUnload.getData();
                        if (isTransferUnload){
                            iterator.remove();
                            continue;
                        }
                    }
                    //判断是否是中转卸货
                    ContractChargeVO chargeSubjectsInfos = getContractChargeVO(contract.getDocumentId(), DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH, contract);
                    ContractDispatchBatchVo batchVo = new ContractDispatchBatchVo();
                    if (chargeSubjectsInfos != null){
                        List<ContractChargeSubjectsInfo> subjectsInfos = chargeSubjectsInfos.getChargeSubjectsInfos();
                        if (subjectsInfos != null &&!subjectsInfos.isEmpty()){
                            batchVo.setContractUnitPrice(subjectsInfos.get(0).getContractUnitPrice());
                            batchVo.setContractPrice(subjectsInfos.get(0).getContractPrice());
                        }
                    }
                    batchVo.setDispatchBatchCode(contract.getContractCode());
                    batchVo.setDispatchBatchCreateTime(cn.hutool.core.date.DateUtil.format(contract.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    List<DispatchGoodsInfoVo> dispatchGoodsInfoVos = new ArrayList<>();
                    //货物
                    List<ContractGoods> goods = getContractGoods(contract.getId());
                    if (goods !=null && !goods.isEmpty()){
                        dispatchNum = dispatchNum + goods.size();
                        goods.forEach(good-> {
                            DispatchGoodsInfoVo vo = new DispatchGoodsInfoVo();
                            List<ContractGoodsJsonReq> contractGoodsJsonReqs = JSON.parseObject(good.getContractGoodsJson(), new TypeReference<List<ContractGoodsJsonReq>>(){});
                            vo.setSendTime(DateUtil.day2String(good.getSendTime()));
                            vo.setReceiveTime(DateUtil.day2String(good.getReceiveTime()));
                            vo.setSendAddress(good.getSendAddress());
                            vo.setReceiveAddress(good.getReceiveAddress());
                            if (contractGoodsJsonReqs != null && !contractGoodsJsonReqs.isEmpty()){
                                vo.setGoodMeasuring(contractGoodsJsonReqs.get(0).getGoodMeasuring());
                                vo.setGoodsName(contractGoodsJsonReqs.get(0).getGoodsName());
                                vo.setGoodsDesc(contractGoodsJsonReqs.get(0).getGoodsDesc());
                            }
                            dispatchGoodsInfoVos.add(vo);
                        });
                    }
                    batchVo.setDispatchGoodsInfoVos(dispatchGoodsInfoVos);
                    contractGoodsInfoVos.add(batchVo);
                }
                dispatchBatchContractVo.setDispatchBatchNum(contracts.size());
                dispatchBatchContractVo.setDispatchNum(dispatchNum);
                dispatchBatchContractVo.setContractGoodsInfoVos(contractGoodsInfoVos);
            }
            contractInfo.setDispatchBatchContractVo(dispatchBatchContractVo);
        }
        return contractInfo;
    }

    private @Nullable ContractChargeVO getContractChargeVO(String documentId, Integer documentType, ContractInfoQryVO contractInfo) {
        ContractChargeVO chargeSubjectsInfos = getTransportChargeDetail(documentId, documentType, contractInfo.getChargeSubjectId());
        if(documentType.equals(DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH)){ // 运单
            // 判断单据是否中转卸货来源，如果是中转卸货，费用金额显示未合同金额
            Result  transferUnload = orderFeign.isTransferUnload(documentId);
            boolean isTransferUnload = (boolean) transferUnload.getData();
            if (isTransferUnload) {
                chargeSubjectsInfos.setContractTotalPrice(contractInfo.getContractPrice());
                for (ContractChargeSubjectsInfo info : chargeSubjectsInfos.getChargeSubjectsInfos()) {
                    info.setContractPrice(contractInfo.getContractPrice());
                    if (StringUtils.isNotBlank(contractInfo.getContractMeasuringCollection()) && contractInfo.getContractMeasuringCollection().contains("吨")) {
                        BigDecimal weight = new BigDecimal(contractInfo.getContractMeasuringCollection().substring(0, contractInfo.getContractMeasuringCollection().indexOf("吨")));
                        if (weight.compareTo(BigDecimal.ZERO) != 0) {
                            info.setContractUnitPrice(contractInfo.getContractPrice().divide(weight, 2, RoundingMode.HALF_UP));
                        }
                    }
                }
            }
        }
        return chargeSubjectsInfos;
    }

    private List<SupplementContractInfo> supplementContractInfoList(String contractId) {
        Contract cont = getById(contractId);
        if (cont == null) {
            return null;
        }
        List<SupplementContractInfo> supplementContractInfos = contractMapper.findSupplementContract(contractId);
        return supplementContractInfos;
    }

    /**
     * 查询法大大是否能签署
     */
    public boolean isFFVasAuth(String carrierContractMobile , String xtmCompanyId) {
        String cardNo = "";
        if(nacosValueConfig.getXtmTjCompanyId().equals(xtmCompanyId)){
            cardNo = nacosValueConfig.getXtmTjCardNo();
        } else if (nacosValueConfig.getXtmGsCompanyId().equals(xtmCompanyId)) {
            cardNo = nacosValueConfig.getXtmGsCardNo();
        }
        List<String> openIds = new ArrayList<>();
        openIds.add(cardNo);
        openIds.add(carrierContractMobile);
        Map<String, FddElectricSealResp> openIdMap = getOpenIdMap(openIds);
        String customerId = getCustomerId(cardNo,openIdMap);
        String trustorCompanyCustomerId = getCustomerId(carrierContractMobile,openIdMap);
        if (customerId == null || trustorCompanyCustomerId == null) {
            return false;
        }
        return true;
    }

    /**
     * 根据openid查询签章信息
     */
    @Override
    public Map<String, FddElectricSealResp> getOpenIdMap(List<String> openIds){
        List<FddElectricSealResp> fddList = fddFeignService.getFddElectricSealByOpenIds(openIds, null);
        //过滤 VerifyStatus = 1 AuthAutoSignStatus=1
        fddList = fddList.stream().filter(fdd -> fdd.getVerifyStatus() == 1 && fdd.getAuthAutoSignStatus() == 1).collect(Collectors.toList());
        Map<String, FddElectricSealResp> openIdMap = fddList.stream().collect(Collectors.toMap(FddElectricSealResp::getOpenId, fdd -> fdd));
        return openIdMap;
    }

    @Override
    public Result rebuildUnionDispatchBatchContract(String unionBatchId) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                .eq(Contract::getDocumentId, unionBatchId)
                .eq(Contract::getIsDelete,DicConstant.IS_DELETE.NO)
                .orderByDesc(Contract::getVer);
        Contract contract = contractMapper.selectOne(wrapper);
        if(contract==null){
            log.error("合同不存在!:{}",unionBatchId);
            throw new BusinessException("合同不存在");
        }
        String sourceContractId = contract.getId();
        SysUser user = LoginUserContextHolder.getUser();
        LambdaQueryWrapper<ContractExt> extQueryWrapper = new LambdaQueryWrapper<>();
        extQueryWrapper.eq(ContractExt::getContractId,sourceContractId);
        List<ContractExt> contractExts = contractExtMapper.selectList(extQueryWrapper);
        //删除旧数据 重新插入新数据
        LambdaUpdateWrapper<Contract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Contract::getId,sourceContractId);
        updateWrapper.set(Contract::getIsDelete,DicConstant.IS_DELETE.YES);
        updateWrapper.set(Contract::getModifyTime,LocalDateTime.now());
        updateWrapper.set(Contract::getModifier,user.getId());
        contractMapper.update(null,updateWrapper);
        contract.setId(IdWorker.getIdStr());
        contract.setCreateTime(LocalDateTime.now());
        contract.setCreater(user.getId());
        contract.setModifyTime(LocalDateTime.now());
        contract.setModifier(user.getId());
        contract.setEcContractPath( null);
        contract.setEcContractPdfId( null);
        contract.setEcContractEsignFlowId(null);
        contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
        contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
        contract.setIsDelete(DicConstant.IS_DELETE.NO);
        contractMapper.insert(contract);
        for (ContractExt contractExt : contractExts) {
            contractExt.setId(IdWorker.getIdStr());
            contractExt.setContractId(contract.getId());
            contractExt.setCreateTime(new Date());
            contractExt.setModifyTime(new Date());
            contractExtMapper.insert(contractExt);
        }
        String id = contract.getId();
        log.info("重新生成联合运单的电子签章,联合运单编号为:{},{}", id,contract.getContractCode());
        ContractInfoQryVO batchCon = contractService.selectContractDetail(id,true);
        log.info("重新生成联合运单的电子签章本地pdf,联合运单编号为：{}", batchCon.getContractCode());
        boolean localDeatilPdf = contractService.createLocalDeatilPdf(batchCon, user, batchCon.getBusinessType());
        if (!localDeatilPdf){
            log.error("重新生成联合运单的电子签章本地pdf失败,联合运单编号为：{}", batchCon.getContractCode());
            return Result.error("重新生成联合运单的电子签章pdf失败");
        }
        log.info("重新生成联合运单的电子签章本地pdf成功,联合运单编号为:{},{}", id,batchCon.getContractCode());
        contractService.fDDCreateEcContractPdf(batchCon);
        try {
            log.info("重新生成联合运单的电子签章完成,联合运单编号为：{}", batchCon.getContractCode());
            contractService.updateFddPdfId(id, user);
            log.info("更新签章pdf成功,联合运单编号为：{}", batchCon.getContractCode());
        } catch (Exception e) {
            log.error("从法大大下载电子签章并上传文件服务器异常:", e);
        }
        return Result.ok();
    }

    @Override
    public boolean isFFVasAuth(String carrierContractMobile) {
        List<String> openIds = new ArrayList<>();
        openIds.add(carrierContractMobile);
        Map<String, FddElectricSealResp> openIdMap = chargeService.getOpenIdMap(openIds);
        String trustorCompanyCustomerId = chargeService.getCustomerId(carrierContractMobile,0,openIdMap);
        if (trustorCompanyCustomerId == null) {
            return false;
        }
        return true;
    }

    /**
     * 保存本地合同pdf入库
     * @param filId
     * @param contractId
     */
    @Override
    public void saveLocalPdfToContract(String filId,String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(filId)) {
            contract.setEcContractPath(filId);
        }
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate()
                .eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        contractMapper.update(contract, extUpdateWrapper);
        if ((contract.getContractType().equals(DicConstant.CONTRACT_TYPE.JOINT_CONTRACT) ||
                contract.getContractType().equals(DicConstant.CONTRACT_TYPE.BATCH_CONTRACT))
                && StringUtils.isNotBlank(contract.getParentContractId())
                && StringUtils.isNotBlank(filId)){ // 联合运单或批量运输合同
            extUpdateWrapper.clear();
            extUpdateWrapper.eq(Contract::getId,contract.getParentContractId());
            extUpdateWrapper.set(Contract::getEcContractPath,filId);
            extUpdateWrapper.set(Contract::getModifyTime,new Date());
            contractMapper.update(null,extUpdateWrapper);
        }
    }

    private void updateFddPdfToContract(String fddPdfId, String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!{}",contractId);
            return;
        }
        contract.setEcContractPdfId(fddPdfId);
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate().eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        contractMapper.update(contract, extUpdateWrapper);
        // 批量联合运输新增逻辑
        if ((contract.getContractType().equals(DicConstant.CONTRACT_TYPE.JOINT_CONTRACT) ||
                contract.getContractType().equals(DicConstant.CONTRACT_TYPE.BATCH_CONTRACT))
                && StringUtils.isNotBlank(contract.getParentContractId())){ // 联合运单或批量运输合同
            extUpdateWrapper.clear();
            extUpdateWrapper.eq(Contract::getId,contract.getParentContractId());
            extUpdateWrapper.set(Contract::getEcContractPdfId,fddPdfId);
            extUpdateWrapper.set(Contract::getModifyTime,new Date());
            contractMapper.update(null,extUpdateWrapper);
        }
    }

    /**
     * 保存下载的电子合同ID
     * @param ecPdfId
     * @param contractId
     */
    @Override
    public void saveEcPdfToContract(String ecPdfId,String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(ecPdfId)) {
            contract.setEcContractPdfId(ecPdfId);
        }
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate()
                .eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        contractMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 保存e签宝的文件ID
     * @param eSignFileId
     * @param contractId
     */
    @Override
    public void saveEQBFileIdToContract(String eSignFileId,String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(eSignFileId)) {
            contract.setEcContractEsignFileId(eSignFileId);
        }
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate()
                .eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        contractMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 更新合同签署状态
     * @param trustorStatu
     * @param carrierStatu
     * @param contractId
     */
    @Override
    public void updateContractSignStatus(Integer trustorStatu, Integer carrierStatu, String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!，{}",contractId);
            return;
        }
        contract.setTrustorSignStatus(trustorStatu);
        contract.setCarrierSignStatus(carrierStatu);
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate()
                .eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        contractMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 保存e签宝的流程ID
     * @param flowId
     * @param contractId
     */
    public Integer saveEQBFlowIdToContract(String flowId,String contractId,Integer signType,String carrierContractIdCardNo) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("合同签署---->当前合同不存在!");
            return null;
        }
        contract.setEcContractEsignFlowId(flowId);
        //更改签署状态为已签署
        contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
        contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
        saveContractExt(contractId, signType,carrierContractIdCardNo);
        LambdaUpdateWrapper<Contract> extUpdateWrapper = Wrappers.<Contract>lambdaUpdate()
                .eq(Contract::getId, contractId);
        contract.setCreateTime(null);
        int count = contractMapper.update(contract, extUpdateWrapper);
        if (count <= 0) {
            log.info("合同签署---->保存归档ID失败");
            return null;
        }
        if ((contract.getContractType().equals(DicConstant.CONTRACT_TYPE.JOINT_CONTRACT) ||
                contract.getContractType().equals(DicConstant.CONTRACT_TYPE.BATCH_CONTRACT))
                && StringUtils.isNotBlank(contract.getParentContractId())){ // 联合运单或批量运输合同
            extUpdateWrapper.clear();
            extUpdateWrapper.eq(Contract::getId,contract.getParentContractId());
            extUpdateWrapper.set(Contract::getEcContractEsignFlowId,flowId);
            extUpdateWrapper.set(Contract::getModifyTime,new Date());
            extUpdateWrapper.set(Contract::getTrustorSignStatus,DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
            extUpdateWrapper.set(Contract::getCarrierSignStatus,DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
            contractMapper.update(null,extUpdateWrapper);
        }
        return count;
    }

    @Override
    public void saveContractExt(String contractId, Integer signType, String cardNo) {
        ContractExt contractExtQuery = new ContractExt();
        contractExtQuery.setContractId(contractId);
        List<ContractExt> contractExtList = contractExtMapper.selectList(new QueryWrapper<>(contractExtQuery));
        if (CollectionUtil.isNotEmpty(contractExtList)) {
            contractExtList.get(0).setSignType(signType);
            contractExtList.get(0).setModifyTime(new Date());
            contractExtMapper.updateById(contractExtList.get(0));
        } else {
            ContractExt contractExt = new ContractExt();
            contractExt.setContractId(contractId);
            contractExt.setId(UUID.randomUUID().toString().replace("-", ""));
            contractExt.setSignType(signType);
            contractExt.setCreateTime(new Date());
            contractExt.setModifyTime(contractExt.getCreateTime());
            contractExt.setCardNo(cardNo);
            contractExtMapper.insert(contractExt);
        }
    }

    /**
     * 保存错误信息
     * @param result
     * @param contractId
     */
    @Override
    public void saveErrorInfoToContract(ContractResult result,String contractId) {
        Contract contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        contract.setEcContractResultCode(String.valueOf(result.getCode()));
        contract.setEcContractResultDesc(result.getMsg());
        contract.setCreateTime(null);
        contractMapper.updateById(contract);
    }

    @Override
    public ContractPathVO rebuildEcContract(String id) throws BusinessException {
        // 查询签章开关标识（0：e签宝；1：法大大）
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        Integer signSwitchTag = electricSealSwitch.getSignSwitchTag();

        log.info("rebuildEcContract 法大大簽章 id={} signSwitchTag={}", id, signSwitchTag);
        ContractPathVO contractPath = new ContractPathVO();
        try {
            SysUser sessionInfo = LoginUserContextHolder.getUser();
            ContractInfoQryVO contractInfo = selectContractDetail(id,true);
            if(contractInfo.getDocumentType().equals(DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH)){ // 运单
                // 判断单据是否中转卸货来源，如果是中转卸货，费用金额显示未合同金额
                Result  transferUnload = orderFeign.isTransferUnload(contractInfo.getDocumentId());
                boolean isTransferUnload = (boolean) transferUnload.getData();
                if (isTransferUnload) {
                    int code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.TRANSFER_UNLOAD_CONTRACT_NOT_SIGN.getCode());
                    throw new BusinessException(code,ContractErrorCode.TRANSFER_UNLOAD_CONTRACT_NOT_SIGN.getMessage());
                }
            }
            if (Objects.equals(signSwitchTag, ContractConstant.ContractType.FDD )) {
                log.info("rebuildEcContract 法大大 id={}",id);
                if (StrUtil.isBlank(contractInfo.getEcContractEsignFlowId())) {
                    //重新生成电子合同
                    ContractResult contractResult = fDDCreateEcContractPdf(contractInfo);
                    if (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractInfo.getContractType())
                            || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contractInfo.getContractType())){
                        if(contractResult.getCode()!=0){
                            //1.根据合同id从法大大下载已经签章的电子合同，并设置到ec_contract_path 和 ec_contract_pdf_id字段
                            //2.签章顺序是先签托运人 在签承运人，如果托运人签章成功，承运人签章失败，则生成的pdf文件中显示托运人签章，承运人签章显示未签章，并且下载下来的文件一直是第一次托运人签章成功的文件，即使后面签章上传的pdf发生变化，下载的签章后pdf文件不会更新，直到承运人签章成功为止
                            //3.联合运单中每次子运单生成合同会覆盖联合运单ec_contract_path 和 ec_contract_pdf_id字段
                            //4.如果出现第二条描述情况承运人签章失败，则在web端每次点击电子合同签章按钮 会执行重新签章方法，如果使用联合运单id去法大大拉取pdf文件则一直会是第一次签章的文件,直到签章成功才会显示正确
                            //5.所以联合运单签章失败，则不能覆盖ec_contract_path 和 ec_contract_pdf_id字段，否则显示的内容会有误
                            log.info("重新生成联合运单的电子签章失败,联合运单编号为：{}", contractInfo.getContractCode());
                            contractPath.setResultCode(CommonLang.SUCCESS.getCode());
                            return contractPath;
                        }
                    }
                }
                //1.根据合同id从法大大下载已经签章的电子合同，并设置到ec_contract_path 和 ec_contract_pdf_id字段
                //2.签章顺序是先签托运人 在签承运人，如果托运人签章成功，承运人签章失败，则生成的pdf文件中显示托运人签章，承运人签章显示未签章，
                //并且下载下来的文件一直是第一次托运人签章成功的文件，即使后面签章上传的pdf发生变化，下载的签章后pdf文件不会更新，直到承运人签章成功为止
                contractPath.setEcontractUrl(updateFddPdfId(id, sessionInfo));
                log.info("rebuildEcContract: 法大大 contractPath={}", JSON.toJSON(contractPath));
                contractPath.setResultCode(CommonLang.SUCCESS.getCode());
                return contractPath;
            }
            if (StrUtil.isBlank(contractInfo.getEcContractEsignFlowId())) {
                //重新生成电子合同
                ContractResult contractResult = createEcContractPdf(contractInfo, sessionInfo);
                if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() != contractResult.getCode()) {
                    contractPath.setResultCode(contractResult.getCode());
                    contractPath.setResultDesc(contractResult.getMsg());
                    return contractPath;
                }
                //从归档到下载盖章大约需要3s
                Thread.sleep(3500L);
            }

            //获取E签宝的配置信息
            EqbConfigInfo configInfo = eqbHelper.getEqbConfigInfo();
            //下载合同
            String ecPdfUrl = eqbSignService.getDownloadDocumentUrl(configInfo, contractInfo.getEcContractEsignFlowId());
            if (StrUtil.isBlank(ecPdfUrl)) {
                log.error("E签宝文件下载失败");
                int errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_DOWNLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.FILE_DOWNLOAD_FAIL.getMessage());
            }
            //上传到服务器
            FileInfoOut fileInfo = fileHelper.urlUploadFile(ecPdfUrl, sessionInfo.getAgentCode(), DicConstant.DOCUMENT_TYPE.CONTRACT.toString() + ".pdf");
            if (fileInfo == null) {
                int errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            contractPath.setEcontractUrl(fileInfo.getFileUrl());
            //保存电子印章ID
            saveEcPdfToContract(fileInfo.getFileID(), id);
            contractPath.setResultCode(CommonLang.SUCCESS.getCode());
        } catch (BusinessException e) {
            ContractResult contractResult = new ContractResult();
            contractResult.setCode(e.getCode());
            contractResult.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                contractResult.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
            saveErrorInfoToContract(contractResult, id);
            contractPath.setResultCode(contractResult.getCode());
            contractPath.setResultDesc(contractResult.getMsg());
        } catch (Exception ex) {
            log.error("rebuildEcContract 异常", ex);
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            ContractResult contractResult = new ContractResult();
            contractResult.setCode(code);
            contractResult.setMsg(ex.getMessage());
            saveErrorInfoToContract(contractResult, id);
            contractPath.setResultCode(contractResult.getCode());
            contractPath.setResultDesc(contractResult.getMsg());
        }
        return contractPath;
    }

    @Override
    public ContractPathVO rebuildLocalPdf(String id) throws BusinessException {
        ContractPathVO contractPath = new ContractPathVO();

        SysUser sessionInfo = LoginUserContextHolder.getUser();
        ContractInfoQryVO contractInfo = selectContractDetail(id);
//        String contractTemplateId = contractInfo.getContractTemplateId();
//        ContractTemplate contractTemplate = contractTemplateService.getById(contractTemplateId);
        //重新生成PDF
        if (StrUtil.isBlank(contractInfo.getEcContractPath())) {
            createLocalDeatilPdf(contractInfo, sessionInfo, contractInfo.getBusinessType());
            contractInfo.setEcContractPath(contractMapper.findContractById(id).getEcContractPath());
        }
        if (StrUtil.isBlank(contractInfo.getEcContractPdfId())) {
            createFDDContractPdf(contractInfo);
        }
        String localPdf = contractInfo.getEcContractPath();
        contractPath.setEcontractUrl(localPdf);
        return contractPath;
    }

    /**
     * 获取合同方联系人信息
     * @param companyInfo
     * @return
     */
    @Override
    public ContactVo selectContractSideAdminInfo(CompanyBO companyInfo) {
        if (companyInfo == null) {
            return null;
        }
        //没有管理员就取公司联系人作为合同联系人
        if (StrUtil.isBlank(companyInfo.getCompanyAdmin())) {
            return companyInfo.getContact();
        }
        UserInfoVo user = tmsUserService.getUserById(companyInfo.getCompanyAdmin());
        ContactVo contactInfo = new ContactVo();
        if (user != null) {
            contactInfo.setUserId(user.getId());
            contactInfo.setName(user.getName());
            contactInfo.setMobile(user.getMobile());
        }
        return contactInfo;
    }

    @Override
    public ContractCodeQryVO selectContractCodeByDocumentId(String documentId) {
        if (StrUtil.isBlank(documentId)) {
            log.error("查询合同的单据ID不能为空！");
            return null;
        }
        ContractCodeQryVO contractCodeInfo = new ContractCodeQryVO();
        Contract contract = null;
        ContractMigrate contractMigrate = contractMigrateService.lambdaQuery().eq(ContractMigrate::getDocumentId, documentId)
                .list().stream().findFirst().orElse(null);
        if (contractMigrate == null) {
            contract = this
                    .lambdaQuery()
                    .select(Contract::getId,Contract::getContractCode,Contract::getTitle,Contract::getContractType,Contract::getParentContractId)
                    .eq(Contract::getDocumentId,documentId)
                    .eq(Contract::getIsDelete,DicConstant.COMMON_ZERO)
                    .orderByDesc(Contract::getVer)
                    .last(" limit 1")
                    .one();
        } else {
            contract = contractMapper.getMigrateContract(contractMigrate.getId(),contractMigrate.getTableSuffix());
        }
        if (contract != null) {
            // 如果联合运单存在的情况下处理方式
            if (StringUtils.isNotBlank(contract.getParentContractId()) && !"0".equals(contract.getParentContractId()) &&
                    (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contract.getContractType()) || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contract.getContractType()))){
                ContractMigrate pcontractMigrate = contractMigrateService.getById(contract.getParentContractId());
                Contract pContract = new Contract();
                if (pcontractMigrate == null) {
                    pContract = contractMapper.selectById(contract.getParentContractId());
                } else {
                    pContract = contractMapper.getMigrateContract(pcontractMigrate.getId(),pcontractMigrate.getTableSuffix());
                }
                contractCodeInfo.setContractId(pContract.getId());
                contractCodeInfo.setContractCode(pContract.getContractCode());
                contractCodeInfo.setTitle(pContract.getTitle());
                contractCodeInfo.setContractType(pContract.getContractType());
            }else {
                contractCodeInfo.setContractId(contract.getId());
                contractCodeInfo.setContractCode(contract.getContractCode());
                contractCodeInfo.setTitle(contract.getTitle());
                contractCodeInfo.setContractType(contract.getContractType());
            }
        }

        return contractCodeInfo;
    }

    @Override
    public List<Contract> selectExpiresContract() {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contract::getContractType, DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT);
        wrapper.and(item -> item.lt(Contract::getValidStartDate,new Date()).or().gt(Contract::getValidEndDate,new Date()));
        List<Contract> contractList = contractMapper.selectList(wrapper);
        return contractList;
    }

    @Override
    public void updateSettlePriceByDocumentId(ContractCreUpdReq contractCreUpdReq) {
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentUserId = ContractSessionUtil.getCurrentUserID(sessionInfo);
        Contract contract = selectContractByDocumentId(contractCreUpdReq.getDocumentId());
        contract.setModifier(currentUserId);
        contract.setModifyTime(LocalDateTime.now());
        contract.setCreateTime(null);
        contractMapper.updateById(contract);
        //修改费用信息
        assemblyAddContractCharge(contractCreUpdReq,contract.getId(),null,false);
    }

    @Override
    public ApiPageResult<ContractInfoQryVO> selectContractList(ContractListQryReq contractListQryReq) {
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);
        contractListQryReq.setCompanyId(currentCompanyId);
        // 判断是否是顶级公司
        if(sessionInfo.getPlatformCompanyId().equals(currentCompanyId)){
            contractListQryReq.setIsTopLevelCompany(true);
        } else {
            contractListQryReq.setIsTopLevelCompany(false);
        }
        // 过滤单据类型是订单的数据
        if(Objects.nonNull(contractListQryReq.getDocumentType())){
            if (1133000 == contractListQryReq.getDocumentType()){
                return ApiPageResult.<ContractInfoQryVO>builder()
                        .currentPage(contractListQryReq.getPageNum())
                        .pageSize(contractListQryReq.getPageSize())
                        .build();
            }
        }
        //获取用户的数据权限
        List<String> authCompanyIds = getAuthCompanyIds();
        //没有权限
        if(authCompanyIds != null&& authCompanyIds.isEmpty()){
            return ApiPageResult.<ContractInfoQryVO>builder()
                    .currentPage(contractListQryReq.getPageNum())
                    .pageSize(contractListQryReq.getPageSize())
                    .build();
        }

        //查询父级或顶级合同
        contractListQryReq.setQueryTop(1);
        String tradeStartTime = contractListQryReq.getTradeStartTime();
        if (StringUtils.isNotBlank(tradeStartTime)){
            contractListQryReq.setCreateTimeStart(cn.hutool.core.date.DateUtil.parseLocalDateTime(tradeStartTime));
        }
        String tradeEndTime = contractListQryReq.getTradeEndTime();
        if (StringUtils.isNotBlank(tradeEndTime)){
            contractListQryReq.setCreateTimeEnd(cn.hutool.core.date.DateUtil.parseLocalDateTime(tradeEndTime));
        }

        //============dispatchBatchCode不为空 需要先搜索下联合运单下的运单合同 获取到联合运单的父级合同id
        String dispatchBatchCode = contractListQryReq.getDispatchBatchCode();
        Integer contractType = contractListQryReq.getContractType();
        if (StringUtils.isNotBlank(dispatchBatchCode)){
            contractListQryReq.setQueryTop(0);
            List<Integer> contractTypeList = new ArrayList<>();
            contractListQryReq.setContractTypeList(contractTypeList);
            if(contractType==null){
                contractTypeList.add(DicConstant.CONTRACT_TYPE.JOINT_CONTRACT);
                contractTypeList.add(DicConstant.CONTRACT_TYPE.BATCH_CONTRACT);
            }else{
                contractTypeList.add(contractType);
            }
            List<ContractInfoQryVO> contractList = contractMapper.findContractList(contractListQryReq, authCompanyIds);
            if(CollectionUtil.isNotEmpty(contractList)){
                ContractInfoQryVO contractInfoQryVO = contractList.get(0);
                contractListQryReq.setContractId(contractInfoQryVO.getParentContractId());
            }else{
                return ApiPageResult.<ContractInfoQryVO>builder()
                        .currentPage(contractListQryReq.getPageNum())
                        .pageSize(contractListQryReq.getPageSize())
                        .totalPage(0)
                        .total(0)
                        .build();
            }
            //设置父级查询参数
            contractListQryReq.setContractTypeList( null);
            contractListQryReq.setDispatchBatchCode( null);
            contractListQryReq.setQueryTop(1);
        }
        //===========================

        PageHelper.startPage(contractListQryReq.getPageNum(),contractListQryReq.getPageSize());
        List<ContractInfoQryVO> contractList = contractMapper.findContractList(contractListQryReq, authCompanyIds);
        PageInfo<ContractInfoQryVO> pageInfo = new PageInfo<>(contractList);
        List<ContractInfoQryVO> contractInfoQryList = pageInfo.getList();
        ApiPageResult<ContractInfoQryVO> resultApiPageResult = ApiPageResult.<ContractInfoQryVO>builder()
                .currentPage(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .totalPage(pageInfo.getPages())
                .total((int)pageInfo.getTotal())
                .build();
        if (CollectionUtil.isEmpty(contractInfoQryList)) {
            return resultApiPageResult;
        }
        //获取父级合同id
        List<String> parentIdList = new ArrayList<>();
        Map<String, ContractInfoQryVO> parentContractMap = new HashMap<>();
        for (ContractInfoQryVO contractInfoQryVO : contractList) {
            contractInfoQryVO.setChildContractInfos(new ArrayList<>());
            if (contractInfoQryVO.getContractType() != null && (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractInfoQryVO.getContractType())
                    || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contractInfoQryVO.getContractType()))){
                parentIdList.add(contractInfoQryVO.getId());
            }
            parentContractMap.put(contractInfoQryVO.getId(),contractInfoQryVO);
        }
        //查询联合运单下的子级合同
        if(CollectionUtil.isNotEmpty(parentIdList)){
            contractListQryReq.setContractId(null);
            contractListQryReq.setQueryTop(0);
            contractListQryReq.setParentContractIds(parentIdList);
            contractListQryReq.setDispatchBatchCode(null);
            contractListQryReq.setContractCode(null);
            LocalDateTime createTimeStart = contractListQryReq.getCreateTimeStart();
            if (createTimeStart != null) {
                contractListQryReq.setCreateTimeStart(createTimeStart.minusMonths(1));
            }
            LocalDateTime createTimeEnd = contractListQryReq.getCreateTimeEnd();
            if (createTimeEnd != null) {
                LocalDateTime localDateTime = createTimeEnd.plusMonths(1);
                LocalDateTime now = LocalDateTime.now();
                if (localDateTime.isAfter(now)) {
                    localDateTime = now;
                }
                contractListQryReq.setCreateTimeEnd(localDateTime);
            }
            contractListQryReq.setDispatchBatchCode(dispatchBatchCode);
            List<ContractInfoQryVO> childContractInfoList = contractMapper.findContractList(contractListQryReq,authCompanyIds);
            for (ContractInfoQryVO contractInfoQryVO : childContractInfoList) {
                String parentContractId = contractInfoQryVO.getParentContractId();
                ContractInfoQryVO parent = parentContractMap.get(parentContractId);
                parent.getChildContractInfos().add(contractInfoQryVO);
            }
        }
        //编制方企业查询
        List<String> compileCompanyIds = new ArrayList<>();
        contractInfoQryList.forEach(info ->{
            if (info.getChildContractInfos() != null && !info.getChildContractInfos().isEmpty()){
                info.getChildContractInfos().forEach(cInfo ->{
                    if (!cInfo.getCompileSideId().equals(info.getCompileSideId())){
                        compileCompanyIds.add(cInfo.getCompileSideId());
                    }
                });
            }
            compileCompanyIds.add(info.getCompileSideId());
        });

        List<CompanyVo> compaileCompanyList = tmsCompanyService.getCompanyByIds(compileCompanyIds);
        //key-->公司Id:value-->公司名称
        Map<String, String> compileCompanyMap = compaileCompanyList.stream().collect(Collectors.toMap(CompanyVo::getId, x -> x.getName()));
        //创建人查询
        List<String> userIds = new ArrayList<>();
        contractInfoQryList.forEach(info ->{
            if (info.getChildContractInfos() != null && !info.getChildContractInfos().isEmpty()){
                info.getChildContractInfos().forEach(cInfo ->{
                    if (!cInfo.getCreater().equals(info.getCreater())){
                        userIds.add(cInfo.getCreater());
                    }
                });
            }
            userIds.add(info.getCreater());
        });
        List<UserInfoVo> userList = tmsUserService.getUserByIds(userIds);
        //key-->用户Id:value-->用户名称
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(UserInfoVo::getId, x -> x.getName()));

        //查询货物中的收发货地址
        List<String> contractIds = new ArrayList<>();
        contractInfoQryList.forEach(info ->{
            if (info.getChildContractInfos() != null && !info.getChildContractInfos().isEmpty()){
                info.getChildContractInfos().forEach(cInfo ->{
                    if (cInfo.getContractType() != null && (DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.equals(cInfo.getContractType())
                            || DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(cInfo.getContractType())
                            || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(cInfo.getContractType()))){
                        contractIds.add(cInfo.getId());
                    }
                });
            }else{
                if (info.getContractType() != null && (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(info.getContractType())
                        || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(info.getContractType()))){

                }
            }
            if (info.getContractType() != null && (DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.equals(info.getContractType())
                    || DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(info.getContractType())
                    || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(info.getContractType()))){
                contractIds.add(info.getId());
            }
        });
        // 查询类型为框架合同时 过滤掉货物信息
        Map<String, List<String>> sendAddressMap = new HashMap<>();
        Map<String, List<String>> receiveAddressMap = new HashMap<>();

        if(CollUtil.isNotEmpty(contractIds)) {
            LambdaQueryWrapper<ContractGoods> contractGoodsWrapper = new LambdaQueryWrapper();
            contractGoodsWrapper.in(ContractGoods::getContractId, contractIds);
            List<ContractGoods> goodsList = contractGoodsMapper.selectList(contractGoodsWrapper);
            //key-->合同id,value -->发货地址
             sendAddressMap = goodsList.stream().collect(Collectors.groupingBy(ContractGoods::getContractId, Collectors.mapping(ContractGoods::getSendAddress, Collectors.toList())));
            //key-->合同id,value -->收货地址
             receiveAddressMap = goodsList.stream().collect(Collectors.groupingBy(ContractGoods::getContractId, Collectors.mapping(ContractGoods::getReceiveAddress, Collectors.toList())));

        }

        //查询字典值
        List<Long> contractTypeList = new ArrayList<>();
        contractInfoQryList.forEach(info ->{
            if (info.getChildContractInfos() != null && !info.getChildContractInfos().isEmpty()){
                info.getChildContractInfos().forEach(cInfo ->{
                    if(cInfo.getContractType()!=null){
                        contractTypeList.add(cInfo.getContractType().longValue());
                    }
                    if(cInfo.getDocumentType()!=null){
                        contractTypeList.add(cInfo.getDocumentType().longValue());
                    }
                    if(cInfo.getBusinessType()!=null){
                        contractTypeList.add(cInfo.getBusinessType().longValue());
                    }
                });
            }
            if(info.getContractType()!=null){
                contractTypeList.add(info.getContractType().longValue());
            }
            if(info.getDocumentType()!=null){
                contractTypeList.add(info.getDocumentType().longValue());
            }
            if(info.getBusinessType()!=null){
                contractTypeList.add(info.getBusinessType().longValue());
            }
        });
        List<DictionaryVo> dictionaryVoList = settingService.listDictionaries(contractTypeList);
        Map<Long, String> dicMap = dictionaryVoList.stream().collect(Collectors.toMap(DictionaryVo::getId,x -> x.getName()));

        List<String> contractIdList = new ArrayList<>();
        contractInfoQryList.forEach(info ->{
            if (info.getChildContractInfos() != null && !info.getChildContractInfos().isEmpty()){
                List<String> list = info.getChildContractInfos().stream().filter(e -> e.getContractType().equals(DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT)).map(ContractInfoQryVO::getId).collect(Collectors.toList());
                contractIdList.addAll(list);

            }
            contractIdList.add(info.getId());
        });
        //附件信息
        List<ContractAttach> contractAttachList = getContractAttachInfoByContractIds(contractIdList);
        Map<String, String> fileInfo = getContractFileInfos(contractAttachList);

        changeData(contractInfoQryList, dicMap, fileInfo, compileCompanyMap, userMap, sendAddressMap, receiveAddressMap,true);
        //查询附件数量
        Map<String,List<ContractAttach>> attachCountMap = getContractAttachCount(contractAttachList);
        for (ContractInfoQryVO contractInfoQryVO : contractInfoQryList) {
            List<ContractAttach> attachList = attachCountMap.get(contractInfoQryVO.getId());
            contractInfoQryVO.setAttachNumber(CollUtil.isEmpty(attachList)?0:attachList.size());
            List<ContractInfoQryVO> childContractInfos = contractInfoQryVO.getChildContractInfos();
            if(CollUtil.isNotEmpty(childContractInfos)){
                for (ContractInfoQryVO childContractInfo : childContractInfos) {
                    List<ContractAttach> childAttachList = attachCountMap.get(childContractInfo.getId());
                    childContractInfo.setAttachNumber(CollUtil.isEmpty(childAttachList)?0:childAttachList.size());
                }
            }
        }
        resultApiPageResult.setList(contractInfoQryList);
        return resultApiPageResult;
    }

    /**
     * 根据合同id分组查询合同附件数目
     * @param contractAttachList 合同附件列表
     */
    private Map<String, List<ContractAttach>> getContractAttachCount(List<ContractAttach> contractAttachList) {
        if(CollUtil.isEmpty(contractAttachList)){
            return new HashMap<>();
        }
        //contractAttachList 根据合同id分组统计
        return contractAttachList.stream().collect(Collectors.groupingBy(ContractAttach::getContractId));
    }

    /**
     * @Param: [java.util.List<com.xiaoniu.contract.model.vo.contract.ContractInfoQryVO>, java.util.Map<java.lang.Long,java.lang.String>, java.util.Map<java.lang.String,java.lang.String>, java.util.Map<java.lang.String,java.lang.String>, java.util.Map<java.lang.String,java.lang.String>, java.util.Map<java.lang.String,java.util.List<java.lang.String>>, java.util.Map<java.lang.String,java.util.List<java.lang.String>>]
     * @return: void
     * @Author: wwh
     * @Date: 2025/3/13 17:05
     * @Description: 数据转换
     */
    private void changeData(List<ContractInfoQryVO> contractInfoQryList, Map<Long, String> dicMap, Map<String, String> fileInfo,
                            Map<String, String> compileCompanyMap, Map<String, String> userMap,
                            Map<String, List<String>> sendAddressMap, Map<String, List<String>> receiveAddressMap,Boolean fFlag) {
        for (ContractInfoQryVO contractInfo : contractInfoQryList) {
            //合同类型描述
            contractInfo.setContractTypeDesc(dicMap.get(contractInfo.getContractType().longValue()));
            if (contractInfo.getContractType().equals(DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT) && fileInfo != null){
                contractInfo.setHasFrameworkContractAttach(true);
            }
            //单据类型描述
            contractInfo.setDocumentTypeDesc(dicMap.get(contractInfo.getDocumentType()==null?null:contractInfo.getDocumentType().longValue()));
            //业务性质描述
            contractInfo.setBusinessTypeDesc(dicMap.get(contractInfo.getBusinessType()==null?null:contractInfo.getBusinessType().longValue()));
            //编制方
            contractInfo.setCompileSideCompanyName(compileCompanyMap.get(contractInfo.getCompileSideId()));
            //创建人
            contractInfo.setCreaterName(userMap.get(contractInfo.getCreater()));
            //合同收发货地址
            contractInfo.setListSendAddress(getAddress(sendAddressMap.get(contractInfo.getId())));
            contractInfo.setListReceiveAddress(getAddress(receiveAddressMap.get(contractInfo.getId())));
            /** 数据类型转换开始 **/
            if (contractInfo.getChildContractInfos() != null && !contractInfo.getChildContractInfos().isEmpty()){
               changeData(contractInfo.getChildContractInfos(), dicMap, fileInfo, compileCompanyMap, userMap, sendAddressMap, receiveAddressMap,false);
            }else{
                if (contractInfo.getContractType() != null && (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractInfo.getContractType())
                        || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contractInfo.getContractType()))){
                    //批量联合运单如果没有子运单就显示为-
                    continue;
                }
                if (fFlag){ //最外层需要复制出个对象给页面展示使用
                    List<ContractInfoQryVO> contractInfoList = new ArrayList<>();
                    ContractInfoQryVO childVo = new ContractInfoQryVO();
                    BeanUtils.copyProperties(contractInfo,childVo);
                    contractInfoList.add(childVo);
                    contractInfo.setChildContractInfos(contractInfoList);
                }
            }
            /** 数据类型转换结束**/
        }
    }

    /**
     * 根据合同ID获取合同附件
     *
     * @param contractIds
     * @return
     */
    public List<ContractAttach> getContractAttachInfoByContractIds(List<String> contractIds) {
        if (CollUtil.isEmpty(contractIds)) {
            return new ArrayList<>();
        }

        QueryWrapper<ContractAttach> qw = new QueryWrapper<>();
        qw.in("contract_id", contractIds);
        List<ContractAttach> contractAttachList = attachMapper.selectList(qw);
        return contractAttachList;
    }

    /**
     * 根据合同附件获取合同文件
     *
     * @param contractAttaches
     * @return
     */
    public  Map<String, String> getContractFileInfos(List<ContractAttach> contractAttaches) {


        Map<String, String> result = new HashMap<>();

        if (CollUtil.isEmpty(contractAttaches)) {
            return result;
        }

        //获取文件URL
        List<String> ids = contractAttaches.stream().filter(e -> StrUtil.isNotBlank(e.getFileId())).map(ContractAttach::getFileId).collect(Collectors.toList());
        List<FileInfoVo> files = tmsFileService.listByIds(ids);
        if(files==null){
            return result;
        }
        for (ContractAttach contractAttach : contractAttaches) {
            for (FileInfoVo file : files) {
                if (file != null) {
                    if (file.getId().equals(contractAttach.getFileId())) {
                        result.put(contractAttach.getContractId(), file.getFileServerUrl() + file.getUrl());
                    }

                }
            }

        }

        return result;
    }
    private String getAddress(List<String> addressList){
        if(CollUtil.isEmpty(addressList)){
            return null;
        }
        String address = addressList.get(0);
        StringBuffer stringBuffer = new StringBuffer();
        int size = addressList.size();
        if(size > 1){
            address = stringBuffer.append(address).append("等【").append(size).append("】").toString();
        }
        return address;
    }

    /**
     * 根据单据ID查询合同
     * @param documentId
     * @return
     */
    @Override
    public ContractInfoQryVO selectContractByDocumentId(String documentId) {
        log.info("查询合同的单据ID为" + documentId);
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                .select(Contract::getId)
                .eq(Contract::getDocumentId, documentId)
                .eq(Contract::getIsDelete,DicConstant.IS_DELETE.NO)
                .orderByDesc(Contract::getVer);
        List<Contract> contractList = contractMapper.selectList(wrapper);
        if(CollUtil.isNotEmpty(contractList)){
            return selectContractDetail(contractList.get(0).getId());
        }
        return null;
    }

    /**
     * 校验合同入参
     * @return
     */
    private boolean checkContractCreParam(ContractCreUpdReq contractCreUpdReq) {
        if (contractCreUpdReq == null) {
            log.info("合同创建入参不能为空");
            return false;
        }
        if (StrUtil.isBlank(contractCreUpdReq.getTrustorCompanyID())) {
            log.info("托运人不能为空");
            return false;
        }
        CompanyVo company = tmsCompanyService.findCompanyById(contractCreUpdReq.getTrustorCompanyID());
        if(Objects.isNull(company)){
            log.info("托运人不能为空");
            return false;
        }
        if (StrUtil.isBlank(contractCreUpdReq.getCarrierCompanyID()) || StrUtil.isBlank(contractCreUpdReq.getCarrierCompanyName())) {
            log.info("承运人不能为空");
            return false;
        }
        if (StrUtil.isBlank(contractCreUpdReq.getDocumentId())) {
            log.info("单据ID不能为空");
            return false;
        }
        if (StrUtil.isBlank(contractCreUpdReq.getDocumentCode())) {
            log.info("单据号不能为空");
            return false;
        }
        if (null == contractCreUpdReq.getContractDocumentType()) {
            log.info("单据类型不能为空");
            return false;
        }
        if (null == contractCreUpdReq.getTradeTime()) {
            log.info("单据交易时间不能为空");
            return false;
        }
        if (contractCreUpdReq.getDocumentGoodsInfoList().isEmpty()) {
            log.info("单据货物不能为空");
            return false;
        }
        if (StrUtil.isBlank(contractCreUpdReq.getChargeSubjectId())) {
            log.info("费用科目ID不能为空");
            return  false;
        }
        if (null == contractCreUpdReq.getPaymentScheduleVer()) {
            log.info("支付方式版本不能为空");
            return  false;
        }
        if (null == contractCreUpdReq.getCreater()) {
            log.info("创建人不能为空");
            return  false;
        }
        if(contractCreUpdReq.getContractPrice() == null || BigDecimal.ZERO.compareTo(contractCreUpdReq.getContractPrice()) == 0){
            log.info("合同金额不能为空");
            return  false;
        }
        if (contractCreUpdReq.getBusinessType() == null) {
            log.info("业务类型不能为空");
            return false;
        }
        if (Objects.equals(contractCreUpdReq.getBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
            if (contractCreUpdReq.getPlatCompanyId() == null) {
                log.info("三方业务平台公司ID不能为空");
                return false;
            }
        }
        return true;
    }

    /**
     * 组装创建合同的基本信息
     * @param contractCreUpdReq
     * @param currentCompanyId
     * @param contract
     */
    private void assemblyContractCreBaseInfo(ContractCreUpdReq contractCreUpdReq, String currentCompanyId, Contract contract) {
        UserInfoVo user = tmsUserService.getUserById(contractCreUpdReq.getCreater());
        String createrCompanyId = currentCompanyId;
        if(ObjectUtil.isNotNull(user)){
            createrCompanyId = user.getCompanyId();
        }
        Integer businessType = contractCreUpdReq.getBusinessType();
        if (businessType == null) {
            businessType = DicConstant.CONTRACT_BUSINESS_TYPE.BOTH;
        }
        contract.setContractTemplateBusinessType(businessType);
        //当业务类型为三方合同时，保存三方业务平台公司id
        if (Objects.equals(contractCreUpdReq.getBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
            contract.setPlatCompanyId(contractCreUpdReq.getPlatCompanyId());
        }
        //单据ID
        contract.setDocumentId(contractCreUpdReq.getDocumentId());
        //单据编号
        contract.setDocumentCode(contractCreUpdReq.getDocumentCode());
        //合同编号
        contract.setContractCode(contractCreUpdReq.getDocumentCode());
        //单据类型
        contract.setDocumentType(contractCreUpdReq.getContractDocumentType()==null?null:contractCreUpdReq.getContractDocumentType());
        //托运人
        contract.setTrustorCompanyId(contractCreUpdReq.getTrustorCompanyID());
        //托运人名称
        contract.setTrustorCompanyName(tmsCompanyService.findCompanyById(contractCreUpdReq.getTrustorCompanyID()).getName());
        //承运人
        contract.setCarrierCompanyId(contractCreUpdReq.getCarrierCompanyID());
        //承运人名称
        contract.setCarrierCompanyName(contractCreUpdReq.getCarrierCompanyName());
        //托运方联系人
        contract.setTrustorContactName(contractCreUpdReq.getTrustorContactName());
        //托运方联系人手机号
        contract.setTrustorContactMobile(contractCreUpdReq.getTrustorContactMobile());
        //承运方联系人
        contract.setCarrierContactName(contractCreUpdReq.getCarrierContractName());
        //承运方联系人手机号
        contract.setCarrierContactMobile(contractCreUpdReq.getCarrierContractMobile());
        //合同类型
        contract.setContractType(DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.intValue());
        //单据合同签署时间即交易时间
        contract.setContractDate(contractCreUpdReq.getTradeTime()!=null?Date.from(contractCreUpdReq.getTradeTime().atZone(ZoneId.systemDefault()).toInstant()):null);
        //合同金额
        contract.setContractPrice(contractCreUpdReq.getContractPrice().setScale(2,BigDecimal.ROUND_DOWN));
        //交易时间
        contract.setTradeTime(Date.from(contractCreUpdReq.getTradeTime().atZone(ZoneId.systemDefault()).toInstant()));
        //编制方公司ID
        contract.setCompileSideId(createrCompanyId);
        //父合同的父合同ID默认0
        contract.setParentContractId("0");
        contract.setIsDelete(DicConstant.IS_DELETE.NO);
        //获取匹配的合同模板信息
        ContractTemplate contractTemplate = null;
        if (contractCreUpdReq.getParentContractReq() != null
                && DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(contractCreUpdReq.getContractDocumentType())
                && (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractCreUpdReq.getParentContractReq().getContractType())
                || DicConstant.CONTRACT_TYPE.BATCH_CONTRACT.equals(contractCreUpdReq.getParentContractReq().getContractType()))){
            contract.setContractType(contractCreUpdReq.getParentContractReq().getContractType());
            Integer documentType = 0;
            if (DicConstant.CONTRACT_TYPE.JOINT_CONTRACT.equals(contractCreUpdReq.getParentContractReq().getContractType())){
                documentType = 1133450; // 联合运输
            }else {
                documentType = 1133460; // 批量运输
            }
            contractTemplate = contractTemplateService.selectContractTempateByCompany(documentType,
                    createrCompanyId, businessType);
        }else {
            contractTemplate = contractTemplateService.selectContractTempateByCompany(contractCreUpdReq.getContractDocumentType(),
                    createrCompanyId, businessType);
        }
        if (contractTemplate != null) {
            contract.setContractTemplateId(contractTemplate.getId());//合同模板ID
            contract.setContractTemplateName(contractTemplate.getTemplateName());//合同模板名称
            contract.setTitle(contractTemplate.getTitle());//合同主题
            contract.setContent(contractTemplate.getContent());//合同内容
        }
        // 载具编码批量联合运输新增
        if(StringUtils.isNotBlank(contractCreUpdReq.getVehicleCode())){
            contract.setVehicleCode(contractCreUpdReq.getVehicleCode());
        }
    }

    /**
     * 组装合同单据信息
     * @param sendContactParam 发货联系人信息
     * @param receiveContactParam 收货联系人信息
     * @param commonGoodsReqList 货物信息
     * @param transportTypeDesc 运输方式
     * @param vehicleCode  载具号
     * @param contractId 合同id
     * @throws BusinessException
     */
    private void assemblyContractReceiveDeliverGoodInfo(ContactParam sendContactParam, ContactParam receiveContactParam, List<CommonGoodsReq> commonGoodsReqList, String transportTypeDesc, String vehicleCode, String contractId) throws BusinessException {
        if (sendContactParam == null || receiveContactParam == null) {
            log.error("单据收发货联系人不能为空");
            throw new BusinessException(Constant.ERROR_CODE,"单据收发货联系人不能为空");
        }
        if (commonGoodsReqList == null || commonGoodsReqList.isEmpty()) {
            log.error("单据货物参数不能为空");
            throw new BusinessException(Constant.ERROR_CODE,"单据货物参数不能为空");
        }
        ContractGoods contractGoods = new ContractGoods();

        Address sAddress = sendContactParam.getAddress();
        Address rAddress = receiveContactParam.getAddress();
        //发货地址
        if (sAddress != null) {
            log.info("发货地地址"+JSON.toJSONString(sAddress));
            String streetName = StrUtil.isBlank(sAddress.getStreetName()) ? "" : sAddress.getStreetName();
            AddressDto sAddressDto = new AddressDto();
            sAddressDto.setAdministratorDivisionId(sAddress.getAdministratorDivisionId()==null?null:sAddress.getAdministratorDivisionId().longValue());
            sAddressDto.setStreetName(streetName);
            String sendAddress = settingService.detailAddressByAreaCode(sAddressDto);
            sendAddress = StringUtils.isNotBlank(sendAddress) ? sendAddress.replace(",县", "").replace(",", "").replace("市辖区", "") : "";
            contractGoods.setSendAddress(sendAddress);
        }
        //收货地址
        if (rAddress !=null) {
            log.info("收货地地址"+JSON.toJSONString(rAddress));
            String streetName = StrUtil.isBlank(rAddress.getStreetName()) ? "" : rAddress.getStreetName();
            AddressDto rAddressDto = new AddressDto();
            rAddressDto.setAdministratorDivisionId(rAddress.getAdministratorDivisionId()==null?null:rAddress.getAdministratorDivisionId().longValue());
            rAddressDto.setStreetName(streetName);
            String receiveAddress = settingService.detailAddressByAreaCode(rAddressDto);
            receiveAddress = StringUtils.isNotBlank(receiveAddress) ? receiveAddress.replace(",县", "").replace(",", "").replace("市辖区", "") : "";
            contractGoods.setReceiveAddress(receiveAddress);
        }
        contractGoods.setId(IdWorker.getIdStr());
        contractGoods.setContractId(contractId);
        contractGoods.setSendContactName(sendContactParam.getName());//发货联系人名称
        contractGoods.setReceiveContactName(receiveContactParam.getName());//收货联系人名称
        contractGoods.setSendContactMobile(sendContactParam.getMobile());//发货联系人手机号
        contractGoods.setReceiveContactMobile(receiveContactParam.getMobile());//收货联系人手机号
        contractGoods.setSendTime(sendContactParam.getConversionTime());//发货时间
        contractGoods.setReceiveTime(receiveContactParam.getConversionTime());//收货时间
        contractGoods.setVehicleCode(vehicleCode);//载具号
        contractGoods.setTransportType(transportTypeDesc);//运输方式
        assemblyContractGoodsJson(commonGoodsReqList,contractGoods);
        contractGoodsMapper.insert(contractGoods);
        log.info("创建合同-->合同货物添加成功");
    }

    /**
     * 组装合同单据货物JSON
     * @param commonGoodsReqList
     * @param contractGoods
     */
    private void assemblyContractGoodsJson(List<CommonGoodsReq> commonGoodsReqList,ContractGoods contractGoods) {
        List<ContractGoodsJsonReq> contractGoodsJsonReqs = new ArrayList<>();
        for (int i = 0 ;i < commonGoodsReqList.size(); i ++ ) {
            CommonGoodsReq commonGood = JSON.parseObject(JSON.toJSONString(commonGoodsReqList.get(i)), new TypeReference<CommonGoodsReq>() {});
            ContractGoodsJsonReq contractGoodsJsonReq = new ContractGoodsJsonReq();
            BeanUtils.copyProperties(commonGood,contractGoodsJsonReq);
            //货物计量组装
            String goodsMeasuring = getGoodMeasuring(commonGood);
            contractGoodsJsonReq.setGoodMeasuring(goodsMeasuring);
            //货物描述（扩展字段处理）
            String goodsDesc = getGoodsDesc(commonGood.getBusinessExpansionFields());
            contractGoodsJsonReq.setGoodsDesc(goodsDesc);
            contractGoodsJsonReqs.add(contractGoodsJsonReq);
        }
        String goodsJson = JSONObject.toJSONString(contractGoodsJsonReqs);
        contractGoods.setContractGoodsJson(goodsJson);
    }

    /**
     * 组装新增合同费用信息
     * @param contractCreUpdReq
     * @param contractId
     */
    private void assemblyAddContractCharge(ContractCreUpdReq contractCreUpdReq,String contractId,Integer ver,boolean creFlag) {
        log.info("新增或修改合同-->添加合同费用信息");
        List<ContractCharge> oldCharges = getContractCharge(contractId);
        log.info("修改结算价格，合同id:{},charges数量:{}",contractId,oldCharges.size());
        ContractCharge contractCharge = new ContractCharge();
        if (CollUtil.isNotEmpty(oldCharges)) {
            creFlag = false;
            contractCharge = oldCharges.get(0);
        }
        String chargeId = contractCreUpdReq.getChargeSubjectId();
        Integer paymentType = contractCreUpdReq.getPaymentScheduleVer();
        log.info("新增或修改合同-->费用ID:{},付款方式版本：{}",chargeId,paymentType);
        contractCharge.setContractId(contractId);
        contractCharge.setChargeSubjectId(chargeId);
        contractCharge.setPaymentScheduleVersion(paymentType);
        contractCharge.setVer(ver);
        log.info("修改结算价格，合同id:{},creFlag:{}",contractId,creFlag);
        if (creFlag) {
            contractCharge.setId(IdWorker.getIdStr());
            contractChargeMapper.insert(contractCharge);
            log.info("修改结算价格,新增成功，contractCharge：{}",contractCharge);
        } else {
            contractChargeMapper.updateById(contractCharge);
            log.info("修改结算价格,更新成功，contractCharge：{}",contractCharge);
        }
        log.info("新增或修改合同-->添加合同费用信息成功");
    }

    /**
     * 组装新增合同货物信息
     * @param contractCreUpdParam
     * @param contractId
     */
    private void assemblyAddContractGoods(ContractCreUpdReq contractCreUpdParam,String contractId) {
        log.info("创建合同-->添加合同货物信息");
        //单据货物信息
        List<Map<String,Object>> documentList = contractCreUpdParam.getDocumentGoodsInfoList();
        //总货物
        List<CommonGoodsReq> commonGoodsReqList = new ArrayList<>();
        //保存合同收发货联系人、货物信息
        for (Map<String,Object> docMap : documentList) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(docMap.get("documentSenderContact")));
            ContactParam senderContactParam = jsonObject.toJavaObject(ContactParam.class);//单据发货联系人

            JSONObject jsonObject2 = JSON.parseObject(JSON.toJSONString(docMap.get("documentReceiveContact")));
            ContactParam receiveContactParam = jsonObject2.toJavaObject(ContactParam.class);//单据收货联系人

            String transportTypeDesc = docMap.get("transportTypeDesc") == null ? "" : docMap.get("transportTypeDesc").toString();//运输方式
            String vehicleCode = docMap.get("vehicleCode") == null ? "" : docMap.get("vehicleCode").toString();//载具号

            List<CommonGoodsReq> commonGoodsReqs = (List<CommonGoodsReq>)docMap.get("documentGoodsList");//单据货物
            commonGoodsReqList.addAll(commonGoodsReqs);
            //组装收发货联系人、货物信息
            assemblyContractReceiveDeliverGoodInfo(senderContactParam,receiveContactParam,commonGoodsReqs,transportTypeDesc,vehicleCode,contractId);
        }
        //统计货物和货物计量并入库
        statisticalGoodsList(commonGoodsReqList,contractId);
    }

    /**
     * 查询合同货物
     * @param contractId
     * @return
     */
    private List<ContractGoods> getContractGoods(String contractId) {
        if (StrUtil.isBlank(contractId)) {
            return null;
        }
        LambdaQueryWrapper<ContractGoods> wrapper = new LambdaQueryWrapper<ContractGoods>()
                .eq(ContractGoods::getContractId, contractId)
                .eq(ContractGoods::getIsDelete,DicConstant.IS_DELETE.NO);
        List<ContractGoods> contractGoods = contractGoodsMapper.selectList(wrapper);
        return contractGoods;
    }

    /**
     * 查询合同费用
     * @param contractId
     * @return
     */
    private List<ContractCharge> getContractCharge(String contractId) {
        if (StrUtil.isBlank(contractId)) {
            return null;
        }
        LambdaQueryWrapper<ContractCharge> wrapper = new LambdaQueryWrapper<ContractCharge>()
                .eq(ContractCharge::getContractId, contractId);
        List<ContractCharge> contractCharges = contractChargeMapper.selectList(wrapper);
        return contractCharges;
    }

    /**
     * 获取货物描述如：螺纹钢（222）等【2】
     * @param goodsReq
     * @return
     */
    private String getListGooodDesc(List<CommonGoodsReq> goodsReq) {
        if (goodsReq == null || goodsReq.isEmpty()) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        CommonGoodsReq contractGood = JSON.parseObject(JSON.toJSONString(goodsReq.get(0)), new TypeReference<CommonGoodsReq>() {});

        if (contractGood != null) {
            stringBuffer.append(contractGood.getGoodsName());
            if (StrUtil.isNotBlank(contractGood.getModel())) {
                stringBuffer.append("(").append(contractGood.getModel()).append(")");
            }
            if (goodsReq.size() > 1) {
                stringBuffer.append("等").append("【" + goodsReq.size() + "】");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 详情货物描述
     * @param businessExpansionFields
     * @return
     */
    private String getGoodsDesc(List<BusinessExpansionFieldReq> businessExpansionFields) {
        if (businessExpansionFields == null || businessExpansionFields.isEmpty()) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        for (BusinessExpansionFieldReq businessExpansion : businessExpansionFields) {
            if(businessExpansion != null){
                String expansionFieldId = businessExpansion.getExpansionFieldId();
                String columnValue = businessExpansion.getColumnValue();
                str.append(expansionFieldId).append(":").append(columnValue).append("; ");
            }
        }
        return str.toString();
    }

    /**
     * 获取担货物计量
     * @param commonGood
     * @return
     */
    private String getGoodMeasuring(CommonGoodsReq commonGood) {
        if (commonGood == null) {
            return null;
        }
        log.info("单据的货物信息为："+ JSON.toJSONString(commonGood));
        BigDecimal weight = BigDecimal.ZERO;//重量
        String weightUnit = settingService.getDicNameById(DicConstant.WEIGHT_UNIT.TON.longValue());//重量单位

        GoodsMeasuring measuring = commonGood.getMeasuring();
        if (measuring != null) {
            BigDecimal goodWeight = measuring.getWeight() ==  null ? BigDecimal.ZERO : measuring.getWeight();


            weight = weight.add(goodWeight);

            weightUnit = settingService.getDicNameById(measuring.getWeightUnit()==null?null:measuring.getWeightUnit().longValue()) == null ? "吨" : settingService.getDicNameById(measuring.getWeightUnit()==null?null:measuring.getWeightUnit().longValue());

        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(weight.stripTrailingZeros().toPlainString());
        buffer.append(weightUnit);


        return buffer.toString();
    }

    /**
     * 获取总计量
     * @param commonGoodsReqList
     * @return
     */
    private String getTotalGoodMeasuring(List<CommonGoodsReq> commonGoodsReqList) {
        BigDecimal weight = BigDecimal.ZERO;//重量
        BigDecimal volume = BigDecimal.ZERO;//体积
        long quantity = 0;//数量
        List<String> quantityUnits = new ArrayList<>();//体积单位
        for (int i = 0 ;i < commonGoodsReqList.size(); i ++ ) {
            CommonGoodsReq commonGood = JSON.parseObject(JSON.toJSONString(commonGoodsReqList.get(i)), new TypeReference<CommonGoodsReq>() {});
            if (commonGood == null) {
                break;
            }
            GoodsMeasuring measuring = commonGood.getMeasuring();
            if (measuring != null) {
                BigDecimal goodWeight = measuring.getWeight();
                BigDecimal goodVolume = measuring.getVolume() ==  null ? BigDecimal.ZERO : measuring.getVolume();
                Integer goodQuantity = measuring.getQuantity() ==  null ? 0 : measuring.getQuantity();
                BigDecimal count = new BigDecimal(1000);

                if (DicConstant.WEIGHT_UNIT.KILO.equals(measuring.getWeightUnit())) {
                    goodWeight = goodWeight.divide(count,2,BigDecimal.ROUND_HALF_UP);
                }
                if (DicConstant.VOLUME_UNIT.LITRE.equals(measuring.getVolumeUnit())) {
                    goodVolume = goodVolume.divide(count,2,BigDecimal.ROUND_HALF_UP);
                }

                weight = weight.add(goodWeight);
                volume = volume.add(goodVolume);
                quantity = quantity + goodQuantity;
                String quantityUnit = "";
                if (null!=measuring.getQuantityUnit()){
                    quantityUnit = settingService.getDicNameById(measuring.getQuantityUnit().longValue());
                }
                if(!quantityUnits.contains(quantityUnit)) {
                    quantityUnits.add(quantityUnit);
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(weight.stripTrailingZeros().toPlainString());
        buffer.append("吨");
//        if(BigDecimal.ZERO.compareTo(volume) == -1) {
//            buffer.append("/");
//            buffer.append((volume.setScale(2,BigDecimal.ROUND_HALF_UP)).toString());
//            buffer.append("m³");
//        }
//        if(quantity != 0) {
//            buffer.append("/");
//            buffer.append(String.valueOf(quantity));
//            if (CollUtil.isNotEmpty(quantityUnits)) {
//                buffer.append(quantityUnits.size() > 1 ? "单位" : quantityUnits.get(0));
//            } else {
//                buffer.append("件");
//            }
//        }
        return buffer.toString();
    }

    /**
     * 合同计费科目
     * @param chargeSubjectId
     * @param documentId
     * @return
     */
    private ContractChargeVO getTransportChargeDetail(String documentId,Integer documentType,String chargeSubjectId) {
        if (StrUtil.isBlank(chargeSubjectId) || StrUtil.isBlank(documentId) || documentType == null) {
            return null;
        }
        ContractChargeVO contractChargeInfo = new ContractChargeVO();
        DocumentInfoQryIn documentInfoQryIn = new DocumentInfoQryIn(documentId,documentType,chargeSubjectId,null);
        TransportChargeDetail chargeDetail = documentFeginClient.selectTransportChargeDetail(documentInfoQryIn);
        log.info("合同详情-->调用单据服务根据单据ID:{}和费用ID:{},查询的费用为:{}",documentId,chargeSubjectId,JSON.toJSONString(chargeDetail));
        transformContractSubject(chargeDetail,contractChargeInfo);
        return contractChargeInfo;
    }

    /**
     * 合同付款方式
     */
    private ContractPaymentVO getContractPaymentDetail(String documentId, Integer documentType,Integer paymentTypeVer) {
        if (StrUtil.isBlank(documentId) || documentType == null) {
            return null;
        }
        if(paymentTypeVer == null || paymentTypeVer == 0){
            paymentTypeVer = 1;
        }
        String ver = paymentTypeVer.toString();
        ContractPaymentVO contractPayment = new ContractPaymentVO();
        DocumentInfoQryIn documentInfoQryIn = new DocumentInfoQryIn(documentId,documentType,null,ver);
        List<PaymentSchedule> paymentScheduleList = documentFeginClient.selectPaymentSchedule(documentInfoQryIn);
        log.info("合同详情-->调用单据服务根据单据ID:{},查询付款方式：{},版本号：{}",documentId,JSON.toJSON(paymentScheduleList),ver);
        transformPaymentSchedule(paymentScheduleList,contractPayment);
        return contractPayment;
    }

    /**
     * 转换合同费用科目信息
     * @param contractChargeInfo
     */
    private void transformContractSubject(TransportChargeDetail transportCharge,ContractChargeVO contractChargeInfo) {
        if (transportCharge == null) {
            return;
        }
        List<ContractChargeSubjectsInfo> contractChargeSubjectsInfoList = new ArrayList<>();

        TransportCharge contractTransportCharge = transportCharge.getContractTransportCharge();
        TransportCharge settleTransportCharge = transportCharge.getSettleTransportCharge();
        if(contractTransportCharge == null || settleTransportCharge == null){
            return;
        }
        //合同总金额
        BigDecimal contractTotalPrice = contractTransportCharge.getTotalPrice()  == null ? BigDecimal.ZERO : contractTransportCharge.getTotalPrice();
        //结算总金额
        BigDecimal settleTotalPrice = settleTransportCharge.getTotalPrice() == null ? BigDecimal.ZERO : settleTransportCharge.getTotalPrice();

        List<TransportChargeItem> contractChargeItems = transportCharge.getContractTransportChargeItemList();
        List<TransportChargeItem> settleChargeItems = transportCharge.getSettleTransportChargeItemList();
        if(CollUtil.isNotEmpty(contractChargeItems) && CollUtil.isNotEmpty(settleChargeItems)){
            int chargeNum = transportCharge.getContractTransportChargeItemList().size();
            int settleItemsNum = settleChargeItems.size();
            for (int i = 0 ; i < chargeNum ; i ++){
                ContractChargeSubjectsInfo chargeSubjectsInfo = new ContractChargeSubjectsInfo();
                TransportChargeItem contractChargeItem = contractChargeItems.get(i);
                if(settleItemsNum > i){
                    TransportChargeItem settlectChargeItem = settleChargeItems.get(i);
                    //结算单价
                    chargeSubjectsInfo.setSettleUnitPrice(settlectChargeItem.getUnitPrice());
                    //结算金额
                    chargeSubjectsInfo.setSettlePrice(settlectChargeItem.getSubtotalPrice());
                }
                //取科目描述
                chargeSubjectsInfo.setFeesDesc(contractChargeItem.getDescription());
                //合同单价
                chargeSubjectsInfo.setContractUnitPrice(contractChargeItem.getUnitPrice());
                //合同金额
                chargeSubjectsInfo.setContractPrice(contractChargeItem.getSubtotalPrice());

                contractChargeSubjectsInfoList.add(chargeSubjectsInfo);
            }
        }
        contractChargeInfo.setContractTotalPrice(contractTotalPrice);
        contractChargeInfo.setSettleTotalPrice(settleTotalPrice);
        contractChargeInfo.setChargeSubjectsInfos(contractChargeSubjectsInfoList);
    }

    /**
     * 转换支付进度
     * @param paymentScheduleList
     * @param contractPayment
     */
    private void transformPaymentSchedule(List<PaymentSchedule> paymentScheduleList,ContractPaymentVO contractPayment) {
        if(CollUtil.isEmpty(paymentScheduleList)){
            return;
        }
        //合计金额
        BigDecimal totalCount = BigDecimal.ZERO;
        //合同付款方式详细信息
        List<ContractPaymentInfo> contractChargeSubjectsInfoList = new ArrayList<>();
        for (PaymentSchedule payment : paymentScheduleList) {
            if (payment == null) {
                break;
            }
            if (payment.getPrice() != null) {
                totalCount = totalCount.add(payment.getPrice());
            }
            Integer paymentMode = payment.getPaymentMode();//付款进度
            Integer voucherType = payment.getVoucherType();//支付方式
            ContractPaymentInfo contractPaymentInfo = new ContractPaymentInfo();
            contractPaymentInfo.setPaymentMode(paymentMode);
            contractPaymentInfo.setPaymentModeDesc(paymentMode == null ? "" : settingService.getDicNameById(paymentMode.longValue()));
            contractPaymentInfo.setVoucherType(voucherType);
            contractPaymentInfo.setVoucherTypeDesc(voucherType == null ? "" : settingService.getDicNameById(voucherType.longValue()));
            contractPaymentInfo.setMoney(payment.getPrice() == null ? new BigDecimal(0) : payment.getPrice());
            contractPaymentInfo.setVer(payment.getVer());
            contractPaymentInfo.setPriceRatio(payment.getPriceRatio());
            if (paymentMode != null && paymentMode > 0) {
                contractChargeSubjectsInfoList.add(contractPaymentInfo);
            }
        }
        contractPayment.setContractPaymentInfos(contractChargeSubjectsInfoList);
        contractPayment.setTotalPrice(totalCount);
    }

    /**
     * 删除合同货物
     * @param contractId
     */
    private void deleteByContractId(String contractId) {
        List<ContractGoods> contractGoods = getContractGoods(contractId);
        if (contractGoods.isEmpty()) {
            return;
        }
        for (ContractGoods dealGoods : contractGoods) {
            dealGoods.setContractId(contractId);
            dealGoods.setIsDelete(DicConstant.IS_DELETE.YES);
            contractGoodsMapper.updateById(dealGoods);
        }
    }

    /**
     * 生成本地合同Pdf
     *
     * @param cont
     * @param sessionInfo
     * @return
     * @throws Exception
     */
    @Override
    public boolean createLocalDeatilPdf(ContractInfoQryVO cont, SysUser sessionInfo, Integer businessType) {
        try {
            if (cont == null) {
                log.error("生成本地合同PDF的合同不存在");
                return false;
            }
            String trustorCode = organizationOrSettingHelper.getKeyWord(cont.getTrustorCompany());
            String carrierCode = organizationOrSettingHelper.getKeyWord(cont.getCarryCompany());
            Map<String, Object> map = new HashMap<>();
            map.put("contract", cont);
            map.put("firstKeyWord", trustorCode);
            map.put("secondKeyWord", carrierCode);
            map.put("id", cont.getId());
            log.info("firstKeyWord是：{}，secondKeyWord是：{}", trustorCode, carrierCode);
            if (DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(businessType)) {
                map.put("thirdKeyWord", cont.getPlatCompanyId());
                log.info("三方合同，thirdKeyWord是：{}", cont.getPlatCompanyId());
            }
            log.info("createLocalDeatilPdf: cont={}",JSON.toJSON(cont));
            String htmlTempl = PdfHelper.changeFtlToHtml(map, cont.getContractType(), businessType);
            if (StrUtil.isBlank(htmlTempl)) {
                log.info("LOCAL HTML生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            }
            log.info("====> 合同生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf前，cont: {}, htmlTempl:  <====", cont);
            FileInfoOut fileInfo = fileHelper.htmlToPdf(htmlTempl, sessionInfo.getAgentCode());
            if (fileInfo == null) {
                log.info("LOCAL PDF生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120002.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            }
            log.info("====> 合同生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf后，cont: {}, fileInfo:  <====", cont);
            cont.setEcContractPath(fileInfo.getFileID());
            cont.setEcContractPathUrl(fileInfo.getFileUrl());
            saveLocalPdfToContract(fileInfo.getFileID(), cont.getId());
            return true;
        } catch (BusinessException e) {
            ContractResult result = new ContractResult();
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            log.error("本地pdf生成失败,原因：" + e.getMessage());
            //错误信息落库
            saveErrorInfoToContract(result, cont.getId());
            return false;
        }
    }

    /**
     * 组装签章账户信息
     * @param companyBO
     * @param localPdfUrl
     * @param accoutId
     * @return
     * @throws BusinessException
     */
    private ESignAccountDTO assemblyESignAccountInfo(CompanyBO companyBO,String localPdfUrl,String accoutId) throws BusinessException {
        //获取关键字
        String keyword = organizationOrSettingHelper.getKeyWord(companyBO);
        if (StrUtil.isBlank(keyword)) {
            log.error("找不到当前"+companyBO.getName()+"的关键字!");
            throw new BusinessException(Constant.ERROR_CODE,"找不到当前"+companyBO.getName()+"的关键字!");
        }
        //获取关键字所在坐标
        float[] coordinates = PdfHelper.getKeyWordsByPath(localPdfUrl,keyword);
        if (coordinates !=null&& coordinates.length == 0) {
            log.error(keyword+"关键字的坐标获取失败!");
            throw new BusinessException(Constant.ERROR_CODE,keyword+"关键字的坐标获取失败!");
        }
        ESignAccountDTO eSignAccount = new ESignAccountDTO();
        eSignAccount.setAutoExecute(true);//静默签为true
        eSignAccount.setSignerAccountId(accoutId);
        eSignAccount.setPosX(coordinates[0]);
        eSignAccount.setPosY(coordinates[1]);
        eSignAccount.setPosPage(String.valueOf((int) coordinates[2]));
        return eSignAccount;
    }

    private CompanyBO getCarrierCompanyInfo(String companyId,String companyName){
        DriverVo driver = motorcadeServiceFeign.getDriverById(companyId);
        CompanyBO carryCompany = new CompanyBO();
        if(driver != null) {
            UserInfoVo user = tmsUserService.getUserById(driver.getUserId());
            carryCompany.setId(user.getCompanyId());
            carryCompany.setName(driver.getName());
            return carryCompany;
        }

        String name = companyName;
        try {
            CompanyVo company = tmsCompanyService.findCompanyByName(name);
            if (ObjectUtil.isNotNull(company)) {
                carryCompany.setId(company.getId());
            }
        }catch(Exception e){
            log.error("承运方找不到公司:",e);
        }
        carryCompany.setName(name);
        return carryCompany;
    }

    /**
     * 根据单据IDS查询合同
     * @param documentIds
     * @return
     */
    @Override
    public List<String> getContractExistByDocumentIds(List<String> documentIds) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<Contract>()
                .select(Contract::getDocumentId)
                .in(Contract::getDocumentId, documentIds)
                .eq(Contract::getIsDelete,DicConstant.IS_DELETE.NO)
                .orderByDesc(Contract::getVer);
        List<Contract> contracts = contractMapper.selectList(wrapper);
        if(CollectionUtil.isEmpty(contracts)){
            return null;
        }
        return contracts.stream()
                .map(Contract::getDocumentId)
                .collect(Collectors.toList());
    }

    /**
     * 找车费用汇总单电子签章
     * @param summaryPdfParam
     * @return
     */
    @Override
    public Result<FindCarChargeSummaryPdfVo> findCarChargeSummaryEcSign(FindCarChargeSummaryPdfParam summaryPdfParam) throws Exception {

        log.info("开始找车费用汇总单电子签章 contractCreUpdParam={}",summaryPdfParam);
        boolean flag = checkFindCarChargeSummaryParam(summaryPdfParam);
        if (!flag) {
            log.error("找车费用汇总单电子签章失败，原因：参数校验失败！");
            return Result.error("找车费用汇总单电子签章失败，原因：参数校验失败！");
        }
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        FindCarChargeSummaryPdfVo summaryPdfVo = new FindCarChargeSummaryPdfVo();
        summaryPdfVo.setId(summaryPdfParam.getSummaryId());
        summaryPdfVo.setFirstPartyId(summaryPdfParam.getFirstPartyId());
        summaryPdfVo.setSecondPartyId(summaryPdfParam.getSecondPartyId());
        summaryPdfVo.setServiceProject(summaryPdfParam.getServiceProject());
        // 生成本地找车汇总单
        this.createLocalFindCarChargeSummaryPdf(summaryPdfParam, sessionInfo, summaryPdfVo);
        // 生成本地找车汇总单电子签章
        log.info("生成找车费用汇总单电子签章");
        FddConfigInfo fddConfigInfo = eqbHelper.getFDDConfigInfo();
        if(fddConfigInfo==null){
            throw new BusinessException(ResultCode.VALIDATOR.getCode(), ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
        }
        FindCarConstantResult result = this.createEcFindCarChargeSummaryPdf(summaryPdfVo);
        if (result.getCode() != CommonLang.SUCCESS.getCode()) {
            log.error("生成找车费用汇总单电子签章失败，原因：" + result.getMsg());
            return Result.error(result.getMsg());
        }
        summaryPdfVo.setSignLink(result.getData());
        this.updateFindCarChargeEcPdfId(String.valueOf(summaryPdfVo.getId()), sessionInfo , summaryPdfVo);
        return Result.of(summaryPdfVo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    /**
     * 找车汇总单生成本地pdf
     */
    @Override
    public void createLocalFindCarChargeSummaryPdf(FindCarChargeSummaryPdfParam cont, SysUser sessionInfo, FindCarChargeSummaryPdfVo findCarPdfVo) throws Exception {
        try {
            if (cont == null) {
                log.error("生成本地找车汇总单PDF的汇总单不存在");
                throw new BusinessException(ResultCode.VALIDATOR.getCode(), "生成本地找车汇总单PDF的汇总单不存在");
            }
            //
            Map<String, Object> map = new HashMap<>();
            map.put("contract", cont);
            map.put("firstKeyWord", cont.getFirstPartyId());
            map.put("secondKeyWord", cont.getSecondPartyId());
            map.put("id", cont.getSummaryId());
            log.info("firstKeyWord是：{}，secondKeyWord是：{}", cont.getFirstPartyId(), cont.getSecondPartyId());
            log.info("createLocalFindCarChargeSummaryPdf: cont={}",JSON.toJSON(cont));
            // 合同类型
            Integer contractType = DicConstant.DOCUMENT_TYPE.FIND_CAR_CHARGE;
            if (cont.getSignType() != null && cont.getSignType().equals(2)){ // 卸车修改，默认为找车服务费
                contractType = DicConstant.DOCUMENT_TYPE.UNLOAD_CAR_CHARGE;
            }
            String htmlTempl = PdfHelper.changeFtlToHtml(map, contractType, DicConstant.CONTRACT_BUSINESS_TYPE.BOTH);
            if (StrUtil.isBlank(htmlTempl)) {
                log.info("LOCAL HTML生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            }
            log.info("====> 合同生成 - 创建找车汇总单生成本地PDF文件 - 调用fileHelper.htmlToPdf前，cont: {}, htmlTempl:  <====", cont);
            FileInfoOut fileInfo = fileHelper.htmlToPdf(htmlTempl, sessionInfo.getAgentCode());
            if (fileInfo == null) {
                log.info("LOCAL PDF生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120002.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            }
            log.info("====> 合同生成 - 创建找车汇总单生成本地PDF文件 - 调用fileHelper.htmlToPdf后，cont: {}, fileInfo:  <====", cont);
            findCarPdfVo.setLocalPdfId(fileInfo.getFileID());
            findCarPdfVo.setLocalPdfUrl(fileInfo.getFileUrl());

        } catch (BusinessException e) {
            log.error("找车汇总单生成本地pdf失败原因:" , e);
            ContractResult result = new ContractResult();
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            throw new BusinessException(ResultCode.FAIL.getCode() ,e.getMessage());
        } catch (Exception e) {
            log.error("找车汇总单生成本地pdf失败原因:" , e);
            throw new Exception(e);
        }
    }

    /**
     * 更新找车费用电子签章pdfId
     * @param contractId
     * @param sessionInfo
     */
    @Override
    public String updateFindCarChargeEcPdfId(String contractId, SysUser sessionInfo,FindCarChargeSummaryPdfVo summaryPdfVo) {
        log.info("从法大大下载电子签章并上传文件服务器开始, contractId={}", contractId);
        Result<FileInfoOut> fddResult = contractFddSignFeign.downLoadEcPdfId(contractId,sessionInfo);
        log.info("从法大大下载电子签章并上传文件服务器结束, fddResult={}", fddResult);
        if(!fddResult.isSuccess()){
            log.error("从法大大下载电子签章并上传文件服务器失败, message={}", fddResult.getMessage());
            Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
            throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
        }
        FileInfoOut fileInfo = fddResult.getData();
        summaryPdfVo.setEcPdfId(fileInfo.getFileID());
        summaryPdfVo.setEcPdfUrl(fileInfo.getFileUrl());
        return fileInfo.getFileUrl();
    }

    /**
     * 校验找车费用汇总单生成合同入参
     * @return
     */
    private boolean checkFindCarChargeSummaryParam(FindCarChargeSummaryPdfParam summaryPdfParam) {
        if (summaryPdfParam == null) {
            log.info("找车费用汇总单生成合同入参不能为空");
            return false;
        }
        if (StrUtil.isBlank(summaryPdfParam.getFirstPartyId())) {
            log.info("甲方不能为空");
            return false;
        }
        if (StrUtil.isBlank(summaryPdfParam.getSecondPartyId())) {
            log.info("乙方不能为空");
            return false;
        }
        if(summaryPdfParam.getSummaryId() == null){
            log.info("合同id不能为空");
            return false;
        }
        if(StrUtil.isEmpty(summaryPdfParam.getServiceProject())){
            log.info("服务项目不能为空");
            return false;
        }
        return true;
    }


    /**
     * 找车费用汇总单生成电子签章
     * @param summaryPdfVo
     * @return
     */
    @Override
    public FindCarConstantResult createEcFindCarChargeSummaryPdf(FindCarChargeSummaryPdfVo summaryPdfVo) {
        log.info("====> 法大大签署合同入参: summaryPdfVo: {} <====", summaryPdfVo);
        FindCarConstantResult result = new FindCarConstantResult();
        String contractId = String.valueOf(summaryPdfVo.getId());
        List<String> openIds = new ArrayList<>();
        try {
            String cardNo = "";
            if(nacosValueConfig.getXtmTjCompanyId().equals(summaryPdfVo.getFirstPartyId())){
                cardNo = nacosValueConfig.getXtmTjCardNo();
            } else if (nacosValueConfig.getXtmGsCompanyId().equals(summaryPdfVo.getFirstPartyId())) {
                cardNo = nacosValueConfig.getXtmGsCardNo();
            }
            openIds.add(cardNo);
            CompanyBO secondPartyCompany = tmsCompanyService.getCompanyById(summaryPdfVo.getSecondPartyId());
            openIds.add(secondPartyCompany.getCompanyAdminInfo().getIdcardNo());
            openIds.add(secondPartyCompany.getUnifiedSocialCreditIdentifier());
            Map<String, FddElectricSealResp> openIdNew = chargeService.getOpenIdMap(openIds);
            // 法大大实名认证校验
            String secondCustomerId = chargeService.getCustomerId(secondPartyCompany.getCompanyAdminInfo().getIdcardNo(),0,openIdNew);
            if (StrUtil.isBlank(secondCustomerId)){
                secondCustomerId = chargeService.getCustomerId(secondPartyCompany.getUnifiedSocialCreditIdentifier(),0,openIdNew);
            }

            Map<String, FddElectricSealResp> openIdMap = getOpenIdMap(openIds);
            String customerId = getCustomerId(cardNo,openIdMap);
            if (StringUtils.isBlank(customerId) || StringUtils.isBlank(secondCustomerId)){
                result.setCode(ContractResult.XIAONIU_CONTRACT_BACK_140009.getCode());
                result.setMsg("法大大未认证，请先认证");
                return result;
            }
            SignIntegrationExtReq signIntegrationReq = new SignIntegrationExtReq();
            signIntegrationReq.setSource(1);
            signIntegrationReq.setContractId(contractId);
            signIntegrationReq.setCarryCustomerId(secondCustomerId); // 乙方
            signIntegrationReq.setTrustorCustomerId(customerId); // 甲方
            signIntegrationReq.setDocTitle(summaryPdfVo.getServiceProject());
            signIntegrationReq.setPdfUrl(summaryPdfVo.getLocalPdfUrl());
            signIntegrationReq.setCarryMobile(secondPartyCompany.getCompanyAdminInfo().getMobile());
//            signIntegrationReq.setCarrySignKeyword(ContractConstant.signKeyword.FIRST_PARTY);
            signIntegrationReq.setTrustorSignKeyword(ContractConstant.signKeyword.FIRST_PARTY);
            log.info("电子签章开始，contractId：{}，signIntegrationReq={}", contractId, signIntegrationReq);
            // 托运人自动签章；承运人手动签章，发送短信，返回短信发送链接
            Result<FddExtsignAutoResponse> fddResult = contractFddSignFeign.signIntegrationBzd(signIntegrationReq);
            log.info("电子签章结束，返回信息：{}", JSON.toJSONString(fddResult));
            if(!fddResult.isSuccess()){
                log.error("电子签章失败，fddResult：{}", JSON.toJSONString(fddResult));
                throw new BusinessException("电子签章失败:"+fddResult.getMsg());
            }
            FddExtsignAutoResponse fddResultData = fddResult.getData();
            result.setCode(CommonLang.SUCCESS.getCode());
            result.setData(fddResultData.getSignUrl());
            return result;
        } catch (BusinessException e) {
            log.info("电子签章contractId：{}，失败原因：", contractId, e);
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                result.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
        } catch (Exception e) {
            log.info("电子签章contractId：{}，失败原因：", contractId , e);
            result.setCode(CommonLang.SYS_FAIL.getCode());
            result.setMsg("电子签章生成失败发现未知异常原因："+e.getMessage());
        }
        return result;
    }

    /**
     * 预览合同内容
     *
     * @param contractPreviewReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseEntity<byte[]> contractPreview(ContractPreviewReq contractPreviewReq, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        //校验权限是否有交易模块
        String userId = sessionInfo.getId();
        String companyId = sessionInfo.getCompanyId();
        Result<Boolean> reponse = userActionExtFeginClient.checkMenuUrl(userId, companyId, CONTRACT_MODEL_MENUID);
        if(CommonLang.SYS_FAIL.getCode()==reponse.getCode()){
            log.error("API服务无法访问");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("API服务无法访问".getBytes(StandardCharsets.UTF_8));
        }
        if(CommonLang.SUCCESS.getCode()!=reponse.getCode()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse.getMessage().getBytes(StandardCharsets.UTF_8));
        }
        String contractId = contractPreviewReq.getContractId();
        Integer contractType = contractPreviewReq.getType();
        String fileUrl = null;
        String fileName = "";
        String fileId = null;
        if(ContractPreviewEnum.PREVIEW_ELECTRONIC_SIGNATURE_CONTRACT.getType().equals(contractType)){
            Contract contract = getById(contractId);
            if(contract==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同不存在".getBytes(StandardCharsets.UTF_8));
            }
            //预览电子签章
            String ecContractPdfId = contract.getEcContractPdfId();
            FileInfoVo ecFile = tmsFileService.getFileById(ecContractPdfId);
            if(ecFile==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("电子签章合同不存在".getBytes(StandardCharsets.UTF_8));
            }
            fileUrl = ecFile.getFileServerUrl() + ecFile.getUrl();
            fileName= ecFile.getUrl();
            fileId = ecFile.getId();
        }else if(ContractPreviewEnum.PREVIEW_FRAMEWORK_CONTRACT.getType().equals(contractType)){
            Contract contract = getById(contractId);
            if(contract==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同不存在".getBytes(StandardCharsets.UTF_8));
            }
            //预览框架合同
            QueryWrapper<ContractAttach> qw = new QueryWrapper<>();
            qw.in("contract_id", contract.getId());
            Optional<ContractAttach> first = attachMapper.selectList(qw).stream().findFirst();
            if(!first.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("框架合同附件不存在".getBytes(StandardCharsets.UTF_8));
            }
            ContractAttach contractAttach = first.get();
            fileId = contractAttach.getFileId();
            FileInfoVo frameFile = tmsFileService.getFileById(fileId);
            if(frameFile==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("框架合同不存在".getBytes(StandardCharsets.UTF_8));
            }
            fileUrl = frameFile.getFileServerUrl() + frameFile.getUrl();
            fileName= frameFile.getUrl();
            fileId = frameFile.getId();
        }else if(ContractPreviewEnum.PREVIEW_NO_SHIP_NO_CARGO_TOB_WT_CONTRACT.getType().equals(contractType)||ContractPreviewEnum.PREVIEW_NO_SHIP_NO_CARGO_TOB_TY_CONTRACT.getType().equals(contractType)){
            Result<String> urlResult = userActionExtFeginClient.getDjDdxxAttachUrl(contractId,contractType);
              if(CommonLang.SUCCESS.getCode()!=urlResult.getCode()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlResult.getMessage().getBytes(StandardCharsets.UTF_8));
              }
              fileUrl = urlResult.getData();
              if(fileUrl==null){
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同不存在".getBytes(StandardCharsets.UTF_8));
              }
              fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        }else if((ContractPreviewEnum.PREVIEW_GANSU_TO_CARGO_TOB_WT_CONTRACT.getType().equals(contractType)||ContractPreviewEnum.PREVIEW_GANSU_TO_CARGO_TY_CONTRACT.getType().equals(contractType))){
            Result<String> urlResult = userActionExtFeginClient.getGansuDdxxAttachUrl(contractId,contractType);
            if(CommonLang.SUCCESS.getCode()!=urlResult.getCode()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlResult.getMessage().getBytes(StandardCharsets.UTF_8));
            }
            fileUrl = urlResult.getData();
            if(fileUrl==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同不存在".getBytes(StandardCharsets.UTF_8));
            }
            fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同类型错误".getBytes(StandardCharsets.UTF_8));
        }
        Response downLoadResponse = null;
        InputStream inputStream = null;
        try {
            if(StringUtils.isNotBlank(fileId)){
                downLoadResponse = fileFeignAdapter3.downLoadToResponseByFileId(fileId);
            }else{
                DownloadIn downloadIn = new DownloadIn();
                downloadIn.setFilePath(fileUrl);
                downLoadResponse = fileFeignAdapter3.downLoadToResponseByUrl(fileUrl);
            }

            if(downLoadResponse.status()!=HttpStatus.OK.value()){
                log.error("文件下载失败:{}",fileUrl);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("文件下载失败".getBytes(StandardCharsets.UTF_8));
            }
            Response.Body body = downLoadResponse.body();
            inputStream = body.asInputStream();
            byte[] data = IoUtil.readBytes(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("文件流打开异常：",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件访问失败".getBytes(StandardCharsets.UTF_8));
        }finally {
            IoUtil.close(inputStream);
            IoUtil.close(downLoadResponse);
        }
    }

    @Override
    public FindCarContractResVo checkSignStatus(String contractId, String companyId) {
        log.info("查询签章结果请求参数，contractId：{}，companyId：{}",contractId,companyId);
        CompanyVo companyBO = tmsCompanyService.findCompanyById(companyId);
        UserInfoVo user = tmsUserService.getUserById(companyBO.getCompanyAdmin());
        String userCustomerNoAutoId = getCustomerNoAutoId(user.getIdcardNo());
        String companyCustomerNoAutoId = getCustomerNoAutoId(companyBO.getUnifiedSocialCreditIdentifier());;
        //如果企业和个人都有法大大认证信息，则先查询个人 个人结果为false 再查询企业
        boolean isPerson = true;
        String firstCustomerId = userCustomerNoAutoId;
        if(StringUtils.isBlank(userCustomerNoAutoId)){
            isPerson = false;
            firstCustomerId = companyCustomerNoAutoId;
        }

        boolean equals = false;
        QuerySignStatusRes response = getFddSignStatus(contractId, firstCustomerId);
        FindCarContractResVo contractResVO = new FindCarContractResVo();
        equals = checkStatus(response);
        if(!equals&&isPerson){
            //如果企业和个人都有法大大认证信息，则先查询个人 个人结果为false 再查询企业
            log.info("根据当前账号查询法大大签章信息-个人结果为false，开始查询企业");
            response = getFddSignStatus(contractId, companyCustomerNoAutoId);
            equals = checkStatus(response);
        }
        if (equals) {
            String downloadUrl = response.getDownload_url();
            FileInfoOut fileInfo = fileHelper.urlUploadFile(downloadUrl, nacosValueConfig.getAgentCode(), DicConstant.DOCUMENT_TYPE.FIND_CAR_CHARGE + "_" + IdWorker.getIdStr() + ".pdf");
            if (fileInfo == null) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            contractResVO.setPdfUrl(fileInfo.getFileUrl());
            contractResVO.setFileId(fileInfo.getFileID());
        }
        contractResVO.setStatus(equals);
        return contractResVO;
    }

    private boolean checkStatus(QuerySignStatusRes response) {
        if (FddSignResultConstant.UPLOAD_PDF_TO_FDD_SUCCESS.toString().equals(response.getCode())) {
            return FddSignResultConstant.success_long.equals(response.getSign_status()) || "已签".equals(response.getSign_status_desc());
        }
        return false;
    }

    /**
     * 查询法大大签署结果
     * @param contractId
     * @param companyCustomerNoAutoId
     * @return
     */
    private QuerySignStatusRes getFddSignStatus(String contractId, String companyCustomerNoAutoId) {
        if(StringUtils.isBlank(contractId)||StringUtils.isBlank(companyCustomerNoAutoId)){
            log.error("查询签署信息参数为空");
            QuerySignStatusRes querySignStatusRes = new QuerySignStatusRes();
            querySignStatusRes.setCode(CommonLang.SYS_FAIL.getCode()+"");
            return querySignStatusRes;
        }
        log.info("法大大查询用户签署结果,开始:{},{}",contractId,companyCustomerNoAutoId);
        Result<QuerySignStatusRes> fddResult = contractFddSignFeign.checkSignStatus(contractId, companyCustomerNoAutoId, "batchCon.get(0).getExtsignAutoTransId()");
        log.info("法大大查询用户签署结果,结束:{}",fddResult);
        if (!fddResult.isSuccess()) {
            log.error("查询法大大签章状态异常:{}",fddResult.getMsg());
            throw new BusinessException("查询法大大签章状态异常:"+fddResult.getMsg());
        }
        return fddResult.getData();
    }

    private String getCustomerNoAutoId(String openId){
        log.info("getCustomerId: openId={}", openId);
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        FddElectricSealResp fddResult = fddFeignService.getFddElectricSealByOpenId(openId);
        if(fddResult==null){
            return null;
        }
        Integer verifyStatus = fddResult.getVerifyStatus();
        if (verifyStatus!=null&&verifyStatus == 1) { // 已认证
            return fddResult.getCustomerId();
        }
        return null;
    }

    /**
     * 获取当前用户的权限公司id集合
     * @return
     */
    private List<String> getAuthCompanyIds(){
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(LoginUserContextHolder.getUser().getId());
        Result<UserDataAuthVo> result = null;
        try {
            log.info("-----获取用户权限参数：{}----", JSONUtil.toJsonStr(userInfoDto));
            result = userFeign.getUserManageCompany(userInfoDto);
            log.info("-----获取用户权限参数：{}，结果：{}----", JSONUtil.toJsonStr(userInfoDto),JSONUtil.toJsonStr(result));
            //获取权限失败
            if (!Objects.equals(result.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
                throw new BusinessException(ResultCode.FAIL.getCode(), "获取用户数据权限异常");
            }
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage());
        }
        //说明所有权限都有
        if(result.getData().getIsAuthAll()){
            return null;
        }
        //说明当前用户没有数据权限
        if(CollUtil.isEmpty(result.getData().getCompanyIds())){
            return new ArrayList<>();
        }
        //权限公司的集合
        return result.getData().getCompanyIds();
    }

    @Override
    public List<ContractVoRes> findContractTemplate(ContractReq contractReq) {
        List<String> authCompanyIds = getAuthCompanyIds();
        if(CollectionUtils.isEmpty(authCompanyIds)){
            return new ArrayList<>();
        }
        contractReq.setAuthCompanyIds(authCompanyIds);
        List<ContractVoRes> contractTemplates = contractMapper.findContractTemplate(contractReq);
        if (contractTemplates != null && !contractTemplates.isEmpty()){
            contractTemplates.forEach(contractVoRes -> {
                if (StringUtils.isNotBlank(contractVoRes.getFileId())){
                    FileInfoVo file = tmsFileService.getFileById(contractVoRes.getFileId());
                    contractVoRes.setFileUrl(file.getFileServerUrl() + file.getUrl());
                }
            });
            return contractTemplates;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Contract> queryShardingTable(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if(startTime!=null){
            queryWrapper.gt(Contract::getCreateTime,startTime);
        }
        if(endTime!=null){
            queryWrapper.lt(Contract::getCreateTime,endTime);
        }
        if(startTime.isAfter(endTime)){
            throw new BusinessException("开始时间不能大于结束时间");
        }
        return contractMapper.selectList(queryWrapper);
    }

    /**
     * 修改合同表数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractData(UpdateContractDataParam param) {

        if(param == null){
            throw new BusinessException("修改合同数据失败，请求参数为空");
        }

        ContractParam contractSet = param.getContractSet();
        ContractParam contractFilter = param.getContractFilter();

        if(param.getContractSet() == null || param.getContractFilter() == null){
            throw new BusinessException("修改合同数据失败，请求参数为空");
        }

        // 修改值
        Contract contract = new Contract();
        if(StrUtil.isNotEmpty(contractSet.getDocumentId())){
            contract.setDocumentId(contractSet.getDocumentId());
        }
        if(contractSet.getContractPrice() != null){
            contract.setContractPrice(contractSet.getContractPrice());
        }

        // 过滤条件
        LambdaUpdateWrapper<Contract> wrapper = Wrappers.lambdaUpdate();
        if(StrUtil.isNotEmpty(contractFilter.getDocumentId())){
            wrapper.eq(Contract::getDocumentId, contractFilter.getDocumentId());
        }
        if(contractFilter.getDocumentType() != null){
            wrapper.eq(Contract::getDocumentType, contractFilter.getDocumentType());
        }
        if(contractFilter.getIsDelete() != null){
            wrapper.eq(Contract::getIsDelete, contractFilter.getIsDelete());
        }

        contractMapper.update(contract, wrapper);
    }

    /**
     * 查询合同表数据
     * @param param
     * @return
     */
    @Override
    public List<ContractVo> queryContractAllData(ContractParam param) {
        return contractMapper.queryContractAllData(param);
    }

    @Override
    public BigDecimal getCumulativeTradingVolume() {
        return contractMapper.getCumulativeTradingVolume();
    }


}
