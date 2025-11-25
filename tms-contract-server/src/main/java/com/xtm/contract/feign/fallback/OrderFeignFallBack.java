package com.xtm.contract.feign.fallback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.OrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class OrderFeignFallBack implements OrderFeign {

    @Override
    public Result<Boolean> isTransferUnload(String dispatchBatchId) {
        return null;
    }

    @Override
    public Result<Void> saveContract(List<String> dispatchBatchIds) {
        log.error("====> 运单重新生成合同 - 发生异常 - 传入参数: {} <====", dispatchBatchIds);
        return null;
    }

}
