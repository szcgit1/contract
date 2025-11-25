package com.xtm.contract.model.vo.fdd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <h3>tms-service</h3>
 * <p>司机信息</p>
 *
 * @author shenpeng
 * @since 2024-02-20
 **/
@Data
@ApiModel("司机信息")
public class DriverInfoVO {
    @ApiModelProperty("身份证号")
    private String idCard;
}
