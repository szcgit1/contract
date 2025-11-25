package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NcSalesContractAddOrUpdateParam {

    @ApiModelProperty(value = "nc销售合同ID")
    private String salesContractId;

    @ApiModelProperty(value = "销售组织名称",required = false)
    private String salesOrganization;


    @ApiModelProperty(value = "销售组织统一社会信用代码",required = false)
    private String salesOrganizationUscc;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称",required = true)
    private String contractName;

    /**
     * 合同编码
     */
    @ApiModelProperty(value = "合同编码",required = true)
    private String contractCode;

    /**
     * 合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    @ApiModelProperty(value = "合同类型", required = true, dataType = "String", example = "ABC")
    private Integer contractType;

    /**
     * 合同签订日期
     */
    @ApiModelProperty(value = "签订合同日期",required = true)
    private LocalDateTime signedTime;

    /**
     * 计划生效日期
     */
    @ApiModelProperty(value = "计划生效日期",required = true)
    private LocalDateTime effectiveTime;

    /**
     * 计划终止日期
     */
    @ApiModelProperty(value = "计划终止日期",required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "业务员",required = false)
    private String salesman;

    @ApiModelProperty(value = "部门")
    private String department;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户")
    private String customerName;

    /**
     * 客户统一社会信用代码
     */
    @ApiModelProperty(value = "客户统一社会信用代码")
    private String customerUscc;

    /**
     * 托盘客户
     */
    @ApiModelProperty(value = "托盘客户")
    private String trayCustomerName;

    /**
     * nc托盘客户id
     */
    @ApiModelProperty(value = "nc托盘客户Id")
    private String ncTrayCustomerId;
    /**
     * 产品线
     */
    @ApiModelProperty(value = "产品线")
    private String productLineCode;

    /**
     * 销售类型 0：锁价 1：协议 2：到港定价3：联合销售 4：锁价协议
     */
    @ApiModelProperty(value = "销售类型")
    private Integer saleType;

    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    @ApiModelProperty(value = "价格类型")
    private Integer priceType;

    @ApiModelProperty(value = "年度协议")
    private String yearAgreement;

    /**
     * 协议月份（会计月）
     */
    @ApiModelProperty(value = "协议月份（会计月）", required = false)
    private LocalDate agreementDate;

    /**
     * 运费承担方 0：自担 1：自提 2：回结
     */
    @ApiModelProperty(value = "运费承担方 0:自担 1:自提 2:回结")
    private Integer bearCostType;

    /**
     * 起运地点
     */
    @ApiModelProperty(value = "起运地点")
    private String departurePlace;

    /**
     * 滞留品 1:是 0:否
     */
    @ApiModelProperty(value = "滞留品 1:是 0:否")
    private Boolean isDetainedGoods;

    /**
     * 业务类型（自定义档案）
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    /**
     * 是否整单计量（自定义档案）0:否 1:是
     */
    @ApiModelProperty(value = "是否整单计量 0:否 1:是")
    private Integer isWholeMeasurement;


    /**
     * 是否海运客户 0:否 1:是
     */
    @ApiModelProperty(value = "是否海运客户 0:否 1:是")
    private Integer isOceanCustomers;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量", required = true)
    private BigDecimal totalNumber;

    /**
     * 价税合计
     */
    @ApiModelProperty(value = "价税合计", required = false)
    private BigDecimal totalPriceTax;

    /**
     * 磅单备注
     */
    @ApiModelProperty(value = "磅单备注")
    private String poundNote;

    /**
     * 0:禁用 1:启用
     */
    @ApiModelProperty(value = "合同状态")
    private Integer contractStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 协议优惠月份
     */
    @ApiModelProperty(value = "协议优惠月份", required = false)
    private LocalDate discountYear;


    /**
     * 是否是两厂采销
     */
    @ApiModelProperty(value = "是否是两厂采销 0:否 1:是")
    private Integer twoFactoriesBus;

    /**
     * 两厂采销编号
     */
    @ApiModelProperty(value = "两厂采销编号")
    private String twoFactoriesBusCode;

    @ApiModelProperty(value = "系统来源 1:NC")
    private Integer systemSource;

    @ApiModelProperty(value = "NC销售合同变更原因")
    private List<NcSalesContractChangeParam> changeList;

    /**
     * 条款
     */
    @ApiModelProperty(value = "条款", required = false)
    private List<NcSalesContractTermsAddParam> salesContractTerms;

    /**
     * 货物
     */
    @ApiModelProperty(value = "货物", required = true)
    private List<NcSalesContractGoodsAddParam> salesContractGoods;
}
