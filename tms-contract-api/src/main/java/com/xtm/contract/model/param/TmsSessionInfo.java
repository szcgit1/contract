package com.xtm.contract.model.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 沿用老系统sessionInfo对象
 * @author zhang
 */
@Data
public class TmsSessionInfo
{
	/**
	 * 用户来源
	 */
	private String userAgent;

	/**
	 * 用户ID
	 */
	private String id;

	/**
	 * 用户名称
	 */
	private String name;


	/**
	 * 手机号码
	 */
	private String mobile;

	private String openId;
	/**
	 * 企业ID
	 */
	private String companyId;

	/**
	 * 企业名称
	 */
	private String companyName;

	private String headPhotoId;

	/**
	 * agentCode
	 */
	private String agentCode;

	/**
	 * 是否为平台
	 */
	private Boolean platform;

	/**
	 * 平台公司ID
	 */
	private String platformCompanyId;

	/**
	 * 用户类目
	 */
	private Long userCategory;

	/**
	 * 企业角色
	 */
	private List<Long> organTypeIds;

	/**
	 * 自己及分公司ID add by sq
	 */
	private List<String> arrAllCompanyID;

	/**
	 * 根公司
	 */
	private String rootCompanyID;

	/**
	 * 用户拥有权限的组织/岗位
	 */
	private List<String> arrUserAuthOrgId;

	/**
	 * 根公司下面的所有组织
	 */
	private List<String> arrRootCompanyOrgId;

	/**
	 * 平台公司ID
	 */
	private String companyAgentId;

	/**
	 * 平台公司代码
	 */
	private String companyAgentCode;

	/**
	 * 公司管理员ID
	 */
	private String companyAdmin;

	/**
	 * 客户端IP
	 */
	private String clientIp;

	public void setPlatform(Boolean platform) {
		if(StringUtils.isEmpty(this.companyId)){
			this.platform = false;
		}else {
			this.platform = this.companyId.equals(this.platformCompanyId);
		}
	}

	public String getName() {
		return asciiToString(name);
	}


	public String getCompanyName() {
		return asciiToString(companyName);
	}

	/**
	 * 将Ascii表达的字符串转换为UTF-8
	 *
	 * @param originStr
	 * @return
	 */
	public static String asciiToString(String originStr) {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(originStr)) {
			boolean isFirstAscii = originStr.indexOf("\\u") == 0 ? true : false;

			String[] asciiStr = originStr.split("\\\\u");
			for (int i = 0; i < asciiStr.length; i++) {
				if (i == 0) {
					if (isFirstAscii){
						continue;
					}
					else{
						sb.append(asciiStr[i]);
					}
				} else {
					sb.append((char)Integer.parseInt(asciiStr[i].substring(0, 4), 16));
					sb.append(asciiStr[i].substring(4));
				}
			}
		}

		return sb.toString();
	}
}
