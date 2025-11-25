package com.xtm.contract.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-01  21:03
 *@Description:
 *@title: ContractParam
 */
@Data
@ApiModel(value = "承运合同",description = "")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主鍵")
    private String id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同标题")
    private String title;

    @ApiModelProperty(value = "合同内容")
    private String content;

    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date validStartDate;

    @ApiModelProperty(value = "截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date validEndDate;

    @ApiModelProperty(value = "签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date contractDate;

    @ApiModelProperty(value = "交易日期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date tradeTime;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    @ApiModelProperty(value = "stampPhotoId")
    private Long stampPhotoId;

    @ApiModelProperty(value = "合同类型")
    private Integer contractType;

    @ApiModelProperty(value = "isOrder")
    private Integer isOrder;

    @ApiModelProperty(value = "dispatchBatchId")
    private Long dispatchBatchId;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    @ApiModelProperty(value = "版本")
    private Integer ver;

    @ApiModelProperty(value = "托运人公司ID")
    private String trustorCompanyId;

    @ApiModelProperty(value = "托运人公司名称")
    private String trustorCompanyName;

    @ApiModelProperty(value = "承运人公司ID")
    private String carrierCompanyId;

    @ApiModelProperty(value = "承运人公司名称")
    private String carrierCompanyName;

    @ApiModelProperty(value = "托运方联系人名称")
    private String trustorContactName;

    @ApiModelProperty(value = "托运方联系人手机号")
    private String trustorContactMobile;

    @ApiModelProperty(value = "承运方联系人名称")
    private String carrierContactName;

    @ApiModelProperty(value = "承运方联系人手机号")
    private String carrierContactMobile;

    @ApiModelProperty(value = "合同模版ID")
    private String contractTemplateId;

    @ApiModelProperty(value = "合同模版名称")
    private String contractTemplateName;

    @ApiModelProperty(value = "编制方公司ID")
    private String compileSideId;

    @ApiModelProperty(value = "策略ID")
    private String strategyId;

    @ApiModelProperty(value = "策略名称")
    private String strategyName;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

    @ApiModelProperty(value = "电子合同（未盖章）")
    private String ecContractPath;

    @ApiModelProperty(value = "电子合同（盖章）")
    private String ecContractPdfId;

    @ApiModelProperty(value = "电子合同调用结果编码")
    private String ecContractResultCode;

    @ApiModelProperty(value = "电子合同调用结果描述")
    private String ecContractResultDesc;

    @ApiModelProperty(value = "电子合同生成时间")
    private Date ecContractCreateTime;

    @ApiModelProperty(value = "e签宝合同文件ID")
    private String ecContractEsignFileId;

    @ApiModelProperty(value = "e签宝合同流水ID")
    private String ecContractEsignFlowId;

    @ApiModelProperty(value = "委托方签署状态")
    private Integer trustorSignStatus;

    @ApiModelProperty(value = "合同计量汇总")
    private String contractMeasuringCollection;

    @ApiModelProperty(value = "合同货物汇总描述")
    private String contractGoodsCollection;

    @ApiModelProperty(value = "承运方签署状态")
    private Integer carrierSignStatus;

    @ApiModelProperty(value = "父合同ID")
    private String parentContractId;

    @ApiModelProperty(value = "单据ID")
    private String documentId;

    @ApiModelProperty(value = "单据ID集合")
    private List<String> documentIds;

    @ApiModelProperty(value = "单据编号")
    private String documentCode;

    @ApiModelProperty(value = "单据类型")
    private Integer documentType;

    @ApiModelProperty(value = "合同（总）金额")
    private BigDecimal contractPrice;

    @ApiModelProperty("合同方向:0甲方，1乙方,2丙方")
    private Integer direction;

    @ApiModelProperty(value = "三方合同平台方公司ID")
    private String platCompanyId;

    @ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
    private Integer contractTemplateBusinessType;

    @ApiModelProperty(value = "代办人公司ID")
    private String agentCompanyId;

    @ApiModelProperty(value = "代办人公司名称")
    private String agentCompanyName;

    @ApiModelProperty(value = "代办方联系人名称")
    private String agentContactName;

    @ApiModelProperty(value = "代办方联系人手机号")
    private String agentContactMobile;

    @ApiModelProperty(value = "代办方签署状态")
    private Integer agentSignStatus;

    @ApiModelProperty(value = "运输方式",notes = "1041000L：汽运；1041030L：海运")
    private Integer transportMode;

    @ApiModelProperty(value = "载具编码")
    private String vehicleCode;


}
