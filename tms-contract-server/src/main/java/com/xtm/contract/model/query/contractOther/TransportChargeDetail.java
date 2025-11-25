package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class TransportChargeDetail {
    @ApiModelProperty("合同金额信息")
    private TransportCharge contractTransportCharge;
    @ApiModelProperty("合同金额内容列表")
    private List<TransportChargeItem> contractTransportChargeItemList;

    @ApiModelProperty("结算金额信息")
    private TransportCharge settleTransportCharge;
    @ApiModelProperty("结算金额内容列表")
    private List<TransportChargeItem> settleTransportChargeItemList;
}
