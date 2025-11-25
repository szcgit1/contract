package com.xtm.contract.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.feign.ValueFeiginClient;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.query.contractOther.CompanyVasParam;
import com.xtm.contract.model.query.contractOther.ConsumeAccountParam;
import com.xtm.contract.model.query.eqbDto.EAccountInfoDTO;
import com.xtm.contract.model.query.eqbDto.EEnterpriseAuthenticationDTO;
import com.xtm.contract.model.query.eqbDto.ESealTemplateDTO;
import com.xtm.contract.model.query.eqbDto.ESignDocumentDTO;
import com.xtm.contract.model.query.eqbDto.EUploadFileDTO;
import com.xtm.contract.model.query.listener.EqbConsumeAccountReq;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.eqb.EAccountInfoRes;
import com.xtm.contract.model.vo.eqb.EAuthenticationUrlRes;
import com.xtm.contract.model.vo.eqb.EDocsInfoRes;
import com.xtm.contract.model.vo.eqb.EStampTemplateRes;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.model.vo.fdd.FddConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/3 15:28
 * @desc
 */
@Slf4j
@Component
public class EqbHelper {
    @Autowired
    private ValueFeiginClient valueFeiginClient;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询E签宝配置
     * @return
     */
    public EqbConfigInfo getEqbConfigInfo() {
        Result<EqbConfigInfo> response = valueFeiginClient.getConfig("1");
        log.info("查询E签宝配置信息"+JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        } else if (ContractResult.XIAONIU_CONTRACT_BACK_110001.getCode() == response.getCode()) {
            log.info(ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
            return null;
        } else {
            if (StrUtil.isNotBlank(response.getMessage())) {
               Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            String message = ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage();
            throw new BusinessException(code,message);
        }
    }

    public FddConfigInfo getFDDConfigInfo() {
        Result<FddConfigInfo> response = valueFeiginClient.getFDDConfig("1");
        log.info("查询E签宝配置信息"+JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        } else if (ContractResult.XIAONIU_CONTRACT_BACK_110001.getCode() == response.getCode()) {
            log.error(ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
            return null;
        } else {
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            String message = ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage();
            throw new BusinessException(code,message);
        }
    }

    public CompanyVasInfo vasAuth() {
        CompanyVasParam companyVasParam = new CompanyVasParam();
        companyVasParam.setVasCode(DicConstant.VAS_CODE.EQB_NUMBER_SIGN);
        companyVasParam.setUsePlatAccountStatus(true);
        try{
            Result<CompanyVasInfo> response = valueFeiginClient.vasAuth(companyVasParam);
            log.info("增值服务授权信息：" + JSON.toJSONString(response));
            if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
                return response.getData();
            }else{
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage();
                throw new BusinessException(code,message);
            }
        }catch (Exception exception){
            log.error("增值服务异常",exception);
            throw new BusinessException(-1,"增值服务异常");
        }
    }


    public ContractResult checkVasInfo(CompanyVasInfo companyVasInfo){
        if (companyVasInfo == null) {
            return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_110001.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
        }
        if (companyVasInfo.getBalance() == null) {
            log.info("余额不足");
            return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_140007.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_140007.getMsg());
        }
        if (BigDecimal.ZERO.compareTo(companyVasInfo.getBalance()) != -1 || companyVasInfo.getPerPrice().compareTo(companyVasInfo.getBalance()) != -1) {
            log.info("余额不足");
            return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_140007.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_140007.getMsg());
        }
        if (BigDecimal.ZERO.compareTo(companyVasInfo.getPerPrice()) != -1) {
            log.info("未设置EQB消费单价");
            return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_140007.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_140007.getMsg());
        }
        if (!companyVasInfo.getAuthorizedStatus() || !companyVasInfo.getEnabledStatus()) {
            log.info("E签宝未授权或未启用");
            return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_110001.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
        }
        return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_0.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_0.getMsg());
    }
    /**
     * 增值业务扣款
     * @param accountReq
     * @return
     */
    public Boolean consumeAccountPay(EqbConsumeAccountReq accountReq) throws BusinessException {
        ConsumeAccountParam paramIn = new ConsumeAccountParam();
        paramIn.setVasCode(accountReq.getVasCode());
        paramIn.setDocumentId(accountReq.getContractId());

        Result  response = valueFeiginClient.consumeAccountPay(paramIn);
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            if(Boolean.TRUE.equals(response.getData())){
                log.error("消费流水扣除成功："+response.getMessage());
                return true;
            }else{
                log.error("消费流水扣除失败："+response.getMessage());
                return false;
            }
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                log.error("消费流水扣除失败："+response.getMessage());
            }
            return false;
        }
    }

    /**
     * 扣除流水
     * @param vasInfo
     */
    public void deductionCapitalFlow(CompanyVasInfo vasInfo,String contractId){
        if(vasInfo == null){
            log.error("E签宝增值业务在增值服务配置中找不到！！！");
        }
        EqbConsumeAccountReq consumeAccountReq = new EqbConsumeAccountReq("deductionEqbFlow",contractId);
        if (vasInfo != null) {
            consumeAccountReq.setVasCode(Integer.valueOf(vasInfo.getVasCode()));
            consumeAccountReq.setVasId(vasInfo.getVasId());
        }
        applicationContext.publishEvent(consumeAccountReq);
    }


    /**
     * 创建个人账户
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String createPersonAccount(EqbConfigInfo configInfo, EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("创建个人账户入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.createAccount(config,paramIn);
        log.info("创建个人账户出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 修改个人账户
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String updatePersonAccount(EqbConfigInfo configInfo,EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("修改个人账户入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.updateAccount(config,paramIn);
        log.info("修改个人账户出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 注销个人账户
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String deletePersonAccount(EqbConfigInfo configInfo,EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("注销个人账户入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.deleteAccount(config,paramIn);
        log.info("注销个人账户出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }


    /**
     *创建机构账户账户
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String createOrganizationsAccount(EqbConfigInfo configInfo,EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("创建机构账户账户入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.createOrganizationsAccount(config,paramIn);
        log.info("创建机构账户出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 查询个人或机构账户
     * @param configInfo
     * @param paramIn
     * @return
     */
    public EAccountInfoRes queryAccountOrOrganizations(EqbConfigInfo configInfo, EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("查询个人或机构账户入参：{}",JSON.toJSONString(paramIn));
        Result<EAccountInfoRes> response = valueFeiginClient.queryAccountOrOrganizations(config,paramIn);
        log.info("查询个人或机构账户出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (53000001 == response.getCode()) {
                return null;
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            if (StrUtil.isNotBlank(response.getMessage())) {
                throw new BusinessException(code,response.getMessage());
            }
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 设置静默签署
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String signAuth(EqbConfigInfo configInfo,EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("设置静默签署入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.signAuth(config,paramIn);
        log.info("设置静默签署出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            log.info("合同签署---->设置静默签署成功，ID："+paramIn.getAccountId());
            return response.getData();
        }else{
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            if (StrUtil.isNotBlank(response.getMessage())) {
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            log.error("设置静默签署失败！");
            throw new BusinessException(code,"签署失败！");
        }
    }

    /**
     * 解除静默签署
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String cancelAuth(EqbConfigInfo configInfo,EAccountInfoDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        Result<String> response = valueFeiginClient.cancelAuth(config,paramIn);
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     *  上传文件
     * @param configInfo
     * @param paramIn
     * @return
     */
    public String uploadFile(EqbConfigInfo configInfo, EUploadFileDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("EQB上传文件入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.uploadFile(config,paramIn);
        log.info("EQB上传文件出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 一步发起签署
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    public String createFlowStep(EqbConfigInfo configInfo, ESignDocumentDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("一步发起签署入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.createFlowStep(config,paramIn);
        log.info("一步发起签署出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else if(1435002 == response.getCode()){
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.ADMIN_IDCARD_FORMAT_ERROR.getCode());
            String message = response.getMessage();
            throw new BusinessException(code,message);
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());

            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 流程文档下载
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    public EDocsInfoRes flowsDocumentsDownload(EqbConfigInfo configInfo,EUploadFileDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("流程文档下载入参：{}",JSON.toJSONString(paramIn));
        Result<EDocsInfoRes> response = valueFeiginClient.flowsDocumentsDownload(config,paramIn);
        log.info("流程文档下载出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else if(1437135 == response.getCode()) {
            //未归档状态
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.PROCESS_NOT_ARCHIVED.getCode());
            String message = ContractErrorCode.PROCESS_NOT_ARCHIVED.getMessage();
            throw new BusinessException(code,message);
        }else{
            if (StrUtil.isNotBlank(response.getMessage())) {
                Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 创建个人模板印章
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    public EStampTemplateRes createPersonalTemplate(EqbConfigInfo configInfo, ESealTemplateDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("创建个人模板印章入参：{}",JSON.toJSONString(paramIn));
        Result<EStampTemplateRes> response = valueFeiginClient.createPersonalTemplate(config,paramIn);
        log.info("创建个人模板印章出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            if (StrUtil.isNotBlank(response.getMessage())) {
                throw new BusinessException(code,"签署失败");
            }
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 设置个人默认印章
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    public String setDefaultPersonalStamp(EqbConfigInfo configInfo,ESealTemplateDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("设置个人默认印章入参：{}",JSON.toJSONString(paramIn));
        Result<String> response = valueFeiginClient.setDefaultPersonalStamp(config,paramIn);
        log.info("设置个人默认印章出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return ContractResult.XIAONIU_CONTRACT_BACK_0.getMsg();
        }else{
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            if (StrUtil.isNotBlank(response.getMessage())) {
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            throw new BusinessException(code,"签署失败");
        }
    }

    /**
     * 企业实名认证
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    public EAuthenticationUrlRes getEnterpriseAuthenticationAddress(EqbConfigInfo configInfo, EEnterpriseAuthenticationDTO paramIn) throws BusinessException {
        String config = JSON.toJSONString(configInfo);
        paramIn.setBusinessType("1");
        log.info("设置企业实名认证入参：{}",JSON.toJSONString(paramIn));
        Result<EAuthenticationUrlRes> response = valueFeiginClient.getEnterpriseAuthenticationAddress(config,paramIn);
        log.info("设置企业实名认证出参：{}",JSON.toJSONString(response));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() == response.getCode()) {
            return response.getData();
        }else{
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            if (StrUtil.isNotBlank(response.getMessage())) {
                String message = response.getMessage();
                throw new BusinessException(code,message);
            }
            throw new BusinessException(code,"签署失败");
        }
    }
}
