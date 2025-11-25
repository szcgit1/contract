package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.ContractSignVo
 * @author: wwh
 * @create: 2025-03-28 14:19
 * @description: 正常合同签署通用类定义
 **/
@Data
@ApiModel("合同签署通用类定义")
public class ContractSignVo<T> implements Serializable {
    private static final long serialVersionUID = 4123945504563171422L;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String businessId;
    /**
     * 业务编码
     */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;
    /**
     * 甲方id（承运人id）
     */
    @ApiModelProperty(value = "甲方id")
    private String firstPartyId;
    /**
     * 甲方签章位置
     */
    @ApiModelProperty(value = "甲方签章位置")
    private String firstSignLocation;

    /**
     * 甲方签章方式：0-自动签章，1-手动签章,默认0自动签章
     */
    @ApiModelProperty(value = "甲方签章方式：0-自动签章，1-手动签章,默认0自动签章")
    private Integer firstSignType = 0;
    /**
     * 甲方签章模式：0-公司，1-个人，默认公司
     */
    @ApiModelProperty(value = "甲方签章模式：0-公司，1-个人，默认公司")
    private Integer firstSignModel = 0;
    /**
     * 乙方id（服务商id）
     */
    @ApiModelProperty(value = "乙方id（服务商id）")
    private String secondPartyId;
    /**
     * 乙方签章位置
     */
    @ApiModelProperty(value = "乙方签章位置")
    private String secondSignLocation;
    /**
     * 乙方签章方式：0-自动签章，1-手动签章,默认0自动签章
     */
    @ApiModelProperty(value = "乙方签章方式：0-自动签章，1-手动签章,默认0自动签章")
    private Integer secondSignType = 0;
    /**
     * 乙方签章模式：0-公司，1-个人，默认公司
     */
    @ApiModelProperty(value = "乙方签章模式：0-公司，1-个人，默认公司")
    private Integer secondSignModel = 0;
    /**
     * 签章类型 1:找车，2：卸车，3-技术服务费，4-结算单
     */
    @ApiModelProperty(value = "签章类型 1:找车，2：卸车，3-技术服务费，4-结算单")
    private Integer signType = 1;
    /**
     * 服务项目
     */
    @ApiModelProperty(value = "服务项目")
    private String serviceProject;
    /**
     * 签署参数
     */
    @ApiModelProperty(value = "签署参数")
    private T signParam;

}
