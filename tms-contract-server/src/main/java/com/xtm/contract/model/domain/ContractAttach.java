package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("contract_attach")
public class ContractAttach implements Serializable {
    @TableId("ID")
    private String id;

    @ApiModelProperty("合同ID")
    @TableField("CONTRACT_ID")
    private String contractId;

    @ApiModelProperty("文件ID")
    @TableField("FILE_ID")
    private String fileId;

    @ApiModelProperty("是否删除")
    @TableField("IS_DELETE")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty("创建人")
    @TableField("CREATER")
    private String creater;

    @ApiModelProperty("版本")
    @TableField("VER")
    private Integer ver;

    @ApiModelProperty("修改人")
    @TableField("MODIFIER")
    private String modifier;

    @ApiModelProperty("修改时间")
    @TableField("MODIFY_TIME")
    private Date modifyTime;
}