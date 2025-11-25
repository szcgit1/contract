package com.xtm.contract.enums;

/**
 * 文件业务来源
 */
public enum FileBizSourceEnum {

    QRCODE("qrcode",  "运营-用户二维码"),
    COMPANY_DRIVER_EXPORT("companyDriverExport",  "司机（船员）列表导出"),
    MEMBER_EXPORT("memberExport",  "(托运人承运人收发货单位)导出"),
    ELECTRONIC_RECEIPT("electronicReceipt",  "上传银行电子回单"),
    RECORD_EXPORT("recordExport",  "资金流水导出"),
    OPERATE_CAPITAL_ACCOUNT_EXPORT("operateCapitalAccountExport",  "运营下资金账户列表导出"),
    UPLOAD_ERROR("uploadError",  "上传错误excel到文件服务器"),
    EXPORT_TRUCKS("exportTrucks",  "运输队下车辆导出"),
    EXPORT_HIS_STOP_INFO("exportHisStopInfo",  "载具经停点查询导出"),
    ORDER_IMPORT("orderImport",  "订单导入"),
    ORDER_EXPORT("orderExport",  "订单导出"),
    COMMON_IMPORT("commonImport",  "通用导入"),
    UPDATE_FDD_PDF_ID("updateFddPdfId",  "法大大合同签章"),
    EQB_PDF("eqbPdf",  "E签宝文件下载"),
    CONTRACT("contract",  "生成合同"),
    ROUTE_QRCODE("routeQrCode",  "路线二维码"),
    DISPATCH_SHIPMENT("dispatchShipment",  "装运发车上传水印图片"),
    DISPATCH_SIGN("dispatchSign",  "运单签收上传水印图片"),
    TRANSFER_SIGN("transferSign",  "执行二程卸货调度单中转协助签收上传水印图片"),
    BD("bd",  "上传磅单"),
    RCHZ("rchz",  "上传人车合照"),
    FINISHED_FREIGHT_EXPORT("finishedFreightExport",  "成品运费复核汇总表导出"),
    AWAYBILL_EXPORT("awaybillExport",  "运单核销记录导出"),
    RECEIPT_EXPORT("receiptExport",  "回单导出"),
    SETTLEMENT_EXPORT("settlementExport",  "结算委运共同业务导出"),
    LIST_THIRD_RESULT_LOG_EXPORT("listThirdResultLogExport",  "接口日志分页列表导出"),
    EXPORT_APPLY_INVOICE("exportApplyInvoice",  "发票申请导出"),
    EXPORT_INVOICE_APPLY_ORDER("exportInvoiceApplyOrder",  "申请发票订单列表导出"),
    EXPORT_CONSIGN_BALANCE("exportConsignBalance",  "货主余额导出"),
    RECEIVE_PAY_EXPORT("receivePayExport",  "应结明细导出"),
    RESHIPMENT_EXPORT("reshipmentExport",  "倒运库存列表导出"),
    EXPORT_INVOICE("exportInvoice",  "发票导出"),
    EXPORT_DISPATCH_BATCH("exportDispatchBatch",  "运单导出"),
    EXPORT_TRANSIT("exportTransit",  "报名在途导出"),
    LOADING_BILL_PICTURE("loadingBillPicture",  "生成提货单图片"),
    POUND_UPLOAD("poundUpload",  "生成磅单图片"),
    DETECTION_SCORE("detectionScore",  "风控检测评分报告"),
    FEE_INVOICE("feeInvoice",  "缔联路桥发票"),
    GRANULE_DATA("granuleData",  "米粒旺旺保单pdf"),
    CONSUME_SUPPLY("consumeSupply",  "供应商二维码"),
    CONSUME_ACCOUNT_EXPORT("consumeAccountExport",  "消费账户流水导出"),
    DISPATCH_IMAGES("dispatchImages",  "装运图片上传"),
    COMPANY_IMAGE("companyImage",  "营业执照上传"),
    TRANSPORT_TEAM_VEHICLE("transportTeamVehicle",  "运输队载具导出"),
    ETC_INVOICE("etcInvoice",  "ETC发票"),
    DELEGATE_AGREEMENT_SIGN("delegateAgreementSign",  "协议签署"),
    GOODS_SUPPLY_EXPORT("goodsSupplyExport",  "货源导出"),
    TENDERS_EXPORTIN_EXPORT("tendersExportInExport",  "招标列表导出"),
    RESERVE_APPLY_EXPORT("reserveApplyExport",  "报名记录列表导出"),
    EXPORT_TEMPLATE_CREATE("exportTemplateCreate",  "导入模板生成"),
    IMAGE_TAILORING("imageTailoring",  "图片剪裁"),
    FINISHED_FREIGHT_EXPORT_FINANCE("finishedFreightExportFinance","成品运费复核汇总表导出(财务)"),
    OVER_100000_INTERCEPTIONS("over100000Interceptions","超十万拦截"),
    ORDER_ADJUST_PRICE_RECORD_EXPORT("orderAdjustPriceRecordExport",  "订单历史记录导出"),
    DISPATCH_ADJUST_PRICE_RECORD_EXPORT("dispatchAdjustPriceRecordExport",  "调度单历史记录导出"),
    DISPATCH_BATCH_ADJUST_PRICE_RECORD_EXPORT("dispatchBatchAdjustPriceRecordExport",  "运单历史记录导出"),
    UNION_DISPATCH_BATCH_CHARGE_RECORD_EXPORT("unionDispatchBatchChargeRecord",  "充电运单导出"),
    OPERATE_LOG("operateLog","运营日志"),
    ROLE_DISPATCH_CONSIGNOR_EXPORT("roleDispatchConsignor",  "托运人角色调度记录导出"),
    ROLE_DISPATCH_LOGISTICSCOMPANY_EXPORT("roleDispatchLogisticsCompany",  "运输公司角色调度记录导出"),
    ROLE_DISPATCH_TRANSPORTTEAM_EXPORT("roleDispatchTransportTeam",  "运输队角色调度记录导出"),
    ENERGY_GD_DAILY_BILL_RECORD_EXPORT("energyDailyBill","高灯日志导出"),
    ENERGY_NGJ_DAILY_BILL_RECORD_EXPORT("energyManageBill","能管家油气日账单导出"),
    ENERGY_MANAGE_MONTHLY_BILL("energyManageMonthlyBill","能管家油气周期账单导出"),
    //-------------------------华丽分割线-batch迁移需新增类型-start-----------------------------
    LINE_EXPORT_GPS("lineExportGps",  "路线轨迹"),
    ;

    private String code;
    private String desc;

    private FileBizSourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
