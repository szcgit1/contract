<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>长途充电日账单</title>

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

    .content-table {
        width: 100%;
        /*margin-top: 15px;*/
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

    .content-table .last-table, .table-price {
        text-align: center;
    }

    body th {
        padding: 4px !important;
    }
    .table-price th {
        background-color: #F2F2F2;
    }
    label {
        display: block;
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
    .fontCenter{
        text-align: center;
    }
    .fontCenter tr:first-child{
        text-align: left;
    }
</style>
</head>
<body>
<div id="content">
    <div class="content-content">
        <div class="content-table">
            <div style="display: flex;justify-content: center;">
                <table id="myTable" style="width: 100%">
                    <tr>
                        <td colspan="8" class="fontCenter" >${contract.chargePeriod}充电日账单</td>

                    </tr>
                    <tr>
                        <td colspan="4" class="" style="border-right: none;">充电日账单：<span>${contract.settleNo}</span></td>
                        <td colspan="4" class="" style="border-left: none;">充电平台：<span><#if contract.chargePlatform??>${contract.chargePlatform}</#if></span></td>
                    </tr>
                    <tbody id="shuju" class="fontCenter">
                    <tr>
                        <td class="">序号</td>
                        <td colspan="1" class="">费用类型</td>
                        <td colspan="1" class="">累计充电量(千瓦时)</td>
                        <td colspan="1" class="">单价(元/千瓦时)</td>
                        <td colspan="1" class="">累计充电金额(元)</td>
                        <td colspan="1" class="">不含税金额(元)</td>
                        <td colspan="1" class="">税额(元)</td>
                        <td colspan="1" class="">税率</td>
                    </tr>
                    <#list contract.chargeInfos as item>
                        <tr>
                            <td class="">${item.index}</td>
                            <td colspan="1" class=""><#if item.feeType??>${item.feeType}</#if></td>
                            <td colspan="1" class=""><#if item.chargeTotal??>${item.chargeTotal}</#if></td>
                            <td colspan="1" class=""><#if item.chargePrice??>${item.chargePrice}</#if></td>
                            <td colspan="1" class=""><#if item.chargeAmount??>${item.chargeAmount}</#if></td>
                            <td colspan="1" class=""><#if item.totalAmountExcludingTax??>${item.totalAmountExcludingTax}</#if></td>
                            <td colspan="1" class=""><#if item.totalTaxAmount??>${item.totalTaxAmount}</#if></td>
                            <td colspan="1" class=""><#if item.taxRate??>${item.taxRate}</#if></td>
                        </tr>
                    </#list>
                    <tr>
                        <td class="">合计</td>
                        <td colspan="1" class="">——</td>
                        <td colspan="1" class="">——</td>
                        <td colspan="1" class="">——</td>
                        <td colspan="1" class=""><#if contract.totalAmount??>${contract.totalAmount}</#if></td>
                        <td colspan="1" class=""><#if contract.totalAmountExcludingTax??>${contract.totalAmountExcludingTax}</#if></td>
                        <td colspan="1" class=""><#if contract.totalTaxAmount??>${contract.totalTaxAmount}</#if></td>
                        <td colspan="1" class=""><#if contract.taxRate??>${contract.taxRate}</#if></td>
                    </tr>
                    </tbody>
                    <tr>
                        <td colspan="4">供方:<#if contract.supplierName??>${contract.supplierName}</#if></td>
                        <td colspan="4">需方:<#if contract.buyerName??>${contract.buyerName}</#if></td>
                    </tr>
                    <tr>
                        <td colspan="2">税号:<#if contract.supplierTaxNumber??>${contract.supplierTaxNumber}</#if></td>
                        <td colspan="1">开户行:<#if contract.supplierDepositBankName??>${contract.supplierDepositBankName}</#if></td>
                        <td colspan="1">结算账户:<#if contract.supplierBankNo??>${contract.supplierBankNo}</#if></td>
                        <td colspan="2">税号:<#if contract.buyerTaxNumber??>${contract.buyerTaxNumber}</#if></td>
                        <td colspan="1">开户行:<#if contract.buyerDepositBankName??>${contract.buyerDepositBankName}</#if></td>
                        <td colspan="1">结算账户:<#if contract.buyerBankNo??>${contract.buyerBankNo}</#if></td>
                    </tr>
                    <tr>
                        <td colspan="4" class="">供方盖章：</td>
                        <td colspan="4" class="">需方盖章：</td>
                    </tr>
<#--                    <tr>-->
<#--                        <td colspan="4" class="">盖章：</td>-->
<#--                        <td colspan="4" class="">盖章：</td>-->
<#--                    </tr>-->
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
