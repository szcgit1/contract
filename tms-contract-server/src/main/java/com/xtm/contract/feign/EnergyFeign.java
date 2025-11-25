package com.xtm.contract.feign;

import com.xtm.contract.config.FeignConfig;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.fallback.EnergyFeignFallBack;
import com.xtm.contract.model.energy.BalanceDetailRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "energy", configuration = FeignConfig.class, fallback = EnergyFeignFallBack.class,path = "/apiPlat/energy")
public interface EnergyFeign {

    /**
     * 通过结算单ID，查询订单对应的结算单数据;
     *
     * @param id
     * @return
     */
    @PostMapping("/balance/detail/{id}")
    Result<BalanceDetailRes> getPayDetailInfolList(@PathVariable("id") Long id);


    /**
     * 法大大签章回调地址;
     *
     * @param param
     * @return
     */
//    @PostMapping("/energy/audit/fadadaCallback")
//    Result<?> fadadaCallback(@RequestBody FadadaCallbackReq param);


}
