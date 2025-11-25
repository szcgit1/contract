<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>能源待收汇总单</title>

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
    <div class="content-header">
        <strong class="header-num">能源待收汇总单</strong>
    </div>
    <div class="content-content">
        <div class="content-table">
            <div style="display: flex;justify-content: center;">
                <table id="myTable" style="width: 100%">
                    <tr>
                        <td colspan="1" class="" style="background-color: #d9edf7;">能源待收汇总单号：</td>
                        <td colspan="1" class="" style="border-right: none;"><span><#if contract.summaryTime??>${contract.summaryCode} &nbsp; &nbsp;</#if></span></td>
                        <td colspan="1" class="" style="background-color: #d9edf7;">汇总周期：</td>
                        <td colspan="1" class="" style="border-left: none;"><span><#if contract.summaryTime??>${contract.summaryTime}</#if></span></td>
                        <td colspan="1" class="" style="background-color: #d9edf7;">汇总时间：</td>
                        <td colspan="1" class="" style="border-left: none;"><span><#if contract.createTime??>${contract.createTime}</#if></span></td>
                    </tr>
                    <tbody id="shuju" class="fontCenter">
                    <tr>
                        <td class="background-color: #d9edf7;">序号</td>
                        <td colspan="1" class="background-color: #d9edf7;">能源待收单号</td>
                        <td colspan="1" class="background-color: #d9edf7;">含税金额(元)</td>
                        <td colspan="1" class="background-color: #d9edf7;">不含税金额(元)</td>
                        <td colspan="1" class="background-color: #d9edf7;">税额(元)</td>
                        <td colspan="1" class="background-color: #d9edf7;">税率</td>
                    </tr>
                    <#list contract.billList as item>
                        <tr>
                            <td class="">${item_index+1}</td>
                            <td colspan="1" class=""><#if item.billCode??>${item.billCode} &nbsp; &nbsp;</#if></td>
                            <td colspan="1" class=""><#if item.totalAmount??>${item.totalAmount}</#if></td>
                            <td colspan="1" class=""><#if item.excludingTaxAmount??>${item.excludingTaxAmount}</#if></td>
                            <td colspan="1" class=""><#if item.taxAmount??>${item.taxAmount}</#if></td>
                            <td colspan="1" class=""><#if item.taxRate??>${item.taxRate}%</#if></td>
                        </tr>
                    </#list>
                    <tr>
                        <td colspan="2" class="background-color: #d9edf7;">合计</td>
                        <td colspan="1" class="background-color: #d9edf7;"><#if contract.totalAmount??>${contract.totalAmount}</#if></td>
                        <td colspan="1" class="background-color: #d9edf7;"><#if contract.excludingTaxAmount??>${contract.excludingTaxAmount}</#if></td>
                        <td colspan="1" class="background-color: #d9edf7;"><#if contract.taxAmount??>${contract.taxAmount}</#if></td>
                        <td colspan="1" class="background-color: #d9edf7;"></td>
                    </tr>
                    </tbody>
                    <tr>
                        <td colspan="1" class="background-color: #d9edf7;">能源销售方:</td>
                        <td colspan="2" class="background-color: #d9edf7;"><#if contract.firstParty??>${contract.firstParty}</#if></td>
                        <td colspan="1" class="background-color: #d9edf7;">服务方名称:</td>
                        <td colspan="2" class="background-color: #d9edf7;"><#if contract.secondParty??>${contract.secondParty}</#if></td>
                    </tr>
                    <tr>
                        <td colspan="1" class="background-color: #d9edf7;">统一信用代码:</td>
                        <td colspan="2" class="background-color: #d9edf7;"><#if contract.firstCreditCode??>${contract.firstCreditCode}</#if></td>
                        <td colspan="1" class="background-color: #d9edf7;">身份证号:</td>
                        <td colspan="2" class="background-color: #d9edf7;"><#if contract.secondIdCard??>${contract.secondIdCard}</#if></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="background-color: #d9edf7;">销售方签章：</td>
                        <td colspan="3" class="background-color: #d9edf7;">服务方签章：</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
