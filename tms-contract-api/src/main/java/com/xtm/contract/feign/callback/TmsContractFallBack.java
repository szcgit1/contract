package com.xtm.contract.feign.callback;

import cn.hutool.json.JSONUtil;
import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.TmsContractFeign;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.FindCarChargeSummaryPdfParam;
import com.xtm.contract.model.param.FrameContractPartnerReq;
import com.xtm.contract.model.param.FrameContractVO;
import com.xtm.contract.model.param.IdsEntity;
import com.xtm.contract.model.param.ProofDocumentParam;
import com.xtm.contract.model.param.UpdateContractDataParam;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.req.ContractDeleteParam;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractResVO;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.ElectricSealResponse;
import com.xtm.contract.model.vo.FddElectricSealResp;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.contract.model.vo.FindCarChargeSummaryPdfVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import com.xtm.contract.model.vo.PartnerFraContractVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-01  20:55
 *@Description:
 *@title: ContractFallBack
 */
@Slf4j
@Component
public class TmsContractFallBack implements TmsContractFeign {
    @Override
    public Result<String> updateContractData(UpdateContractDataParam param) {
        log.error("修改合同表数据失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "修改合同表数据失败，请稍后再试");
    }

    @Override
    public Result<List<ContractVo>> queryContractAllData(ContractParam param){
        log.error("查询合同表数据失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同表数据失败，请稍后再试");
    }

    @Override
    public Result deleteByDocument(List<String> ids) {
        log.error("删除合同表数据失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "删除合同表数据失败，请稍后再试");
    }

    @Override
    public Result insertOrUpd(String sessionInfo, ContractCreUpdReq contractCreUpdReq) {
        log.error("创建合同表数据失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "创建合同表数据失败，请稍后再试");
    }

    @Override
    public Result<String> batchCreateContract(String sessionInfo, List<ContractCreUpdReq> contractCreUpdParam) {
        log.error("批量创建合同表数据失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "批量创建合同表数据失败，请稍后再试");
    }

    @Override
    public Result<FrameContractVO> getFrameContractDetail(String frameContractId) {
        log.error("获取框架合同详情失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "获取框架合同详情失败，请稍后再试");
    }

    @Override
    public Result<String> updateSettlePriceByDocumentId(ContractCreUpdReq contractCreUpdReq) {
        log.error("修改结算价失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "修改结算价失败，请稍后再试");
    }

    @Override
    public Result<BigDecimal> getCumulativeTradingVolume() {
        log.error("获取累计交易量失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "获取累计交易量失败，请稍后再试");
    }

    @Override
    public Result<FddElectricSealResp> findFddSealResult(Integer accountType, String operatorId) {
        log.error("查询法大大签章结果失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询法大大签章结果失败，请稍后再试");
    }

    @Override
    public Result<ElectricSealResponse> querySignSwitchTag() {
        log.error("查询签章开关失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询签章开关失败，请稍后再试");
    }

    @Override
    public Result<List<PartnerFraContractVO>> queryPartnerContracts(FrameContractPartnerReq companyMemberReq) {
        log.error("查询伙伴合同列表失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询伙伴合同列表失败，请稍后再试");
    }

    @Override
    public Result<List<String>> getContractExistByDocumentIds(IdsEntity documentIds) {
        log.error("查询合同是否存在失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同是否存在失败，请稍后再试");
    }

    @Override
    public void fddContractDelete(String param) {
        log.error("删除法大大合同失败，请稍后再试");
    }

    @Override
    public Result<?> updateContractSignStatusExperid() {
        log.error("合同过期提醒失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "合同过期提醒失败，请稍后再试");
    }

    @Override
    public Result findFddSealResultBatch() {
        log.error("获取法大大签章结果定时失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "获取法大大签章结果定时失败，请稍后再试");
    }

    @Override
    public List<ContractFrameRsp> getContractFrame(ContractFrameReq contractFrameReq) {
        log.error("获取框架合同及附件信息失败，请稍后再试");
        return null;
    }

    @Override
    public Result contractCreOrUpd(CommonCreUpdReq contractCreUpdParam) {
        log.error("结算单创建修改失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "结算单创建修改失败，请稍后再试");
    }


    @Override
    public Result<ContractResVO> checkSignStatus(String documentId) {
        log.error("查询合同签署状态失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同签署状态失败，请稍后再试");
    }

    @Override
    public Result creOrUpdGaoDengSettle(CommonCreUpdReq contractCreUpdParam) {
        log.error("高灯结算单创建修改失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "高灯结算单创建修改失败，请稍后再试");
    }

    @Override
    public Result<FindCarChargeSummaryPdfVo> findCarChargeSummaryEcSign(FindCarChargeSummaryPdfParam summaryPdfParam) {
        log.error("找车费用汇总单电子签章失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "找车费用汇总单电子签章失败，请稍后再试");
    }

    @Override
    public Result<ContractPathVO> rebuildEcSignature(String id) {
        log.error("重新生成合同电子签章失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "重新生成合同电子签章失败，请稍后再试");
    }

    @Override
    public Result<ContractCodeQryVO> queryContractCodeByDocumentId(String documentId) {
        log.error("查询合同编号失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同编号失败，请稍后再试");
    }

    @Override
    public Result rebuildUnionDispatchBatchContract(String unionBatchId) {
        log.error("重新生成联合运单合同失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "重新生成联合运单合同失败，请稍后再试");
    }
}
