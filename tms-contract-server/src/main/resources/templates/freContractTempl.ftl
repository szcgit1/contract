<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8"/>
	<title>框架合同</title>
	<style type="text/css">
		@page{size:a4 }
		html {
			font-family:  SimSun;
		}

		.header-num {
            float: right;
			margin-bottom: 15px;
        }
		.label-contact {
			margin-top: 15px;
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
			padding: 20px 40px;
		}

		.content-header h2 {
			text-align: center;
			clear: both;
		}

		.content-content {
			padding-top: 30px;
			clear: both;
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

		.content-table .last-table th {
			border-top: none;
		}

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
			color:#DDD;
			text-align: center
		}

		.table-price {
			margin-top: 30px;
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
			margin-top: 15px;
		}
		.left-user {
			float: left;
		}
		.right-user {
			float: right;
		}
		.bottom-signer {
			color: #FFFFFF;
			padding-top: 20px;
		}
		.left-keyword {
			padding-left: 100px;
		}
		.right-keyword {
			float: right;
			padding-left: 100px;
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
				<span class="label-contact">联系人：<#if contract.trustorCompany.contact??>${contract.trustorCompany.contact.name}</#if><#if contract.trustorCompany.contact.mobile??><b class="kindent">${contract.trustorCompany.contact.mobile}</b></#if></span>
			</div>
			<div>
				<strong>承运人：<#if contract.carryCompany??>${contract.carryCompany.name}</#if></strong>
				<span class="label-contact">联系人：<#if contract.carryCompany.contact??>${contract.carryCompany.contact.name}</#if><#if contract.carryCompany.contact.mobile??><b class="kindent">${contract.carryCompany.contact.mobile}</b></#if></span>
			</div>
		</div>
	</div>
	<div class="content-content">
		<#if contract.content??>${contract.content}</#if>
	</div>
	<div class="content-footer">
		<div>
			<label>
				<strong>
					本合同的签署日期为<u> ${contract.contractDate?string("yyyy年MM月dd日")}</u> 。
					有效期自<u> ${contract.validStartDate?string("yyyy年MM月dd日")}</u>&nbsp;起，至<u> ${contract.validEndDate?string("yyyy年MM月dd日")}</u>。
				</strong>
			</label>
		</div>
		<div class="bottom-user">
			<strong class="left-user">托运人：${contract.trustorCompany.name}</strong>
			<strong class="right-user">承运人：${contract.carryCompany.name}</strong>
		</div>
		<div class="bottom-signer">
			<span class = "left-keyword">${firstKeyWord}</span>
			<span class="right-keyword">${secondKeyWord}</span>
		</div>
	</div>
</div>
</body>
</html>