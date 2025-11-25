package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.query.finania.FddCostDetailReq;
import com.xtm.contract.model.vo.finance.FddAuthMonthCostVo;
import com.xtm.contract.model.vo.finance.FddContractDayCostVo;
import com.xtm.contract.model.vo.finance.FddCostDetailVo;
import com.xtm.contract.service.FinacialCountingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/financial/counting")
@Api(tags = "财务对账信息")
public class FinancialCountingController {

    @Autowired
    private FinacialCountingService finacialCountingService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Result<ApiPageResult<FddCostDetailVo>> page(@RequestBody FddCostDetailReq listQryParam) throws Exception {
        return Result.of(finacialCountingService.page(listQryParam), CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/fddDetail/export")
    @ApiOperation(value = "法大大详情导出")
    public Result<Boolean> fddDetailExport(@RequestBody FddCostDetailReq listQryParam){
        finacialCountingService.fddDetailExport(listQryParam);
        return Result.of(true,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/auth/page")
    @ApiOperation(value = "分页查询实名认证")
    public Result<ApiPageResult<FddAuthMonthCostVo>> authPage(@RequestBody FddCostDetailReq listQryParam) throws Exception {
        return Result.of(finacialCountingService.authPage(listQryParam),CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/fddAuthMonth/export")
    @ApiOperation(value = "法大大实名认证月汇总导出")
    public Result<Boolean> fddAuthMonthExport(@RequestBody FddCostDetailReq listQryParam){
        finacialCountingService.fddAuthMonthExport(listQryParam);
        return Result.of(true,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/constract/page")
    @ApiOperation(value = "分页查询自动签署合同")
    public Result<ApiPageResult<FddContractDayCostVo>> contractPage(@RequestBody FddCostDetailReq listQryParam) throws Exception {
        return Result.of(finacialCountingService.contractPage(listQryParam),CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/fddContarctMonth/export")
    @ApiOperation(value = "法大大合同签署月汇总导出")
    public Result<Boolean> fddContarctMonthExport(@RequestBody FddCostDetailReq listQryParam){
        finacialCountingService.fddContarctMonthExport(listQryParam);
        return Result.of(true, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

}
