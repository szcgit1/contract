package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.feign.EnergyFeign;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.energy.BalanceDetailRes;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.model.vo.contract.SettleBillsInfoQryVO;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.service.ContractEqbSignService;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.SettleBillsService;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.FileHelper;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ContractAsyncServiceImpl {
    private ContractService contractService;
    @Autowired
    public void setContractService(@Lazy ContractService contractService) {
        this.contractService = contractService;
    }
    private SettleBillsService settleBillsService;
    @Autowired
    public void setSettleBillsService(@Lazy SettleBillsService settleBillsService) {
        this.settleBillsService = settleBillsService;
    }
    @Autowired
    private EqbHelper eqbHelper;
    @Autowired
    private ContractEqbSignService eqbSignService;
    @Autowired
    private FileHelper fileHelper;
    @Resource
    private EnergyFeign energyFeign;

    @Async("asyncExecutor")
    protected void fddAfterContractSave(ContractCreUpdReq contractCreUpdParam, String id, SysUser sessionInfo) {
        //生成本地合同
        ContractInfoQryVO contractInfo = contractService.selectContractDetail(id,true);
        contractService.createLocalDeatilPdf(contractInfo, sessionInfo, contractCreUpdParam.getBusinessType());
        //判断是否都授权了法大大，如果没有授权，则走旧的e签宝
        String xtmCompanyId = "";
        Integer documentType = contractInfo.getDocumentType();
        // 根据合同类型，获取companyId
        if(DicConstant.DOCUMENT_TYPE.ORDER.equals(documentType)){
            xtmCompanyId = contractInfo.getCarrierCompanyId();
        } else if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(documentType)){
            xtmCompanyId = contractInfo.getTrustorCompanyId();
        }
        if (!contractService.isFFVasAuth(contractCreUpdParam.getCarrierContractIdCardNo(),xtmCompanyId)) {
            log.info("没有授权不签署 id={},carrierContractIdCardNo={}", id, contractCreUpdParam.getCarrierContractIdCardNo());
            return;
        }
        if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(contractCreUpdParam.getContractDocumentType())) {
            log.info("生成运单的电子签章 documentId={} orderIds={}", contractCreUpdParam.getDocumentId(), contractCreUpdParam.getOrderIds());
            ContractInfoQryVO batchCon = contractService.selectContractByDocumentId(contractCreUpdParam.getDocumentId());
            contractService.fDDCreateEcContractPdf(batchCon);
            try {
                contractService.updateFddPdfId(id, sessionInfo);
            } catch (Exception e) {
                log.error("法大大上传文件保存文件id异常:",e);
                throw new BusinessException(e.getMessage());
            }
        }
    }

    @Async("asyncExecutor")
    protected void fddAfterCommonSave(CommonCreUpdReq contractCreUpdParam, String id, SysUser sessionInfo) {
        //生成本地合同
        SettleBillsInfoQryVO contractInfo = settleBillsService.selectContractDetail(id);
        BalanceDetailRes balanceDetailRes = contractCreUpdParam.getBalanceDetailRes();
        if (contractCreUpdParam.getBalanceDetailRes() == null) {
            Result<BalanceDetailRes> payDetailInfolList = energyFeign.getPayDetailInfolList(Long.parseLong(contractInfo.getDocumentId()));
            if (payDetailInfolList.getCode() != CommonLang.SUCCESS.getCode() || payDetailInfolList.getData() == null) {
                log.error("获取账单信息失败，id={}", id);
                throw new BusinessException(payDetailInfolList.getCode(), payDetailInfolList.getMessage());
            }
            balanceDetailRes = payDetailInfolList.getData();
        }
        if (contractInfo.getEcContractPath() == null) {
            settleBillsService.createLocalDeatilPdf(contractInfo, balanceDetailRes, sessionInfo, contractCreUpdParam.getBusinessType());
        }
        //判断是否都授权了法大大，如果没有授权，则走旧的e签宝
        if (!contractService.isFFVasAuth(contractCreUpdParam.getSupplierCompanyIdCardNo())) {
            log.info("没有授权不签署 id={},getSupplierCompanyId={}", id, contractCreUpdParam.getSupplierCompanyId());
            return;
        }

        SettleBillsInfoQryVO batchCon = settleBillsService.selectContractByDocumentId(contractCreUpdParam.getDocumentId());
        if (batchCon != null && batchCon.getEcContractPath() != null && batchCon.getEcContractEsignFlowId() == null) {
            settleBillsService.fDDCreateEcContractPdf(batchCon);
            try {
                contractService.updateFddPdfId(id, sessionInfo);
            } catch (Exception e) {
                log.error("fddAfterCommonSave error:", e);
                throw new BusinessException(e.getMessage());
            }
        }
    }

    @Async("asyncExecutor")
    protected void afterContractSave(ContractCreUpdReq contractCreUpdParam, String id, SysUser sessionInfo) {
        //生成本地合同
        ContractInfoQryVO contractInfo = contractService.selectContractDetail(id);
        contractService.createLocalDeatilPdf(contractInfo, sessionInfo, contractCreUpdParam.getBusinessType());
        //当合同为三方订单合同时
        if (DicConstant.DOCUMENT_TYPE.ORDER.equals(contractCreUpdParam.getContractDocumentType())
                || DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(contractCreUpdParam.getBusinessType())) {
            log.info("删除订单的电子合同签章 id={}", id);
        }
        //运单签署合同时生成对应的订单合同电子签章
        if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(contractCreUpdParam.getContractDocumentType())) {
            List<String> contractIdList = new ArrayList<>();
            log.info("生成运单的电子签章 documentId={} orderIds={}", contractCreUpdParam.getDocumentId(), contractCreUpdParam.getOrderIds());
            //先生成运单电子签章,再生成关联的订单电子签章
            ContractInfoQryVO batchCon = contractService.selectContractByDocumentId(contractCreUpdParam.getDocumentId());
            contractService.createEcContractPdf(batchCon, sessionInfo);

            // 运单-自动签署电子签章，需要下载合同，上次到服务器，保存电子印章ID start
            //从归档到下载盖章大约需要3s
            try {
                Thread.sleep(3500L);
            } catch (InterruptedException e) {
                throw new BusinessException(e.getMessage());
            }
            // 获取E签宝的配置信息
            log.info("=====>获取E签宝的配置信息开始，contractId={}", id);
            EqbConfigInfo configInfo = eqbHelper.getEqbConfigInfo();
            // 下载合同
            //多个请求，导致下载请求过多，大约需要3s  流程文档下载出参：{"code":429,"message":"TOO_MANY_REQUESTS(RULEID_1:A444D3E1A979A36406EEBA6B2DAEECE9)"}
            try {
                Thread.sleep(3500L);
            } catch (InterruptedException e) {
                throw new BusinessException(e.getMessage());
            }
            String ecPdfUrl = eqbSignService.getDownloadDocumentUrl(configInfo, batchCon.getEcContractEsignFlowId());
            if (StrUtil.isBlank(ecPdfUrl)) {
                log.error("E签宝文件下载失败");
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_DOWNLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.FILE_DOWNLOAD_FAIL.getMessage());
            }
            log.info("=====>E签宝文件下载-ecPdfUrl={}", ecPdfUrl);
            // 上传到服务器
            FileInfoOut fileInfo = fileHelper.urlUploadFile(ecPdfUrl, sessionInfo.getAgentCode(), DicConstant.DOCUMENT_TYPE.CONTRACT.toString() + ".pdf");
            if (fileInfo == null) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            log.info("=====>E签宝上传到服务器结束");
            // 保存电子印章ID
            contractService.saveEcPdfToContract(fileInfo.getFileID(), id);
            log.info("=====>保存电子印章ID={}", id);
            // 运单-自动签署电子签章，需要下载合同，上次到服务器，保存电子印章ID end

            if (StringUtils.isNotBlank(batchCon.getEcContractEsignFlowId())) {
                contractIdList.add(batchCon.getId());
            }
            if (CollUtil.isNotEmpty(contractCreUpdParam.getOrderIds())) {
                log.info("删除运单关联的订单的电子合同签章 OrderIds={}", contractCreUpdParam.getOrderIds());
            }
            /**
             * XNWEB-13136
             * 已对接电子签开启静默签且账户余额充足时，订单运单明细合同一已生成，合同列表我方签署和对方签署状态应该是已签署的状态.
             */
            if (CollUtil.isNotEmpty(contractIdList)) {
                contractService.update(
                        Wrappers.<Contract>lambdaUpdate()
                                .set(Contract::getTrustorSignStatus, DicConstant.CONTRACT_SIGN_STATUS.SUCCESS)
                                .set(Contract::getCarrierSignStatus, DicConstant.CONTRACT_SIGN_STATUS.SUCCESS)
                                .in(Contract::getId, contractIdList)
                );
            }
        }
    }

    @Async("asyncExecutor")
    protected void afterCommonSave(CommonCreUpdReq contractCreUpdParam, String id, SysUser sessionInfo) {
        //生成本地合同
        SettleBillsInfoQryVO contractInfo = settleBillsService.selectContractDetail(id);
        Result<BalanceDetailRes> payDetailInfolList = energyFeign.getPayDetailInfolList(Long.parseLong(contractInfo.getDocumentId()));
        if (payDetailInfolList.getCode() != CommonLang.SUCCESS.getCode() || payDetailInfolList.getData() == null) {
            throw new BusinessException(payDetailInfolList.getCode(), payDetailInfolList.getMessage());
        }
        settleBillsService.createLocalDeatilPdf(contractInfo, payDetailInfolList.getData(), sessionInfo, contractCreUpdParam.getBusinessType());
        //当合同为三方订单合同时
        if (DicConstant.DOCUMENT_TYPE.ORDER.equals(contractCreUpdParam.getContractDocumentType())
                || DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(contractCreUpdParam.getBusinessType())) {
            log.info("删除订单的电子合同签章 id={}", id);
        }
        //运单签署合同时生成对应的订单合同电子签章
        if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(contractCreUpdParam.getContractDocumentType())) {
            List<String> contractIdList = new ArrayList<>();
            log.info("生成运单的电子签章 documentId={} ", contractCreUpdParam.getDocumentId());
            //先生成运单电子签章,再生成关联的订单电子签章
            SettleBillsInfoQryVO batchCon = settleBillsService.selectContractByDocumentId(contractCreUpdParam.getDocumentId());
            settleBillsService.createEcContractPdf(batchCon, sessionInfo);

            // 运单-自动签署电子签章，需要下载合同，上次到服务器，保存电子印章ID start
            //从归档到下载盖章大约需要3s
            try {
                Thread.sleep(3500L);
            } catch (InterruptedException e) {
                throw new BusinessException(e.getMessage());
            }
            // 获取E签宝的配置信息
            log.info("=====>获取E签宝的配置信息开始，contractId={}", id);
            EqbConfigInfo configInfo = eqbHelper.getEqbConfigInfo();
            // 下载合同
            //多个请求，导致下载请求过多，大约需要3s  流程文档下载出参：{"code":429,"message":"TOO_MANY_REQUESTS(RULEID_1:A444D3E1A979A36406EEBA6B2DAEECE9)"}
            try {
                Thread.sleep(3500L);
            } catch (InterruptedException e) {
                throw new BusinessException(e.getMessage());
            }
            String ecPdfUrl = eqbSignService.getDownloadDocumentUrl(configInfo, batchCon.getEcContractEsignFlowId());
            if (StrUtil.isBlank(ecPdfUrl)) {
                log.error("E签宝文件下载失败");
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_DOWNLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.FILE_DOWNLOAD_FAIL.getMessage());
            }
            log.info("=====>E签宝文件下载-ecPdfUrl={}", ecPdfUrl);
            // 上传到服务器
            FileInfoOut fileInfo = fileHelper.urlUploadFile(ecPdfUrl, sessionInfo.getAgentCode(), DicConstant.DOCUMENT_TYPE.CONTRACT.toString() + ".pdf");
            if (fileInfo == null) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode, ContractErrorCode.LOCAL_PDF_ERROR.getMessage());
            }
            log.info("=====>E签宝上传到服务器结束");
            // 保存电子印章ID
            contractService.saveEcPdfToContract(fileInfo.getFileID(), id);
            log.info("=====>保存电子印章ID={}", id);
            // 运单-自动签署电子签章，需要下载合同，上次到服务器，保存电子印章ID end

            if (StringUtils.isNotBlank(batchCon.getEcContractEsignFlowId())) {
                contractIdList.add(batchCon.getId());
            }

            /**
             * XNWEB-13136
             * 已对接电子签开启静默签且账户余额充足时，订单运单明细合同一已生成，合同列表我方签署和对方签署状态应该是已签署的状态.
             */
            if (CollUtil.isNotEmpty(contractIdList)) {
                contractService.update(
                        Wrappers.<Contract>lambdaUpdate()
                                .set(Contract::getTrustorSignStatus, DicConstant.CONTRACT_SIGN_STATUS.SUCCESS)
                                .set(Contract::getCarrierSignStatus, DicConstant.CONTRACT_SIGN_STATUS.SUCCESS)
                                .in(Contract::getId, contractIdList)
                );
            }
        }
    }
}
