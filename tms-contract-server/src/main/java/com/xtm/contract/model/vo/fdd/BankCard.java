package com.xtm.contract.model.vo.fdd;

import lombok.Data;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  20:19
 *@Description: 银行信息
 *@title: Company
 */
@Data
public class BankCard{
	/**
	 *
	 * 联行号
	 */
	private String branchBankCode;
	/**
	 * 打款随机码
	 * 在企业认证方式为对公打款 且认证通过后 且打款方式为随机码才有值
	 */
	private String verifyCode;
	/**
	 * 银行卡号
	 */
	private String bankCardNo;
	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 打款类型
	 * 1.打款金额;
	 * 2.打款随机码
	 * 在企业认证方式为对公打款且认证通过后才有值
	 */
	private String payType;
	/**
	 * 开户市
	 */
	private String cityName;
	/**
	 * 支行名称
	 */
	private String bankDetailName;
	/**
	 * 开户省
	 */
	private String provinceName;
	/**
	 * 打款时间 yyyy-MM-dd HH:mm:ss  打款状态为1已打款时有值
	 */
	private String enterTime;
	/**
	 * 用户回填打款随机码
	 */
	private String enterVerifyCode;
	/**
	 * 打款状态
	 * 0-未打款 1-已打款 2-打款中。
	 * 认证方式为对公打款且认证状态为3-已提交待审核或6-人工初审通过时有值
	 */
	private String status;
}
