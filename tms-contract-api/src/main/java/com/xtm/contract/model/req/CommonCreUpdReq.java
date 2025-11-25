package com.xtm.contract.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xtm.contract.model.energy.BalanceDetailRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/3/29 15:33
 * @version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonCreUpdReq
{
    /**
     * 单据ID
     */
    @ApiModelProperty(value = "单据ID",required = true)
    private String documentId;

    /**
     * 单据号
     */
    @ApiModelProperty(value = "单据code",required = true)
    private String documentCode;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型",required = true)
    private Integer contractDocumentType;

    /**
     * 业务类型：双方、三方
     */
    @ApiModelProperty(value = "业务类型")
    private Integer businessType;

    /**
     * 平台公司ID
     */
    @ApiModelProperty(value = "平台公司ID")
    private String platCompanyId;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间",example = "2021-03-30 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date tradeTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required = true)
    private String creater;

    /**
     * 新增标识（ture:新增,false:修改）
     */
    @ApiModelProperty("新增标识（ture:新增,false:修改）")
    private boolean createFlag;

    @Builder.Default
    @ApiModelProperty(value = "是否E签宝静默签署", notes = "默认签署")
    private Boolean eqbSignFlg = true;

    @ApiModelProperty(value = "供应商公司ID", notes = "法大大认证的公司ID")
    private String supplierCompanyId;

    @ApiModelProperty(value = "供应商公司IDCardNo", notes = "法大大认证的公司ID")
    private String supplierCompanyIdCardNo;

    @ApiModelProperty(value = "能源结算单详情对象", notes = "能源结算单详情对象")
    private BalanceDetailRes balanceDetailRes;
}
