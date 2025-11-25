package com.xtm.contract.model.query.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtm.utils.json.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 临时运力转正式同步重新签署合同请求
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-07-08 17:28
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "TemporaryCapacityReSignReq", description = "临时运力转正式同步重新签署合同请求")
public class TemporaryCapacityReSignReq implements Serializable {

    /**
     * 载具ID
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String vehicleId;

    /**
     * 车牌号(载具号)
     */
    @NotBlank(message = "车牌号(载具号)不能为空")
    @Size(max = 64, message = "车牌号(载具号)长度不能超过64")
    @ApiModelProperty(value = "车牌号(载具号)", notes = "车牌号(载具号)", required = true, dataType = "String", example = "浙A12345", position = 1)
    private String vehicleCode;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @ApiModelProperty(value = "开始时间", notes = "开始时间", dataType = "Date", example = "2025-07-08", position = 2)
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @ApiModelProperty(value = "结束时间", notes = "结束时间", dataType = "Date", example = "2025-07-09", position = 3)
    private Date endTime;


    @Override
    public String toString() {
        return JsonUtils.toJSONString(this);
    }

}
