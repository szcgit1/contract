package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.config.FeignConfig;
import com.xtm.contract.feign.fallback.OldContractFallBack;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.FindCarChargeSummaryPdfParam;
import com.xtm.contract.model.param.FrameContractPartnerReq;
import com.xtm.contract.model.param.FrameContractVO;
import com.xtm.contract.model.param.IdsEntity;
import com.xtm.contract.model.param.UpdateContractDataParam;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractResVO;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.ElectricSealResponse;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.contract.model.vo.FindCarChargeSummaryPdfVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import com.xtm.contract.model.vo.PartnerFraContractVO;
import com.xtm.thirdparty.auth.model.vo.ContractSignResVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
* @Author: fengyj
* @Description: 合同
* @Date: Create in 14:54 2022/12/17
*/
@FeignClient(value = "base-contract", configuration = FeignConfig.class, contextId = "contract", fallback = OldContractFallBack.class,path = "/apiPlat/contract")
public interface OldContractFeign {

    /**
     * 创建合同
     * @param contractCreUpdReq 合同单据数据
     * @return
     */
    @PostMapping("/detail/insertOrUpd")
    Result insertOrUpd(ContractCreUpdReq contractCreUpdReq);

    @PostMapping(value = "/serviceCharge/serviceEcSign")
    @ApiOperation(value = "电子签章")
    Result<ContractSignResVo> serviceEcSign(@RequestBody ContractSignVo<?> contractSignVo);

    @PostMapping(value = "/serviceCharge/energyEcSign")
    @ApiOperation(value = "能源单电子签章")
    Result<ContractSignResVo> energyEcSign(@RequestBody ContractSignVo<?> contractSignVo);

    /**
     * @Description: 找车服务费签署结果查询
     */
    @GetMapping(value = "/detail/checkSignStatus/{contractId}/{companyId}")
    @ApiOperation(value = "查询合同签署状态")
    Result<FindCarContractResVo> checkSignStatus(@PathVariable("contractId") String contractId, @PathVariable("companyId") String companyId);

    /**
     * 修改合同表数据
     */
    @PostMapping(value = "/detail/updateContractData")
    @ApiOperation(value = "修改合同表数据")
    Result<String> updateContractData(@RequestBody UpdateContractDataParam param);

    /**
     * 查询合同表数据
     */
    @PostMapping(value = "/detail/queryContractAllData")
    @ApiOperation(value = "查询合同表数据")
    Result<List<ContractVo>> queryContractAllData(@RequestBody ContractParam param);

    @PostMapping("/detail/deleteByDocument")
    @ApiOperation(value = "删除合同表数据")
    Result deleteByDocument(@RequestParam(value = "ids") List<String> ids);

    /**
     * 批量创建合同接口
     * @param contractCreUpdParam 合同单据数据
     */
    @PostMapping("/detail/batchCreateContract")
    @ApiOperation(value = "批量创建合同接口")
    Result<String> batchCreateContract(@RequestHeader("session") String sessionInfo, @RequestBody List<ContractCreUpdReq> contractCreUpdParam);

    /**
     * 根据框架合同id查询框架合同详情
     *
     * @param frameContractId 框架合同id
     * @return
     */
    @GetMapping("/frame/detail/{id}")
    @ApiOperation(value = "根据框架合同id查询框架合同详情")
    Result<FrameContractVO> getFrameContractDetail(@PathVariable("id") String frameContractId);

    /**
     * 根据订单id修改合同的结算金额
     * @param contractCreUpdReq
     * @return
     */
    @PostMapping("/detail/updateSettlePriceByDocumentId")
    @ApiOperation(value = "根据订单id修改合同的结算金额")
    Result<String> updateSettlePriceByDocumentId(@RequestBody ContractCreUpdReq contractCreUpdReq);

    /**
     * @Desc 统计全平台交易额
     */
    @GetMapping("/detail/getCumulativeTradingVolume")
    @ApiOperation(value = "统计全平台交易额")
    Result<BigDecimal> getCumulativeTradingVolume();

    /**
     * 获取法大大认证结果
     * @param accountType
     * @param operatorId
     * @return
     */
    @GetMapping(value = "/fddElectricSealNew/findFddSealResult")
    @ApiOperation(value = "获取法大大认证结果")
    Result<FddElectricSealVo> findFddSealResult(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId);

    /**
     * 查询签章开关标识
     */
    @PostMapping("/electricSealSwitch/querySignSwitchTag")
    @ApiOperation(value = "查询签章开关标识")
    Result<ElectricSealResponse> querySignSwitchTag();

    /**
     * 通过伙伴公司ID查询伙伴合同列表
     * @param companyMemberReq
     */
    @PostMapping("/frame/queryPartnerContracts")
    @ApiOperation(value = "通过伙伴公司ID查询伙伴合同列表")
    Result<List<PartnerFraContractVO>> queryPartnerContracts(@RequestBody FrameContractPartnerReq companyMemberReq);

    /**
     * 根据单据IDS查询所有存在的单据
     * @param documentIds
     * @return
     */
    @PostMapping("/detail/getContractExistByDocumentIds")
    @ApiOperation(value = "根据单据IDS查询所有存在的单据")
    Result<List<String>> getContractExistByDocumentIds(@RequestBody IdsEntity documentIds);

    /**
     * 法大大合同文件删除
     *
     * @param param 请求参数
     */
    @PostMapping(value = "/fddElectricSeal/fddContractDelete")
    @ApiOperation(value = "法大大合同文件删除")
    void fddContractDelete(@RequestBody(required = false) String param);
    /**
     * 合同过期提醒
     */
    @GetMapping("/detail/updateContractSignStatusExperid")
    @ApiOperation(value = "合同过期提醒")
    Result<?> updateContractSignStatusExperid();

    /**
     * 1、合同法大大认证和自动授权结果查询;
     * @return
     */
    @GetMapping(value = "/fddElectricSealNew/findFddSealResultBatch")
    @ApiOperation(value = "查询法大大签章结果")
    Result findFddSealResultBatch();

    @GetMapping("/frame/getContractFrame")
    @ApiOperation(value = "框架合同及其附件查询")
    List<ContractFrameRsp> getContractFrame(@RequestBody ContractFrameReq contractFrameReq);

    @PostMapping(value = "/settleBills/insertOrUpd")
    @ApiOperation(value = "结算单创建修改")
    Result  contractCreOrUpd(@RequestBody CommonCreUpdReq contractCreUpdParam);

    @GetMapping(value = "/settleBills/checkSignStatus/{documentId}")
    @ApiOperation(value = "查询合同签署状态")
    Result<ContractResVO> checkSignStatus(@PathVariable("documentId") String documentId);

    @PostMapping(value = "/settleBills/gdInsertOrUpd")
    @ApiOperation(value = "创建高灯能源结算单")
    Result  creOrUpdGaoDengSettle(@RequestBody CommonCreUpdReq contractCreUpdParam);

    @PostMapping(value = "/detail/findCarChargeSummaryEcSign")
    @ApiOperation(value = "找车费用汇总单电子签章")
    Result<FindCarChargeSummaryPdfVo> findCarChargeSummaryEcSign(@RequestBody FindCarChargeSummaryPdfParam summaryPdfParam);

    @PostMapping(value = "/frame/rebuildFraEcSignature")
    @ApiOperation(value = "重新生成电子签章")
    Result<ContractPathVO> rebuildEcSignature(@RequestParam("id") String id);

    @PostMapping(value = "/queryPartnerContracts")
    @ApiOperation(value = "通过伙伴公司ID查询伙伴合同列表")
    Result<List<PartnerFraContractVO>> selectPartnerFraContractList(@RequestBody FrameContractPartnerReq partnerReq);

    /**
     * 根据订单Id获取合同
     * @param documentId 订单Id
     * @return
     */
    @GetMapping("/detail/queryContractCodeByDocumentId/{id}")
    Result<ContractCodeQryVO> queryContractCodeByDocumentId(@PathVariable("id") String documentId);

}
