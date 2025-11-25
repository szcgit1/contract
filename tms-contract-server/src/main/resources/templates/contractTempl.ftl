<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>合同</title>
    <style type="text/css">
        @page{size:a4 }
        html {
            font-family:  SimSun;
        }

        body {
            margin: auto;
            font-family:  SimSun;
            font-size: 14px;
            line-height: 20px;
            color: #333333;
            background-color: #fff;
        }

        .header-num {
            float: right;
        }

        .content {
            width:650px;
            padding: 20px 40px;
        }

        .content-header h2 {
            text-align: center;
            clear: both;
        }

        .content-content {
            width: 100%;
            padding-top: 30px;
            clear: both;
            word-wrap:break-word;
            word-break:break-all;
        }

        .back-color {
            background-color: #F2F2F2;
            white-space: nowrap;
        }

        .content-table {
            width: 100%;
            margin-top: 30px;
        }

        .content-table table label {
            text-align: left;
        }

        .content-table table th, .content-table table td {
            border: 1px solid #000;
        }

        .content-table table {
            border-collapse: collapse;
            width: 100%;
        }

        /*.content-table .last-table th {
            border-top: none;
        }*/

        .content-table .last-table, .table-price {
            text-align: center;
        }

        body th {
            padding: 4px !important;
        }

        .tbale-money {
            text-align: right;
        }

        .line-show {
            color:#000000;
            text-align: center;
        }

        .table-price {
            margin-top: 14px;
        }

        .table-price th {
            background-color: #F2F2F2;
        }

        .content-footer {
            margin-top: 10px;
            margin-bottom: 50px;
        }

        .content-footer:after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden;
        }
        label {
            display: block;
        }
        .bottom-user {
            margin-top:15px;
        }
        .left-user {
            float: left;
        }
        .right-user {
            float: right;
        }
        .bottom-signer {
            color: #FFFFFF;
            line-height: 0;
            padding-top: 22px;
            padding-left: 100px;
        }
        .label-contact {
            margin-left: 25px;
        }
        .kindent {
            font-weight:normal;
            text-indent:4em;
        }
        table {
            page-break-inside: auto;
            -fs-table-paginate: paginate;
            border-spacing: 0;
            cellspacing: 0;
            cellpadding: 0;
        }

        tr {
            page-break-inside: avoid;
            page-break-after: auto;
        }
    </style>
</head>
<body>
<div class="content">
    <div class="content-header">
        <strong class="header-num">合同编号：${contract.contractCode}</strong>
        <h2>${contract.title}</h2>
        <div class="header-userInfo">
            <div>
                <strong>托运人：<#if contract.trustorCompany??>${contract.trustorCompany.name}</#if></strong>
                <span class="label-contact">联系人：<#if contract.trustorCompany.contact?? && contract.trustorCompany.contact.name??>${contract.trustorCompany.contact.name}</#if><#if contract.trustorCompany.contact.mobile??><b class="kindent">${contract.trustorCompany.contact.mobile}</b></#if></span>
            </div>
            <div>
                <strong>承运人：<#if contract.carryCompany??>${contract.carryCompany.name}</#if></strong>
                <span class="label-contact">联系人：<#if contract.carryCompany.contact?? && contract.carryCompany.contact.name??>${contract.carryCompany.contact.name}</#if><#if contract.carryCompany.contact.mobile??><b class="kindent">${contract.carryCompany.contact.mobile}</b></#if></span>
            </div>
        </div>
    </div>
    <div class="content-content">
        <#if contract.content??>${contract.content}</#if>
    </div>
    <div class="content-table">
        <#if contract.contractGoodsInfos??>
            <#list contract.contractGoodsInfos as goodsInfo>
                <table>
                    <tbody>
                    <tr>
                        <th class="back-color">发货地址</th>
                        <th><#if goodsInfo.sendAddress??>${goodsInfo.sendAddress}</#if><#if goodsInfo.sendContactName??>/${goodsInfo.sendContactName}</#if><#if goodsInfo.sendContactMobile??> /${goodsInfo.sendContactMobile}</#if></th>
                        <th class="back-color">收货地址</th>
                        <th><#if goodsInfo.receiveAddress??>${goodsInfo.receiveAddress}</#if><#if goodsInfo.receiveContactName??>/${goodsInfo.receiveContactName}</#if><#if goodsInfo.receiveContactMobile??> /${goodsInfo.receiveContactMobile}</#if></th>
                    </tr>
                    <tr>
                        <th class="back-color">计划发货</th>
                        <th><label><#if goodsInfo.sendTime??> ${goodsInfo.sendTime?string("yyyy-MM-dd HH:mm:ss")}</#if></label></th>
                        <th class="back-color">计划到货</th>
                        <th><label><#if goodsInfo.sendTime??> ${goodsInfo.receiveTime?string("yyyy-MM-dd HH:mm:ss")}</#if></label></th>
                    </tr>
                    <tr>
                        <th class="back-color">运输方式</th>
                        <th><#if goodsInfo.transportType??><label>${goodsInfo.transportType}</label></#if></th>
                        <th class="back-color">运输车辆</th>
                        <th><#if goodsInfo.vehicleCode??><label>${goodsInfo.vehicleCode}</label></#if></th>
                    </tr>
                    </tbody>
                </table>
                <br/>
                <table class="last-table">
                    <tbody>
                    <tr>
                        <th>序号</th>
                        <th>货物名称</th>
                        <th>描述</th>
                        <th>托运量</th>
                    </tr>

                    <#if goodsInfo.contractGoodsJson??>
                        <#assign goodsInfoJson = goodsInfo.contractGoodsJson ? eval/>
                        <#list goodsInfoJson as goodJson>
                            <tr>
                                <td style="text-align: center;">${goodJson_index+1}</td>
                                <td><#if goodJson.goodsCode??>${goodJson.goodsCode} </#if> <#if goodJson.goodsName??>${goodJson.goodsName}  <#if goodJson.model??>（${goodJson.model}）</#if></#if></td>
                                <td><#if goodJson.goodsDesc??>${goodJson.goodsDesc}</#if></td>
                                <td><#if goodJson.goodMeasuring??>${goodJson.goodMeasuring}</#if></td>
                            </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
                <br/>
            </#list>
        </#if>
        <#assign conTotalPrice = 0/>
        <#assign setTotalPrice = 0/>
        <table class="table-price">
            <tbody>
                <tr>
                    <th>序号</th>
                    <th>计费科目</th>
                    <th>合同单价（元）</th>
                    <th>合同金额（元）</th>
                    <th>结算单价（元）</th>
                    <th>结算金额（元）</th>
                </tr>
                <#if contract.contractChargeInfos?? && contract.contractChargeInfos.chargeSubjectsInfos??>
                    <#list contract.contractChargeInfos.chargeSubjectsInfos as contractChargeInfos>
                        <tr>
                            <td style="text-align: center;">${contractChargeInfos_index+1}</td>
                            <td><#if contractChargeInfos.feesDesc??>${contractChargeInfos.feesDesc}</#if></td>
                            <td class="tbale-money">
                                <#if contractChargeInfos.contractUnitPrice??>
                                    <#if (0 lte contractChargeInfos.contractUnitPrice && contractChargeInfos.contractUnitPrice lt 1)>
                                        ${contractChargeInfos.contractUnitPrice?string('0.00')}
                                    <#else>
                                        ${contractChargeInfos.contractUnitPrice?string(',###.00')}
                                    </#if>
                                </#if>
                            </td>
                            <td class="tbale-money">
                                <#if contractChargeInfos.contractPrice??>
                                    <#if (0 lte contractChargeInfos.contractPrice && contractChargeInfos.contractPrice lt 1)>
                                        ${contractChargeInfos.contractPrice?string('0.00')}
                                    <#else>
                                        ${contractChargeInfos.contractPrice?string(',###.00')}
                                    </#if>
                                </#if>
                            </td>
                            <td class="tbale-money">
                                <#if contractChargeInfos.settleUnitPrice??>
                                    <#if (0 lte contractChargeInfos.settleUnitPrice && contractChargeInfos.settleUnitPrice lt 1)>
                                        ${contractChargeInfos.settleUnitPrice?string('0.00')}
                                    <#else>
                                        ${contractChargeInfos.settleUnitPrice?string(',###.00')}
                                    </#if>
                                </#if>
                            </td>
                            <td class="tbale-money">
                                <#if contractChargeInfos.settlePrice??>
                                    <#if (0 lte contractChargeInfos.settlePrice && contractChargeInfos.settlePrice lt 1)>
                                        ${contractChargeInfos.settlePrice?string('0.00')}
                                    <#else>
                                        ${contractChargeInfos.settlePrice?string(',###.00')}
                                    </#if>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </#if>
                <tr>
                    <th></th>
                    <th>金额合计：</th>
                    <th></th>
                    <th class="tbale-money">
                        <#if contract.contractChargeInfos?? && contract.contractChargeInfos.contractTotalPrice??>
                            <#if (0 lte contract.contractChargeInfos.contractTotalPrice && contract.contractChargeInfos.contractTotalPrice lt 1)>
                                ${contract.contractChargeInfos.contractTotalPrice?string('0.00')}
                            <#else>
                                ${contract.contractChargeInfos.contractTotalPrice?string(',###.00')}
                            </#if>
                        </#if>
                    </th>
                    <th></th>
                    <th class="tbale-money">
                        <#if contract.contractChargeInfos?? && contract.contractChargeInfos.settleTotalPrice??>
                            <#if (0 lte contract.contractChargeInfos.contractTotalPrice && contract.contractChargeInfos.contractTotalPrice lt 1)>
                                ${contract.contractChargeInfos.settleTotalPrice?string('0.00')}
                            <#else>
                                ${contract.contractChargeInfos.settleTotalPrice?string(',###.00')}
                            </#if>
                        </#if>
                    </th>
                </tr>
            </tbody>
        </table>

        <table class="table-price">
            <tbody>
                <tr>
                    <th>现付</th>
                    <th>到付</th>
                    <th>回单付</th>
                    <th>月结</th>
                    <th>合计</th>
                </tr>
                <tr>
                    <#if contract.contractPaymentDetail?? && contract.contractPaymentDetail.contractPaymentInfos??>
                        <#assign pay_daofu = 0/>
                        <#assign pay_xianfu = 0/>
                        <#assign pay_huidanfu = 0/>
                        <#assign pay_yuejie = 0/>

                        <#assign voucher_daofu_desc = ""/>
                        <#assign voucher_xianfu_desc = ""/>
                        <#assign voucher_huidanfu_desc = ""/>
                        <#assign voucher_yuejie_desc = ""/>
                        <#list contract.contractPaymentDetail.contractPaymentInfos as payment>
                            <#if payment.paymentMode = 1021040>
                                <#if payment.money != 0 >
                                    <#assign pay_daofu = pay_daofu + payment.money/>
                                    <#assign voucher_daofu_desc = payment.voucherTypeDesc/>
                                </#if>
                            <#elseif payment.paymentMode = 1021000>
                                <#if payment.money != 0 >
                                    <#assign pay_xianfu = pay_xianfu + payment.money/>
                                    <#assign voucher_xianfu_desc = payment.voucherTypeDesc/>
                                </#if>
                            <#elseif payment.paymentMode = 1021020>
                                <#if payment.money != 0 >
                                    <#assign pay_huidanfu = pay_huidanfu + payment.money/>
                                    <#assign voucher_huidanfu_desc = payment.voucherTypeDesc/>
                                </#if>
                            <#elseif payment.paymentMode = 1021030>
                                <#if payment.money != 0 >
                                    <#assign pay_yuejie = pay_yuejie + payment.money/>
                                    <#assign voucher_yuejie_desc = payment.voucherTypeDesc/>
                                </#if>
                            </#if>
                        </#list>

                        <td>
                            <#if (0 lt pay_xianfu && pay_xianfu lt 1)>
                                <span class="tbale-money">${voucher_xianfu_desc} ￥${pay_xianfu?string('0.00')}</span>
                            <#elseif (0 = pay_xianfu)>
                                <span class="line-show">——</span>
                            <#else>
                                <span class="tbale-money">${voucher_xianfu_desc} ￥${pay_xianfu?string(',###.00')}</span>
                            </#if>
                        </td>

                        <td>
                            <#if (0 lt pay_daofu && pay_daofu lt 1)>
                                <span class="tbale-money">${voucher_daofu_desc} ￥${pay_daofu?string('0.00')}</span>
                            <#elseif (0 = pay_daofu)>
                                <span class="line-show">——</span>
                            <#else>
                                <span class="tbale-money">${voucher_daofu_desc} ￥${pay_daofu?string(',###.00')}</span>
                            </#if>
                        </td>

                        <td>
                            <#if (0 lt pay_huidanfu && pay_huidanfu lt 1)>
                                <span class="tbale-money">${voucher_huidanfu_desc} ￥${pay_huidanfu?string('0.00')}</span>
                            <#elseif (0 = pay_huidanfu)>
                                <span class="line-show">——</span>
                            <#else>
                                <span class="tbale-money">${voucher_huidanfu_desc} ￥${pay_huidanfu?string(',###.00')}</span>
                            </#if>
                        </td>

                        <td>
                            <#if (0 lt pay_yuejie && pay_yuejie lt 1)>
                                <span class="tbale-money">${voucher_yuejie_desc} ￥${pay_yuejie?string('0.00')}</span>
                            <#elseif (0 = pay_yuejie)>
                                <span class="line-show">——</span>
                            <#else>
                            <span class="tbale-money">${voucher_yuejie_desc} ￥${pay_yuejie?string(',###.00')}</span>
                            </#if>
                        </td>

                        <td>
                            <#if (0 lte contract.contractPaymentDetail.totalPrice && contract.contractPaymentDetail.totalPrice lt 1)>
                                <span class="tbale-money">${contract.contractPaymentDetail.totalPrice?string('0.00')}</span>
                            <#elseif (0 = contract.contractPaymentDetail.totalPrice)>
                                <span class="line-show">——</span>
                            <#else>
                                <span class="tbale-money">${contract.contractPaymentDetail.totalPrice?string(',###.00')}</span>
                            </#if>
                        </td>
                    <#else>
                        <td class="line-show">——</td>
                        <td class="line-show">——</td>
                        <td class="line-show">——</td>
                        <td class="line-show">——</td>
                        <td class="line-show">——</td>
                    </#if>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="content-footer">
        <#--只有运单合同，才显示油气费用提示-->
        <#if contract.documentType == 1133030 >
             <div>
                <label><strong>合同金额包含能源费用，以最终结算为准</strong></label>
             </div>
        </#if>

        <div>
            <label><strong>本合同的签署日期为<u>${contract.contractDate?string("yyyy年MM月dd日")} 。</u></strong></label>
        </div>
        <div class="bottom-user">
            <strong class="left-user">托运人：${contract.trustorCompany.name}</strong>
            <strong class="right-user">承运人：${contract.carryCompany.name}</strong>
        </div>
        <div class="bottom-signer">
            <span>${firstKeyWord}</span>
            <span class="right-user">${secondKeyWord}</span>
        </div>
    </div>
</div>
</body>
</html>