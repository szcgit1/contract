package com.xtm.contract.model.energy;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 油品信息列表;
 *
 * @author miaoyouhu
 * @date 2024/4/25 13:50
 */
public class OilProductInfosVo {

    @ApiModelProperty(value = "油品名称")
    private String oilName;

    @ApiModelProperty(value = "共计笔数")
    private int totalStrokeCount;

    @ApiModelProperty(value = "油品类型")
    private String oilType;

    @ApiModelProperty(value = "油品交易列表")
    private List<OilListsVo> oilLists;

    public String getOilName() {
        return oilName;
    }

    public void setOilName(String oilName) {
        this.oilName = oilName;
    }

    public int getTotalStrokeCount() {
        return totalStrokeCount;
    }

    public void setTotalStrokeCount(int totalStrokeCount) {
        this.totalStrokeCount = totalStrokeCount;
    }

    public List<OilListsVo> getOilLists() {
        return oilLists;
    }

    public void setOilLists(List<OilListsVo> oilLists) {
        this.oilLists = oilLists;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }
}