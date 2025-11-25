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
            margin-top: 15px;
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

        .contractContentStyle{
            font-weight: 900;
            font-size: 10.6667px;
        }
        .fade-type {
            text-align: center;
            font-weight: 400;
        }
        .content-style {
            font-size: 10.6667px;
            text-indent: 2em;
        }
        .content-style-1 {
            font-size: 10.6667px;
            text-indent: 2em;
        }
        .content-left-two{
            padding-left: 2em;
        }
        table {
            width:100%;
            table-layout:fixed;
            word-wrap:break-word;
            word-break:break-all;
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
                <span>托运人：<#if contract.trustorCompany??>${contract.trustorCompany.name}</#if></span>
                <br/>
                <span>联系人：<#if contract.trustorCompany.contact?? && contract.trustorCompany.contact.name??>${contract.trustorCompany.contact.name}</#if></span>
                <br/>
                <span>联系方式：<#if contract.trustorCompany.contact.mobile??><b class="kindent">${contract.trustorCompany.contact.mobile}</b></#if></span>
            </div>
            <br/>
            <div>
                <span>承运人：<#if contract.carryCompany??>${contract.carryCompany.name}</#if></span>
                <br/>
                <span>联系人：<#if contract.carryCompany.contact?? && contract.carryCompany.contact.name??>${contract.carryCompany.contact.name}</#if></span>
                <br/>
                <span>联系方式：<#if contract.carryCompany.contact.mobile??><b class="kindent">${contract.carryCompany.contact.mobile}</b></#if></span>
            </div>
        </div>
    </div>
    <br/>

    <div class="content-header">
        <div class="header-userInfo">
            <div style="margin-top: 10px;">
                <span>
                    <strong class="content-style-1" style="font-size: 10.6667px;text-indent: 0em;">
                        <p>根据国家相关法律法规和托运人运输业务要求，经双方充分协商，特订立本合同，以便共同遵守，本合同适用于所有注册成为<span><#if contract.trustorCompany??>${contract.trustorCompany.name}</#if></span>平台（以下简称"平台"）的承运人用户。</p>
                        <p>本合同为批量运输合同，批量运输指司机通过小铁马APP 承接的包含多个货物运单的运输任务,需一次性全部完成所有运单业务。</p>
                    </strong>
                </span>

            </div>

            <div>
                <strong class="content-style">一、承运货物的相关详细信息</strong>
            </div>
        </div>
    </div>
    <#--承运货物的相关详细信息-->
    <div class="content-table">
        <#if contract.dispatchBatchContractVo??>
            <table style="width: 100%">
                <tbody>
                <tr>
                    <th class="back-color">批量运单号</th>
                    <th class="fade-type" colspan="2"><#if contract.dispatchBatchContractVo.dispatchBatchContractNo??>${contract.dispatchBatchContractVo.dispatchBatchContractNo}</#if></th>
                    <th class="back-color">单据创建时间</th>
                    <th class="fade-type" colspan="2"><#if contract.dispatchBatchContractVo.dispatchBatchContractTime??>${contract.dispatchBatchContractVo.dispatchBatchContractTime}</#if></th>
                </tr>
                <tr>
                    <th class="back-color">承运人</th>
                    <th class="fade-type"><#if contract.carryCompany??>${contract.carryCompany.name}</#if></th>
                    <th class="back-color">承运人电话</th>
                    <th class="fade-type"><#if contract.carryCompany.contact.mobile??><b class="kindent">${contract.carryCompany.contact.mobile}</b></#if></th>
                    <th class="back-color">承运人身份证号</th>
                    <th><#if contract.carryCompany.contact.idcardNo??><b class="kindent">${contract.carryCompany.contact.idcardNo}</b></#if></th>
                </tr>
                <tr>
                    <th class="back-color">运单数量</th>
                    <th class="fade-type"><#if contract.dispatchBatchContractVo.dispatchBatchNum??>${contract.dispatchBatchContractVo.dispatchBatchNum}</#if></th>
                    <th class="back-color">调度单数量</th>
                    <th class="fade-type"><#if contract.dispatchBatchContractVo.dispatchNum??>${contract.dispatchBatchContractVo.dispatchNum}</#if></th>
                    <th class="back-color">车牌号</th>
                    <th><#if contract.dispatchBatchContractVo.vehicleCode??>${contract.dispatchBatchContractVo.vehicleCode}</#if></th>
                </tr>
                </tbody>
            </table>
            <br/>
            <br/>
        </#if>
        <#if contract.dispatchBatchContractVo??>
            <#if contract.dispatchBatchContractVo.contractGoodsInfoVos??>
                <#list contract.dispatchBatchContractVo.contractGoodsInfoVos as contractGoodsInfoVos>
                    <table style="width: 100%">
                        <tbody>
                        <tr>
                            <th class="back-color">运单编号${contractGoodsInfoVos_index+1}</th>
                            <th class="fade-type" colspan="2"><#if contractGoodsInfoVos.dispatchBatchCode??>${contractGoodsInfoVos.dispatchBatchCode}</#if></th>
                            <th class="back-color">运单创建时间</th>
                            <th class="fade-type" colspan="2"><#if contractGoodsInfoVos.dispatchBatchCreateTime??>${contractGoodsInfoVos.dispatchBatchCreateTime}</#if></th>
                        </tr>
                        <#if contractGoodsInfoVos.dispatchGoodsInfoVos??>
                            <#list contractGoodsInfoVos.dispatchGoodsInfoVos as dispatchGoodsInfoVos>
                                <tr>
                                    <th class="back-color">托运量/吨</th>
                                    <th class="back-color" colspan="3">货物名称</th>
                                    <th class="fade-type" colspan="2">描述</th>
                                </tr>
                                <tr>
                                    <td class="fade-type"><#if dispatchGoodsInfoVos.goodMeasuring??>${dispatchGoodsInfoVos.goodMeasuring}</#if></td>
                                    <td class="fade-type" colspan="3"><#if dispatchGoodsInfoVos.goodsName??>${dispatchGoodsInfoVos.goodsName}</#if></td>
                                    <td class="fade-type" colspan="2"><#if dispatchGoodsInfoVos.goodsDesc??>${dispatchGoodsInfoVos.goodsDesc}</#if></td>
                                </tr>
                                <tr>
                                    <th class="back-color">合同单价（元）</th>
                                    <th class="fade-type" colspan="2"><#if contractGoodsInfoVos.contractUnitPrice??>${contractGoodsInfoVos.contractUnitPrice}</#if></th>
                                    <th class="back-color">合同金额（元）</th>
                                    <th class="fade-type" colspan="2"><#if contractGoodsInfoVos.contractPrice??>${contractGoodsInfoVos.contractPrice}</#if></th>
                                </tr>
                                <tr>
                                    <th class="back-color">起运地</th>
                                    <th class="fade-type" colspan="2"><#if dispatchGoodsInfoVos.sendAddress??>${dispatchGoodsInfoVos.sendAddress}</#if></th>
                                    <th class="back-color">目的地</th>
                                    <th class="fade-type" colspan="2"><#if dispatchGoodsInfoVos.receiveAddress??>${dispatchGoodsInfoVos.receiveAddress}</#if></th>
                                </tr>
                                <tr>
                                    <th class="back-color">计划装车时间</th>
                                    <th class="fade-type" colspan="2"><#if dispatchGoodsInfoVos.sendTime??>${dispatchGoodsInfoVos.sendTime}</#if></th>
                                    <th class="back-color">计划卸车时间</th>
                                    <th class="fade-type" colspan="2"><#if dispatchGoodsInfoVos.receiveTime??>${dispatchGoodsInfoVos.receiveTime}</#if></th>
                                </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                    <br/>
                    <br/>
                </#list>
            </#if>
        </#if>
    </div>

    <#--只有运单合同，才显示油气费用提示-->
    <#if contract.documentType == 1133030 >
        <div>
            <label><p style="font-size: 12px; padding-left: 2em;">附：合同金额包含能源费用，以最终结算为准</p></label>
        </div>
    </#if>

    <#--合同内容-->
    <div class="contractContentStyle" style="margin-top: 5px;width: 100%;margin-bottom: 10px">
        <#if contract.content??>${contract.content}</#if>
    </div>

    <div class="header-userInfo">
        <div class="content-style">
            <strong>托运人：<#if contract.trustorCompany??>${contract.trustorCompany.name}</#if>（签章）</strong>
            <br/>
            <strong class="content-left-two">法定代表人或授权代表：<#if contract.trustorCompany??>${contract.trustorCompany.legalRepresentative}</#if></strong>
            <br/>
            <strong class="content-left-two">签订日期：${contract.contractDate?string("yyyy年MM月dd日")}</strong>
            <br/>
            <span class="content-left-two bottom-signer" style="padding-left: 100px;" >${firstKeyWord}firstKeyWord</span><#--签章坐标-->
        </div>
        <br/>
        <br/>
        <br/>
        <br/>
        <div class="content-style">
            <strong>承运人：<#if contract.carryCompany??>${contract.carryCompany.name}</#if>（签章）</strong>
            <br/>
            <strong class="content-left-two">签订日期：${contract.contractDate?string("yyyy年MM月dd日")}</strong>
            <br/>
            <span class="content-left-two bottom-signer" style="padding-left: 100px;">${secondKeyWord}secondKeyWord</span><#--签章坐标-->
        </div>
    </div>
    <br/>
    <br/>
    <div class="content-header">
        <div class="header-userInfo">
            <div>
                <span>附件：</span>
            </div>
            <br/>
            <div style="text-align: center;">
                <span style="font-size: 28px;">委托代开增值税发票协议</span>
            </div>
        </div>
    </div>
    <br/>

    <div class="header-userInfo">
        <div>
            <span>托运人全称：<#if contract.trustorCompany??>${contract.trustorCompany.name}</#if></span>
            <br/>
            <span>托运人统一社会信用代码：<#if contract.trustorCompany??>${contract.trustorCompany.unifiedSocialCreditIdentifier}</#if></span>
            <br/>
            <span>地址：<#if contract.trustorCompanyRegisteredAddress??>${contract.trustorCompanyRegisteredAddress}</#if></span>
        </div>
        <br/>
        <div>
            <span>承运人全称（姓名）：<#if contract.carryCompany??>${contract.carryCompany.name}</#if></span>
            <br/>
            <span>统一社会信用代码（身份证号）：<#if contract.carryCompany??>${contract.carryCompany.companyAdminInfo.idcardNo}</#if></span>
        </div>
    </div>

    <div class="content-header">
        <div style="margin-top: 10px;">
            <li style="list-style: none;line-height: 15.75pt">
                    <strong class="content-style-1">
                        <p>承运人通过托运人互联网物流平台（以下简称“平台”）向托运人提供货物运输服务。根据《中华人民共和国税收征收管理法》、《中  华人民共和国发票管理办法》、《税务机关代开增值税专用发票管理办法（试行）》、《国家税务总局关于发布&lt;货物运输业小规模纳税人申请代开增值税专用发票管理办法&gt;的公告》和《关于开展互联网物流平台企业代开增值税专用发票试点工作的通知》（税总函[2019]405号）等的有关规定，为方便办理代开增值税专用发票（以下简称专用发票）及相关涉税事项，经双方友好协商，达成以下协议：</p>
                        <p>一、承运人委托托运人到税务机关，办理其通过托运人互联网物流平台向托运人提供货物运输服务的代开发票相关涉税事项：</p>
                        <p style="padding-left: 2em">1、代开发票。</p>
                        <p style="padding-left: 2em">2、代缴相关税费，并取得完税凭证。</p>
                        <p>二、承运人向托运人明确，承运人向托运人提供的注册用户信息是真实的，承运人身份非增值税一般纳税人，申请代开发票对应的业务  是真实的，是通过托运人互联网物流平台承接的，并承诺承担相关法律责任。</p>
                        <p>三、托运人根据承运人委托，负责代承运人办理临时税务登记、代办代开专用发票相关手续，按照税务机关要求的格式向税务机关递交  代开发票申请，代承运人缴纳相关税费，取得发票和完税凭证明。承运人若需完税证明，托运人应及时转交。</p>
                        <p>四、因承运人提供的数据信息及资料不真实、不准确，或弄虚作假，由此造成的后果由承运人承担。</p>
                        <p>五、本协议应在承运人成为托运人互联网物流平台注册用户并采集相关信息后签署。本协议在托运人互联网物流平台以电子协议的形式  进行签署。协议可由双方一次性签署，长期有效。</p>
                        <p>六、承运人是代开专用发票的委托人、实际承运业务的纳税义务人，应承担代开发票应缴的相关税费。托运人应按代开专用发票实际发  生的税费代承运人缴纳。托运人不得向承运人额外收取其它代办涉税事项的费用。</p>
                        <p>七、承运人注册登记信息发生变化的，承运人应立即告知托运人，托运人收到承运人通知后，应立即进行信息修改，重新向税务机关申  请变更承运人税务登记信息。承运人已转为一般纳税人的，应在登记一般纳税人当日或之前，通知托运人中止代开专用发票协议。</p>
                        <p>八、托运人应根据承运人的委托，根据承运人在其平台上为其发生的业务，按税务机关要求的格式，形成申请代开专用发票数据清册。  托运人在向税务机关递交代开专用发票申请时，需提供上述数据清册。托运人应按照税务机关要求的格式，每月向税务机关传递在平台上交  易的信息。</p>
                    </strong>
            </li>
        </div>
    </div>



</div>
</body>
</html>