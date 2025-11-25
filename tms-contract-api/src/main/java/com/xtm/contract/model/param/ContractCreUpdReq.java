package com.xtm.contract.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/3/29 15:33
 * @version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContractCreUpdReq {
    /**
     * 批量联合运单信息
     */
    @ApiModelProperty(value = "批量联合运单信息")
    private ParentContractReq parentContractReq;
    /**
     * 单据ID
     */
    @ApiModelProperty(value = "单据ID",required = true)
    private String documentId;

    /**
     * 单据号
     */
    @ApiModelProperty(value = "单据code",required = true)
    private String documentCode;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型",required = true)
    private Integer contractDocumentType;

    /**
     * 业务类型：双方、三方
     */
    @ApiModelProperty(value = "业务类型")
    private Integer businessType;

    /**
     * 平台公司ID
     */
    @ApiModelProperty(value = "平台公司ID")
    private String platCompanyId;
    /**
     * 合同金额
     */
    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractPrice;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间",example = "2021-03-30 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime tradeTime;

    /**
     * 委托人公司id
     */
    @ApiModelProperty(value = "委托人公司id",required = true)
    private String trustorCompanyID;

    /**
     * 承运人公司id
     */
    @ApiModelProperty(value = "承运人公司id",required = true)
    private String carrierCompanyID;

    /**
     * 承运人公司名称
     */
    @ApiModelProperty(value = "承运人公司名称",required = true)
    private String carrierCompanyName;

    /**
     * 单据信息
     */
    @ApiModelProperty(value = "单据信息",required = true)
    List<Map<String, Object>> documentGoodsInfoList;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required = true)
    private String creater;

    /**
     * 费用科目版本
     */
    @ApiModelProperty("费用科目ID")
    private String chargeSubjectId;

    /**
     * 支付方式版本
     */
    @ApiModelProperty("支付方式版本")
    private Integer paymentScheduleVer;

    /**
     * 新增标识（ture:新增,false:修改）
     */
    @ApiModelProperty("新增标识（ture:新增,false:修改）")
    private boolean createFlag;

    @ApiModelProperty("订单集合（创建运单合同时必填）")
    private List<String> orderIds;

    @ApiModelProperty(value = "托运方联系人姓名",required = true)
    private String trustorContactName;

    @ApiModelProperty(value = "托运方联系人手机号",required = true)
    private String trustorContactMobile;

    @ApiModelProperty(value = "承运方联系人姓名",required = true)
    private String carrierContractName;

    @ApiModelProperty(value = "承运方联系人手机号",required = true)
    private String carrierContractMobile;
    @ApiModelProperty(value = "承运方身份证号")
    private String carrierContractIdCardNo;


    @Builder.Default
    @ApiModelProperty(value = "是否E签宝静默签署", notes = "默认签署")
    private Boolean eqbSignFlg = true;

    @ApiModelProperty(value = "载具编码")
    private String vehicleCode;

    @ApiModelProperty("Session信息，用于调用合同自定义Session")
    @JsonIgnore
    private TmsSessionInfo sessionInfo;
}
