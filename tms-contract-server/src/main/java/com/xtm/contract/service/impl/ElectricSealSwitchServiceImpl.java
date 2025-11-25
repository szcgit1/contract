package com.xtm.contract.service.impl;

import com.xtm.common.exception.*;
import com.xtm.common.model.*;
import com.xtm.contract.service.*;
import com.xtm.thirdparty.auth.feign.*;
import com.xtm.thirdparty.auth.model.resp.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-27 13:44
 *@Description: 电子签章开关表接口实现类
 *@title: ElectricSealSwitchServiceImpl
 */
@Slf4j
@Service(value = "ElectricSealSwitchService")
public class ElectricSealSwitchServiceImpl implements ElectricSealSwitchService {

    @Autowired
    private ElectricSealSwitchFeign electricSealSwitchFeign;

    /**
     * mp修复
     */
    public static final String SQL_FIXED = " LIMIT 1";

    /**
     * 查询签章开关标识（0：e签宝；1：法大大）
     * @return
     */
    @Override
    public ElectricSealResponse querySignSwitchTag() {
        // 查询签章开关标识
        log.info("开始查询签章开关标识");
        Result<ElectricSealResponse> electricSealResponseResult = electricSealSwitchFeign.querySignSwitchTag();
        log.info("查询签章开关标识结果:{}",electricSealResponseResult);
        if(electricSealResponseResult.isSuccess()){
            return electricSealResponseResult.getData();
        }
        log.error("查询签章开关标识失败:{}",electricSealResponseResult.getMessage());
        throw new BusinessException("查询签章开关标识失败");
    }
}
