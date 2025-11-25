package com.xtm.contract.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesContractDetailVO {

    private Long id;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 基础信息-销售组织id
     */
    private String orgId;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称", required = true, dataType = "String", example = "海南中嘉鹏伟实业有限公司")
    private String orgUscc;

    /**
     * 销售组织
     */
    private String orgName;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 物流合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    private Integer contractTypeCode;

    /**
     * 物流合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    private String contractTypeName;

    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signedTime;

    /**
     * 计划生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveTime;

    /**
     * 计划终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 部门
     */
    private String department;

    /**
     * 基础信息-客户id
     */
    private String customerId;

    /**
     * 客户社会统一信用代码
     */
    @ApiModelProperty(value = "客户id", required = true, dataType = "String", example = "123456")
    private String customerUscc;

    /**
     * 客户
     */
    private String customerName;

    /**
     * 托盘客户社会统一信用代码
     */
    private String trayCustomerUscc;

    /**
     * 托盘客户id
     */
    private Long trayCustomerId;

    /**
     * 托盘客户名称
     */
    private String trayCustomerName;

    /**
     * 产品线id
     */
    private Long productLineId;

    /**
     * 基础信息-系统产品线id
     */
    private String productLineCode;

    /**
     * 产品线名称
     */
    private String productLineName;

    /**
     * 物流协议id
     */
    @ApiModelProperty(value = "物流协议编号", required = false, dataType = "String", example = "ABC")
    private Long agreementId;

    /**
     * 年度协议编号
     */
    private String agreementCode;

    /**
     * 销售类型 0：锁价priceTypeName 1：协议 2：到港定价3：联合销售 4：锁价协议
     */
    private Integer saleTypeCode;

    /**
     * 销售类型 0：锁价 1：协议 2：到港定价3：联合销售 4：锁价协议
     */
    @ApiModelProperty(value = "销售类型名称", required = true, dataType = "Integer", example = "0")
    private String saleTypeName;


    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    private Integer priceTypeCode;

    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    @ApiModelProperty(value = "价格类型", required = true, dataType = "String", example = "ABC")
    private String priceTypeName;


    /**
     * 运费承担方 0：自担 1：自提 2：回结
     */
    private Integer bearCostType;

    /**
     * 协议月份（会计月）
     */
    private LocalDate agreementDate;

    /**
     * 业务类型（自定义档案）
     */
    private String businessType;
    /**
     * 起运地点
     */
    private String departurePlace;

    /**
     * 滞留品 1:是 0:否
     */
    private Integer detainedGoods;

    /**
     * 是否整单计量（自定义档案）0:否 1:是
     */
    private Integer wholeMeasurement;

    /**
     * 总数量
     */
    private BigDecimal totalNumber;

    /**
     * 是否海运客户 0:否 1:是
     */
    private Integer oceanCustomers;


    /**
     * 价税合计
     */
    private String totalPriceTax;

    /**
     * 协议优惠月份
     */
    private LocalDate discountYear;

    /**
     * 磅单备注
     */
    private String poundNote;

    /**
     * 备注
     */
    private String remark;


    /**
     * 是否是两厂采销 0:否 1:是
     */
    @ApiModelProperty(value = "是否是两厂采销 0:否 1:是")
    private Integer twoFactoryTrade;

    /**
     * 两厂采销编号
     */
    @ApiModelProperty(value = "两厂采销编号")
    private String twoFactoryTradeCode;

    @ApiModelProperty("停用标记 0: 未停用 1: 已停用")
    private Boolean disabled;
    /**
     * 条款
     */
    @ApiModelProperty(value = "条款")
    private List<SalesContractTermsVO> salesContractTerms;

    /**
     * 货物
     */
    @ApiModelProperty(value = "货物")
    private List<SalesContractGoodsVO> salesContractGoods;
}
