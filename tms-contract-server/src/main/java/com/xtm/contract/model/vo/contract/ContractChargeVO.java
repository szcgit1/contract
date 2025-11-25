package com.xtm.contract.model.vo.contract;

import com.xtm.contract.model.vo.contractOther.ContractChargeSubjectsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/8/19 17:24
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractChargeVO {
    @ApiModelProperty("合同科目详细信息")
    List<ContractChargeSubjectsInfo> chargeSubjectsInfos;

    @ApiModelProperty("结算金额")
    private BigDecimal contractTotalPrice;

    @ApiModelProperty("结算金额")
    private BigDecimal settleTotalPrice;
}
