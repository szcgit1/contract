package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.callback.SalesContractFeignFallBack;
import com.xtm.contract.model.vo.SalesContractDetailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "tms-contract", fallback = SalesContractFeignFallBack.class, path = "apiPlat/tms-contract")
public interface SalesContractFeign {

    @ApiOperation(value = "查询最新版本物流合同基本信息")
    @GetMapping("/salesContract/getContractInfo")
    Result<SalesContractDetailVO> getContractInfo(@RequestParam("contractCode") String contractCode);
}
