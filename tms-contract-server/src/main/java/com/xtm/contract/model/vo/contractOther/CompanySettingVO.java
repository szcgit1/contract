package com.xtm.contract.model.vo.contractOther;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("公司设置")
public class CompanySettingVO {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "报价关闭时间")
    private Integer quotationClosingTime;

    @ApiModelProperty(value = "托运人手机显示")
    private Integer trustorMobileShow;

    @ApiModelProperty(value = "放弃投标设置")
    private Integer abandonTenderSetting;

    @ApiModelProperty(value = "调整结算价格")
    private Integer adjustSettlePrice;

    @ApiModelProperty(value = "调整结算价状态节点")
    private Integer adjustSettlePriceStatusPoint;

    @ApiModelProperty(value = "合理损耗")
    private BigDecimal reasonableLoss;

    @ApiModelProperty(value = "平台名称")
    private String platName;

    @ApiModelProperty(value = "货源转发")
    private Integer goodsSubcontract;

    @ApiModelProperty(value = "延迟时间")
    private Integer delayTime;

    @ApiModelProperty(value = "开票订单通知还是全部通知")
    private Integer invoiceOrderNotify;

    @ApiModelProperty(value = "平台资金确认方式")
    private Integer capitalVerificationMethod;

    @ApiModelProperty(value = "强制定向")
    private Integer forceOpt;

    @ApiModelProperty(value = "评价节点")
    private Integer ratingNode;

    @ApiModelProperty(value = "评价次数")
    private Integer ratingTimes;

    @ApiModelProperty(value = "用户注册")
    private Integer loginEnable;

    @ApiModelProperty(value = "报价设置")
    private Integer certificationQuotation;

    @ApiModelProperty(value = "最低价自动委托")
    private Integer minpriceAutoDelegate;

    @ApiModelProperty(value = "竞价截止时间")
    private Integer quotationDisplay;

    @ApiModelProperty(value = "货运发车限定")
    private Integer ecFenceLoading;

    @ApiModelProperty(value = "货运发车km")
    private BigDecimal ecFenceLoadingKm;

    @ApiModelProperty(value = "货物签收是否限定")
    private Integer ecFenceSign;

    @ApiModelProperty(value = "货物签收KM")
    private BigDecimal ecFenceSignKm;

    @ApiModelProperty(value = "到达预报")
    private BigDecimal ecFenceArrForecastKm;

    @ApiModelProperty(value = "发布主体")
    private Integer otherPubSubject;

    @ApiModelProperty(value = "下单主体")
    private Integer otherOrderSubject;

    @ApiModelProperty(value = "未中标")
    private Integer failBid;

    @ApiModelProperty(value = "公司平台LOGO文件ID")
    private String platLogoFileId;

    @ApiModelProperty(value = "回单日期设置")
    private Integer receiptDateSetting;

    @ApiModelProperty(value = "电子路单上报设置")
    private Integer shippingNoteSwitch;

    @ApiModelProperty(value = "资金流水上报设置")
    private Integer capitalSeqSwitch;

    @ApiModelProperty(value = "车辆调度设置")
    private Integer vehicleDispatchSetting;

    @ApiModelProperty(value = "货物损坏")
    private Integer cargoDamage;

    @ApiModelProperty(value = "监管数据")
    private Integer regulatoryData;

    @ApiModelProperty(value = "完整订单导入")
    private Integer wholeOrderImport;

    @ApiModelProperty(value = "双因子登陆")
    private Integer loginType;

    @ApiModelProperty(value = "是否必须认证")
    private Integer loginNeedVerify;

    @ApiModelProperty(value = "运费代收开关")
    private Integer freightSettleSwitch;

    @ApiModelProperty(value = "订单自动余量完结百分比")
    private BigDecimal orderAutoRemainCompletionPercent;

    @ApiModelProperty(value = "自动发送")
    private Integer autoSend;

    @ApiModelProperty(value = "装运与签收量默认为空")
    private Integer loadReceiptSetting;

    @ApiModelProperty(value = "是否开启人脸识别")
    private Integer checkDriverFace;

    @ApiModelProperty(value = "银行卡校验")
    private Integer checkBankNo;

    @ApiModelProperty(value = "晚于计划出发时间范围")
    private BigDecimal planDepartureDateScope;

    @ApiModelProperty(value = "是否限制计划出发时间")
    private Integer isPlanDepartureDate;

    @ApiModelProperty(value = "晚于计划到达时间范围")
    private BigDecimal planArrivalDateScope;

    @ApiModelProperty(value = "是否限制计划到达时间")
    private Integer isPlanArrivalDate;

    @ApiModelProperty(value = "损耗量范围")
    private BigDecimal lossAmountScope;

    @ApiModelProperty(value = "是否限制损耗量")
    private Integer isLossAmount;

    @ApiModelProperty(value = "运力公开")
    private Integer transportCapacityOpen;

    @ApiModelProperty(value = "超吨开关")
    private Integer overTon;

    @ApiModelProperty(value = "运力共享开关")
    private Integer transportCapacityShare;

    @ApiModelProperty(value = "主题")
    private String theme;

    @ApiModelProperty(value = "装货和签收地核查范围")
    private BigDecimal departureArrivalAddrScope;

    @ApiModelProperty(value = "是否装货和签收地核查")
    private Integer isDepartureArrivalAddr;

    @ApiModelProperty(value = "全局默认运费抹零数位")
    private Integer freightRoundDownDigit;

    @ApiModelProperty(value = "默认运费结算对象类型")
    private Integer waybillSettlementTargetType;

    @ApiModelProperty(value = "默认结算重量类型")
    private Integer settlementMeasuringType;

    @ApiModelProperty(value = "业务审核类型")
    private Integer busiAuditType;

    @ApiModelProperty(value = "财务审核类型")
    private Integer financeAuditType;

    @ApiModelProperty(value = "路线承运方审核开关")
    private Integer carrierAudit;

    @ApiModelProperty(value = "是否更新过异常原因")
    private Integer isAbnormalReason;

    @ApiModelProperty(value = "申请发票")
    private Integer isInvoiceRequest;

    @ApiModelProperty(value = "最大打印数")
    private Integer maxPrintCount;

    @ApiModelProperty(value = "查询调度状态")
    private String queryDispatchStatus;

    @ApiModelProperty(value = "驱动dbt数打开")
    private Integer driverDbtNumsOpen;

    @ApiModelProperty(value = "驱动dbt数量限制")
    private Integer driverDbtNumsLimit;

    @ApiModelProperty(value = "代理公司id")
    private String companyAgentId;

    @ApiModelProperty(value = "是否启动自动申请运输发票")
    private Integer autoEtc;

    @ApiModelProperty(value = "收发货地址显示")
    private Integer showAddress;

    @ApiModelProperty(value = "定位装置")
    private String locationDevice;

    @ApiModelProperty(value = "委运审核设置")
    private Integer entrustAudit;

    @ApiModelProperty(value = "转发布下沉价格")
    private BigDecimal downPricePercent;

    @ApiModelProperty(value = "向上报价上浮价格")
    private BigDecimal upPricePercent;

    @ApiModelProperty(value = "装运签收图片")
    private Integer shippingSign;

    @ApiModelProperty(value = "货源自动审核")
    private Integer automaticSourceAuditSetting;

    @ApiModelProperty(value = "运费支付方式")
    private Integer payTargetTypeSetting;

    @ApiModelProperty(value = "企业意愿签署 1：意愿签；0：静默签")
    private int companyIntendSign;

    @ApiModelProperty(value = "司机意愿签署 1：意愿签；0：静默签")
    private int driverIntendSign;

    @ApiModelProperty("版本")
    private Integer ver;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date modifyTime;

}