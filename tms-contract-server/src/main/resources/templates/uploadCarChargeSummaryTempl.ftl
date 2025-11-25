<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>卸车服务费结算单</title>
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
        .content {
            width:100%;
        }
        .content-header h2 {
            text-align: center;
            clear: both;
        }
        .content-table {
            width: 100%;
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
        .table-price {
            margin-top: 14px;
        }
        .table-price th {
            background-color: #F2F2F2;
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
        .bottom-signer {
            color: #FFFFFF;
            line-height: 0;
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
        .content-style {
            font-size: 14px;
            text-indent: 2em;
        }
        .content-left-two{
            padding-left: 2em;
        }
        #myTable td{
            border: 1px solid #979494;
            font-weight: bolder;
            padding-left: 5px;
        }
        #myTable td{
            height: 40px;
            width: 10%;
        }
        #myTable tr{
            text-align: center;
        }
        .daedf7{
            background-color: #daedf7;
        }
    </style>
</head>
<body>
<div class="content">
    <div class="content-header">
    </div>
    <br/>
    <div class="content-header">
        <div class="header-userInfo">
        </div>
    </div>
    <div class="content-header">
        <div class="header-userInfo">
<#--            <div style="text-align: center;">-->
<#--                <span style="font-size: 28px;"><#if contract.secondParty??>${contract.secondParty}</#if></span>-->
<#--            </div>-->
<#--            <br/>-->
            <div style="text-align: center;">
                <span style="font-size: 28px;">卸车服务费结算单</span>
            </div>
        </div>
    </div>
    <div class="header-userInfo" style="text-align: right;margin-top: 20px">
        <div class="content-style">
            <strong></strong>
            <strong> 制单日期：<span class="writeOffTime"><#if contract.writeOffTime??>${contract.writeOffTime?string("yyyy/MM/dd")}</#if></span></strong>
        </div>
    </div>
    <div class="content-table">
        <table id="myTable" style="border-collapse:collapse;width: 100%"  class="table-price">
            <tr class="myTr">
                <td rowspan="4">甲方</td>
                <td>公司名称：</td>
                <td class="firstParty"><#if contract.firstParty??>${contract.firstParty}</#if></td>
                <td rowspan="4">乙方</td>
                <td>公司名称：</td>
                <td colspan="2" class="secondParty"><#if contract.secondParty??>${contract.secondParty}</#if></td>
            </tr>
            <tr class="myTr">
                <td>税号：</td>
                <td class="firstCreditCode"><#if contract.firstCreditCode??>${contract.firstCreditCode}</#if></td>
                <td>税号：</td>
                <td colspan="2" class="secondCreditCode"><#if contract.secondCreditCode??>${contract.secondCreditCode}</#if></td>
            </tr>
            <tr class="myTr">
                <td>开户行：</td>
                <td class="firstDepositBank"><#if contract.firstDepositBank??>${contract.firstDepositBank}</#if></td>
                <td>开户行：</td>
                <td colspan="2" class="secondDepositBank"><#if contract.secondDepositBank??>${contract.secondDepositBank}</#if></td>
            </tr>
            <tr class="myTr">
                <td>银行账号：</td>
                <td class="firstBankAccount"><#if contract.firstBankAccount??>${contract.firstBankAccount}</#if></td>
                <td>银行账号：</td>
                <td colspan="2" class="secondBankAccount"><#if contract.secondBankAccount??>${contract.secondBankAccount}</#if></td>
            </tr>
            <tr class="myTr">
                <td colspan="3">服务期间</td>
                <td colspan="4">服务项目</td>
            </tr>
            <tr class="myTr">
                <td colspan="3" class="servicePeriod"><#if contract.servicePeriod??>${contract.servicePeriod}</#if></td>
                <td colspan="4" class="serviceProject"><#if contract.serviceProject??>${contract.serviceProject}</#if></td>
            </tr>
            <tr class="myTr" >
                <td colspan="2">结算数量（车）</td>
                <td>含税单价（元/车）</td>
                <td colspan="2">不含税金额（元）</td>
                <td>税额（元）</td>
                <td>含税金额（元）</td>
            </tr>
            <tr class="myTr">
                <td colspan="2" class="billingCar"><#if contract.billingCar??>${contract.billingCar}</#if></td>
                <td class="unitPriceTaxIncluded"><#if contract.unitPriceTaxIncluded??>${contract.unitPriceTaxIncluded}</#if></td>
                <td colspan="2" class="excludingTaxPrice"><#if contract.excludingTaxPrice??>${contract.excludingTaxPrice}</#if></td>
                <td class="taxAmount"><#if contract.taxAmount??>${contract.taxAmount}</#if></td>
                <td class="amountTaxIncluded"><#if contract.amountTaxIncluded??>${contract.amountTaxIncluded}</#if></td>
            </tr>
            <tr class="myTr">
                <td colspan="2">合同编号：</td>
                <td colspan="2" class="contractCode"><#if contract.contractCode??>${contract.contractCode}</#if></td>
                <td>税率</td>
                <td colspan="2" class="taxRate"><#if contract.taxRate??>${contract.taxRate}%</#if></td>
            </tr>
        </table>
    </div>

    <div class="header-userInfo" style="display: grid;grid-template-columns: 50% 50%;justify-content:space-around;margin-top:25px;">
        <div class="content-style" style="margin-left: 50px">
            <strong>甲方（签章）</strong>
            <span class="content-left-two bottom-signer" style="padding-left: 120px;">${secondKeyWord}secondKeyWord</span><#--签章坐标-->
            <strong>乙方（签章）</strong>
            <span class="content-left-two bottom-signer" style="padding-left: 120px;" >${firstKeyWord}firstKeyWord</span><#--签章坐标-->
        </div>
    </div>
</div>
</body>
</html>
