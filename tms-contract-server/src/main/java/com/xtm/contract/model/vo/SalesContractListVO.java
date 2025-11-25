package com.xtm.contract.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xtm.contract.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class SalesContractListVO {

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 停用标记 0: 未停用 1: 已停用
     */
    private Integer disabled;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 基础信息-销售组织id
     */
    private String orgId;

    /**
     * 销售组织
     */
    private String orgName;

    /**
     * 销售合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    private Integer contractType;

    /**
     * 销售合同类型 0：锁价基准合同 1：协议基准合同 2：钢品销售合同 3：非钢产品销售合同4：其他合同类型
     */
    private String contractTypeName;

    /**
     * 基础信息-客户id
     */
    private String customerId;

    /**
     * 客户
     */
    private String customerName;

    /**
     * 托盘客户id
     */
    private String trayCustomerId;

    /**
     * 托盘客户名称
     */
    private String trayCustomerName;

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

    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    private Integer priceType;

    /**
     * 价格类型 0：价证协议 1：锁价 2：合同
     */
    private String priceTypeName;

    /**
     * 年度协议id
     */
    private String agreementId;

    /**
     * 年度协议编号
     */
    private String agreementCode;

    /**
     * 协议月份（会计月）
     */
    @JsonFormat(pattern = "yyyy-MM")
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
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalNumber;

    /**
     * 价税合计
     */
    private String totalPriceTax;

    /**
     * 磅单备注
     */
    private String poundNote;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    private String contractVersion;

    /**
     * 累计订单主数量
     */
    private String mainOrdersQuantity;

    /**
     * 系统来源 0:丰南 1:中铁 2:中重 98:NC 99:本平台
     */
    private Integer busiSource;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 系统来源 0:TMS 1:nc
     */
    private Integer systemSource;
}
