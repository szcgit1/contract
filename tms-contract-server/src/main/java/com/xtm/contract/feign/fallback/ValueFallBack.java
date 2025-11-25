package com.xtm.contract.feign.fallback;

import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.ValueFeiginClient;
import com.xtm.contract.model.query.contractOther.CompanyVasParam;
import com.xtm.contract.model.query.contractOther.ConsumeAccountParam;
import com.xtm.contract.model.query.eqbDto.EAccountInfoDTO;
import com.xtm.contract.model.query.eqbDto.EEnterpriseAuthenticationDTO;
import com.xtm.contract.model.query.eqbDto.ESealTemplateDTO;
import com.xtm.contract.model.query.eqbDto.ESignDocumentDTO;
import com.xtm.contract.model.query.eqbDto.EUploadFileDTO;
import com.xtm.contract.model.vo.eqb.EAccountInfoRes;
import com.xtm.contract.model.vo.eqb.EAuthenticationUrlRes;
import com.xtm.contract.model.vo.eqb.EDocsInfoRes;
import com.xtm.contract.model.vo.eqb.EStampTemplateRes;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.model.vo.fdd.FddConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/2 21:35
 * @desc
 */
@Component
@Slf4j
public class ValueFallBack implements ValueFeiginClient {

    @Override
    public Result<EqbConfigInfo> getConfig(String businessType) {
        log.error("调用 base-subscribe 服务查询配置接口失败 参数："+ businessType);
        return null;
    }

    @Override
    public Result<FddConfigInfo> getFDDConfig(String businessType) {
        return null;
    }

    @Override
    public Result<String> createAccount(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> updateAccount(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> deleteAccount(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> createOrganizationsAccount(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<EAccountInfoRes> queryAccountOrOrganizations(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> signAuth(String config, EAccountInfoDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> cancelAuth(String config, EAccountInfoDTO paramIn) {
        return null;
    }


    @Override
    public Result<String> uploadFile(String config, EUploadFileDTO paramIn) {
        return null;
    }

    @Override
    public Result<String> createFlowStep(String config, ESignDocumentDTO paramIn) throws BusinessException {
        return null;
    }

    @Override
    public Result<EDocsInfoRes> flowsDocumentsDownload(String config, EUploadFileDTO paramIn) throws BusinessException {
        return null;
    }

    @Override
    public Result<EStampTemplateRes> createPersonalTemplate(String config, ESealTemplateDTO paramIn) throws BusinessException {
        return null;
    }

    @Override
    public Result<String> setDefaultPersonalStamp(String config, ESealTemplateDTO paramIn) throws BusinessException {
        return null;
    }

    @Override
    public Result  vasAuth(CompanyVasParam companyVasParam) {
        return null;
    }

    @Override
    public Result<Boolean> consumeAccountPay(ConsumeAccountParam companyVasParam) {
        return null;
    }

    @Override
    public Result<EAuthenticationUrlRes> getEnterpriseAuthenticationAddress(String config, EEnterpriseAuthenticationDTO paramIn) {
        return null;
    }
}
