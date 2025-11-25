package com.xtm.contract.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ServerCode;

import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.query.eqbDto.EAccountInfoDTO;
import com.xtm.contract.model.query.eqbDto.ESealTemplateDTO;
import com.xtm.contract.model.query.eqbDto.ESignAccountDTO;
import com.xtm.contract.model.query.eqbDto.ESignDocumentDTO;
import com.xtm.contract.model.query.eqbDto.EUploadFileDTO;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.query.eqbReq.EFileInfoReq;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.model.vo.contract.FrameContractDtlQryVO;
import com.xtm.contract.model.vo.eqb.ContractSignInfo;
import com.xtm.contract.model.vo.eqb.EAccountInfoRes;
import com.xtm.contract.model.vo.eqb.EDocsInfoRes;
import com.xtm.contract.model.vo.eqb.EStampTemplateRes;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.service.ContractEqbSignService;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.ContractTemplateService;
import com.xtm.contract.service.FrameworkContractService;
import com.xtm.contract.utils.DataCheckUtils;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.user.model.dto.UserInfoDto;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/4 11:28
 * @desc
 */
@Slf4j
@Service
public class ContractEqbSignServiceImpl implements ContractEqbSignService {
    @Autowired
    private EqbHelper eqbHelper;
    @Autowired
    private TmsUserService userService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private FrameworkContractService fraContractService;
    @Autowired
    private ContractTemplateService contractTemplateService;

    @Override
    public String createAccount(EqbConfigInfo configInfo, CompanyBO companyInfo) throws BusinessException {
        Long orgType = companyInfo.getOrganizationType();
        //组织类型
        if (DicConstant.ORGANIZATION_TYPE.LEGAL_PERSON.equals(orgType==null?null:orgType.intValue())) {
            log.info("-------------创建EQB企业账户---------------");
            //创建公司账户
            String accountId = createComapnyAccount(configInfo,companyInfo);
            return accountId;
        }
        log.info("------------创建EQB个人账户-------------");
        UserInfoVo userInfo = userService.getUserById(companyInfo.getCompanyAdmin());
        //创建个人账户
        String accountId = createPersonAccount(configInfo,userInfo);
        return accountId;
    }

    @Override
    public EFileInfoReq uploadPdfToYQB(EqbConfigInfo configInfo, EContractEcSignReq ecSignReq){
        EFileInfoReq eFileInfo = new EFileInfoReq();

        String localPdf = ecSignReq.getLocalPdfUrl();//文件路径
        String id = ecSignReq.getContractId();
        if (StrUtil.isBlank(localPdf)) {
            if(DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.equals(ecSignReq.getContractType())) {
                //重新生成明细合同PDF
                ContractInfoQryVO contractInfo = contractService.selectContractDetail(id);
                ContractTemplate contractTemplate = contractTemplateService.getById(contractInfo.getContractTemplateId());
                contractService.createLocalDeatilPdf(contractInfo, LoginUserContextHolder.getUser(), contractTemplate.getBusinessType());
                localPdf = contractInfo.getEcContractPath();
            } else {
                //重新生成框架合同PDF
                FrameContractDtlQryVO contractInfo = fraContractService.getFrameContractDtlById(id);
                fraContractService.createLocalFramePdf(contractInfo);
                localPdf = contractInfo.getEcContractPath();
            }
            log.info("合同签署---->本地pdf合同地址:"+localPdf);
            if (StrUtil.isBlank(localPdf)) {
                log.error("合同签署---->本地pdf合同不能为空！");
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_INSERTUPD.getCode(), ContractErrorCode.LOCAL_PDF_ERROR.getCode());
                throw new BusinessException(errorCode, ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            }
        }
        String pdfName = localPdf.substring(localPdf.lastIndexOf("/") + 1);//文件名称
        eFileInfo.setFileUrl(localPdf);
        eFileInfo.setFileName(pdfName);
        String fileId = ecSignReq.getEcContractEsignFileId();
        if (StrUtil.isBlank(fileId)) {
            //没有就请求上传
            EUploadFileDTO eUploadFile = new EUploadFileDTO();
            eUploadFile.setFileName(pdfName);
            eUploadFile.setFilePath(localPdf);
            fileId = eqbHelper.uploadFile(configInfo,eUploadFile);
            log.info("合同签署---->上传成功，请求签署的FILEID为："+fileId);
            eFileInfo.setFileId(fileId);
            //保存请求到的e签宝文件ID
            contractService.saveEQBFileIdToContract(fileId,ecSignReq.getContractId());
            return eFileInfo;
        }
        eFileInfo.setFileId(fileId);
        log.info("合同签署---->上传成功，请求签署的FILEID为："+fileId);
        return eFileInfo;
    }

    @Override
    public String bothInitiationSign(EqbConfigInfo configInfo, ContractSignInfo contractSignInfo) {
        String title = contractSignInfo.getTitle();
        List<ESignAccountDTO> eSignAccounts = new ArrayList<>();
        eSignAccounts.add(contractSignInfo.getTrustorEsignAccountInfo());
        eSignAccounts.add(contractSignInfo.getCarrierEsignAccountInfo());
        if (Objects.nonNull(contractSignInfo.getPlatEsignAccountInfo())
                && Objects.equals(contractSignInfo.getBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
            eSignAccounts.add(contractSignInfo.getPlatEsignAccountInfo());
        }

        ESignDocumentDTO documentParam = new ESignDocumentDTO();
        documentParam.setFileId(contractSignInfo.getEqbFileId());
        documentParam.setFileName(contractSignInfo.getFileName());
        documentParam.setBusinessScene(title);
        documentParam.setSignAccounts(eSignAccounts);
        String flowId = eqbHelper.createFlowStep(configInfo,documentParam);
        return flowId;
    }

    @Override
    public EAccountInfoDTO assemblyEnterpriseAccount(EqbConfigInfo configInfo, CompanyBO companyInfo) {
        UserInfoVo userInfo = new UserInfoVo();
        //校验字段
        checkcreateComapnyAccountParam(companyInfo,userInfo);
        //创建公司管理员
        String adminAccountID = createPersonAccount(configInfo,userInfo);
        //组装企业账户请求数据
        EAccountInfoDTO enInfoDTO = new EAccountInfoDTO();
        enInfoDTO.setThirdPartyUserId(companyInfo.getId());
        enInfoDTO.setCreator(adminAccountID);
        enInfoDTO.setName(companyInfo.getName());
        enInfoDTO.setIdNumber(companyInfo.getUnifiedSocialCreditIdentifier());
        enInfoDTO.setOrgLegalName(userInfo.getName());
        enInfoDTO.setOrgLegalIdNumber(userInfo.getIdcardNo());
        return enInfoDTO;
    }

    @Override
    public String createComapnyAccount(EqbConfigInfo configInfo, CompanyBO companyInfo) {
        if (companyInfo == null) {
            log.error("创建EQB企业账户的公司不存在！");
            return null;
        }
        String accountId = queryAccountId(configInfo,companyInfo.getId(),1,true,companyInfo.getCompanyAdmin() );
        if (StrUtil.isBlank(accountId)) {
            EAccountInfoDTO enInfoDTO = assemblyEnterpriseAccount(configInfo,companyInfo);
            //创建公司账户
            accountId = eqbHelper.createOrganizationsAccount(configInfo, enInfoDTO);
        }
        log.info("合同签署---->" + companyInfo.getName() + "创建E签宝账户成功，ID："+accountId);
        //设置静默授权
        EAccountInfoDTO eAccountInfo = new EAccountInfoDTO();
        eAccountInfo.setAccountId(accountId);
        eqbHelper.signAuth(configInfo,eAccountInfo);
        return accountId;
    }


    @Override
    public String createPersonAccount(EqbConfigInfo configInfo, UserInfoVo user) throws BusinessException {
        if (user == null) {
            log.error("创建EQB个人账户的用户不存在！");
            return null;
        }
        String accountId = queryAccountId(configInfo,user.getId(),0,false,user.getName());
        if (StrUtil.isBlank(accountId)) {
            //校验字段
            checkcreateUserAccountParam(user);
            EAccountInfoDTO userInfoDTO = new EAccountInfoDTO();
            userInfoDTO.setThirdPartyUserId(user.getId());
            userInfoDTO.setName(user.getName());
            userInfoDTO.setMobile(user.getMobile());
            userInfoDTO.setIdNumber(user.getIdcardNo());
            accountId = eqbHelper.createPersonAccount(configInfo, userInfoDTO);
        }
        log.info("合同签署---->" + user.getName()+"创建E签宝账户成功,ID"+accountId);
        //设置个人默认印章
        setSealDefaultSignature(configInfo,accountId,user);
        //设置静默授权
        EAccountInfoDTO eAccountInfo = new EAccountInfoDTO();
        eAccountInfo.setAccountId(accountId);
        eqbHelper.signAuth(configInfo,eAccountInfo);
        return accountId;
    }

    @Override
    public void setSealDefaultSignature(EqbConfigInfo configInfo, String accountId, UserInfoVo userInfo) throws BusinessException {
        ESealTemplateDTO paramIn = new ESealTemplateDTO();
        paramIn.setAccountId(accountId);
        EStampTemplateRes eStampTemplate = eqbHelper.createPersonalTemplate(configInfo,paramIn);
        if (eStampTemplate == null) {
            return;
        }
        if (StrUtil.isNotBlank(eStampTemplate.getSealId())) {
            ESealTemplateDTO templateParam = new ESealTemplateDTO();
            templateParam.setAccountId(accountId);
            templateParam.setSealId(eStampTemplate.getSealId());
            String message = eqbHelper.setDefaultPersonalStamp(configInfo,templateParam);
            if (!("成功").equals(message)) {
                // TODO: 2021/7/8 异常错误信息
                log.error("设置印章模板失败");
            }else{
                UserInfoDto updUser = new UserInfoDto();
                updUser.setId(userInfo.getId());
                updUser.setPersonalSealTemplateId(eStampTemplate.getSealId());
                userService.updatePersonalSealTemplateIdById(updUser);
            }
        }
    }

    /**
     * 校验创建E签宝企业信息
     * @param companyInfo
     * @throws BusinessException
     */
    private void checkcreateComapnyAccountParam(CompanyBO companyInfo,UserInfoVo userInfo) throws BusinessException {
        String companyName = companyInfo.getName();
        if (StrUtil.isBlank(companyName) || StrUtil.isBlank(companyInfo.getId())) {
            log.info("公司名称不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage());
        }

        String idCard = companyInfo.getIdCardNo();//身份证
        String mobile = companyInfo.getLegalRepresentativePhone();//法人手机号
        String name = companyInfo.getLegalRepresentative();//法人姓名

        if (StrUtil.isBlank(name)) {
            log.info(companyName+"的法人/管理员不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            String msg = companyName+ContractErrorCode.ADMIN_NOT_NULL.getMessage();
            throw new BusinessException(code,msg);
        }

        if (StrUtil.isBlank(idCard)) {
            log.info(companyName+"的法人/管理员身份证不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.ADMIN_IDCARD_NOT_NULL.getCode());
            String msg = companyName+ContractErrorCode.ADMIN_IDCARD_NOT_NULL.getMessage();
            throw new BusinessException(code,msg);
        }

        boolean checkIdCard = DataCheckUtils.checkIdCard(idCard);
        if (!checkIdCard) {
            log.info(companyInfo.getName()+"的法人/管理员身份证格式不正确！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.ADMIN_IDCARD_FORMAT_ERROR.getCode());
            String msg = companyName+ContractErrorCode.ADMIN_IDCARD_FORMAT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }
        //如果包含小写x，替换为大写X
        if(idCard.contains("x")){
            idCard = idCard.replace("x","X");
        }

        String unsId = companyInfo.getUnifiedSocialCreditIdentifier();
        //校验社会信用码以及格式
        if (StrUtil.isBlank(unsId)) {
            log.info(companyInfo.getName() + "的统一社会信用码不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.SOCIALCREDITCODE_NOT_NULL.getCode());
            String msg = companyName+ContractErrorCode.SOCIALCREDITCODE_NOT_NULL.getMessage();
            throw new BusinessException(code,msg);
        }
        Boolean isFklag = DataCheckUtils.checkUnifiedSocialCreditIdentifier(unsId);
        if (!isFklag) {
            log.info(companyInfo.getName() + "的统一社会信用码格式不正确！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.SOCIALCREDITCODE_FORMAT_ERROR.getCode());
            String msg = companyName+ContractErrorCode.SOCIALCREDITCODE_FORMAT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }

        //法人信息赋值
        userInfo.setId(companyInfo.getId() + idCard.substring(idCard.length()-3));//法人标识公司ID+身份证后三位
        userInfo.setIdcardNo(idCard);
        userInfo.setMobile(mobile);
        userInfo.setName(name);
    }

    /**
     * 校验创建E签宝个人信息
     * @param userInfo
     * @throws BusinessException
     */
    private void checkcreateUserAccountParam(UserInfoVo userInfo) throws BusinessException {
        String name = userInfo.getName();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(userInfo.getId())) {
            log.info("公司名称不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage());
        }

        String idCard = userInfo.getIdcardNo();
        if (StrUtil.isBlank(idCard)) {
            log.info(name+"的身份证不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.DRIVER_IDCARD_NOT_NULL.getCode());
            String msg = name+ContractErrorCode.DRIVER_IDCARD_NOT_NULL.getMessage();
            throw new BusinessException(code,msg);
        }

        boolean checkIdCard = DataCheckUtils.checkIdCard(idCard);
        if (!checkIdCard) {
            log.info(name+"的身份证格式不正确！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.DRIVER_IDCARD_FORMAT_ERROR.getCode());
            String msg = name+ContractErrorCode.DRIVER_IDCARD_FORMAT_ERROR.getMessage();
            throw new BusinessException(code,msg);
        }

        //如果包含小写x，替换为大写X
        if(idCard.contains("x")){
            idCard = idCard.replace("x","X");
            userInfo.setIdcardNo(idCard);
        }

        if (StrUtil.isBlank(userInfo.getMobile())) {
            log.info(userInfo.getName()+"的手机号不能为空！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.PHONE_NOT_NULL.getCode());
            String msg = name+ContractErrorCode.PHONE_NOT_NULL.getMessage();
            throw new BusinessException(code,msg);
        }

    }

    @Override
    public String queryAccountId(EqbConfigInfo configInfo,String xnBusinessId,int type,boolean orgFlag,String name) throws BusinessException {
        log.info("----------------查询双方所在E签宝账户信息------------------");
        EAccountInfoDTO paramIn = new EAccountInfoDTO();
        paramIn.setThirdPartyUserId(xnBusinessId);
        paramIn.setType(type);
        //查询机构有没有创建过
        EAccountInfoRes accountInfo = eqbHelper.queryAccountOrOrganizations(configInfo,paramIn);
        if (accountInfo == null) {
            return  null;
        }
        if(type == 0 && !name.equals(accountInfo.getName())){
            paramIn.setName(name);
            eqbHelper.updatePersonAccount(configInfo,paramIn);
        }
        log.info("获取到的E签宝账户信息："+JSON.toJSONString(accountInfo));
        if (orgFlag) {
            return accountInfo.getOrgId();
        } else {
            return accountInfo.getAccountId();
        }
    }

    @Override
    public String getDownloadDocumentUrl(EqbConfigInfo eqbConfigInfo, String flowId) throws BusinessException {
        EUploadFileDTO paramIn = new EUploadFileDTO();
        paramIn.setFlowId(flowId);
        EDocsInfoRes docsInfoRes = eqbHelper.flowsDocumentsDownload(eqbConfigInfo,paramIn);
        if (docsInfoRes == null) {
            return null;
        }
        return docsInfoRes.getFileUrl();
    }
}
