package com.xtm.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtm.common.model.Result;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.SysUser;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.constant.FindCarConstantResult;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.param.FindCarChargeSummaryPdfParam;
import com.xtm.contract.model.param.UpdateContractDataParam;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.query.contract.ContractPreviewReq;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.FindCarChargeSummaryPdfVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.model.vo.fdd.ContractReq;
import com.xtm.contract.model.vo.fdd.Response.ContractVoRes;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.user.model.vo.ContactVo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
* @Author: fengyj
* @Description: 合同管理服务接口
* @Date: Create in 14:27 2022/12/17
*/
public interface ContractService extends IService<Contract> {

    /**
     * 创建合同
     * @param contractCreUpdParam
     * @return
     * @throws Exception
     */
    Result  createContract(ContractCreUpdReq contractCreUpdParam) throws Exception;

    /**
     * 批量创建合同
     * @param contractCreUpdReqs
     * @return
     * @throws Exception
     */
    void batchCreateContract(List<ContractCreUpdReq> contractCreUpdReqs) throws Exception;

    /**
     * 创建合同
     * @param contractCreUpdParam
     * @return
     * @throws Exception
     */
    void createSupplementContract(ContractCreUpdReq contractCreUpdParam) throws Exception;

    /**
     * 删除合同
     */
    int deleteContract(List<String> contractIds) throws Exception;

    /**
     * 通过单据删除合同
     * @param rebuildPdf 联合运单删除合同后是否重新生成合同PDF
     */
    void deleteByDocument(List<String> documentIds) throws Exception;
    /**
     * 查询合同详情
     * @return
     */
    ContractInfoQryVO selectContractDetail(String contractId);

    /**
     * 查询合同详情
     * @param contractId
     * @param forCreatePdf true表示用于签章查询,针对联合运单有特殊处理逻辑
     */
    public ContractInfoQryVO selectContractDetail(String contractId,boolean forCreatePdf);
    /**
     * 查询合同列表
     * @param contractListQryReq
     * @return
     */
    ApiPageResult<ContractInfoQryVO> selectContractList(ContractListQryReq contractListQryReq);

    /**
     * 通过单据ID查询合同信息
     * @param documentId
     * @return
     */
    ContractInfoQryVO selectContractByDocumentId(String documentId);

    /**
     * 生成本地合同PDF
     * @param cont
     * @param sessionInfo
     * @param businessType
     */
    boolean createLocalDeatilPdf(ContractInfoQryVO cont, SysUser sessionInfo, Integer businessType);

    boolean isFFVasAuth(String carrierContractMobile);

    /**
     * 保存本地合同PDF
     * @param pdfFileId
     * @param contractId
     */
    void saveLocalPdfToContract(String pdfFileId,String contractId);

    /**
     * 查询当前单据关联的最新合同版本
     * @param documentId
     * @return
     */
    Integer selectLastestVer(String documentId);

    /**
     * 查询当前单据的父合同信息
     * @param documentId
     * @return
     */
    Contract selectParentContract(String documentId);

    /**
     * 创建电子合同PDF
     * @param contractInfo
     */
    ContractResult createEcContractPdf(ContractInfoQryVO contractInfo, SysUser sessionInfo);

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
     * 获取合同方联系人信息
     * @return
     */
    ContactVo selectContractSideAdminInfo(CompanyBO companyInfo);

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
    List<Contract> selectExpiresContract();

    /**
     * 根据订单id修改合同的结算金额
     * @param contractCreUpdReq
     */
    void updateSettlePriceByDocumentId(ContractCreUpdReq contractCreUpdReq);

    /**
     * 法大大自动签署
     *
     * @param carrierContractIdCardNo
     * @param contractInfo
     * @return
     */
    ContractResult fDDCreateEcContractPdf(ContractInfoQryVO contractInfo);

    /**
     * 法大大合同签署
     * @param contractInfo
     */
    void createFDDContractPdf(ContractInfoQryVO contractInfo);

    /**
     * 法大大上传文件保存文件id
     *
     * @param contractId
     * @param sessionInfo
     * @return
     */
    String updateFddPdfId(String contractId, SysUser sessionInfo) throws Exception;

    /**
     * 合同双方是否授权
     * @param phone
     * @return
     */
    boolean isFFVasAuth(String phone , String xtmCompanyId);


    /**
     * 保存合同扩展表
     * @param contractId
     * @param signType
     * @param cardNo
     */
    void saveContractExt(String contractId, Integer signType,String cardNo);

    /**
     * 根据单据IDS查询所有存在的单据
     * @param documentIds
     * @return
     */
    List<String> getContractExistByDocumentIds(List<String> documentIds);

    /**
     * 预览合同内容
     *
     * @param contractPreviewReq
     * @param request
     * @param response
     * @return
     */
    ResponseEntity<byte[]> contractPreview(ContractPreviewReq contractPreviewReq, HttpServletRequest request, HttpServletResponse response);

    /**
     * 找车费用汇总单电子签章
     * @param summaryPdfParam
     * @return
     */
    Result<FindCarChargeSummaryPdfVo> findCarChargeSummaryEcSign(FindCarChargeSummaryPdfParam summaryPdfParam) throws Exception;

    /**
     * 找车费用汇总单生成电子签章PDF
     * @param summaryPdfVo
     * @return
     */
    FindCarConstantResult createEcFindCarChargeSummaryPdf(FindCarChargeSummaryPdfVo summaryPdfVo);

    /**
     * 更新找车费用电子签章PdfId
     *
     * @param contractId
     * @param sessionInfo
     */
    String updateFindCarChargeEcPdfId(String contractId, SysUser sessionInfo, FindCarChargeSummaryPdfVo summaryPdfVo) throws Exception;

    /**
     * 找车费用汇总单生成本地PDF合同
     */
    void createLocalFindCarChargeSummaryPdf(FindCarChargeSummaryPdfParam cont, SysUser sessionInfo, FindCarChargeSummaryPdfVo summaryPdfVo) throws Exception;

    FindCarContractResVo checkSignStatus(String contractId, String companyId);

    List<ContractVoRes> findContractTemplate(ContractReq contractReq);

    /**
     * 测试分表查询
     * @param start 创建开始时间
     * @param end 创建结束时间
     */
    List<Contract> queryShardingTable(LocalDateTime start, LocalDateTime end);

    /**
     * 修改合同表数据
     * @return
     */
    void updateContractData(UpdateContractDataParam param);

    /**
     * 查询合同表数据
     * @param param
     * @return
     */
    List<ContractVo> queryContractAllData(ContractParam param);

    BigDecimal getCumulativeTradingVolume();

    Map<String, FddElectricSealResp> getOpenIdMap(List<String> openIds);

    /**
     * 重新生成联合运单合同
     * @param unionBatchId
     */
    Result rebuildUnionDispatchBatchContract(String unionBatchId);
}
