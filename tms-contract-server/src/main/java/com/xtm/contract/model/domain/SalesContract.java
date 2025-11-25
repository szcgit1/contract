package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xtm.v1.common.model.TransitionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *@Description: 物流合同表
 */
@TableName(value ="sales_contract")
@Data
public class SalesContract extends TransitionEntity<SalesContract> {

    /**
     * 基础信息-销售组织id
     */
    private String salesOrgId;

    /**
     * 销售组织
     */
    private String salesOrgName;

    @ApiModelProperty(value = "销售组织统一社会信用代码",required = false)
    private String salesOrgUscc;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 物流合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    private Integer contractType;

    /**
     * 合同签订日期
     */
    private LocalDateTime signedTime;

    /**
     * 计划生效日期
     */
    private LocalDateTime effectiveTime;

    /**
     * 计划终止日期
     */
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
    private String customerUscc;

    /**
     * 客户
     */
    private String customerId;

    /**
     * 客户
     */
    private String customerName;

    /**
     * 托盘客户id
     */
    private Long trayCustomerId;

    /**
     * 托盘客户
     */
    private String trayCustomerName;

    /**
     * 托盘客户id
     */
    private String ncTrayCustomerId;

    /**
     * 基础信息-系统产品线id
     */
    private Long productLineId;

    /**
     * 基础信息-系统产品线id
     */
    private String productLineCode;

    /**
     * 产品线
     */
    private String productLineName;

    /**
     * 销售类型 0：锁价 1：协议 2：到港定价3：联合销售 4：锁价协议
     */
    private Integer saleType;

    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    private Integer priceType;

    /**
     * 关联年度协议编码
     */
    private String frameAgreementCode;

    /**
     * nc年度协议编码
     */
    private String ncAgreementCode;

    /**
     * 协议月份（会计月）
     */
    private LocalDate agreementDate;

    /**
     * 运费承担方 0：自担 1：自提 2：回结
     */
    private Integer bearCostType;

    /**
     * 起运地点
     */
    private String departurePlace;

    /**
     * 滞留品 1:是 0:否
     */
    private Integer detainedGoods;

    /**
     * 业务类型（自定义档案）
     */
    private String businessType;

    /**
     * 是否整单计量（自定义档案）0:否 1:是
     */
    private Integer wholeMeasurement;

    /**
     * 是否海运客户 0:否 1:是
     */
    private Integer oceanCustomers;

    /**
     * 总数量
     */
    private BigDecimal totalNumber;

    /**
     * 价税合计
     */
    private BigDecimal totalPriceTax;

    /**
     * 磅单备注
     */
    private String poundNote;

    /**
     * 系统来源 0:TMS 1:NC
     */
    private Integer systemSource;

    /**
     * 备注
     */
    private String remark;

    /**
     * 协议优惠月份
     */
    private LocalDate discountYear;

    /**
     * 是否是两厂采销 0:否 1:是
     */
    private Integer twoFactoryTrade;

    /**
     * 两厂采销编号
     */
    private String twoFactoryTradeCode;


    /**
     * nc销售合同主键
     */
    private String salesContractId;

    /**
     * nc推送的版本号
     */
    private Integer contractVersion;

    /**
     * 物流合同协议id
     */
    private Long frameAgreementId;

    /**
     * 关联月份
     */
    private String reMonth;

    /**
     * 备注
     */
    private String reRemark;

    /**
     * 关联合并状态 0：未合并 1：已合并
     */
    private Boolean reMergeState;

    /**
     * 系统来源 0:丰南 1:中铁 2:中重 99:本平台
     */
    private Integer busiSource;

    @ApiModelProperty(value = "变更人")
    private String changePerson;

    @ApiModelProperty(value = "变更日期")
    private LocalDateTime changeTime;

    @ApiModelProperty(value = "变更原因")
    private String changeReason;

    @ApiModelProperty(value = "备注")
    private String changeRemark;

    @ApiModelProperty(value = "nc版本号")
    private Integer version;

    @TableField(exist = false)
    private List<SalesContractGoods> contractGoods;
}
