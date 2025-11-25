package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtm.common.context.LoginUserContextHolder;
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
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.enums.SnowflakeEnum;
import com.xtm.contract.feign.TmsCompanyService;
import com.xtm.contract.feign.EnergyFeign;
import com.xtm.contract.feign.SettingServiceFeign;
import com.xtm.contract.feign.TmsFileService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.mapper.SettleBillsMapper;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.bo.AddressInfo;
import com.xtm.contract.model.domain.ContractExt;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.domain.SettleBills;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.service.ChargeService;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.FddFeignService;
import com.xtm.thirdparty.auth.feign.ContractFddSignFeign;
import com.xtm.thirdparty.auth.model.req.ElecSealOffSetXy;
import com.xtm.thirdparty.auth.model.req.SignIntegrationExtReq;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.FddExtsignAutoResponse;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.energy.BalanceDetailRes;
import com.xtm.contract.model.query.eqbDto.ESignAccountDTO;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.query.eqbReq.EFileInfoReq;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractResVO;
import com.xtm.contract.model.vo.contract.SettleBillsInfoQryVO;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.contractOther.SupplementContractInfo;
import com.xtm.contract.model.vo.eqb.ContractSignInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.thirdparty.auth.model.resp.QuerySignStatusRes;
import com.xtm.contract.service.ContractEqbSignService;
import com.xtm.contract.service.ContractFddSignService;
import com.xtm.contract.service.ContractTemplateService;
import com.xtm.contract.service.ElectricSealSwitchService;
import com.xtm.contract.service.SettleBillsService;
import com.xtm.contract.utils.ContractSessionUtil;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.FileHelper;
import com.xtm.contract.utils.GDInvoicesPdfHelper;
import com.xtm.contract.utils.IdWorker;
import com.xtm.contract.utils.InvoicesPdfHelper;
import com.xtm.contract.utils.OrganizationOrSettingHelper;
import com.xtm.contract.utils.PdfHelper;
import com.xtm.file.model.vo.FileInfoVo;
import com.xtm.setting.model.vo.DictionaryVo;
import com.xtm.thirdparty.auth.model.resp.ElectricSealResponse;
import com.xtm.user.model.vo.ContactVo;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: zt
 * @Desc: 合同业务SERVICE
 * @date: 2021/6/25 14:43
 * @version: 1.0
 */
@Slf4j
@Service
public class SettleBillsServiceImpl extends ServiceImpl<SettleBillsMapper, SettleBills> implements SettleBillsService {
    @Autowired
    private SettleBillsMapper settleBillsMapper;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private TmsUserService userService;
    @Autowired
    private TmsFileService fileService;
    @Autowired
    private TmsCompanyService companyService;
    @Autowired
    private ContractEqbSignService eqbSignService;
    @Autowired
    private SettingServiceFeign settingService;
    @Autowired
    private OrganizationOrSettingHelper organizationOrSettingHelper;
    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private EqbHelper eqbHelper;
    @Autowired
    private ContractFddSignService contractFddSignService;
    @Autowired
    private ElectricSealSwitchService electricSealSwitchService;
    @Autowired
    private ContractAsyncServiceImpl contractAsyncService;
    @Autowired
    private NacosValueConfig nacosValueConfig;
    @Autowired
    private EnergyFeign energyFeign;

    @Resource
    private FddFeignService fddFeignService;

    @Resource
    private ChargeService chargeService;

    @Resource
    private ContractFddSignFeign contractFddSignFeign;

    @Resource
    private ContractService contractService;

    @Override
    public Result  createContract(CommonCreUpdReq contractCreUpdParam) {
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
        SettleBills contract = new SettleBills();
        String id = "";
        log.info("生成合同 documentId={}", contractCreUpdParam.getDocumentId());
        //是否新增
        if (createFlag) {
            id = IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT);
            contract.setId(id);
        } else {
            contract = selectContractByDocumentId(contractCreUpdParam.getDocumentId());
            if (contract == null) {
                contract = new SettleBills();
                id = IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT);
                contract.setId(id);
                createFlag = true;
            } else {
                id = contract.getId();
            }
        }
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);
        if (StrUtil.isBlank(currentCompanyId)) {
            currentCompanyId = contractCreUpdParam.getPlatCompanyId();
        }
        String currentUserId = ContractSessionUtil.getCurrentUserID(sessionInfo);
        //组装合同基本信息
        assemblyContractCreBaseInfo(contractCreUpdParam, currentCompanyId, contract);
        if (StrUtil.isBlank(contract.getContractTemplateId())) {
            return Result.error(CommonLang.NOSUCHMETHOD_FAIL.getCode(), "未配置该类型业务的合同模板，请至合同中心配置");
        }
        //新增
        if (createFlag) {
            int ver = 1;
            contract.setCreater(contractCreUpdParam.getCreater());
            contract.setCreateTime(new Date());
            contract.setVer(ver);
            contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            settleBillsMapper.insert(contract);
        } else {
            contract.setModifier(currentUserId);
            contract.setModifyTime(new Date());
            settleBillsMapper.updateById(contract);
        }
        try {
            contractCreUpdParam.setSupplierCompanyIdCardNo(userService.getUserByCompanyId(contractCreUpdParam.getSupplierCompanyId()).getIdcardNo());
        }catch(Exception e){
            log.error("获取供应商身份证号失败",e);
            throw new BusinessException(ContractResult.XIAONIU_CONTRACT_BACK_140001.getCode(), ContractResult.XIAONIU_CONTRACT_BACK_140001.getMsg());
        }
        //异步后置处理方法
        // 是否静默签署
        Boolean eqbSignFlg = contractCreUpdParam.getEqbSignFlg();
        log.info("====> 创建合同，是否静默签署：{} <====", eqbSignFlg);
        if (eqbSignFlg) {
            String contractId = id;
            if (signSwitchTag.equals(ContractConstant.ContractType.FDD)) {
                contractAsyncService.fddAfterCommonSave(contractCreUpdParam, contractId, sessionInfo);
            } else {
                contractAsyncService.afterCommonSave(contractCreUpdParam, contractId, sessionInfo);
            }
        }
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    public String updateFddPdfId( String contractId, SysUser sessionInfo) {
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
    public void batchCreateContract(List<CommonCreUpdReq> contractCreUpdReqs) {
        log.info("Batch create contract start!");
        if(CollUtil.isEmpty(contractCreUpdReqs)){
            log.error("批量生成合同的参数不能为空！");
            return;
        }
        log.info("The number of create contracts: "+contractCreUpdReqs.size());
        contractCreUpdReqs.forEach(this::createContract);
        log.info("Batch create contract end!");
    }
    private String  getCustomerId(String openId,Map<String, FddElectricSealResp> openIdMap){
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
    public ContractResult fDDCreateEcContractPdf(SettleBillsInfoQryVO contractInfo) {
        log.info("====> 法大大签署合同入参: contractInfo: {} <====", contractInfo);
        ContractResult result = new ContractResult();
        String contractId = contractInfo.getId();
        List<String> openIds = new ArrayList<>();
        Map<String, FddElectricSealResp> customerIdMap = null;
        try {
            SignIntegrationExtReq signIntegrationReq = new SignIntegrationExtReq();
            signIntegrationReq.setSource(1);
            signIntegrationReq.setContractId(contractId);
            /** 结算合同动态获取法大大客户id 开始 **/
            CompanyVo company = companyService.findCompanyById(contractInfo.getCarrierCompanyId());
            UserInfoVo user = userService.getUserById(company.getCompanyAdmin());
            openIds.add(nacosValueConfig.getTjzwCardNo());
            openIds.add(user.getIdcardNo());
            signIntegrationReq.setTrustorCustomerId(chargeService.getCustomerId(nacosValueConfig.getTjzwCardNo(),0,customerIdMap));
            customerIdMap = chargeService.getOpenIdMap(openIds);
            signIntegrationReq.setCarryMobile(user.getMobile());
            String carryCustomerId = chargeService.getCustomerId(user.getIdcardNo(),0,customerIdMap);
            signIntegrationReq.setCarryCustomerId(carryCustomerId);
            /** 结算合同动态获取法大大客户id 结束 **/
            signIntegrationReq.setDocTitle(contractInfo.getTitle());
            signIntegrationReq.setPdfUrl(contractInfo.getEcContractPathUrl());
//            signIntegrationReq.setCarrySignKeyword(ContractConstant.signKeywordSettleBill.GF);
            signIntegrationReq.setTrustorSignKeyword(ContractConstant.signKeywordSettleBill.XF);

            //尊骏能源月账单需要设置 甲方(小铁马)签章偏移量，正数表示在关键字右边签章，数字在【-595,595】之间
            Integer contractDocumentType = contractInfo.getDocumentType();
            if(contractDocumentType!=null&&contractDocumentType==1133440){
                ElecSealOffSetXy elecSealOffSetXy = ElecSealOffSetXy.builder().keyx("55").build();
                signIntegrationReq.setTrustorOffSetXy(elecSealOffSetXy);
            }

            log.info("签署合同签章 signIntegrationReq={}", signIntegrationReq);
            Result<FddExtsignAutoResponse> fddExtsignAutoResponseResult = contractFddSignFeign.signIntegrationBzd(signIntegrationReq);
            log.info("签署合同签章结束，fddExtsignAutoResponseResult:{}", fddExtsignAutoResponseResult);
            if (!fddExtsignAutoResponseResult.isSuccess()){
                log.error("签署合同签章异常:{}",fddExtsignAutoResponseResult.getMsg());
                throw new BusinessException(" 法大大签署合同异常:"+fddExtsignAutoResponseResult.getMsg());
            }
            FddExtsignAutoResponse reponse = fddExtsignAutoResponseResult.getData();
            //签署成功后，保存状态
            saveEQBFlowIdToContract(reponse.getExtsignAutoTransId(), contractId, ContractConstant.SignType.FDD,contractInfo.getCardNo());
        } catch (BusinessException e) {
            log.error("电子签章生成失败", e);
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                result.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
            //错误信息落库
            saveErrorInfoToContract(result, contractId);
        }
        return result;
    }

    @Override
    public void createFDDContractPdf(SettleBillsInfoQryVO contractInfo) {
        SettleBillsInfoQryVO batchCon = selectContractByDocumentId(contractInfo.getDocumentId());
        fDDCreateEcContractPdf(batchCon);
    }


    @Override
    public ContractResult createEcContractPdf(SettleBillsInfoQryVO contractInfo, SysUser sessionInfo) {
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
            List<SettleBills> contractList = this.listByIds(contractIds);
            if (CollUtil.isEmpty(contractList)) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
                String msg = "要删除的合同不存在！";
                throw new BusinessException(code,msg);
            }
            for (SettleBills contract : contractList) {
                contract.setIsDelete(DicConstant.IS_DELETE.YES);
                int res = settleBillsMapper.updateById(contract);
                count = count + res;
            }
        } catch (Exception e) {
            log.error("合同删除异常",e);
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
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
                 List<SettleBills> contractList = this.list(new QueryWrapper<SettleBills>().eq("document_id",docId));
                if (CollUtil.isEmpty(contractList)) {
                    Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
                    String msg = "要删除的合同不存在！";
                    throw new BusinessException(code,msg);
                }
                for (SettleBills contract : contractList) {
                    contract.setIsDelete(DicConstant.IS_DELETE.YES);
                    settleBillsMapper.updateById(contract);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_DELETE.getCode(), ContractErrorCode.DELETE_CONTRACT_ERROR.getCode());
            String msg = ContractErrorCode.DELETE_CONTRACT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }
    }

    @Override
    public SettleBillsInfoQryVO selectContractDetail(String contractId) {
        log.info("====> 查询合同详情 - contractId: {} <====", contractId);
        // 查询签章开关标识（0：e签宝；1：法大大）
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        Integer signSwitchTag = electricSealSwitch.getSignSwitchTag();

        SettleBillsInfoQryVO contractInfo = settleBillsMapper.findContractById(contractId);
        log.info("====> 查询合同详情 - contractInfo: {} <====", JSON.toJSONString(contractInfo));
        if (contractInfo == null) {
            Integer code = ServerCode.getServerCode(ServerCode.TMS, ModuleCode.DETAIL.getCode(), FunctionCode.CON_QUERY.getCode(), ContractErrorCode.CONTRACT_DATA_ISNULL.getCode());
            String msg = ContractErrorCode.CONTRACT_DATA_ISNULL.getMessage();
            throw new BusinessException(code, msg);
        }

        //托运人
        CompanyBO trustorCompany = companyService.getCompanyById(contractInfo.getTrustorCompany().getId());
        ContactVo trustContact = new ContactVo();
        trustContact.setName(contractInfo.getTrustorContactName());
        trustContact.setMobile(contractInfo.getTrustorContactMobile());
        trustorCompany.setContact(trustContact);
        contractInfo.setTrustorCompany(trustorCompany);

        AddressInfo addressInfo = new AddressInfo();
        String registeredAddressId = trustorCompany.getRegisteredAddressId();
        addressInfo.setAddressID(registeredAddressId);

        ContactVo contact = new ContactVo();
        contact.setName(contractInfo.getCarrierContactName());
        contact.setMobile(contractInfo.getCarrierContactMobile());
        String platCompanyId = contractInfo.getPlatCompanyId();
        if (StrUtil.isNotBlank(platCompanyId)) {
            CompanyBO platCompany = companyService.getCompanyById(platCompanyId);
            contractInfo.setPlatCompany(platCompany);
        }

        //本地pdf
        if (StrUtil.isNotBlank(contractInfo.getEcContractPath())) {
            FileInfoVo file = fileService.getFileById(contractInfo.getEcContractPath());
            if (file != null) {
                contractInfo.setEcContractPathUrl(file.getFileServerUrl() + file.getUrl());
                log.info("====> 查询合同详情 - 本地PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
            }
        }

        //电子签章pdf
        if (StrUtil.isNotBlank(contractInfo.getEcContractPdfId())) {
            FileInfoVo file = fileService.getFileById(contractInfo.getEcContractPdfId());
            if (file != null) {
                contractInfo.setEcContractPdfUrl(file.getFileServerUrl() + file.getUrl());
                log.info("====> 查询合同详情 - 电子签章PDF - contractInfo.getEcContractPathUrl(): {} <====", contractInfo.getEcContractPathUrl());
            }
        }

        //按钮权限
        Map<String, String> buttonResult = new HashMap<>();
        buttonResult.put("btnEcSigned", "hide");
        ContractExt contractExt = new ContractExt();
        contractExt.setContractId(contractInfo.getId());

        if (StrUtil.isNotBlank(contractInfo.getEcContractPdfUrl())) {
            buttonResult.put("btnEcSigned", "show");
        } else {
            if (signSwitchTag.equals(ContractConstant.ContractType.FDD)) {
                //法大大 是否认证
                CompanyVo company = companyService.findCompanyById(contractInfo.getCarrierCompanyId());
                UserInfoVo user = userService.getUserById(company.getCompanyAdmin());
                if (user != null && StrUtil.isNotEmpty(user.getIdcardNo())) {
                    if (isFFVasAuth(user.getIdcardNo())) {
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
                    log.error("订阅服务调用异常:" + e.getMessage());
                }
            }
        }
        contractInfo.setButtonPermission(buttonResult);
        return contractInfo;
    }

    private List<SupplementContractInfo> supplementContractInfoList(String contractId) {
        SettleBills cont = getById(contractId);
        if (cont == null) {
            return null;
        }
        List<SupplementContractInfo> supplementContractInfos = settleBillsMapper.findSupplementContract(contractId);
        return supplementContractInfos;
    }

    /**
     * 查询法大大是否能签署
     */
    public boolean isFFVasAuth(String carrierContractMobile) {
        List<String> openIds = new ArrayList<>();
        openIds.add(nacosValueConfig.getTjzwCardNo());
        openIds.add(carrierContractMobile);
        Map<String, FddElectricSealResp> openIdMap = contractService.getOpenIdMap(openIds);
        String customerId = getCustomerId(nacosValueConfig.getTjzwCardNo(),openIdMap);
        String trustorCompanyCustomerId = getCustomerId(carrierContractMobile,openIdMap);
        if (customerId == null || trustorCompanyCustomerId == null) {
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
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(filId)) {
            contract.setEcContractPath(filId);
        }
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate()
                .eq(SettleBills::getId, contractId);
        settleBillsMapper.update(contract, extUpdateWrapper);
    }

    private void updateFddPdfToContract(String fddPdfId, String contractId) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        contract.setEcContractPdfId(fddPdfId);
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate().eq(SettleBills::getId, contractId);
        settleBillsMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 保存下载的电子合同ID
     * @param ecPdfId
     * @param contractId
     */
    @Override
    public void saveEcPdfToContract(String ecPdfId,String contractId) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(ecPdfId)) {
            contract.setEcContractPdfId(ecPdfId);
        }
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate()
                .eq(SettleBills::getId, contractId);
        settleBillsMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 保存e签宝的文件ID
     * @param eSignFileId
     * @param contractId
     */
    @Override
    public void saveEQBFileIdToContract(String eSignFileId,String contractId) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        if (StrUtil.isNotBlank(eSignFileId)) {
            contract.setEcContractEsignFileId(eSignFileId);
        }
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate()
                .eq(SettleBills::getId, contractId);
        settleBillsMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 更新合同签署状态
     * @param trustorStatu
     * @param carrierStatu
     * @param contractId
     */
    @Override
    public void updateContractSignStatus(Integer trustorStatu, Integer carrierStatu, String contractId) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        contract.setTrustorSignStatus(trustorStatu);
        contract.setCarrierSignStatus(carrierStatu);
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate()
                .eq(SettleBills::getId, contractId);
        settleBillsMapper.update(contract, extUpdateWrapper);
    }

    /**
     * 保存e签宝的流程ID
     * @param flowId
     * @param contractId
     */
    public Integer saveEQBFlowIdToContract(String flowId,String contractId,Integer signType,String carrierContractIdCardNo) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("合同签署---->当前合同不存在!");
            return null;
        }
        contract.setEcContractEsignFlowId(flowId);
        //更改签署状态为已签署
        contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
        contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.SUCCESS);
        LambdaUpdateWrapper<SettleBills> extUpdateWrapper = Wrappers.<SettleBills>lambdaUpdate()
                .eq(SettleBills::getId, contractId);
        int count = settleBillsMapper.update(contract, extUpdateWrapper);
        if (count <= 0) {
            log.info("合同签署---->保存归档ID失败");
            return null;
        }
        return count;
    }

    /**
     * 保存错误信息
     * @param result
     * @param contractId
     */
    @Override
    public void saveErrorInfoToContract(ContractResult result,String contractId) {
        SettleBills contract = getById(contractId);
        if (contract == null) {
            log.error("当前合同不存在!");
            return;
        }
        contract.setEcContractResultCode(String.valueOf(result.getCode()));
        contract.setEcContractResultDesc(result.getMsg());
        settleBillsMapper.updateById(contract);
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
            SettleBillsInfoQryVO contractInfo = selectContractDetail(id);

            if (Objects.equals(signSwitchTag, ContractConstant.ContractType.FDD)) {
                log.info("rebuildEcContract 法大大 id={}",id);
                if (StrUtil.isBlank(contractInfo.getEcContractEsignFlowId())) {
                    //重新生成电子合同
                    createFDDContractPdf(contractInfo);
                }
                contractPath.setEcontractUrl(updateFddPdfId(id, sessionInfo));
                log.info("rebuildEcContract: 法大大 contractPath={}", JSON.toJSON(contractPath));
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
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_DOWNLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.FILE_DOWNLOAD_FAIL.getMessage());
            }
            //上传到服务器
            FileInfoOut fileInfo = fileHelper.urlUploadFile(ecPdfUrl, sessionInfo.getAgentCode(), DicConstant.DOCUMENT_TYPE.CONTRACT.toString() + ".pdf");
            if (fileInfo == null) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            contractPath.setEcontractUrl(fileInfo.getFileUrl());
            //保存电子印章ID
            saveEcPdfToContract(fileInfo.getFileID(), id);
        } catch (BusinessException e) {
            log.error("rebuildEcContract 异常", e);
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
            log.error("法大大重新签章异常:",ex);
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
        SettleBillsInfoQryVO contractInfo = selectContractDetail(id);
//        String contractTemplateId = contractInfo.getContractTemplateId();
//        ContractTemplate contractTemplate = contractTemplateService.getById(contractTemplateId);
        Result<BalanceDetailRes> payDetailInfolList = energyFeign.getPayDetailInfolList(Long.parseLong(id));
        if (payDetailInfolList.getCode() != CommonLang.SUCCESS.getCode() || payDetailInfolList.getData() == null) {
            throw new BusinessException(payDetailInfolList.getCode(), payDetailInfolList.getMessage());
        }
        //重新生成PDF
        if (StrUtil.isBlank(contractInfo.getEcContractPath())) {
            createLocalDeatilPdf(contractInfo,payDetailInfolList.getData(), sessionInfo, contractInfo.getBusinessType());
            contractInfo.setEcContractPath(settleBillsMapper.findContractById(id).getEcContractPath());
        }
        createFDDContractPdf(contractInfo);
        String localPdf = contractInfo.getEcContractPath();
        contractPath.setEcontractUrl(localPdf);
        return contractPath;
    }

    @Override
    public ContractCodeQryVO selectContractCodeByDocumentId(String documentId) {
        if (StrUtil.isBlank(documentId)) {
            log.error("查询合同的单据ID不能为空！");
            return null;
        }
        ContractCodeQryVO contractCodeInfo = new ContractCodeQryVO();
        SettleBills contract = this
                .lambdaQuery()
                .select(SettleBills::getId,SettleBills::getContractCode,SettleBills::getTitle)
                .eq(SettleBills::getDocumentId,documentId)
                .eq(SettleBills::getIsDelete,DicConstant.COMMON_ZERO)
                .orderByDesc(SettleBills::getVer)
                .last(" limit 1")
                .one();

        if (contract != null) {
            contractCodeInfo.setContractId(contract.getId());
            contractCodeInfo.setContractCode(contract.getContractCode());
            contractCodeInfo.setTitle(contract.getTitle());
        }

        return contractCodeInfo;
    }

    @Override
    public List<SettleBills> selectExpiresContract() {
        LambdaQueryWrapper<SettleBills> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SettleBills::getContractType, DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT);
        wrapper.lt(SettleBills::getValidStartDate,new Date()).or().gt(SettleBills::getValidEndDate,new Date());
        List<SettleBills> contractList = settleBillsMapper.selectList(wrapper);
        return contractList;
    }

    @Override
    public ApiPageResult<SettleBillsInfoQryVO> selectContractList(ContractListQryReq contractListQryReq) {
        Page<SettleBillsInfoQryVO> pageParam = new Page<>(contractListQryReq.getPageNum(), contractListQryReq.getPageSize());
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);
        contractListQryReq.setCompanyId(currentCompanyId);

        IPage<SettleBillsInfoQryVO> pageContractInfoResult = settleBillsMapper.findListContract(pageParam,contractListQryReq);
        List<SettleBillsInfoQryVO> contractInfoQryList = pageContractInfoResult.getRecords();
        ApiPageResult<SettleBillsInfoQryVO> resultApiPageResult = ApiPageResult.<SettleBillsInfoQryVO>builder()
                .currentPage((int)pageContractInfoResult.getCurrent())
                .pageSize((int)pageContractInfoResult.getSize())
                .totalPage((int)pageContractInfoResult.getPages())
                .total((int)pageContractInfoResult.getTotal())
                .build();
        if (CollectionUtil.isEmpty(contractInfoQryList)) {
            return resultApiPageResult;
        }
        //编制方企业查询
        List<String> compileCompanyIds = contractInfoQryList.stream().distinct().map(SettleBillsInfoQryVO::getCompileSideId).collect(Collectors.toList());
        List<CompanyVo> compaileCompanyList = companyService.findCompanyByIds(compileCompanyIds);
        //key-->公司Id:value-->公司名称
        Map<String, String> compileCompanyMap = compaileCompanyList.stream().collect(Collectors.toMap(CompanyVo::getId, x -> x.getName()));
        //创建人查询
        List<String> userIds = contractInfoQryList.stream().distinct().map(SettleBillsInfoQryVO::getCreater).collect(Collectors.toList());
        List<UserInfoVo> userList = userService.getUserByIds(userIds);
        //key-->用户Id:value-->用户名称
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(UserInfoVo::getId, x -> x.getName()));

        //查询字典值
        List<Long> contractTypeList = contractInfoQryList.stream().distinct().map(settleBills->settleBills.getContractType().longValue()).collect(Collectors.toList());
        List<Long> documentTypeList = contractInfoQryList.stream().distinct().map(settleBills->settleBills.getDocumentType().longValue()).collect(Collectors.toList());
        List<Long> businessTypeList = contractInfoQryList.stream().distinct().map(settleBills->settleBills.getBusinessType().longValue()).collect(Collectors.toList());
        contractTypeList.addAll(documentTypeList);
        contractTypeList.addAll(businessTypeList);

        List<DictionaryVo> dicTypeDescList = settingService.listDictionaries(contractTypeList);
        Map<Long, String> dicMap = dicTypeDescList.stream().collect(Collectors.toMap(DictionaryVo::getId,x -> x.getName()));

        for (SettleBillsInfoQryVO contractInfo : contractInfoQryList) {
            //合同类型描述
            contractInfo.setContractTypeDesc(dicMap.get(contractInfo.getContractType()==null?null:contractInfo.getContractType().longValue()));
            //单据类型描述
            contractInfo.setDocumentTypeDesc(dicMap.get(contractInfo.getDocumentType()==null?null:contractInfo.getDocumentType().longValue()));
            //业务性质描述
            contractInfo.setBusinessTypeDesc(dicMap.get(contractInfo.getBusinessType()==null?null:contractInfo.getBusinessType().longValue()));
            //编制方
            contractInfo.setCompileSideCompanyName(compileCompanyMap.get(contractInfo.getCompileSideId()));
            //创建人
            contractInfo.setCreaterName(userMap.get(contractInfo.getCreater()));

        }
        resultApiPageResult.setList(contractInfoQryList);
        return resultApiPageResult;
    }

    /**
     * 根据单据ID查询合同
     * @param documentId
     * @return
     */
    @Override
    public SettleBillsInfoQryVO selectContractByDocumentId(String documentId) {
        log.info("查询合同的单据ID为" + documentId);
        List<SettleBills> contractList = selectContractListByDocumentId(documentId);
        if(CollUtil.isNotEmpty(contractList)){
            return selectContractDetail(contractList.get(0).getId());
        }
        return null;
    }
    protected List<SettleBills> selectContractListByDocumentId(String documentId) {
        LambdaQueryWrapper<SettleBills> wrapper = new LambdaQueryWrapper<SettleBills>()
                .select(SettleBills::getId,SettleBills::getCarrierCompanyId)
//                .eq(SettleBills::getDocumentType, ContractConstant.ENERGY_BILL)
                .eq(SettleBills::getDocumentId, documentId)
                .eq(SettleBills::getIsDelete,DicConstant.IS_DELETE.NO)
                .orderByDesc(SettleBills::getVer);
       return settleBillsMapper.selectList(wrapper);
    }
    /**
     * 校验合同入参
     * @return
     */
    private boolean checkContractCreParam(CommonCreUpdReq contractCreUpdReq) {
        if (contractCreUpdReq == null) {
            log.info("合同创建入参不能为空");
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
        if (null == contractCreUpdReq.getCreater()) {
            log.info("创建人不能为空");
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
    private void assemblyContractCreBaseInfo(CommonCreUpdReq contractCreUpdReq,String currentCompanyId,SettleBills contract) {
        UserInfoVo user = userService.getUserById(contractCreUpdReq.getCreater());
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
        contract.setDocumentType(contractCreUpdReq.getContractDocumentType());
        //合同类型
        contract.setContractType(DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT);
        //单据合同签署时间即交易时间
        contract.setContractDate(contractCreUpdReq.getTradeTime());
        //交易时间
        contract.setTradeTime(contractCreUpdReq.getTradeTime());
        //编制方公司ID
        contract.setCompileSideId(createrCompanyId);
        //需方
        contract.setTrustorCompanyId(contractCreUpdReq.getPlatCompanyId());
        //供方
        contract.setCarrierCompanyId(contractCreUpdReq.getSupplierCompanyId());
        //父合同的父合同ID默认0
        contract.setParentContractId("0");
        contract.setIsDelete(DicConstant.IS_DELETE.NO);
        //获取匹配的合同模板信息
        ContractTemplate contractTemplate = contractTemplateService.selectContractTempateByCompany(contractCreUpdReq.getContractDocumentType(),
                createrCompanyId, businessType);
        if (contractTemplate != null) {
            contract.setContractTemplateId(contractTemplate.getId());//合同模板ID
            contract.setContractTemplateName(contractTemplate.getTemplateName());//合同模板名称
            contract.setTitle(contractTemplate.getTitle());//合同主题
            contract.setContent(contractTemplate.getContent());//合同内容
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
    public void createLocalDeatilPdf(SettleBillsInfoQryVO cont,BalanceDetailRes balanceBaseInfo, SysUser sessionInfo, Integer businessType) {
        try {
            if (cont == null) {
                log.error("生成本地合同PDF的合同不存在");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("balanceBaseInfo", balanceBaseInfo.getBalanceBaseInfo());
            map.put("oilProductInfos", balanceBaseInfo.getOilProductInfos());
            map.put("firstKeyWord", cont.getTrustorCompany().getName());
//            map.put("secondKeyWord", carrierCode);
            map.put("id", cont.getId());
            log.info("firstKeyWord是：{}", cont.getTrustorCompany().getName());
            if (DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(businessType)) {
                map.put("thirdKeyWord", cont.getPlatCompanyId());
                log.info("三方合同，thirdKeyWord是：{}", cont.getPlatCompanyId());
            }
            log.info("createLocalDeatilPdf: cont={}",JSON.toJSON(cont));
            String htmlTempl = "";
            if (cont.getDocumentType().equals(ContractConstant.ENERGY_BILL_MONTH)){ // 尊俊月账单模板模板
                htmlTempl = InvoicesPdfHelper.changeFtlToHtml(map,InvoicesPdfHelper.ZJ_INVOICES_TEMPL);
            }else { // 西拓尊俊能源单模板
                htmlTempl = InvoicesPdfHelper.changeFtlToHtml(map,InvoicesPdfHelper.INVOICES_TEMPL);
            }
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
            saveLocalPdfToContract(fileInfo.getFileID(), cont.getId());
        } catch (BusinessException e) {
            ContractResult result = new ContractResult();
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            log.error("本地pdf生成失败,原因：" + e.getMessage());
            //错误信息落库
            saveErrorInfoToContract(result, cont.getId());
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
            throw new BusinessException(Constant.ERROR_CODE,"");
        }
        //获取关键字所在坐标
        float[] coordinates = PdfHelper.getKeyWordsByPath(localPdfUrl,keyword);
        if (coordinates !=null&& coordinates.length == 0) {
            log.error(keyword+"关键字的坐标获取失败!");
            throw new BusinessException(Constant.ERROR_CODE,"");
        }
        ESignAccountDTO eSignAccount = new ESignAccountDTO();
        eSignAccount.setAutoExecute(true);//静默签为true
        eSignAccount.setSignerAccountId(accoutId);
        eSignAccount.setPosX(coordinates[0]);
        eSignAccount.setPosY(coordinates[1]);
        eSignAccount.setPosPage(String.valueOf((int) coordinates[2]));
        return eSignAccount;
    }

    @Override
    public ContractResVO checkSignStatus(String documentId) {
        List<SettleBills> batchCon = selectContractListByDocumentId(documentId);
        if (CollUtil.isEmpty(batchCon)){
            throw new BusinessException(Constant.ERROR_CODE, "结算单不存在,或已撤销");
        }
        List<String> openIds = new ArrayList<>();
        SysUser sessionInfo = new SysUser();
        sessionInfo.setName(batchCon.get(0).getCreater());
        sessionInfo.setCompanyName(batchCon.get(0).getCarrierCompanyName());
        sessionInfo.setCompanyId(batchCon.get(0).getCarrierCompanyId());
        sessionInfo.setPlatformCompanyId(batchCon.get(0).getPlatCompanyId());
        sessionInfo.setId(batchCon.get(0).getCreater());
        LoginUserContextHolder.setUser(sessionInfo);
        CompanyVo companyBO = companyService.findCompanyById(batchCon.get(0).getCarrierCompanyId());
        UserInfoVo user = userService.getUserById(companyBO.getCompanyAdmin());
        openIds.add(user.getIdcardNo());
        openIds.add(companyBO.getUnifiedSocialCreditIdentifier());
        Map<String, FddElectricSealResp> customerIdMap = chargeService.getOpenIdMap(openIds);

        String userCustomerNoAutoId = chargeService.getCustomerId(user.getIdcardNo(),0,customerIdMap);
        String companyCustomerNoAutoId = chargeService.getCustomerId(companyBO.getUnifiedSocialCreditIdentifier(),0,customerIdMap);
        //如果企业和个人都有法大大认证信息，则先查询个人 个人结果为false 再查询企业
        boolean isPerson = true;
        String firstCustomerId = userCustomerNoAutoId;
        if(StringUtils.isBlank(userCustomerNoAutoId)){
            isPerson = false;
            firstCustomerId = companyCustomerNoAutoId;
        }
        boolean equals = false;
        QuerySignStatusRes response = getFddSignStatus(batchCon.get(0).getId(), firstCustomerId);
        equals = checkStatus(response);
        if(!equals&&isPerson){
            //如果企业和个人都有法大大认证信息，则先查询个人 个人结果为false 再查询企业
            log.info("根据当前账号查询法大大签章信息-个人结果为false，开始查询企业");
            response = getFddSignStatus(batchCon.get(0).getId(), companyCustomerNoAutoId);
            equals = checkStatus(response);
        }
        String customerNoAutoId = chargeService.getCustomerId(user.getIdcardNo(),0,customerIdMap);
        if (StringUtils.isBlank(customerNoAutoId)){
            customerNoAutoId=chargeService.getCustomerId(companyBO.getUnifiedSocialCreditIdentifier(),0,customerIdMap);
        }
        ContractResVO contractResVO = new ContractResVO();
        if (equals) {
            String downloadUrl = response.getDownload_url();
            FileInfoOut fileInfo = fileHelper.urlUploadFile(downloadUrl, nacosValueConfig.getAgentCode(), DicConstant.DOCUMENT_TYPE.ENERGY_BILL + ".pdf");
            if (fileInfo == null) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            updateFddPdfToContract(fileInfo.getFileID(), batchCon.get(0).getId());
            contractResVO.setPdfUrl(fileInfo.getFileUrl());
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
    @Override
    public String createGDLocalPdf(BalanceDetailRes balanceBaseInfo) {
            SysUser sessionInfo = LoginUserContextHolder.getUser();
            Map<String, Object> map = new HashMap<>();
            map.put("balanceBaseInfo", balanceBaseInfo.getBalanceBaseInfo());
            map.put("oilProductInfos", balanceBaseInfo.getOilProductInfos());
            String htmlTempl = GDInvoicesPdfHelper.changeFtlToHtml(map);
            if (StrUtil.isBlank(htmlTempl)) {
                log.info("LOCAL HTML生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            }
            log.info("====> 合同生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf前，balanceBaseInfo: {}, htmlTempl:  <====",JSON.toJSONString(balanceBaseInfo));
            FileInfoOut fileInfo = fileHelper.htmlToPdf(htmlTempl, sessionInfo.getAgentCode());
            if (fileInfo == null) {
                log.info("LOCAL PDF生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120002.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            }
            log.info("====> 合同生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf后，fileInfo: {} <====", JSON.toJSONString(fileInfo));
            return fileInfo.getFileUrl();
    }
}
