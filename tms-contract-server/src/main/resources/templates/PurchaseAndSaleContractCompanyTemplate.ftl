<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>购销合同</title>
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
        <h2>购销合同</h2>
        <div class="header-userInfo">
            <div>
                <strong class="header-num">合同编号：${contract.contractCode}</strong>
            </div>
            <br/>
            <div>
                <strong class="header-num">签订地点：<#if contract.signPoint??>${contract.signPoint}</#if></strong>
            </div>
        </div>
        <br/>
        <div class="header-userInfo">
            <div>
                <strong>甲方（供方）：<#if contract.firstPartyName??>${contract.firstPartyName}</#if></strong>
            </div>
            <div>
                <strong>乙方（需方）：<#if contract.secondPartyName??>${contract.secondPartyName}</#if></strong>
            </div>
        </div>
    </div>

    <div class="content-content">
        <div name="summernoteContent" type="hidden">
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				&nbsp;
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				<br>
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				依照《中华人民共和国民法典》，甲乙双方经友好协商，就乙方购买、安装甲方提供的车载定位终端及服务的事宜，并为保护双方的合法权益，双方在平等互利的基础上达成了一致，同意按照下列条款签订本合同。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				一、产品名称、型号规格、数量、单价、金额
			</span>
                </font>
            </p>
            <div>
                <br>
            </div>
        </div>
    </div>
    <div class="content-table">
        <table class="table-price">
            <tbody>
                <tr>
                    <th>序号</th>
                    <th>产品名称</th>
                    <th>规格型号</th>
                    <th>数量</th>
                    <th>单位</th>
                    <th>单价</th>
                    <th>含税金额（元）</th>
                    <th>发票税率</th>
                    <th>不含税金额（元）</th>
                    <th>税额（元）</th>
                    <th>备注</th>
                </tr>
                <#if contract.reportList??>
                    <#list contract.reportList as report>
                        <tr>
                            <td style="text-align: center;">${report_index+1}</td>
                            <td><#if report.productName??>${report.productName}</#if></td>
                            <td><#if report.model??>${report.model}</#if></td>
                            <td><#if report.count??>${report.count}</#if></td>
                            <td><#if report.unit??>${report.unit}</#if></td>
                            <td><#if report.price??>${report.price}</#if></td>
                            <td><#if report.taxInclusiveAmount??>${report.taxInclusiveAmount}</#if></td>
                            <td><#if report.tax??>${report.tax}%</#if></td>
                            <td><#if report.taxExclusiveAmount??>${report.taxExclusiveAmount}</#if></td>
                            <td><#if report.taxAmount??>${report.taxAmount}</#if></td>
                            <td><#if report.remark??>${report.remark}</#if></td>
                        </tr>
                    </#list>
                </#if>
                <tr>
                    <td colspan="11">合计：含税金额人民币<#if contract.total??>${contract.total}</#if>元整 ，其中不含税金额<#if contract.totalNoTax??>${contract.totalNoTax}</#if>元，税额<#if contract.totalTax??>${contract.totalTax}</#if>元</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="content-content">
        <div name="summernoteContent" type="hidden">
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				&nbsp;
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				<br>
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				二、产品运输、系统安装调试及费用负担
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、交货时间：<#if contract.deliveryTime??>${contract.deliveryTime}</#if>前
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、交货地点：<#if contract.deliveryBase??>${contract.deliveryBase}</#if>
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				3、产品运输及费用负担：甲方
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				4、系统安装及费用负担：甲方
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				三、产品质量技术标准、产品及系统验收期限
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、产品质量技术标准：按行业标准执行。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、产品（硬件）验收期限：产品到达交货地点后，由甲方负责将产品安装至乙方指定车辆。安装完成后，乙方当场对产品的数量、规格进行清点和核查，如无异议，视为产品验收合格。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				3、委托采集。OBD设备用于证明乙方在甲方网络货运平台承运业务的真实性，乙方同意授权甲方调取车辆的位置信息。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				四、绑定与解绑
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、如乙方更换安装车辆，需经甲方同意，并在甲方指导下进行安装、解绑与重新绑定。因乙方私自拆卸、移动致使产品损坏、影响正常使用的，由乙方承担相关责任。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				五、费用结算及发票
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、乙方应于本合同签署生效后2个工作日内向甲方支付本合同项下的费用。甲方收到款项后15个工作日内按照本合同第一条款项下产品名称以及对应税率向乙方开具增值税专用发票。如乙方继续使用，自第2年起（${contract.renewalTime}），乙方只需支付${contract.renewalFee}元/年/台的信息服务费。甲方收到款项后15个工作日内向乙方开具合法有效的、税率为6%的增值税专用发票。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、乙方向甲方支付全部费用后，甲方给乙方指定车辆安装部署、调试设备。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				3、付款方式：现汇
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				六、甲方责任与义务
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、所交货物的品种、数量、规格、质量不符合国家法律法规和合同规定的，甲方负责三个月内包换，并承担相关费用。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、甲方未在规定时间内为乙方指定车辆安装设备，每延期一天，赔偿合同总额5%违约金，并承担乙方因此所受的相关损失费用。违约金和相关损失费用的总数最多不超过合同总金额的30﹪。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				七、乙方责任与义务
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、乙方应确保车辆在行驶过程中全程保持设备正常开启。如乙方未按要求使用设备，导致甲方系统未能获取车辆全部行驶信息的，由此给甲方造成的损失，由乙方承担。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、乙方未按规定日期付款，每延期一天，应向甲方支付未付款总额5‰的违约金。
			</span>
                </font>
            </p><p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				3、因乙方原因中途变更产品数量、型号、规格或包装等，经双方协议同意变更的，乙方应偿付甲方变更部分货款总额5％的违约金。因乙方原因中途退货，由双方根据实际情况商定，同意退货的乙方向甲方偿付退货部分货款总额20﹪的违约金。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				八、不可抗力
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				甲、乙双方任何一方由于不可抗力原因（包括自然和人为的不可抗力原因，如战争、水灾、台风、地震、火灾等）不能履行合同时，应及时以书面方式向对方通报不能履行或不能完全履行的理由，以减轻可能给对方造成的损失，在取得有关机构证明后，允许延期履行、部分履行或不履行合同，并根据情况可部分或全部免予承担违约责任。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				九、售后服务
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、甲方对所售出的车载定位终端，在无人为损坏的情况下，均承诺三年包修，三年后收取成本费维修。
			</span>
                </font>
            </p><p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、甲方负责对乙方车辆使用人员进行技术培训，并提供7ｘ24小时免费咨询服务。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				3、在系统运行过程中如果出现硬件故障，乙方寄回需返修的产品，相关费用由乙方承担。如果经确认属于人为破坏或拆除，更换设备或安装设备需乙方承担费用。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				4、甲方技术部提供7ｘ24小时的响应服务。
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				十、争议解决
			</span>
                </font>
            </p>
            <p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				执行合同中，发生争议和纠纷，签约双方协商不成，均可向签约地法院提出诉讼。
			</span>
                </font>
            </p><p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				十一、其它条款
			</span>
                </font>
            </p><p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				1、本协议未尽事宜由双方协商解决并签署相应的补充协议。补充协议与合同原件具有同等法律效力。
			</span>
                </font>
            </p><p class="p" style="margin: 0pt 0pt 6pt; text-indent: 0pt; line-height: 15.75pt; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial;">
                <font face="Helvetica">
			<span style="font-size: 10.6667px;">
				2、本协议自双方盖章之日生效，一式五份，甲方持三份，乙方持两份，具有同等法律效力。
			</span>
                </font>
            </p>
            <div>
                <br>
            </div>
        </div>
    </div>
    <div class="content-footer">
        <div class="bottom-user">
            <strong class="left-user">甲方：${contract.firstPartyName}</strong>
            <strong class="right-user">乙方：${contract.secondPartyName}</strong>
        </div>
        <br/>
        <div class="bottom-user">
            <strong class="left-user">${contract.firstPartySignTime}</strong>
            <strong class="right-user">${contract.secondPartySignTime}</strong>
        </div>
    </div>
</div>
</body>
</html>