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
        .fontCenter2{
            text-align: right;
        }
    </style>
</head>
<body>
<div id="content">
    <div class="content-content">
        <div class="content-header">
            <div class="header-userInfo">
                <div style="text-align: center;">
                    <span style="font-size: 28px;">结算单</span>
                </div>
            </div>
        </div>

        <div class="header-userInfo" style="display: grid;grid-template-columns: 50% 50%;justify-content:space-around;margin-top:25px;">
            <div class="content-style" style="margin-left: 20px">
                <span>编号：</span>
                <span class="" style="padding-right: 250px;"><#if contract.summaryCode??>${contract.summaryCode}</#if></span>
                <span>制单日期：</span>
                <span class="" style="padding-left: 10px;" ><#if contract.createTime??>${contract.createTime}</#if></span>
            </div>
            <br/>
        </div>
        <div class="content-table">
            <div style="display: flex;justify-content: center;">
                <table id="myTable" style="width: 100%">

                    <tr>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">销售方：</td>
                        <td colspan="4" class="fontCenter" style=""><span>${contract.firstParty} &nbsp;</span></td>
                        <td colspan="2" class="fontCenter" style="background-color: #d9edf7">采购方：</td>
                        <td colspan="3" class="fontCenter" style="border-left: none;"><span><#if contract.secondParty??>${contract.secondParty}</#if> &nbsp;</span></td>
                    </tr>
                    <tr>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">税号：</td>
                        <td colspan="4" class="fontCenter" style=""><span><#if contract.firstCreditCode??>${contract.firstCreditCode}</#if> &nbsp;</span></td>
                        <td colspan="2" class="fontCenter" style="background-color: #d9edf7;">税号：</td>
                        <td colspan="3" class="fontCenter" style="border-left: none;"><span><#if contract.secondCreditCode??>${contract.secondCreditCode}</#if> &nbsp;</span></td>
                    </tr>
                    <tr>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">开户银行：</td>
                        <td colspan="4" class="fontCenter" style=""><span><#if contract.firstDepositBank??>${contract.firstDepositBank}</#if> &nbsp;</span></td>
                        <td colspan="2" class="fontCenter" style="background-color: #d9edf7;">开户银行：</td>
                        <td colspan="3" class="fontCenter" style="border-left: none;"><span><#if contract.secondDepositBank??>${contract.secondDepositBank}</#if> &nbsp;</span></td>
                    </tr>
                    <tr>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">银行账号：</td>
                        <td colspan="4" class="fontCenter" style=""><span><#if contract.firstBankAccount??>${contract.firstBankAccount}</#if> &nbsp;</span></td>
                        <td colspan="2" class="fontCenter" style="background-color: #d9edf7;">银行账号：</td>
                        <td colspan="3" class="fontCenter" style="border-left: none;"><span><#if contract.secondBankAccount??>${contract.secondBankAccount}</#if> &nbsp;</span></td>
                    </tr>
                    <tr>
                        <td colspan="1" class="fontCenter" style="border-right: none;background-color: #d9edf7;">合同编号：</td>
                        <td colspan="6" class="fontCenter" style=""><span>${contract.contractCode} &nbsp;</span></td>
                        <td colspan="3" class="fontCenter" style="background-color: #d9edf7;">结算金额（元）</td>
                    </tr>
                    <tbody id="shuju" class="fontCenter">
                    <tr>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">日期</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">货物（项目）名称</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">规格型号</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">数量单位</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">结算数量</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">结算含税单价（元）</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">税率</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">无税金额</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">税额</td>
                        <td colspan="1" class="fontCenter" style="background-color: #d9edf7;">含税金额</td>
                    </tr>
                    <#list contract.goodList as item>
                        <tr>
                            <td colspan="1" class="">${item.createTime} &nbsp;</td>
                            <td colspan="1" class=""><#if item.productName??>${item.productName}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.model??>${item.model}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.unit??>${item.unit}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.totalNum??>${item.totalNum}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.totalAmount??>${item.amount}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.tax??>${item.tax}%</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.excludingTaxAmount??>${item.excludingTaxAmount}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.taxAmount??>${item.taxAmount}</#if> &nbsp;</td>
                            <td colspan="1" class=""><#if item.totalAmount??>${item.totalAmount}</#if> &nbsp;</td>
                        </tr>
                    </#list>
                    </tbody>
                    <tr>
                        <td colspan="7" class="fontCenter2" >合计:</td>
                        <td colspan="1" class="fontCenter"><#if contract.excludingTaxAmount??>${contract.excludingTaxAmount}</#if> &nbsp;</td>
                        <td colspan="1" class="fontCenter"><#if contract.taxAmount??>${contract.taxAmount}</#if> &nbsp;</td>
                        <td colspan="1" class="fontCenter"><#if contract.totalAmount??>${contract.totalAmount}</#if> &nbsp;</td>
                    </tr>



                </table>
            </div>
        </div>
<#--        <div class="header-userInfo" style="display: grid;grid-template-columns: 50% 50%;justify-content:space-around;margin-top:25px;">-->
<#--            <div class="content-style" style="margin-left: 50px">-->
<#--                <span class="content-left-two bottom-signer" style="padding-left: 120px;">销售方（签章）:</span>-->
<#--                <span class="content-left-two bottom-signer" style="padding-left: 120px;" >采购方（签章）:</span>-->
<#--            </div>-->
<#--        </div>-->
        <div class="header-userInfo" style="display: grid;grid-template-columns: 50% 50%;justify-content:space-around;margin-top:25px;">
            <div class="content-style" style="margin-left: 20px">
                <span>销售方（签章）</span>
                <span class="content-left-two bottom-signer" style="padding-left: 300px;"></span>
                <span>采购方（签章）</span>
                <span class="content-left-two bottom-signer" style="padding-left: 50px;"></span>
            </div>
        </div>
    </div>
</div>
</body>
</html>
