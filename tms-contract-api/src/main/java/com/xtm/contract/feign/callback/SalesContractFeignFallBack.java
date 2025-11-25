package com.xtm.contract.feign.callback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.SalesContractFeign;
import com.xtm.contract.model.vo.SalesContractDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SalesContractFeignFallBack implements SalesContractFeign {
    @Override
    public Result<SalesContractDetailVO> getContractInfo(String contractCode) {
        log.info("合同服务请求失败,请稍后再试:{}", contractCode);
        return Result.error("合同服务请求失败,请稍后再试");
    }
}
