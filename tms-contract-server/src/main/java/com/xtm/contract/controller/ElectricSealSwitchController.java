package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.service.ElectricSealSwitchService;
import com.xtm.thirdparty.auth.model.resp.ElectricSealResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-27  13:36
 *@Description: 电子签章开关表相关接口
 *@title: ElectricSealSwitchController
 */
@Slf4j
@RestController
@RequestMapping(value = "/electricSealSwitch")
@Api(tags = "电子签章开关表相关接口")
public class ElectricSealSwitchController {

    @Autowired
    private ElectricSealSwitchService electricSealSwitchService;

    @PostMapping(value = "/querySignSwitchTag")
    @ApiOperation(value = "查询签章开关标识")
    public Result  querySignSwitchTag() throws Exception {
        ElectricSealResponse electricSealSwitch = electricSealSwitchService.querySignSwitchTag();
        return Result.of(electricSealSwitch, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

}
