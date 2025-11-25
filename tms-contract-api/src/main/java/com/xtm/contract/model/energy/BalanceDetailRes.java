package com.xtm.contract.model.energy;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 能源结算单详情响应对象;
 *
 * @author miaoyouhu
 * @date 2024/4/25 13:53
 */
public class BalanceDetailRes {

    @ApiModelProperty(value = "结算账单基本信息")
    private BalanceBaseInfoVo balanceBaseInfo;

    @ApiModelProperty(value = "油品信息列表")
    private List<OilProductInfosVo> oilProductInfos;

    public BalanceBaseInfoVo getBalanceBaseInfo() {
        return balanceBaseInfo;
    }

    public void setBalanceBaseInfo(BalanceBaseInfoVo balanceBaseInfo) {
        this.balanceBaseInfo = balanceBaseInfo;
    }

    public List<OilProductInfosVo> getOilProductInfos() {
        return oilProductInfos;
    }

    public void setOilProductInfos(List<OilProductInfosVo> oilProductInfos) {
        this.oilProductInfos = oilProductInfos;
    }
}