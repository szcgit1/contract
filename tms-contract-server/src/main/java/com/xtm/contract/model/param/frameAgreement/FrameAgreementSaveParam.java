package com.xtm.contract.model.param.frameAgreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 框架合同协议表保存入参
 */
@Data
public class FrameAgreementSaveParam implements Serializable {

    private static final long serialVersionUID = 2710789183615131439L;

    /**
     * 合同协议id
     */
    private Long id;

    /**
     * 合同协议名称
     */
    @Length(message = "合同名称上限为{max}个字，下限为{min}个字",min = 1,max = 100)
    @ApiModelProperty(value = "合同协议名称", required = false, dataType = "String", example = "合同协议名称01")
    private String name;

    /**
     * 合同协议编号
     */
    @NotBlank(message = "合同编号不能为空")
    @Length(message = "合同编号上限为{max}个字，下限为{min}个字",min = 1,max = 50)
    @ApiModelProperty(value = "合同协议编号", required = true, dataType = "String", example = "FNZHNDXY-25001-XN")
    private String code;

    /**
     * 发运组织
     */
    @NotBlank(message = "发运组织不能为空")
    @ApiModelProperty(value = "发运组织", required = true, dataType = "String", example = "江苏省纸联再生资源有限公司")
    private String shipping;

    /**
     * 发运组织统一社会信用代码
     */
    @NotBlank(message = "发运组织统一社会信用代码不能为空")
    @ApiModelProperty(value = "发运组织统一社会信用代码", required = true, dataType = "String", example = "91320100660840419W")
    private String shippingUscc;

    /**
     * 发运组织统一主键
     */
    @ApiModelProperty(value = "发运组织统一主键", required = true, dataType = "String", example = "0001A1100000000088BC")
    private String shippingMain;

    /**
     * 客户
     */
    @NotBlank(message = "客户不能为空")
    @ApiModelProperty(value = "客户", required = true, dataType = "String", example = "灵石县华美煤化有限责任公司")
    private String customer;

    /**
     * 客户统一社会信用代码
     */
    @NotBlank(message = "客户统一社会信用代码不能为空")
    @ApiModelProperty(value = "客户统一社会信用代码", required = true, dataType = "String", example = "911407297540837892")
    private String customerUscc;

    /**
     * 销售组织
     */
    @NotBlank(message = "销售组织不能为空")
    @ApiModelProperty(value = "销售组织", required = true, dataType = "String", example = "海南中嘉鹏伟实业有限公司")
    private String saleOrg;

    /**
     * 销售组织统一社会信用代码
     */
    @NotBlank(message = "销售组织统一社会信用代码不能为空")
    @ApiModelProperty(value = "销售组织统一社会信用代码", required = true, dataType = "String", example = "91460000MABREYYR81")
    private String saleOrgUscc;

    /**
     * 年份
     */
    @NotNull(message = "年份不能为空")
    @ApiModelProperty(value = "年份", required = true, dataType = "String", example = "2025")
    private String year;

    /**
     * 总协议量
     */
    @ApiModelProperty(value = "总协议量", required = true, dataType = "BigDecimal", example = "5")
    private BigDecimal totalVolume;

    /**
     * 产品线编号id
     */
    @ApiModelProperty(value = "产品线编号id", required = true, dataType = "String", example = "1958795282251112450")
    private Long productLineId;

    /**
     * 产品线编号
     */
    @ApiModelProperty(value = "产品线编号", required = true, dataType = "String", example = "202508150004")
    private String productLineCode;

    /**
     * 产品线名称
     */
    @ApiModelProperty(value = "产品线名称", required = true, dataType = "String", example = "NC修改产品线0004")
    private String productLineName;

    /**
     * 生效日期
     */
    @NotNull(message = "生效日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(value = "生效日期", required = true, dataType = "LocalDateTime", example = "2025-09-01 01:00:00")
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    @NotNull(message = "失效日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(value = "失效日期", required = true, dataType = "LocalDateTime", example = "2025-09-10 01:00:00")
    private LocalDateTime expiryDate;

    /**
     * 虚拟年度协议标识 0：否 1：是
     */
    @ApiModelProperty(value = "虚拟年度协议标识", required = false, dataType = "Integer", example = "0")
    private Integer virtualTag;

    /**
     * 虚拟协议绑定的合同协议id  合同协议 1:N 虚拟协议
     */
    @ApiModelProperty(value = "虚拟协议绑定的合同协议id", required = false, dataType = "Long", example = "")
    private Long parentId;

    /**
     * 协议号
     */
    @Length(message = "协议号上限为{max}个字，下限为{min}个字",min = 1,max = 50)
    @ApiModelProperty(value = "协议号", required = false, dataType = "String", example = "合同协议名称01")
    private String num;

    /**
     * 备注
     */
    @Length(message = "备注上限为{max}个字，下限为{min}个字",min = 0,max = 2000)
    @ApiModelProperty(value = "备注", required = false, dataType = "String", example = "备注信息")
    private String remark;

    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(value = "单据日期", required = true, dataType = "LocalDateTime", example = "2025-09-10")
    private Date billDate;

}
