package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: zt
 * @Desc:  合同
 * @date: 2021/3/14 15:05
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同")
@TableName(value = "contract")
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主鍵")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "合同编号")
    @TableField("CONTRACT_CODE")
    private String contractCode;

    @ApiModelProperty(value = "合同标题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField("VALID_START_DATE")
    private Date validStartDate;

    @ApiModelProperty(value = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField("VALID_END_DATE")
    private Date validEndDate;

    @ApiModelProperty(value = "签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField("CONTRACT_DATE")
    private Date contractDate;

    @ApiModelProperty(value = "交易日期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("TRADE_TIME")
    private Date tradeTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATER")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "合同类型")
    @TableField("CONTRACT_TYPE")
    private Integer contractType;

    @ApiModelProperty(value = "单据ID")
    @TableField("DOCUMENT_ID")
    private String documentId;

    @ApiModelProperty(value = "单据编号")
    @TableField("DOCUMENT_CODE")
    private String documentCode;

    @ApiModelProperty(value = "单据类型")
    @TableField("DOCUMENT_TYPE")
    private Integer documentType;

    @ApiModelProperty(value = "合同（总）金额")
    @TableField("CONTRACT_PRICE")
    private BigDecimal contractPrice;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFIER")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "版本")
    @TableField("VER")
    private Integer ver;

    @ApiModelProperty(value = "托运人公司ID")
    @TableField("TRUSTOR_COMPANY_ID")
    private String trustorCompanyId;

    @ApiModelProperty(value = "托运人公司名称")
    @TableField("TRUSTOR_COMPANY_NAME")
    private String trustorCompanyName;

    @ApiModelProperty(value = "承运人公司ID")
    @TableField("CARRIER_COMPANY_ID")
    private String carrierCompanyId;

    @ApiModelProperty(value = "承运人公司名称")
    @TableField("CARRIER_COMPANY_NAME")
    private String carrierCompanyName;

    @ApiModelProperty(value = "托运方联系人名称")
    @TableField("TRUSTOR_CONTACT_NAME")
    private String trustorContactName;

    @ApiModelProperty(value = "托运方联系人手机号")
    @TableField("TRUSTOR_CONTACT_MOBILE")
    private String trustorContactMobile;

    @ApiModelProperty(value = "承运方联系人名称")
    @TableField("CARRIER_CONTACT_NAME")
    private String carrierContactName;

    @ApiModelProperty(value = "承运方联系人手机号")
    @TableField("CARRIER_CONTACT_MOBILE")
    private String carrierContactMobile;

    @ApiModelProperty(value = "编制方公司ID")
    @TableField("COMPILE_SIDE_ID")
    private String compileSideId;

    @ApiModelProperty(value = "合同模版ID")
    @TableField("CONTRACT_TEMPLATE_ID")
    private String contractTemplateId;

    @ApiModelProperty(value = "合同模版名称")
    @TableField("CONTRACT_TEMPLATE_NAME")
    private String contractTemplateName;

    @ApiModelProperty(value = "是否删除")
    @TableField("IS_DELETE")
    private Integer isDelete;

    @ApiModelProperty(value = "电子合同（未盖章）")
    @TableField("EC_CONTRACT_PATH")
    private String ecContractPath;

    @ApiModelProperty(value = "电子合同（盖章）")
    @TableField("EC_CONTRACT_PDF_ID")
    private String ecContractPdfId;

    @ApiModelProperty(value = "电子合同调用结果编码")
    @TableField("EC_CONTRACT_RESULT_CODE")
    private String ecContractResultCode;

    @ApiModelProperty(value = "电子合同调用结果描述")
    @TableField("EC_CONTRACT_RESULT_DESC")
    private String ecContractResultDesc;

    @ApiModelProperty(value = "电子合同生成时间")
    @TableField("EC_CONTRACT_CREATE_TIME")
    private Date ecContractCreateTime;

    @ApiModelProperty(value = "e签宝合同文件ID")
    @TableField("EC_CONTRACT_ESIGN_FILE_ID")
    private String ecContractEsignFileId;

    @ApiModelProperty(value = "e签宝合同流水ID")
    @TableField("EC_CONTRACT_ESIGN_FLOW_ID")
    private String ecContractEsignFlowId;

    @ApiModelProperty(value = "委托方签署状态")
    @TableField("TRUSTOR_SIGN_STATUS")
    private Integer trustorSignStatus;

    @ApiModelProperty(value = "承运方签署状态")
    @TableField("CARRIER_SIGN_STATUS")
    private Integer carrierSignStatus;

    @ApiModelProperty(value = "合同内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "策略ID")
    @TableField("STRATEGY_ID")
    private String strategyId;

    @ApiModelProperty(value = "策略名称")
    @TableField("STRATEGY_NAME")
    private String strategyName;

    @ApiModelProperty(value = "父合同ID")
    @TableField("PARENT_CONTRACT_ID")
    private String parentContractId;

    @ApiModelProperty(value = "合同货物汇总描述")
    @TableField("CONTRACT_GOODS_COLLECTION")
    private String contractGoodsCollection;

    @ApiModelProperty(value = "合同计量汇总")
    @TableField("CONTRACT_MEASURING_COLLECTION")
    private String contractMeasuringCollection;

    @ApiModelProperty("合同方向:0甲方，1乙方,2丙方")
    private Integer direction;

    @ApiModelProperty(value = "三方合同平台方公司ID")
    @TableField("PLAT_COMPANY_ID")
    private String platCompanyId;

    @ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
    @TableField("CONTRACT_TEMPLATE_BUSINESS_TYPE")
    private Integer contractTemplateBusinessType;


    @ApiModelProperty(value = "代办人公司ID")
    @TableField("AGENT_COMPANY_ID")
    private String agentCompanyId;

    @ApiModelProperty(value = "代办人公司名称")
    @TableField("AGENT_COMPANY_NAME")
    private String agentCompanyName;

    @ApiModelProperty(value = "代办方联系人名称")
    @TableField("AGENT_CONTACT_NAME")
    private String agentContactName;

    @ApiModelProperty(value = "代办方联系人手机号")
    @TableField("AGENT_CONTACT_MOBILE")
    private String agentContactMobile;

    @ApiModelProperty(value = "代办方签署状态")
    @TableField("AGENT_SIGN_STATUS")
    private Integer agentSignStatus;

//    @ApiModelProperty(value = "平台自定义唯一交易号")
//    @TableField("EXTSIGN_AUTO_TRANS_ID")
//    private String extsignAutoTransId;

    @ApiModelProperty(value = "运输方式",notes = "1041000L：汽运；1041030L：海运")
    @TableField("TRANSPORT_MODE")
    private Integer transportMode;

    @ApiModelProperty(value = "载具编码")
    @TableField("VEHICLE_CODE")
    private String vehicleCode;
}