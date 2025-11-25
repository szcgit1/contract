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
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 三方框架合同DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-08-29 15:33
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ThirdPartyFrameworkContractDto", description = "三方框架合同DTO")
public class ThirdPartyFrameworkContractDto extends BaseDto {
    
    /**
     * 合同协议编号
     */
    @NotBlank(message = "合同协议编号不能为空")
    @Size(max = 50, message = "合同协议编号长度不能超过50")
    @ApiModelProperty(value = "合同协议编号", required = true, dataType = "String", example = "FNZHNDXY-25001-XN", position = 1)
    private String code;
    
    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "单据日期不能为空")
    @ApiModelProperty(value = "单据日期", required = true, dataType = "LocalDateTime", example = "2025-04-23 23:59:59", position = 2)
    private LocalDateTime createTime;
    
    /**
     * 发运组织
     */
    @NotBlank(message = "发运组织不能为空")
    @Size(max = 32, message = "发运组织长度不能超过32")
    @ApiModelProperty(value = "发运组织", required = true, dataType = "String", example = "河北纵横集团丰南钢铁有限公司", position = 3)
    private String shipping;
    
    /**
     * 发运组织统一社会信用代码
     */
    @Size(max = 32, message = "发运组织统一社会信用代码长度不能超过32")
    @ApiModelProperty(value = "发运组织统一社会信用代码", dataType = "String", example = "91370900MA3MANC9XJ", position = 4)
    private String shippingUscc;
    
    /**
     * 发运组织统一主键
     */
    @NotBlank(message = "发运组织统一主键不能为空")
    @Size(max = 32, message = "发运组织统一主键长度不能超过32")
    @ApiModelProperty(value = "发运组织统一主键", required = true, dataType = "String", example = "0001A1100000000088BC", position = 5)
    private String shippingMain;
    
    /**
     * 客户
     */
    @NotBlank(message = "客户不能为空")
    @Size(max = 64, message = "客户长度不能超过64")
    @ApiModelProperty(value = "客户", required = true, dataType = "String", example = "厦门供应链有限公司", position = 6)
    private String customer;
    
    /**
     * 客户统一社会信用代码
     */
    @Size(max = 32, message = "客户统一社会信用代码不能超过32")
    @ApiModelProperty(value = "客户统一社会信用代码", dataType = "String", example = "91370900MA3MANC9AA", position = 7)
    private String customerUscc;
    
    /**
     * 产品线编码
     */
    @NotBlank(message = "产品线编码不能为空")
    @Size(max = 64, message = "产品线编码长度不能超过64")
    @ApiModelProperty(value = "产品线编码", required = true, dataType = "String", example = "1780", position = 8)
    private String productLineCode;
    
    /**
     * 年份
     */
    @NotBlank(message = "年份不能为空")
    @Size(max = 32, message = "年份长度不能超过32")
    @ApiModelProperty(value = "年份", required = true, dataType = "String", example = "2025", position = 9)
    private String year;
    
    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "生效日期不能为空")
    @ApiModelProperty(value = "生效日期", required = true, dataType = "LocalDateTime", example = "2025-05-26 23:59:59", position = 10)
    private LocalDateTime effectiveDate;
    
    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "失效日期不能为空")
    @ApiModelProperty(value = "失效日期", required = true, dataType = "LocalDateTime", example = "2025-05-26 23:59:59", position = 11)
    private LocalDateTime expiryDate;
    
    /**
     * 总协议量
     */
    @NotNull(message = "总协议量不能为空")
    @ApiModelProperty(value = "总协议量", required = true, dataType = "BigDecimal", example = "360000.00", position = 12)
    private BigDecimal totalVolume;
    
    /**
     * 审批状态
     * <br>
     * 0：审批通过
     * <br>
     * 1：禁用
     */
    @ApiModelProperty(value = "审批状态", required = true, dataType = "Integer", example = "0", position = 13, hidden = true)
    private Integer disabled;
    
    /**
     * 是否最新
     */
    @ApiModelProperty(value = "是否最新", dataType = "Boolean", example = "false", position = 14)
    private Boolean latest;
    
    /**
     * 是否加急
     */
    @ApiModelProperty(value = "是否加急", notes = "‘紧急’，‘普通’，‘其它’", dataType = "String", example = "普通", position = 15)
    private String expedited;
    
    /**
     * 虚拟年度协议标识
     */
    @NotNull(message = "虚拟年度协议标识不能为空")
    @ApiModelProperty(value = "虚拟年度协议标识", required = true, dataType = "Boolean", example = "false", position = 16)
    private Boolean virtualTag;
    
    /**
     * 组织名称
     */
    @NotNull(message = "组织名称不能为空")
    @Size(max = 64, message = "组织名称长度不能超过64")
    @ApiModelProperty(value = "组织名称", required = true, dataType = "String", example = "河北纵横集团丰南钢铁有限公司", position = 17)
    private String org;
    
    /**
     * 组织名称统一社会信用代码
     */
    @Size(max = 32, message = "组织名称统一社会信用代码长度不能超过32")
    @ApiModelProperty(value = "组织名称统一社会信用代码", dataType = "String", example = "91370900MA3MANC9BB", position = 18)
    private String orgUscc;
    
    /**
     * 备注
     */
    @Size(max = 2000, message = "备注长度不能超过2000")
    @ApiModelProperty(value = "备注", dataType = "String", example = "备注XXX", position = 19)
    private String remark;
    
    /**
     * 协议号
     */
    @NotBlank(message = "协议号不能为空")
    @Size(max = 64, message = "协议号长度不能超过64")
    @ApiModelProperty(value = "协议号", required = true, dataType = "String", example = "GAT-X105", position = 20)
    private String num;
    
    /**
     * 主表主键
     */
    @NotBlank(message = "主表主键不能为空")
    @Size(max = 32, message = "主表主键长度不能超过32")
    @ApiModelProperty(value = "主表主键", required = true, dataType = "String", example = "1957720294147764224", position = 21)
    private String mainId;
    
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
    @ApiModelProperty(value = "系统来源", required = true, dataType = "Integer", example = "1", position = 22)
    private TmsContractConstant.ThirdPartySystemSource systemSource;
    
    /**
     * 相关合同
     */
    @Valid
    @NotEmpty(message = "相关合同不能为空")
    @ApiModelProperty(value = "相关合同", required = true, position = 23)
    private List<RelatedContract> relatedContracts;
    
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
     * 相关合同明细
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ApiModel(value = "RelatedContract", description = "相关合同明细")
    public static class RelatedContract extends BaseDto {
        
        /**
         * 月份
         */
        @NotBlank(message = "月份不能为空")
        @Size(max = 32, message = "月份长度不能超过32")
        @ApiModelProperty(value = "月份", required = true, dataType = "String", example = "2025-06", position = 1)
        private String month;
        
        /**
         * 数量
         */
        @NotNull(message = "数量不能为空")
        @ApiModelProperty(value = "数量", required = true, dataType = "BigDecimal", example = "30000.00", position = 2)
        private BigDecimal amount;
        
        /**
         * 合并状态
         * <br>
         * 0：未合并
         * <br>
         * 1：合并
         */
        @NotNull(message = "合并状态不能为空")
        @ApiModelProperty(value = "合并状态", required = true, dataType = "Integer", example = "0", position = 3)
        private Integer mergeState;
        
        /**
         * 备注
         */
        @Size(max = 2000, message = "备注长度不能超过2000")
        @ApiModelProperty(value = "备注", dataType = "String", example = "备注YYY", position = 4)
        private String remark;
        
        /**
         * 子表主键
         */
        @NotBlank(message = "子表主键不能为空")
        @Size(max = 32, message = "子表主键长度不能超过32")
        @ApiModelProperty(value = "子表主键", required = true, dataType = "String", example = "1957720294147764225", position = 5)
        private String subId;
        
    }
    
}
