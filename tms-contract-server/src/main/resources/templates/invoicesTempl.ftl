<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>结算单</title>

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
                        <td colspan="8" class="fontCenter" >${balanceBaseInfo.balanceDate?string('yyyy年MM月dd日')} &nbsp; 结算账单</td>

                    </tr>
                    <tr>
                        <td colspan="4" class="" style="border-right: none;">结算单号：<span>${balanceBaseInfo.statementNumber}</span></td>
                        <td colspan="4" class="" style="border-left: none;">运营商平台：<span><#if balanceBaseInfo.operatorPlatform??>${balanceBaseInfo.operatorPlatform}</#if></span></td>
                    </tr>
                    <#list oilProductInfos as item>
                        <tbody id="shuju" class="fontCenter">
                        <tr>
                            <td colspan="2" class="" style="border-right: none;">能源类型：<span>${item.oilType}</span></td>
                            <td colspan="3" class="" style="border-left: none;border-right: none;">能源品类：<span>${item.oilName}</span></td>
                            <td colspan="3" class="" style="border-left: none;">共计笔数：<span>${item.totalStrokeCount}</span></td>
                        </tr>
                        <tr>
                            <td class="">序号</td>
                            <td colspan="2" class="">交易时间</td>
                            <td colspan="2" class="">站点名称</td>
                            <td class=""><#if item.oilType=='天然气'>能源量(公斤)<#else>能源量(L) </#if></td>
                            <td class="">含税金额(元)</td>
                            <td class="">备注</td>
                        </tr>
                        <#list item.oilLists as item2>
                            <tr>
                                <td class="">${item2_index+1}</td>
                                <td colspan="2" class="">${item2.tradeDate?string('yyyy-MM-dd HH:mm:ss')}</td>
                                <td colspan="2" class="">${item2.oilName}</td>
                                <td class="">${item2.oilMass}</td>
                                <td class="">${item2.taxIncludedAmount}</td>
                                <td class=""><#if item2.remark??>${item2.remark}</#if></td>
                            </tr>
                        </#list>
                        </tbody>
                    </#list>
                    <tr class="fontCenter">
                        <td rowspan="2">合计</td>
                        <td><#if balanceBaseInfo.oilType=='天然气'>能源量(公斤)<#else>能源量(L) </#if></td>
                        <td colspan="2">不含税金额合计(元)</td>
                        <td>税额合计(元)</td>
                        <td colspan="2">含税金额合计(元)</td>
                        <td>税率</td>
                    </tr>
                    <tr>
                        <td style="text-align:center;" id="totalNum">${balanceBaseInfo.totalNum}</td>
                        <td colspan="2" style="text-align:center;" id="totalPriceExcludingTax">${balanceBaseInfo.totalPriceExcludingTax}</td>
                        <td style="text-align:center;" id="totalTaxAmount">${balanceBaseInfo.totalTaxAmount}</td>
                        <td colspan="2" style="text-align:center;" id="totalTaxIncludedAmount">${balanceBaseInfo.totalTaxIncludedAmount}</td>
                        <td style="text-align:center;" id="taxRate">${balanceBaseInfo.taxRate}</td>
                    </tr>
                    <tr>
                        <td colspan="4" class="">供方：<span><#if balanceBaseInfo.supplierName??>${balanceBaseInfo.supplierName}</#if></span></td>
                        <td class=""></td>
                        <td colspan="3" class="">需方：<span>${firstKeyWord}</span></td>
                    </tr>
                    <tr>
                        <td colspan="4" class="">盖章：</td>
                        <td class=""></td>
                        <td colspan="3" class="">盖章：</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
