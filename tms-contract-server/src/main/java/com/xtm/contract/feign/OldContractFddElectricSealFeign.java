package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.fallback.OldContractFddElectricSealFeignFallback;
import com.xtm.contract.model.finania.FddCostDetailReq;
import com.xtm.contract.model.mq.FinanceFddCostContract;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.FddCompanyStatusVo;
import com.xtm.contract.model.vo.FddElectricSealVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "tms-contract", fallback = OldContractFddElectricSealFeignFallback.class, path = "apiPlat/contract")
public interface OldContractFddElectricSealFeign {

    /**
     * 法大大详情导出
     */
    @PostMapping(value = "/financial/counting/fddDetail/export")
    @ApiOperation(value = "法大大详情导出")
    Result<Boolean> fddDetailExport(@RequestBody FddCostDetailReq listQryParam);

    /**
     * 法大大实名认证月汇总导出
     * @param listQryParam
     * @return
     */
    @PostMapping(value = "/financial/counting/fddAuthMonth/export")
    @ApiOperation(value = "法大大实名认证月汇总导出")
    Result<Boolean> fddAuthMonthExport(@RequestBody FddCostDetailReq listQryParam);

    /**
     * 法大大合同签署月汇总导出
     * @param listQryParam
     * @return
     */
    @PostMapping(value = "/financial/counting/fddContarctMonth/export")
    @ApiOperation(value = "法大大合同签署月汇总导出")
    Result<Boolean> fddContarctMonthExport(@RequestBody FddCostDetailReq listQryParam);

    /**
     * 法大大合同签署日汇总导出
     * @param message
     * @return
     */
    @PostMapping(value = "/serviceCharge/sendMqMessage")
    @ApiOperation(value = "法大大合同签署日汇总导出")
    Result<String> sendMqMsg(@RequestBody FinanceFddCostContract message);

    /**
     * 查询所有服务商以及认证信息
     */
    @GetMapping(value = "/fddElectricSeal/queryLogisticsCompanyAndFddVerifyAuthStatus")
    Result<List<CompanyFddVO>> queryLogisticsCompanyAndFddVerifyAuthStatus();

    @PostMapping(value = "/fddElectricSeal/queryFddVerifyAuthStatusByUserIds")
    @ApiOperation(value = "根据当前userId查询法大大认证授权状态")
    Result<List<FddElectricSealVo>>  queryFddVerifyAuthStatusByUserIds(@RequestParam(value = "userIds") List<String> userIds);


    @PostMapping(value = "/fddElectricSealNew/companyFddStatus")
    @ApiOperation(value = "查询公司法大大认证信息")
    Result<List<FddCompanyStatusVo>> companyFddStatus(@RequestBody List<String> ids);

    /**
     *  法大大创建用户
     * @param accountType 账号类型 1个人 2企业
     */
    @GetMapping(value = "/fddElectricSealNew/fddCreateAccountByOpenId")
    @ApiOperation(value = "法大大创建用户 accountType 账号类型 1个人 2企业")
    Result fddCreateAccountByOpenId(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId);

    @GetMapping(value = "/fddElectricSealNew/fddUnbindOperator")
    @ApiOperation(value = "法大大解绑操作")
    Result fddUnbindOperator(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId);

    @PostMapping(value = "/queryFddVerifyAuthStatusByUserId/{userId}")
    @ApiOperation(value = "根据当前userId查询法大大认证授权状态")
    Result  queryFddVerifyAuthStatusByUserId(@PathVariable(value="userId") String userId);
}


