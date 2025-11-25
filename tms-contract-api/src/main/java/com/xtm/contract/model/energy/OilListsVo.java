package com.xtm.contract.model.energy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 油品交易列表;
 *
 * @author miaoyouhu
 * @date 2024/4/25 13:45
 */
@Data
public class OilListsVo {

    @ApiModelProperty(value = "交易时间")
    private Date tradeDate;

    @ApiModelProperty(value = "油品名称")
    private String oilName;

    @ApiModelProperty(value = "油量(L)")
    private double oilMass;

    @ApiModelProperty(value = "不含税金额)")
    private double priceExcludingTax;

    @ApiModelProperty(value = "含税金额)")
    private double taxIncludedAmount;

    @ApiModelProperty(value = "税额)")
    private double taxAmount;

    @ApiModelProperty(value = "备注")
    private String remark;
    /** 高灯能源结算单增加字段开始 **/
    @ApiModelProperty(value = "账单日期")
    private String dailyBillDate;

    @ApiModelProperty(value = "日账单编号")
    private String dailyBillNo;

    @ApiModelProperty(value = "能源单数量")
    private String energyOrderNum;

    @ApiModelProperty(value = "能源品类")
    private String energyType;

}