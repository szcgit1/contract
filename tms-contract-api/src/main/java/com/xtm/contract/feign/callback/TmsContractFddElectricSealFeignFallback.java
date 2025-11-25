package com.xtm.contract.feign.callback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.TmsContractFddElectricSealFeign;
import com.xtm.contract.model.finania.FddCostDetailReq;
import com.xtm.contract.model.mq.FinanceFddCostAuth;
import com.xtm.contract.model.mq.FinanceFddCostContract;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.FddCompanyStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TmsContractFddElectricSealFeignFallback implements TmsContractFddElectricSealFeign {
    @Override
    public Result<Boolean> fddDetailExport(FddCostDetailReq listQryParam) {
        log.error("法大大详情导出失败,请稍后再试");
        return Result.error("法大大详情导出失败,请稍后再试");
    }

    @Override
    public Result<Boolean> fddAuthMonthExport(FddCostDetailReq listQryParam) {
        log.error("法大大实名认证月汇总导出失败,请稍后再试");
        return Result.error("法大大实名认证月汇总导出失败,请稍后再试");
    }

    @Override
    public Result<Boolean> fddContarctMonthExport(FddCostDetailReq listQryParam) {
        log.error("法大大合同签署月汇总导出失败,请稍后再试");
        return Result.error("法大大合同签署月汇总导出失败,请稍后再试");
    }

    @Override
    public Result<String> sendFinanceFddCostContractMsg(FinanceFddCostContract message) {
        log.error("法大大发送MQ消息失败,请稍后再试:{}",message);
        return Result.error("法大大发送MQ消息失败,请稍后再试");
    }

    @Override
    public Result<String> sendFinanceFddCostAuthMsg(FinanceFddCostAuth message) {
        log.error("法大大发送MQ消息失败,请稍后再试:{}",message);
        return Result.error("法大大发送MQ消息失败,请稍后再试");
    }

    @Override
    public Result<List<CompanyFddVO>> queryLogisticsCompanyAndFddVerifyAuthStatus() {
        log.error("查询物流公司及法大大认证状态失败,请稍后再试");
        return Result.error("查询物流公司及法大大认证状态失败,请稍后再试");
    }

    @Override
    public Result queryFddVerifyAuthStatusByUserIds(List<String> userIds) {
        log.error("根据当前userId查询法大大认证授权状态失败,请稍后再试");
        return Result.error("根据当前userId查询法大大认证授权状态失败,请稍后再试");
    }

    @Override
    public Result<List<FddCompanyStatusVo>> companyFddStatus(List<String> ids) {
        log.error("查询公司法大大认证信息失败,请稍后再试");
        return Result.error("查询公司法大大认证信息失败,请稍后再试");
    }

    @Override
    public Result fddCreateAccountByOpenId(Integer accountType, String operatorId) {
        log.error("法大大创建用户失败,请稍后再试");
        return Result.error("法大大创建用户失败,请稍后再试");
    }

    @Override
    public Result fddUnbindOperator(Integer accountType, String operatorId) {
        log.error("法大大解绑操作失败,请稍后再试");
        return Result.error("法大大解绑操作失败,请稍后再试");
    }

    @Override
    public Result queryFddVerifyAuthStatusByUserId(String userId) {
        log.error("根据当前userId查询法大大认证授权状态失败,请稍后再试");
        return Result.error("根据当前userId查询法大大认证授权状态失败,请稍后再试");
    }
}
