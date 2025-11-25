package com.xtm.contract.model.vo.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.ContractGoodsInfoVo
 * @author: wwh
 * @create: 2025-03-05 14:51
 * @description:
 **/
@Data
public class ContractDispatchBatchVo implements Serializable {
    private static final long serialVersionUID = 4852314286347102397L;
    @ApiModelProperty("运单编号")
    private String dispatchBatchCode;
    @ApiModelProperty("运单创建时间")
    private String dispatchBatchCreateTime;
    @ApiModelProperty("合同单价")
    private BigDecimal contractUnitPrice;
    @ApiModelProperty("合同金额")
    private BigDecimal contractPrice;
    @ApiModelProperty("调度单货物信息")
    private List<DispatchGoodsInfoVo> dispatchGoodsInfoVos;

}
