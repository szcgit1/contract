package com.xtm.contract.model.vo.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xtm.contract.model.domain.ContractGoods;
import com.xtm.contract.model.domain.SettleBills;
import com.xtm.contract.model.vo.contractOther.SupplementContractInfo;
import com.xtm.company.model.vo.CompanyBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同列表VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleBillsInfoQryVO extends SettleBills {
    /**
     * 托运人公司
     */
    @ApiModelProperty("托运人公司")
    private CompanyBO trustorCompany;

    /**
     * 承运人公司
     */
    @ApiModelProperty("承运人公司")
    private CompanyBO carryCompany;
    /**
     * 代办人公司
     */
    @ApiModelProperty("代办人公司")
    private CompanyBO agentCompany;

    /**
     * 平台公司
     */
    @ApiModelProperty("平台公司")
    private CompanyBO platCompany;

    /**
     * 合同货物信息
      */
    @ApiModelProperty("合同货物信息")
    List<ContractGoods> contractGoodsInfos;

    /**
     * 合同费用信息
     */
    @ApiModelProperty("合同费用信息")
    ContractChargeVO contractChargeInfos;

    /**
     * 付款方式
     */
    @ApiModelProperty("付款方式")
    ContractPaymentVO contractPaymentDetail;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date tradeTime;

    /**
     * 合同编制方
     */
    @ApiModelProperty("合同编制方名称")
    private String compileSideCompanyName;

    /**
     * 签章是否禁用 true-可用 false-禁用
     */
    @ApiModelProperty("签章是否禁用")
    private Boolean isEnable;

    /**
     * 补充合同
     */
    @ApiModelProperty("补充合同")
    private List<SupplementContractInfo> arrSupContract;

    /**
     * 原合同编号
     */
    @ApiModelProperty("原合同编号")
    private String parentContractCode;

    /**
     * 合同类型描述
     */
    @ApiModelProperty("合同类型描述")
    private String contractTypeDesc;

    /**
     * 单据类型描述
     */
    @ApiModelProperty("单据类型描述")
    private String documentTypeDesc;

    @ApiModelProperty("发货地址")
    private String listSendAddress;

    @ApiModelProperty("收货地址")
    private String listReceiveAddress;

    @ApiModelProperty("附件数量")
    private int attachNumber;
    @ApiModelProperty(value = "电子合同URL（未盖章）")
    private String ecContractPathUrl;

    @ApiModelProperty(value = "电子合同URL（盖章）")
    private String ecContractPdfUrl;

    @ApiModelProperty("创建人姓名")
    private String createrName;

    @ApiModelProperty("按钮权限")
    private Map<String,String> buttonPermission;

    @ApiModelProperty(value = "策略id",hidden = true)
    private String chargeSubjectId;

    @ApiModelProperty(value = "付款方式版本",hidden = true)
    private Integer paymentTypeVer;

    @ApiModelProperty(value = "业务性质（5021010-双方合同；5021000-三方合同）")
    private Integer businessType;

    @ApiModelProperty(value = "业务性质描述")
    private String businessTypeDesc;

    @ApiModelProperty(value = "身份证号")
    private String cardNo;

    @ApiModelProperty(value = "托运人注册地址")
    private String trustorCompanyRegisteredAddress;

    @ApiModelProperty(value = "合同地址,仅当合同类型为框架合同时候有值")
    private String contractUrl;
}
