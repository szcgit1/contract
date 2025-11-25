package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/26 16:15
 * @desc 合同费用表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "contract_charge")
public class ContractCharge {
    @ApiModelProperty("主鍵")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "合同ID")
    @TableField("CONTRACT_ID")
    private String contractId;

    @ApiModelProperty(value = "计费科目版本")
    @TableField("CHARGE_SUBJECT_ID")
    private String chargeSubjectId;

    @ApiModelProperty(value = "付款方式版本")
    @TableField("PAYMENT_SCHEDULE_VERSION")
    private Integer paymentScheduleVersion;

/*    @ApiModelProperty(value = "结算对象ID")
    @TableField("SETTLE_SUBJECT_ID")
    private String settleSubjectId;

    @ApiModelProperty(value = "结算对象名称")
    @TableField("SETTLE_SUBJECT_NAME")
    private String settleSubjectName;*/

    @ApiModelProperty(value = "合同版本")
    @TableField("VER")
    private Integer ver;
}
