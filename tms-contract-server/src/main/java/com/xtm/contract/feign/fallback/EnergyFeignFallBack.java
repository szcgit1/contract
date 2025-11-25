package com.xtm.contract.feign.fallback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.EnergyFeign;
import com.xtm.contract.model.energy.BalanceDetailRes;
import org.springframework.stereotype.Component;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/30 22:40
 * @desc
 */
@Component
public class EnergyFeignFallBack implements EnergyFeign {

    public static final String ERROR_CODE = "500";

    public static final String ROOER_MESSAGE = "文件服务连接失败";

    @Override
    public Result<BalanceDetailRes> getPayDetailInfolList(Long id) {
        return null;
    }
}
