package com.xtm.contract.model.vo.contractOther;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 17:26
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentInfo {

    @ApiModelProperty("付款方式")
    private Integer paymentMode;

    @ApiModelProperty("付款方式描述")
    private String paymentModeDesc;

    @ApiModelProperty("支付方式")
    private Integer voucherType;

    @ApiModelProperty("支付方式描述")
    private String voucherTypeDesc;

    @ApiModelProperty("金额")
    private BigDecimal money ;

    @ApiModelProperty("账单日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date settleDate;

    @ApiModelProperty(value = "付款比例(%)",name = "priceRatio")
    private BigDecimal priceRatio;

    @ApiModelProperty("版本")
    private Integer ver;
}
