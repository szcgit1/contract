package com.xtm.contract.feign;

import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.motorcade.feign.MotorcadeFeign;
import com.xtm.motorcade.model.vo.DriverVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MotorcadeServiceFeign {

    @Resource
    private MotorcadeFeign motorcadeFeign;

    public DriverVo getDriverById(String driverId) {
        Result<DriverVo> motorcadeResult = motorcadeFeign.getDriverById(driverId);
        log.info("获取司机信息:{}",motorcadeResult);
        if (motorcadeResult.isSuccess()){
            return motorcadeResult.getData();
        }
        throw new BusinessException("获取司机信息失败:"+motorcadeResult.getMessage());
    }
}
