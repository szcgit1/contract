package com.xtm.contract.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共变量定义
 *
 * @author wumenghua
 * @version 1.0
 */
public class Constant
{
	/** 表示错误结果编码 */
	public final static Integer ERROR_CODE = -1;

	public static final String DEFAULT_VALUE = "——";//默认值;

	/**
	 * 销售合同历史记录在mongo中的模块名
	 */
	public static final String TMS_CONTRACT_SALES_CONTRACT_MODULE = "tms_contract_sales_contract";

	/**
	 * 框架合同协议历史记录在mongo中的模块名
	 */
	public static final String TMS_CONTRACT_FRAME_AGREEMENT_MODULE = "tms_contract_frame_agreement";

    public final static class BATCH_STATUS{
		public static int FAIL = 999;
		public static int SUCCESS = 1;
		public static int READY = 0;
	}

	/**
	 * 导出类型
	 */
	public final static class EXPORT_TYPE{
		public final static String FDD_DETAIL = "fddDetail";//法大大详情

		public final static String FDD_AUTH_MONTH = "fddAuthMonth";//法大大实名认证月汇总

		public final static String FDD_CONTARCT_MONTH = "fddContarctMonth";//法大大合同签署月汇总
	}

	/**
	 * 保存销售合同锁key
	 */
	public static final String SAVE_UPDATE_SALES_CONTRACT_LOCK_KEY = "save_update_sales_contract_lockkey:";
	/**
	 * 保存框架合同协议锁key
	 */
	public static final String SAVE_UPDATE_FRAME_AGREEMENT_LOCK_KEY = "save_update_frame_agreement_lockkey:";

	/**
	 * 业务操作历史记录--mongo
	 */
	public static final String BUSINESS_HISTORY_MONGO_COLLECTION_NAME = "xn_m_business_history";

	/**
	 * 框架合同协议对应基础名称
	 */
	public final static class Frame_Agreement_Basic_Name{
		public final static String SHIPPING = "发运组织";

		public final static String CUSTOMER = "客户";

		public final static String SALE_ORG = "销售组织";
	}

	/**
	 * 框架合同关联销售合同来源 0: NC推送销售合同自动匹配框架合同 1: 页面手动操作 框架合同关联销售合同
	 */
	public final static class Frame_Agreement_Relate_Sales_Contract_Source{
		public final static int NC_PUSH = 0;
		public final static int MANUAL = 1;
	}
}

