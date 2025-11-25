package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.feign.TmsCompanyService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.enums.SignTypeEnum;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.contract.model.vo.fdd.FddConfigInfo;
import com.xtm.contract.service.ChargeService;
import com.xtm.contract.service.ContractFddSignService;
import com.xtm.contract.service.FddFeignService;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.FileHelper;
import com.xtm.contract.utils.InvoicesPdfHelper;
import com.xtm.thirdparty.auth.feign.ContractFddSignFeign;
import com.xtm.thirdparty.auth.model.req.SignIntegrationExtReq;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.thirdparty.auth.model.vo.ContractSignResVo;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @package: com.xiaoniu.contract.service.impl.ChargeService
 * @author: wwh
 * @create: 2025-03-28 16:07
 * @description:
 **/
@Service
@Slf4j
public class ChargeServiceImpl implements ChargeService {
    @Autowired
    private FileHelper fileHelper;
    @Autowired
    private NacosValueConfig nacosValueConfig;
    @Autowired
    private TmsCompanyService companyService;
    @Autowired
    private TmsUserService userService;
    @Autowired
    private ContractFddSignService contractFddSignService;
    @Autowired
    private EqbHelper eqbHelper;

    @Resource
    private ContractFddSignFeign contractFddSignFeign;

    @Resource
    private FddFeignService fddFeignService;


    @Override
    public Result<ContractSignResVo> serviceChargeSummaryEcSign(ContractSignVo<?> contractSignVo) throws Exception {
        log.info("通用电子签章 contractCreUpdParam={}",JSON.toJSONString(contractSignVo));
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        // 生成本地服务汇总单
        FileInfoOut fileInfoOut = this.createLocalChargeSummaryPdf(contractSignVo, sessionInfo);
        // 生成本地服务汇总单电子签章
        log.info("通用电子签章,fileInfoOut : {}",JSON.toJSONString(fileInfoOut));
//        FddConfigInfo fddConfigInfo = eqbHelper.getFDDConfigInfo();
//        if(fddConfigInfo == null){
//            throw new BusinessException(ResultCode.VALIDATOR.getCode(), ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
//        }
        ContractSignResVo contractSignResVo = this.createEcChargeSummaryPdf(contractSignVo, fileInfoOut.getFileUrl(),sessionInfo);
        contractSignResVo.setBusinessId(contractSignVo.getBusinessId());
        contractSignResVo.setBusinessCode(contractSignVo.getBusinessCode());
        contractSignResVo.setLocalPdfId(fileInfoOut.getFileID());
        contractSignResVo.setLocalPdfUrl(fileInfoOut.getFileUrl());
        // 查看pdf 合同结果
        FileInfoOut contract = updateFindCarChargeEcPdfId(contractSignVo.getBusinessId(), sessionInfo);
        contractSignResVo.setEcPdfUrl(contract.getFileUrl());
        contractSignResVo.setEcPdfId(contract.getFileID());
        return Result.of(contractSignResVo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @Override
    public Result<ContractSignResVo> energyChargeSummaryEcSign(ContractSignVo<?> contractSignVo) throws Exception {
        log.info("结算单电子签章 contractCreUpdParam={}",JSON.toJSONString(contractSignVo));
        //取session信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        // 生成本地服务汇总单
        FileInfoOut fileInfoOut = this.createLocalChargeSummaryPdf(contractSignVo, sessionInfo);
        // 生成本地服务汇总单电子签章
        log.info("结算单电子签章,fileInfoOut : {}",JSON.toJSONString(fileInfoOut));
        FddConfigInfo fddConfigInfo = eqbHelper.getFDDConfigInfo();
        if(fddConfigInfo == null){
            throw new BusinessException(ResultCode.VALIDATOR.getCode(), ContractResult.XIAONIU_CONTRACT_BACK_110001.getMsg());
        }
        ContractSignResVo contractSignResVo = this.createEnergyEcChargeSummaryPdf(contractSignVo, fileInfoOut.getFileUrl(),sessionInfo);
        contractSignResVo.setBusinessId(contractSignVo.getBusinessId());
        contractSignResVo.setBusinessCode(contractSignVo.getBusinessCode());
        contractSignResVo.setLocalPdfId(fileInfoOut.getFileID());
        contractSignResVo.setLocalPdfUrl(fileInfoOut.getFileUrl());
        return Result.of(contractSignResVo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    /**
     * 服务汇总单生成本地pdf
     */
    @Override
    public FileInfoOut createLocalChargeSummaryPdf(ContractSignVo<?> contractSignVo, SysUser sessionInfo) throws Exception {
        try {
            if (contractSignVo == null) {
                log.error("生成本地汇总单PDF的汇总单不存在");
                throw new BusinessException(ResultCode.VALIDATOR.getCode(), "电子签章不存在");
            }
            //
            Map<String, Object> map = new HashMap<>();
            map.put("contract", contractSignVo.getSignParam());
            map.put("firstKeyWord", contractSignVo.getFirstPartyId());
            map.put("secondKeyWord", contractSignVo.getSecondPartyId());
            map.put("id", contractSignVo.getBusinessId());
            map.put("businessCode", contractSignVo.getBusinessCode());
            map.put("serviceProject", contractSignVo.getServiceProject());
            log.info("firstKeyWord是：{}，secondKeyWord是：{}", contractSignVo.getFirstPartyId(), contractSignVo.getSecondPartyId());
            log.info("createLocalChargeSummaryPdf: cont={}", JSON.toJSON(contractSignVo));
            // 合同类型
            String fileName = SignTypeEnum.getFileName(contractSignVo.getSignType());
            if (StringUtils.isBlank(fileName)){
                log.info("LOCAL HTML生成失败，签署文件名不存在！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            }
            String htmlTemp = InvoicesPdfHelper.changeFtlToHtml(map,fileName);
            if (StrUtil.isBlank(htmlTemp)) {
                log.info("LOCAL HTML生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            }
            log.info("====> 合同签章生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf前，cont: {}, htmlTempl:  <====", contractSignVo);
            FileInfoOut fileInfo = fileHelper.htmlToPdf(htmlTemp, sessionInfo.getAgentCode());
            if (fileInfo == null) {
                log.info("LOCAL PDF生成失败！");
                throw new BusinessException(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120002.getCode()+""), ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            }
            log.info("====> 合同生成 - 创建本地PDF文件 - 调用fileHelper.htmlToPdf后，cont: {}, fileInfo:  <====", fileInfo);
            return fileInfo;
        } catch (BusinessException e) {
            ContractResult result = new ContractResult();
            result.setCode(e.getCode());
            result.setMsg(e.getMessage());
            log.error("生成本地pdf失败原因:", e);
            throw new BusinessException(ResultCode.FAIL.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("生成本地pdf失败原因:", e);
            throw e;
        }
    }

    @Override
    public ContractSignResVo createEcChargeSummaryPdf(ContractSignVo<?> contractSignVo, String localPdfUrl,SysUser sessionInfo) {
        log.info("====> 法大大签署电子签章入参: contractSignVo: {} <====", JSON.toJSONString(contractSignVo));

        String contractId = contractSignVo.getBusinessId();

            String cardNo = "";
            if(nacosValueConfig.getXtmTjCompanyId().equals(contractSignVo.getFirstPartyId())){
                cardNo = nacosValueConfig.getXtmTjCardNo();
            } else if (nacosValueConfig.getXtmGsCompanyId().equals(contractSignVo.getFirstPartyId())) {
                cardNo = nacosValueConfig.getXtmGsCardNo();
            }
            List<String> openIds = new ArrayList<>();
            openIds.add(cardNo);
            SignIntegrationExtReq signIntegrationReq = new SignIntegrationExtReq();
            signIntegrationReq.setSource(1);
            String secondCustomerId = "";
            Map<String, FddElectricSealResp> customerIdMap = null;
            if (contractSignVo.getSecondSignModel().equals(0)){ // tob
                CompanyBO secondPartyCompany = companyService.getCompanyById(contractSignVo.getSecondPartyId());
                openIds.add(secondPartyCompany.getUnifiedSocialCreditIdentifier());
                openIds.add(secondPartyCompany.getCompanyAdminInfo().getIdcardNo());
                customerIdMap = getOpenIdMap(openIds);
                if (contractSignVo.getSecondSignType().equals(1)){ // 手动签章
                    secondCustomerId = getCustomerId(secondPartyCompany.getUnifiedSocialCreditIdentifier(),0,customerIdMap);
                    if (StrUtil.isBlank(secondCustomerId)){
                        secondCustomerId = getCustomerId(secondPartyCompany.getCompanyAdminInfo().getIdcardNo(), 0,customerIdMap);
                    }
                    signIntegrationReq.setCarryMobile(secondPartyCompany.getCompanyAdminInfo().getMobile());
                }else {
                    secondCustomerId = getCustomerId(secondPartyCompany.getUnifiedSocialCreditIdentifier(),1,customerIdMap);
                    if (StrUtil.isBlank(secondCustomerId)){
                        secondCustomerId = getCustomerId(secondPartyCompany.getCompanyAdminInfo().getIdcardNo(), 1,customerIdMap);
                    }
                    // ContractConstant.signKeyword.SECOND_PARTY
                    signIntegrationReq.setCarrySignKeyword(contractSignVo.getSecondSignLocation());
                }
            }else { //toc
                UserInfoVo userBO = userService.getUserById(contractSignVo.getSecondPartyId());
                openIds.add(userBO.getIdcardNo());
                customerIdMap = getOpenIdMap(openIds);
                if (contractSignVo.getSecondSignType().equals(1)){ // 手动签章
                    secondCustomerId = getCustomerId(userBO.getIdcardNo(), 0,customerIdMap);
                    signIntegrationReq.setCarryMobile(userBO.getMobile());
                }else {
                    secondCustomerId = getCustomerId(userBO.getIdcardNo(), 1,customerIdMap);
                    // ContractConstant.signKeyword.SECOND_PARTY 自动签章才有这项
                    signIntegrationReq.setCarrySignKeyword(contractSignVo.getSecondSignLocation());
                }
            }
            String customerId = getCustomerId(cardNo, 1,customerIdMap);
            if (StringUtils.isBlank(secondCustomerId) || StringUtils.isBlank(customerId)){
                throw new BusinessException(ResultCode.FAIL.getCode(), "customerId无效，法大大未认证或认证已过期，请认证");
            }
            signIntegrationReq.setContractId(contractId);
            signIntegrationReq.setCarryCustomerId(secondCustomerId); // 乙方
            signIntegrationReq.setTrustorCustomerId(customerId); // 甲方
            signIntegrationReq.setDocTitle(contractSignVo.getServiceProject());
            signIntegrationReq.setPdfUrl(localPdfUrl);
            // ContractConstant.signKeyword.FIRST_PARTY 默认都是自动签章，都存在
            signIntegrationReq.setTrustorSignKeyword(contractSignVo.getFirstSignLocation());
            signIntegrationReq.setFirstSignType(contractSignVo.getFirstSignType());
            signIntegrationReq.setSecondSignType(contractSignVo.getSecondSignType());

            Result<ContractSignResVo> fddResult = contractFddSignFeign.signIntegration(signIntegrationReq);
            if (fddResult.isSuccess()) {
                return fddResult.getData();
            }
            throw new BusinessException(fddResult.getMsg());
    }

    @Override
    public Map<String, FddElectricSealResp> getOpenIdMap(List<String> openIds) {
        Map<String, FddElectricSealResp> customerIdMap = new HashMap<>();
        if(CollectionUtil.isEmpty(openIds)){
            return customerIdMap;
        }
        List<FddElectricSealResp> sealRespList = fddFeignService.getFddElectricSealByOpenIds(openIds, null);
        if (CollectionUtil.isEmpty(sealRespList)){
            return customerIdMap;
        }
        //verifyStatus = 1
        sealRespList = sealRespList.stream().filter(sealResp -> sealResp.getVerifyStatus() == 1).collect(Collectors.toList());
        sealRespList.forEach(sealResp -> customerIdMap.put(sealResp.getOpenId(), sealResp));
        return customerIdMap;
    }

    /**
     * @Param: [authAutoSignStatus 签章类型：0-手动签章，1-自动签章，openId 身份证号]
     * @return: java.lang.String
     * @Author: wwh
     * @Date: 2025/3/30 9:49
     * @Description: 获取法大大签章customerId
     */
    @Override
    public String getCustomerId(String openId,Integer authAutoSignStatus,Map<String, FddElectricSealResp> customerIdMap){
        log.info("getCustomerId: openId={},authAutoSignStatus={}", openId,authAutoSignStatus);
        if (StringUtils.isBlank(openId)) {
            log.info("服务费查询法大大认证信息，没有认证 openId={}", openId);
            return null;
        }
        FddElectricSealResp fddElectricSealResp = customerIdMap.get(openId);
        if (fddElectricSealResp == null) {
            log.info("服务费查询法大大认证信息，没有认证1 openId={}", openId);
            return null;
        }
        if (authAutoSignStatus != null && authAutoSignStatus == 1){
            if (fddElectricSealResp.getAuthAutoSignStatus() != 1){
                log.info("服务费查询法大大认证信息，没有认证2 openId={}", openId);
                return null;
            }
        }
        log.info("服务费查询的认证信息 openId={} CustomerId={}",openId,JSON.toJSONString(fddElectricSealResp));
        return fddElectricSealResp.getCustomerId();
    }

    @Override
    public ContractSignResVo createEnergyEcChargeSummaryPdf(ContractSignVo<?> contractSignVo, String localPdfUrl, SysUser sessionInfo) {
        log.info("====> 法大大签署结算单入参: contractInfo: {} <====", contractSignVo);
        String contractId = contractSignVo.getBusinessId();
        try {
            SignIntegrationExtReq signIntegrationReq = new SignIntegrationExtReq();
            signIntegrationReq.setSource(1);
            signIntegrationReq.setContractId(contractId);
            List<String> openIds = new ArrayList<>();
            openIds.add(nacosValueConfig.getTjzwCardNo());
            /** 结算合同动态获取法大大客户id 开始 **/
            String firstCustomerId = "";
            Map<String, FddElectricSealResp> customerIdMap = null;
            if (contractSignVo.getFirstSignModel().equals(0)){ // tob
                CompanyBO firstPartyCompany = companyService.getCompanyById(contractSignVo.getFirstPartyId());
                openIds.add(firstPartyCompany.getCompanyAdminInfo().getIdcardNo());
                openIds.add(firstPartyCompany.getUnifiedSocialCreditIdentifier());
                customerIdMap = getOpenIdMap(openIds);
                /** 签章根据供应商法大大授权自动签情况判断采用自动签章功能或手动签章 开始**/
                FddElectricSealResp fddElectricSealResp = customerIdMap.get(firstPartyCompany.getCompanyAdminInfo().getIdcardNo());
                if (fddElectricSealResp!=null){
                    if (fddElectricSealResp.getAuthAutoSignStatus().equals(1)){
                        contractSignVo.setFirstSignType(0); // 自动签章
                    }else {
                        contractSignVo.setFirstSignType(1); // 手动签章
                    }
                }
                /** 签章根据供应商法大大授权自动签情况判断采用自动签章功能或手动签章 结束 **/
                if (contractSignVo.getFirstSignType().equals(1)){ // 手动签章
                    firstCustomerId = getCustomerId(firstPartyCompany.getCompanyAdminInfo().getIdcardNo(), 0,customerIdMap);
                    if (StrUtil.isBlank(firstCustomerId)){
                        firstCustomerId = getCustomerId(firstPartyCompany.getUnifiedSocialCreditIdentifier(),0,customerIdMap);
                    }
                    signIntegrationReq.setTrustorMobile(firstPartyCompany.getCompanyAdminInfo().getMobile());
                }else {
                    firstCustomerId = getCustomerId(firstPartyCompany.getCompanyAdminInfo().getIdcardNo(), 1,customerIdMap);
                    if (StrUtil.isBlank(firstCustomerId)){
                        firstCustomerId = getCustomerId(firstPartyCompany.getUnifiedSocialCreditIdentifier(),1,customerIdMap);
                    }
                    signIntegrationReq.setTrustorSignKeyword(contractSignVo.getFirstSignLocation());
                }
            }else { //toc
                UserInfoVo userBO = userService.getUserById(contractSignVo.getFirstPartyId());
                openIds.add(userBO.getIdcardNo());
                customerIdMap = getOpenIdMap(openIds);
                /** 签章根据供应商法大大授权自动签情况判断采用自动签章功能或手动签章 开始**/
                FddElectricSealResp fddElectricSealResp = customerIdMap.get(userBO.getIdcardNo());
                if (fddElectricSealResp!=null){
                    if (fddElectricSealResp.getAuthAutoSignStatus().equals(1)){
                        contractSignVo.setFirstSignType(0); // 自动签章
                    }else {
                        contractSignVo.setFirstSignType(1); // 手动签章
                    }
                }
                /** 签章根据供应商法大大授权自动签情况判断采用自动签章功能或手动签章 结束 **/
                if (contractSignVo.getFirstSignType().equals(1)){ // 手动签章
                    firstCustomerId = getCustomerId(userBO.getIdcardNo(), 0, customerIdMap);
                    signIntegrationReq.setTrustorMobile(userBO.getMobile());
                }else {
                    firstCustomerId = getCustomerId(userBO.getIdcardNo(), 1, customerIdMap);
                    signIntegrationReq.setTrustorSignKeyword(contractSignVo.getFirstSignLocation());
                }
            }
            String customerId = getCustomerId(nacosValueConfig.getTjzwCardNo(), 1,customerIdMap);
            // 法大大校验
            if (StringUtils.isBlank(firstCustomerId) || StringUtils.isBlank(customerId)){
                throw new BusinessException(ResultCode.FAIL.getCode(), "customerId无效，法大大未认证或认证已过期，请认证");
            }
            signIntegrationReq.setTrustorCustomerId(firstCustomerId);
            /** 结算合同动态获取法大大客户id 结束 **/
            signIntegrationReq.setDocTitle(contractSignVo.getServiceProject());
            signIntegrationReq.setPdfUrl(localPdfUrl);
            signIntegrationReq.setCarryCustomerId(customerId);
            signIntegrationReq.setCarrySignKeyword(contractSignVo.getSecondSignLocation());
            log.info("能源结算单签章 signIntegrationReq={}", signIntegrationReq);
            Result<ContractSignResVo> fddResult = contractFddSignFeign.signIntegration(signIntegrationReq);
            if (fddResult.isSuccess()) {
                return fddResult.getData();
            }
            throw new BusinessException(fddResult.getMsg());
        } catch (Exception e) {
            log.info("电子签章contractId：{}，失败原因：", contractId , e);
            throw e;
        }
    }

    @Override
    public FileInfoOut updateFindCarChargeEcPdfId(String contractId, SysUser sessionInfo) {
        log.info("从法大大下载电子签章并上传文件服务器开始, contractId={}", contractId);
        Result<FileInfoOut> fddResult = contractFddSignFeign.downLoadEcPdfId(contractId,sessionInfo);
        log.info("从法大大下载电子签章并上传文件服务器结束, fddResult={}", fddResult);
        if(!fddResult.isSuccess()){
            log.error("从法大大下载电子签章并上传文件服务器失败, message={}", fddResult.getMessage());
            throw new BusinessException(fddResult.getMessage());
        }
        FileInfoOut fileInfo = fddResult.getData();
        return fileInfo;
    }
}
