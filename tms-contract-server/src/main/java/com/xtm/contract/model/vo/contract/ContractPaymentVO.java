package com.xtm.contract.model.vo.contract;

import com.xtm.contract.model.vo.contractOther.ContractPaymentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/8/19 22:14
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentVO {
    @ApiModelProperty("合同付款方式")
    List<ContractPaymentInfo> contractPaymentInfos;

    @ApiModelProperty("合计金额")
    private BigDecimal totalPrice;
}
