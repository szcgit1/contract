package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.callback.TmsContractFddElectricSealFeignFallback;
import com.xtm.contract.feign.callback.TmsServiceChargeFeignFallBack;
import com.xtm.contract.model.vo.ContractSignResVo;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "tms-contract", fallback = TmsServiceChargeFeignFallBack.class, path = "apiPlat/tms-contract")
public interface TmsServiceChargeFeign {

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
}


