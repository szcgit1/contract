package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 数据迁移信息表
 * </p>
 *
 * @author khj
 * @since 2024-04-15
 */
@Data
@TableName("contract_migrate")
@Builder
@ApiModel(value = "ContractMigrate对象", description = "合同(已迁移表)")
public class ContractMigrate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty("合同号")
    @TableField("contract_code")
    private String contractCode;

    @ApiModelProperty("单据id")
    @TableField("document_id")
    private String documentId;

    @ApiModelProperty("表后缀")
    @TableField("table_suffix")
    private String tableSuffix;

    @ApiModelProperty("数据日期")
    @TableField("data_date")
    private LocalDate dataDate;

    @ApiModelProperty(value = "父合同ID")
    @TableField(value = "parent_contract_id")
    private String parentContractId;




}




