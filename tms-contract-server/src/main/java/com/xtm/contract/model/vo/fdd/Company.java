package com.xtm.contract.model.vo.fdd;

import lombok.Data;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  20:19
 *@Description: 公司信息
 *@title: Company
 */
@Data
public class Company{
	/**
	 * 关联法人/代理人交易号  此字段为法大大内部使用，可忽略
	 */
	private String relatedTransactionNo;
	/**
	 * 营业执照图片uuid
	 */
	private String organizationPath;

	/**
	 * 企业名称
	 */
	private String companyName;

	/**
	 * 不通过原因
	 */
	private String auditFailReason;

	/**
	 * 是否有代理人，
	 * 1：代理人
	 * 0：法人
	 */
	private String hasagent;
	/**
	 * 法人姓名
	 */
	private String legalName;
	/**
	 * 组织类型
	 * 0：企业；
	 * 1：政府/事业单位；
	 * 2：其他组织；
	 * 3：个体工商户
	 */
	private String organizationType;
	/**
	 * 审核时间  yyyy-MM-dd HH:mm:ss.0
	 */
	private String auditorTime;
	/**
	 * 企业信息登记表图片uuid	企业认证申请表
	 */
	private String regFormPath;
	/**
	 * 企业邮箱
	 */
	private String companyEmail;

	/**
	 * 统一社会信用代码(多合一证件)
	 */
	private String organization;
	/**
	 * 法人身份证号
	 */
	private String legal;
	/**
	 * 法人授权,法人手机号	法人授权认证才会有值
	 */
	private String legalMobile;
	/**
	 * 认证方式：
	 * 0，对公打款认证；
	 * 1，纸质审核认证
	 * 2，法人认证
	 * 4，法人授权认证
	 */
	private String verifyType;
	/**
	 * 证件类型，0，统一社会信用代码；1，普通营业执照；
	 * 当organizationType为3时且certificatesType为0，个体工商户以企业身份去申请证书，certificatesType为1时，个体工商户以其他组织身份证申请证书
	 */
	private String certificatesType;
	/**
	 * 0：未认证；
	 * 1：管理员资料已提交；
	 * 2：企业基本资料(没有申请表)已提交；
	 * 3：已提交待审核；
	 * 4：审核通过（认证完成）；
	 * 5：审核不通过；
	 * 6人工初审通过（认证未完成，还需按提示完成接下来的操作）
	 */
	private String status;
}
