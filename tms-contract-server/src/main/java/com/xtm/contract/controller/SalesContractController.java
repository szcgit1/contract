package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.param.SalesContractHistoryListParam;
import com.xtm.contract.model.param.SalesContractListParam;
import com.xtm.contract.model.vo.SalesContractGoodsVO;
import com.xtm.contract.model.vo.SalesContractHistoryListVO;
import com.xtm.contract.model.vo.SalesContractDetailVO;
import com.xtm.contract.model.vo.SalesContractHistoryDetailVO;
import com.xtm.contract.model.vo.SalesContractListVO;
import com.xtm.contract.model.vo.SalesContractVo;
import com.xtm.contract.service.SalesContractHistoryService;
import com.xtm.contract.service.SalesContractService;
import com.xtm.utils.string.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/salesContract")
@Slf4j
@Api(tags = "销售合同业务相关接口")
public class SalesContractController {

    @Autowired
    private SalesContractService salesContractService;

    @Resource
    private SalesContractHistoryService salesContractHistoryService;

    /**
     * 根据销售合同编码查询启用且未绑定框架合同协议的销售合同
     * @param contractCode 销售合同编码
     */
    @GetMapping("/getEnableUnRelateContractByCode")
    public Result<List<SalesContractVo>> getEnableUnRelateContractByCode(@RequestParam String contractCode) {
        if (StringUtils.isBlank(contractCode)){
            return Result.error(ResultCode.VALIDATOR.getCode(), "合同编号不能为空");
        }
        List<SalesContractVo> salesContractList = salesContractService.getEnableUnRelateContractByCode(contractCode);
        return Result.ok(salesContractList);
    }

    /**
     * 查看销售合同详情
     */
    @ApiOperation(value = "查看销售合同详情")
    @GetMapping("/getDetail/{id}")
    public Result<SalesContractDetailVO> getDetail(@PathVariable("id") String id) {
        if(StringUtils.isBlank( id)){
            return Result.error(ResultCode.VALIDATOR.getCode(), "id不能为空");
        }
        SalesContractDetailVO detailVO = salesContractService.getDetail(id);
        return Result.ok(detailVO);
    }

    /**
     * 销售合同列表查询
     */
    @ApiOperation(value = "销售合同列表查询")
    @PostMapping("/querySalesContractPageList")
    public Result<ApiPageResult<SalesContractListVO>> querySalesContractPageList(@RequestBody SalesContractListParam param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        if (pageNum == null) {
            return Result.error("页码不能为空");
        }
        if (pageSize == null) {
            return Result.error("每页数据量不能为空");
        }
        ApiPageResult<SalesContractListVO> salesContractListVO = salesContractService.querySalesContractPageList(param);
        return Result.ok(salesContractListVO);
    }

    /**
     * 根据合同id查询货物信息
     */
    @ApiOperation(value = "根据合同id查询货物信息")
    @GetMapping("/getSalesContractGoods/{contractId}")
    public Result<List<SalesContractGoodsVO>> getSalesContractGoods(@PathVariable String contractId) {
        List<SalesContractGoodsVO> salesContractGoods = salesContractService.getSalesContractGoods(contractId);
        return Result.ok(salesContractGoods);
    }

    /**
     * 分页查询销售合同协议历史记录
     */
    @PostMapping("/getHistoryList")
    public Result<ApiPageResult<SalesContractHistoryListVO>> getHistoryList(@RequestBody SalesContractHistoryListParam param) {
        try {
            ApiPageResult<SalesContractHistoryListVO> result = salesContractHistoryService.getHistoryList(param);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("分页查询销售合同协议历史记录，发生异常信息:", e);
            return Result.error(CommonLang.SYS_FAIL.getCode(), e.getMessage());
        }
    }

    /**
     * 查询合同协议历史记录的详情
     */
    @RequestMapping("/getHistoryDetailById/{recordId}")
    public Result<SalesContractHistoryDetailVO> detail(@PathVariable(name = "recordId", required = true) String recordId) {
        try {
            if (recordId == null) {
                return Result.error(CommonLang.PARAM_VALID_FAIL.getCode(), "id参数不能空");
            }
            return Result.ok(salesContractHistoryService.getHistoryDetailById(recordId));
        } catch (Exception e) {
            log.error("查询合同协议历史记录的详情，发生异常:", e);
            return Result.error(CommonLang.SYS_FAIL.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询最新版本销售合同基本信息")
    @GetMapping("/getContractInfo")
    public Result<SalesContractDetailVO> getContractInfo(@RequestParam("contractCode") String contractCode) {
        if(StringUtils.isBlank(contractCode)){
            return Result.error("合同编号不能为空");
        }
        SalesContractDetailVO salesContractDetailVO = salesContractService.getContractInfo(contractCode);
        return Result.ok(salesContractDetailVO);
    }


    /**
     * 根据nc销售合同id获取销售合同信息
     * @param salesContractId nc销售合同ID
     */
    @GetMapping("/getSalesContractBySalesContractId")
    public SalesContract getSalesContractBySalesContractId(String salesContractId){
        return salesContractService.getSalesContractBySalesContractId(salesContractId);
    }
}