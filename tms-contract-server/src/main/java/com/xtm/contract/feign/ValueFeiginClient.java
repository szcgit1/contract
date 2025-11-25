package com.xtm.contract.feign;

import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.model.query.contractOther.CompanyVasParam;
import com.xtm.contract.model.query.contractOther.ConsumeAccountParam;
import com.xtm.contract.model.query.eqbDto.EAccountInfoDTO;
import com.xtm.contract.model.query.eqbDto.EEnterpriseAuthenticationDTO;
import com.xtm.contract.model.query.eqbDto.ESealTemplateDTO;
import com.xtm.contract.model.query.eqbDto.ESignDocumentDTO;
import com.xtm.contract.model.query.eqbDto.EUploadFileDTO;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.eqb.EAccountInfoRes;
import com.xtm.contract.model.vo.eqb.EAuthenticationUrlRes;
import com.xtm.contract.model.vo.eqb.EDocsInfoRes;
import com.xtm.contract.model.vo.eqb.EStampTemplateRes;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.model.vo.fdd.FddConfigInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @Author: fengyj
* @Description: 增值服务feign接口
* @Date: Create in 14:26 2022/12/17
*/
@FeignClient(value = "base-subscribe")
public interface ValueFeiginClient {
    /**
     * 获取配置信息
     * @param businessType
     * @return
     */
    @GetMapping("/apiPlat/subscribe/eqb/contract/getConfig")
    Result<EqbConfigInfo> getConfig(@RequestParam("businessType") String businessType);

    @GetMapping("/apiPlat/subscribe/eqb/contract/FDDGetConfig")
    Result<FddConfigInfo> getFDDConfig(@RequestParam("businessType") String businessType);

    /**
     * 创建个人账户
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/createAccount")
    Result<String> createAccount(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     * 修改个人账户
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/updateAccount")
    Result<String> updateAccount(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     * 删除个人账户
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/deleteAccount")
    Result<String> deleteAccount(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     *创建机构账户账户
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/createOrganizationsAccount")
    public Result<String> createOrganizationsAccount(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     * 查询个人或机构账户
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/queryAccountOrOrganizations")
    Result<EAccountInfoRes> queryAccountOrOrganizations(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     *设置静默签署
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/signAuth")
    public Result<String> signAuth(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     *解除静默签署
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/cancelAuth")
    public Result<String> cancelAuth(@RequestHeader("config") String config, @RequestBody EAccountInfoDTO paramIn);

    /**
     *  上传文件
     * @param config
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/uploadFile")
    public Result<String> uploadFile(@RequestHeader("config") String config, @RequestBody EUploadFileDTO paramIn);

    /**
     * 一步发起签署
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/createFlowStep")
    @ResponseBody
    public Result<String> createFlowStep(@RequestHeader("config") String config, @RequestBody ESignDocumentDTO paramIn) throws BusinessException;

    /**
     * 流程文档下载
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/flowsDocumentsDownload")
    @ResponseBody
    public Result<EDocsInfoRes> flowsDocumentsDownload(@RequestHeader("config") String config, @RequestBody EUploadFileDTO paramIn) throws BusinessException;

    /**
     * 创建个人模板印章
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/createPersonalTemplate")
    @ResponseBody
    public Result<EStampTemplateRes> createPersonalTemplate(@RequestHeader("config") String config, @RequestBody ESealTemplateDTO paramIn) throws BusinessException;

    /**
     * 设置个人默认印章
     * @param paramIn
     * @return
     * @throws BusinessException
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/setDefaultPersonalStamp")
    @ResponseBody
    public Result<String> setDefaultPersonalStamp(@RequestHeader("config") String config, @RequestBody ESealTemplateDTO paramIn) throws BusinessException;

    /**
     * 查询增值业务授权
     * @param companyVasParam
     * @return
     */
    @PostMapping("apiPlat/subscribe/companyVas/vasNiuAuth")
    Result<CompanyVasInfo> vasAuth(@RequestBody CompanyVasParam companyVasParam);

    /**
     * 消费账户扣款
     * @param companyVasParam
     * @return
     */
    @PostMapping("apiPlat/subscribe/consumeAccountStatement/saveNiuDistributionStatement")
    Result<Boolean> consumeAccountPay(@RequestBody ConsumeAccountParam companyVasParam);
    /**
     * 企业实名认证
     * @param paramIn
     * @return
     */
    @PostMapping("/apiPlat/subscribe/eqb/contract/getEnterpriseAuthenticationAddress")
    Result<EAuthenticationUrlRes> getEnterpriseAuthenticationAddress(@RequestHeader("config") String config, @RequestBody EEnterpriseAuthenticationDTO paramIn);
}
