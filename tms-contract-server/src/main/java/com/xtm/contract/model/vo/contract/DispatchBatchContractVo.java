package com.xtm.contract.model.vo.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.DispatchBatchContractVo
 * @author: wwh
 * @create: 2025-03-05 14:37
 * @description:
 **/
@Data
public class DispatchBatchContractVo implements Serializable {
    private static final long serialVersionUID = -99768235993021087L;

    @ApiModelProperty("批量联合运单号")
    private String dispatchBatchContractNo;

    @ApiModelProperty("批量联合运单创建时间")
    private String dispatchBatchContractTime;
    @ApiModelProperty("运单数量")
    private Integer dispatchBatchNum;
    @ApiModelProperty("调度单数量")
    private Integer dispatchNum;
    @ApiModelProperty("车牌号")
    private String vehicleCode;
    @ApiModelProperty("运单货物信息")
    private List<ContractDispatchBatchVo> contractGoodsInfoVos;

}
