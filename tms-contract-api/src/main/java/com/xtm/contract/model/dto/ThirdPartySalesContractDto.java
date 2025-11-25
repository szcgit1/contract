package com.xtm.contract.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xtm.contract.constant.TmsContractConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 三方销售合同DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-02 19:00
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ThirdPartySalesContractDto", description = "三方销售合同DTO")
public class ThirdPartySalesContractDto extends BaseDto {
    
    /**
     * 销售组织
     */
    @Size(max = 20, message = "销售组织长度不能超过20")
    @ApiModelProperty(value = "销售组织", dataType = "String", example = "河北纵横集团丰南钢铁有限公司", position = 1)
    private String salesOrganization;
    
    /**
     * 销售组织统一社会信用代码
     */
    @Size(max = 32, message = "销售组织统一社会信用代码长度不能超过32")
    @ApiModelProperty(value = "销售组织统一社会信用代码", dataType = "String", example = "91370900MA3MANC9BB", position = 2)
    private String salesOrganizationUscc;
    
    /**
     * 合同编码
     */
    @NotBlank(message = "合同编码不能为空")
    @Size(max = 50, message = "合同编码长度不能超过50")
    @ApiModelProperty(value = "合同编码", required = true, dataType = "String", example = "FNGTFNGTRZSJ-2502012", position = 3)
    private String contractCode;
    
    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不能为空")
    @Size(max = 50, message = "合同名称长度不能超过50")
    @ApiModelProperty(value = "合同名称", required = true, dataType = "String", example = "工业产品购销合同", position = 4)
    private String contractName;
    
    /**
     * 合同类型
     * <br>
     * 0：锁价基准合同
     * <br>
     * 1：协议基准合同
     * <br>
     * 2：钢品销售合同
     * <br>
     * 3：非钢产品销售合同
     * <br>
     * 4：其他合同类型
     */
    @NotNull(message = "合同类型不能为空")
    @ApiModelProperty(value = "合同类型", notes = "0：锁价基准合同1：协议基准合同2：钢品销售合同3：非钢产品销售合同4：其他合同类型", required = true, dataType = "Integer", example = "1", position = 5)
    private Integer contractType;
    
    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "合同签订日期不能为空")
    @ApiModelProperty(value = "合同签订日期", required = true, dataType = "LocalDateTime", example = "2025-08-20 23:59:59", position = 6)
    private LocalDateTime signedTime;
    
    /**
     * 计划生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "计划生效日期不能为空")
    @ApiModelProperty(value = "计划生效日期", required = true, dataType = "LocalDateTime", example = "2025-08-21 23:59:59", position = 7)
    private LocalDateTime effectiveTime;
    
    /**
     * 计划终止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "计划终止日期不能为空")
    @ApiModelProperty(value = "计划终止日期", required = true, dataType = "LocalDateTime", example = "2025-08-22 23:59:59", position = 8)
    private LocalDateTime endTime;
    
    /**
     * 业务员
     */
    @NotBlank(message = "业务员不能为空")
    @Size(max = 20, message = "业务员长度不能超过20")
    @ApiModelProperty(value = "业务员", required = true, dataType = "String", example = "文义祥", position = 9)
    private String salesman;
    
    /**
     * 部门
     */
    @NotBlank(message = "部门不能为空")
    @Size(max = 20, message = "部门长度不能超过20")
    @ApiModelProperty(value = "部门", required = true, dataType = "String", example = "营销部", position = 10)
    private String department;
    
    /**
     * 客户
     */
    @Size(max = 20, message = "客户长度不能超过20")
    @ApiModelProperty(value = "客户", dataType = "String", example = "天津茂高实业有限公司", position = 11)
    private String customerName;
    
    /**
     * 客户统一社会信用代码
     */
    @Size(max = 32, message = "客户统一社会信用代码长度不能超过32")
    @ApiModelProperty(value = "客户统一社会信用代码", dataType = "String", example = "91370900MA3MANC9BB", position = 12)
    private String customerUscc;
    
    /**
     * 托盘客户
     */
    @Size(max = 20, message = "托盘客户长度不能超过20")
    @ApiModelProperty(value = "托盘客户", dataType = "String", example = "System-99", position = 13)
    private String trayCustomerName;
    
    /**
     * NC托盘客户信息ID
     */
    @Size(max = 32, message = "NC托盘客户信息ID长度不能超过32")
    @ApiModelProperty(value = "NC托盘客户信息ID", dataType = "String", example = "519", position = 14)
    private String ncTrayCustomerId;
    
    /**
     * 产品线编码
     */
    @Size(max = 64, message = "产品线编码长度不能超过64")
    @ApiModelProperty(value = "产品线编码", dataType = "String", example = "CP909", position = 15)
    private String productLineCode;
    
    /**
     * 销售类型
     * <br>
     * 0：锁价
     * <br>
     * 1：协议
     * <br>
     * 2：到港定价
     * <br>
     * 3：联合销售
     * <br>
     * 4：锁价协议
     */
    @NotNull(message = "销售类型不能为空")
    @ApiModelProperty(value = "销售类型", notes = "0：锁价1：协议2：到港定价3：联合销售4：锁价协议", required = true, dataType = "Integer", example = "1", position = 16)
    private Integer saleType;
    
    /**
     * 价格类型
     * <br>
     * 0：价证协议
     * <br>
     * 1：基价
     * <br>
     * 2：合同
     */
    @NotNull(message = "价格类型不能为空")
    @ApiModelProperty(value = "价格类型", notes = "0：价证协议1：基价2：合同", required = true, dataType = "Integer", example = "2", position = 17)
    private Integer priceType;
    
    /**
     * 年度协议编号
     */
    @Size(max = 20, message = "年度协议编号长度不能超过20")
    @ApiModelProperty(value = "年度协议编号", dataType = "String", example = "NDXY20250123106", position = 18)
    private String yearAgreement;
    
    /**
     * 协议月份
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "协议月份不能为空")
    @ApiModelProperty(value = "协议月份", required = true, dataType = "LocalDate", example = "2025-08-20", position = 19)
    private LocalDate agreementDate;
    
    /**
     * 运费承担方
     * <br>
     * 0：自担：贸易公司/基地
     * <br>
     * 1：自提：客户
     * <br>
     * 2：回结：三方代收代付
     */
    @NotNull(message = "运费承担方不能为空")
    @ApiModelProperty(value = "运费承担方", notes = "0：自担：贸易公司/基地1：自提：客户2：回结：三方代收代付", required = true, dataType = "Integer", example = "0", position = 20)
    private Integer bearCostType;
    
    /**
     * 起运地点
     */
    @NotBlank(message = "起运地点不能为空")
    @Size(max = 20, message = "起运地点长度不能超过20")
    @ApiModelProperty(value = "起运地点", required = true, dataType = "String", example = "丰南区", position = 21)
    private String departurePlace;
    
    /**
     * 是否为滞留品
     */
    @NotNull(message = "是否为滞留品不能为空")
    @ApiModelProperty(value = "是否为滞留品", required = true, dataType = "Boolean", example = "false", position = 22)
    private Boolean isDetainedGoods;
    
    /**
     * 业务类型
     */
    @Size(max = 20, message = "业务类型长度不能超过20")
    @ApiModelProperty(value = "业务类型", dataType = "String", example = "热轧产品", position = 23)
    private String businessType;
    
    /**
     * 是否整单计量
     */
    @ApiModelProperty(value = "是否整单计量", notes = "0：是1：否", dataType = "Integer", example = "0", position = 24)
    private Integer isWholeMeasurement;
    
    /**
     * 是否海运客户
     */
    @ApiModelProperty(value = "是否整单计量", notes = "0：是1：否", dataType = "Integer", example = "1", position = 25)
    private Integer isOceanCustomers;
    
    /**
     * 总数量
     */
    @NotNull(message = "总数量不能为空")
    @PositiveOrZero(message = "总数量必须为正数或者0")
    @ApiModelProperty(value = "总数量", required = true, dataType = "BigDecimal", example = "1000", position = 26)
    private BigDecimal totalNumber;
    
    /**
     * 价税合计
     */
    @NotNull(message = "价税合计不能为空")
    @PositiveOrZero(message = "价税合计必须为正数或者0")
    @ApiModelProperty(value = "价税合计", required = true, dataType = "BigDecimal", example = "3440000", position = 27)
    private BigDecimal totalPriceTax;
    
    /**
     * 备注XXX
     */
    @Size(max = 120, message = "磅单备注长度不能超过120")
    @ApiModelProperty(value = "磅单备注", dataType = "String", example = "备注XXX", position = 28)
    private String poundNote;
    
    /**
     * 备注
     */
    @Size(max = 2000, message = "备注长度不能超过2000")
    @ApiModelProperty(value = "备注", dataType = "String", example = "备注YYY", position = 30)
    private String remark;
    
    /**
     * 协议优惠月份
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd")
    @ApiModelProperty(value = "协议优惠月份", dataType = "LocalDate", example = "2025-08-20", position = 31)
    private LocalDate discountYear;
    
    /**
     * 是否两厂采销
     * <br>
     * 0: 否
     * <br>
     * 1: 是
     */
    @NotNull(message = "是否两厂采销不能为空")
    @ApiModelProperty(value = "是否两厂采销", notes = "0: 否1: 是", required = true, dataType = "Integer", example = "1", position = 32)
    private Integer twoFactoriesBus;
    
    /**
     * 两厂采销编号
     */
    @Size(max = 32, message = "两厂采销编号长度不能超过32")
    @ApiModelProperty(value = "两厂采销编号", dataType = "String", example = "816", position = 33)
    private String twoFactoriesBusCode;
    
    /**
     * 销售合同ID
     */
    @NotBlank(message = "销售合同ID不能为空")
    @Size(max = 20, message = "销售合同ID长度不能超过20")
    @ApiModelProperty(value = "销售合同ID", required = true, dataType = "String", example = "114514", position = 34)
    private String salesContractId;
    
    /**
     * 系统来源
     * <br>
     * 0: TMS
     * <br>
     * 1: NC
     * <br>
     * 2: 新网
     * <br>
     *
     * @see TmsContractConstant.ThirdPartySystemSource
     */
    @NotNull(message = "系统来源不能为空")
    @ApiModelProperty(value = "系统来源", notes = "0: TMS 1: NC 2: 新网", required = true, dataType = "Integer", example = "1", position = 35)
    private TmsContractConstant.ThirdPartySystemSource systemSource;
    
    /**
     * 销售合同变更明细
     */
    @Valid
    @ApiModelProperty(value = "销售合同变更明细", dataType = "List", position = 36)
    private List<ContractChange> changes;
    
    /**
     * 合同货物
     */
    @Valid
    @NotEmpty(message = "合同货物不能为空")
    @ApiModelProperty(value = "合同货物", required = true, position = 37)
    private List<ContractGood> contractGoods;
    
    /**
     * 合同条款
     */
    @Valid
    @ApiModelProperty(value = "合同条款", dataType = "List", position = 38)
    private List<ContractTerm> contractTerms;
    
    /**
     * 接口日志功能 - 人工更新（重新推送）
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Boolean manualUpdate;
    
    public Boolean getManualUpdate() {
        return Boolean.TRUE.equals(this.manualUpdate);
    }
    
    
    /**
     * 合同货物明细
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ApiModel(value = "ContractGood", description = "合同货物明细")
    public static class ContractGood extends BaseDto {
        
        /**
         * 合同货物ID
         */
        @NotBlank(message = "合同货物ID不能为空")
        @Size(max = 32, message = "合同货物ID长度不能超过32")
        @ApiModelProperty(value = "合同货物ID", required = true, dataType = "String", example = "1145141919", position = 1)
        private String contractGoodsId;
        
        /**
         * 发运公司
         */
        @Size(max = 20, message = "发运公司长度不能超过20")
        @ApiModelProperty(value = "发运公司", dataType = "String", example = "河北纵横集团丰南钢铁有限公司", position = 2)
        private String shippingCompany;
        
        /**
         * 发运公司ID
         */
        @Size(max = 32, message = "发运公司ID长度不能超过32")
        @ApiModelProperty(value = "发运公司ID", dataType = "String", example = "1145141919519", position = 3)
        private String shippingCompanyId;
        
        /**
         * 发运公司统一社会信用代码
         */
        @Size(max = 32, message = "发运公司统一社会信用代码长度不能超过32}")
        @ApiModelProperty(value = "发运公司统一社会信用代码", dataType = "String", example = "91370900MA3MANC9BB", position = 4)
        private String shippingCompanyUscc;
        
        /**
         * 物料分类
         */
        @Size(max = 20, message = "物料分类长度不能超过20")
        @ApiModelProperty(value = "物料分类", dataType = "String", example = "热轧卷板", position = 5)
        private String materialClassification;
        
        /**
         * 基准材质
         */
        @Size(max = 20, message = "基准材质长度不能超过20")
        @ApiModelProperty(value = "基准材质", dataType = "String", example = "SPHC", position = 6)
        private String referenceMaterial;
        
        /**
         * 物料
         */
        @Size(max = 20, message = "物料长度不能超过20")
        @ApiModelProperty(value = "物料", dataType = "String", example = "高达尼姆合金", position = 7)
        private String material;
        
        /**
         * 材质
         */
        @Size(max = 20, message = "材质长度不能超过20")
        @ApiModelProperty(value = "材质", dataType = "String", example = "金属", position = 8)
        private String texture;
        
        /**
         * 产品线编码
         */
        @Size(max = 64, message = "产品线编码长度不能超过64")
        @ApiModelProperty(value = "产品线编码", dataType = "String", example = "CP999", position = 9)
        private String productLineCode;
        
        /**
         * 质量等级
         */
        @Size(max = 20, message = "质量等级长度不能超过20")
        @ApiModelProperty(value = "质量等级", dataType = "String", example = "SSS", position = 10)
        private String qualityGrade;
        
        /**
         * 数量
         */
        @PositiveOrZero(message = "数量必须为正数或者0")
        @ApiModelProperty(value = "数量", dataType = "BigDecimal", example = "1000", position = 11)
        private BigDecimal quantity;
        
        /**
         * 主数量
         */
        @PositiveOrZero(message = "主数量必须为正数或者0")
        @ApiModelProperty(value = "主数量", dataType = "BigDecimal", example = "1000", position = 12)
        private BigDecimal mainQuantity;
        
        /**
         * 主单位
         */
        @Size(max = 20, message = "主单位长度不能超过20")
        @ApiModelProperty(value = "主单位", dataType = "String", example = "吨", position = 13)
        private String mainUnit;
        
        /**
         * 换算率
         */
        @Size(max = 20, message = "换算率长度不能超过20")
        @ApiModelProperty(value = "换算率", dataType = "String", example = "1.0000/1.0000", position = 14)
        private String conversionRate;
        
        /**
         * 基价
         */
        @PositiveOrZero(message = "基价必须为正数或者0")
        @ApiModelProperty(value = "基价",  dataType = "BigDecimal", example = "3440", position = 15)
        private BigDecimal basePrice;
        
        /**
         * 收货地点
         */
        @Size(max = 20, message = "收货地点长度不能超过20")
        @ApiModelProperty(value = "收货地点", dataType = "String", example = "博兴（船运）", position = 16)
        private String receivingLocation;
        
        /**
         * 码头
         */
        @Size(max = 20, message = "码头长度不能超过20")
        @ApiModelProperty(value = "码头", dataType = "String", example = "曹妃甸", position = 17)
        private String wharf;
        
        /**
         * 物料规格
         */
        @Size(max = 50, message = "物料规格长度不能超过50")
        @ApiModelProperty(value = "物料规格", dataType = "String", example = "3.0*1010-1540", position = 18)
        private String materialSpecification;
        
        /**
         * 主含税单价
         */
        @PositiveOrZero(message = "主含税单价必须为正数或者0")
        @ApiModelProperty(value = "主含税单价", dataType = "BigDecimal", example = "3440", position = 19)
        private BigDecimal mainTaxUnitPrice;
        
        /**
         * 价税合计
         */
        @PositiveOrZero(message = "价税合计必须为正数或者0")
        @ApiModelProperty(value = "价税合计", dataType = "BigDecimal", example = "340000", position = 20)
        private BigDecimal totalPriceTax;
        
        /**
         * 收货国家/地区
         */
        @Size(max = 20, message = "收货国家/地区长度不能超过20")
        @ApiModelProperty(value = "收货国家/地区", dataType = "String", example = "中国", position = 21)
        private String receivingCountry;
        
        /**
         * 税码
         */
        @Size(max = 20, message = "税码长度不能超过20")
        @ApiModelProperty(value = "税码", dataType = "String", example = "13", position = 22)
        private String taxCode;
        
        /**
         * 价格组成
         */
        @Size(max = 50, message = "价格组成长度不能超过50")
        @ApiModelProperty(value = "价格组成", dataType = "String", example = "[基价:3440.00000000,运输加价:0.00000000]", position = 23)
        private String priceComposition;
        
        /**
         * 备注
         */
        @Size(max = 181, message = "备注长度不能超过181")
        @ApiModelProperty(value = "备注", dataType = "String", example = "备注ZZZ", position = 24)
        private String remark;
        
        /**
         * 累计订单主数量
         */
        @PositiveOrZero(message = "累计订单主数量必须为正数或者0")
        @ApiModelProperty(value = "累计订单主数量", dataType = "BigDecimal", example = "999.5", position = 25)
        private BigDecimal mainOrdersQuantity;
        
        /**
         * 行号
         */
        @NotBlank(message = "行号不能为空")
        @Size(max = 20, message = "行号长度不能超过20")
        @ApiModelProperty(value = "行号", required = true, dataType = "String", example = "10", position = 26)
        private String lineNumber;
        
    }
    
    
    /**
     * 合同条款明细
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ApiModel(value = "ContractTerm", description = "合同条款明细")
    public static class ContractTerm extends BaseDto {
        
        /**
         * 条款编号
         */
        @Size(max = 20, message = "条款编号长度不能超过20")
        @ApiModelProperty(value = "条款编号", dataType = "String", example = "SO5502", position = 1)
        private String termCode;
        
        /**
         * 条款名称
         */
        @Size(max = 500, message = "条款名称长度不能超过500")
        @ApiModelProperty(value = "条款名称", required = true, dataType = "String", example = "三、运输方式及费用负担", position = 2)
        private String termName;
        
        /**
         * 条款内容
         */
        @Size(max = 3000, message = "条款内容长度不能超过500")
        @ApiModelProperty(value = "条款内容", required = true, dataType = "String", example = "正品锁基价合同回结-有地点", position = 3)
        private String termContent;
        
    }
    
    
    /**
     * 销售合同变更明细
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ApiModel(value = "ContractChange", description = "销售合同变更明细")
    public static class ContractChange extends BaseDto {
        
        /**
         * 版本号
         */
        @NotNull(message = "版本号不能为空")
        @ApiModelProperty(value = "版本号", dataType = "Integer", example = "2", position = 1)
        private Integer version;
        
        /**
         * 变更人
         */
        @Size(max = 20, message = "变更人长度不能超过20")
        @ApiModelProperty(value = "变更人", dataType = "String", example = "赵日天", position = 2)
        private String changePerson;
        
        /**
         * 变更日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @ApiModelProperty(value = "变更日期", dataType = "LocalDateTime", example = "2025-11-11 10:00:15", position = 3)
        private LocalDateTime changeTime;
        
        /**
         * 变更原因
         */
        @Size(max = 300, message = "变更原因长度不能超过300")
        @ApiModelProperty(value = "变更原因", dataType = "String", example = "变更金额", position = 4)
        private String changeReason;
        
        /**
         * 变更备注
         */
        @Size(max = 180, message = "变更备注长度不能超过180")
        @ApiModelProperty(value = "变更备注", dataType = "String", example = "无", position = 5)
        private String changeRemark;
        
    }
    
}
