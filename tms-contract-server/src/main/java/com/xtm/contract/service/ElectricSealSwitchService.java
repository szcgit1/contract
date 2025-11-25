package com.xtm.contract.service;

import com.xtm.thirdparty.auth.model.resp.*;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-27  18:44
 *@Description:
 *@title: ElectricSealSwitchService
 */
public interface ElectricSealSwitchService {

    /**
     * 查询签章开关标识（0：e签宝；1：法大大）
     * @return
     */
    ElectricSealResponse querySignSwitchTag();
}
