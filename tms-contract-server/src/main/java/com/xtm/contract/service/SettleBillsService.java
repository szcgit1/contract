package com.xtm.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.SettleBills;
import com.xtm.contract.model.energy.BalanceDetailRes;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractResVO;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.contract.SettleBillsInfoQryVO;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;

import java.util.List;

/**
* @Author: fengyj
* @Description: 合同管理服务接口
* @Date: Create in 14:27 2022/12/17
*/
public interface SettleBillsService extends IService<SettleBills> {

    /**
     * 创建合同
     * @param contractCreUpdParam
     * @return
     * @throws Exception
     */
    Result  createContract(CommonCreUpdReq contractCreUpdParam) throws Exception;

    /**
     * 批量创建合同
     * @param contractCreUpdReqs
     * @return
     * @throws Exception
     */
    void batchCreateContract(List<CommonCreUpdReq> contractCreUpdReqs) throws Exception;

    /**
     * 删除合同
     */
    int deleteContract(List<String> contractIds) throws Exception;

    /**
     * 通过单据删除合同
     */
    void deleteByDocument(List<String> documentIds) throws Exception;
    /**
     * 查询合同详情
     * @return
     */
    SettleBillsInfoQryVO selectContractDetail(String contractId);

    /**
     * 查询合同列表
     * @param contractListQryReq
     * @return
     */
    ApiPageResult<SettleBillsInfoQryVO> selectContractList(ContractListQryReq contractListQryReq);

    /**
     * 通过单据ID查询合同信息
     * @param documentId
     * @return
     */
    SettleBillsInfoQryVO selectContractByDocumentId(String documentId);

    /**
     * 生成本地合同PDF
     * @param cont
     * @param sessionInfo
     * @param businessType
     */
    void createLocalDeatilPdf(SettleBillsInfoQryVO cont, BalanceDetailRes balanceBaseInfo, SysUser sessionInfo, Integer businessType);

    /**
     * 保存本地合同PDF
     * @param pdfFileId
     * @param contractId
     */
    void saveLocalPdfToContract(String pdfFileId,String contractId);

    /**
     * 创建电子合同PDF
     * @param contractInfo
     */
    ContractResult createEcContractPdf(SettleBillsInfoQryVO contractInfo, SysUser sessionInfo);

    /**
     * 签署电子合同
     * @param eqbConfigInfo
     * @param ecSignReq
     * @throws BusinessException
     */
    String signEqbElectronicSeal(EqbConfigInfo eqbConfigInfo, EContractEcSignReq ecSignReq, CompanyVasInfo vasInfo) throws BusinessException;

    /**
     * 保存本地合同PDF
     * @param contractResult
     * @param contractId
     */
    void saveErrorInfoToContract(ContractResult contractResult, String contractId);

    /**
     * 保存电子合同PDF
     * @param ecPdfId
     * @param contractId
     */
    void saveEcPdfToContract(String ecPdfId, String contractId);

    /**
     * 保存e签宝的文件ID
     * @param fileId
     * @param contractId
     */
    void saveEQBFileIdToContract(String fileId, String contractId);

    /**
     * 更新合同签署状态
     * @param trustorStatu
     * @param carrierStatu
     * @param contractId
     */
    void updateContractSignStatus(Integer trustorStatu,Integer carrierStatu,String contractId);

    /**
     * 重新生成电子合同
     * @param id
     * @return
     * @throws BusinessException
     */
    ContractPathVO rebuildEcContract(String id) throws BusinessException;

    /**
     * 重新生成本地LocalPdf
     * @param id
     * @return
     * @throws BusinessException
     */
    ContractPathVO rebuildLocalPdf(String id) throws BusinessException;

    /**
     * 通过单据ID获取合同编号
     * @param documentId
     * @return
     */
    ContractCodeQryVO selectContractCodeByDocumentId(String documentId);

    /**
     * 查询过期的框架合同列表
     * @return
     */
    List<SettleBills> selectExpiresContract();

    /**
     * 法大大自动签署
     * @param fddConfigInfo
     * @param contractInfo
     */
    ContractResult fDDCreateEcContractPdf(SettleBillsInfoQryVO contractInfo);

    /**
     * 法大大合同签署
     * @param contractInfo
     */
    void createFDDContractPdf(SettleBillsInfoQryVO contractInfo);

    /**
     * 法大大上传文件保存文件id
     *
     * @param contractId
     * @param sessionInfo
     * @return
     */
    String updateFddPdfId(String contractId, SysUser sessionInfo) throws Exception;

    /**
     * 法大大签章结果
     *
     * @return
     */
    ContractResVO checkSignStatus(String documentId);

    public String createGDLocalPdf(BalanceDetailRes balanceBaseInfo);
}
