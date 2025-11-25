package com.xtm.contract.feign;

import com.xtm.contract.config.FeignConfig;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.fallback.OrderFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "base-tms", configuration = FeignConfig.class, fallback = OrderFeignFallBack.class, path = "apiPlat/tms")
public interface OrderFeign {

    /**
     * 判断运单是否由中转卸货来源
     * @param dispatchBatchId
     * @return
     */
    @GetMapping("/tempOrder/dispatchBatch/isTransferUnload/{dispatchBatchId}")
    Result<Boolean> isTransferUnload(@PathVariable("dispatchBatchId") String dispatchBatchId);

    /**
     * 运单重新生成合同
     *
     * @param dispatchBatchIds 运单ID
     * @return {@link Void}
     */
    @PostMapping("/tempOrder/dispatch/saveContract")
    Result<Void> saveContract(@RequestBody List<String> dispatchBatchIds);

}
