package com.xtm.contract.feign.callback;

import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.TmsServiceChargeFeign;
import com.xtm.contract.model.vo.ContractSignResVo;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TmsServiceChargeFeignFallBack implements TmsServiceChargeFeign {
    @Override
    public Result<ContractSignResVo> serviceEcSign(ContractSignVo<?> contractSignVo) {
        log.error("电子签章服务调用失败");
        return Result.error("电子签章服务调用失败,请稍后再试");
    }

    @Override
    public Result<ContractSignResVo> energyEcSign(ContractSignVo<?> contractSignVo) {
        log.error("能源单电子签章服务调用失败");
        return Result.error("能源单电子签章服务调用失败,请稍后再试");
    }

    @Override
    public Result<FindCarContractResVo> checkSignStatus(String contractId, String companyId) {
        log.error("查询合同签署状态失败，请稍后再试");
        return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同签署状态失败，请稍后再试");
    }
}
