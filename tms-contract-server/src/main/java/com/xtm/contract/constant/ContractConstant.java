package com.xtm.contract.constant;


public interface ContractConstant {

    interface ContractType {
        Integer FDD = 1;
        Integer ECB = 0;
    }

    interface SignType {
        Integer FDD = 1;
        Integer ECB = 0;
    }

    interface signKeyword{
        String CYR="承运人：";
        String TYR="托运人：";
        String FIRST_PARTY="甲方（签章）";
        String SECOND_PARTY="乙方（签章）";
    }

    interface costKeyword{
        String auth="auth";
        String contract="contract";
    }
    interface AuthResultType{
        String success="认证通过";
        String fail="认证不通过";
    }

    interface signKeywordSettleBill{
        String GF="供方：";
        String XF="需方：";
    }

    // 能源单
    public final static Integer ENERGY_BILL = 1133410;
    // 尊俊能源月账单
    public final static Integer ENERGY_BILL_MONTH = 1133440;

    /**
     * 查询合同定时任务参数
     */
    public final static String CONTRACT_DATE_PARAM = "contract_date_param";
    public final static String CONTRACT_DELETE_API = "contract_delete.api";
    /**
     * 法大大解绑客户编号
     */
    public final static String UNBIND_CUSTOMERID_API = "unbind_customerId.api";

    /**
     * 业务类型 枚举 BusinessTypeEnum.java
     * NON_STEEL(1, "非钢"),
     * BOARD(2, "卷板"),
     * TRANSFER(3, "转库"),
     * TWO_FACTORY_SALE(4, "两厂采销");
     */
    public interface BusinessType {
        Integer NON_STEEL = 1;
        Integer BOARD = 2;
        Integer TRANSFER = 3;

        Integer TWO_FACTORY_SALE = 4;
    }







    /**
     * 是否是两厂采销 0:否 1:是
     */
    public interface TwoFactoryBusiness {
        Integer NO = 0;
        Integer YES = 1;
    }

    /**
     * 计划方向 0：销售 1：采购 3：地址方向
     */
    public interface PlanDirection {
        Integer SALE = 0;
        Integer PURCHASE = 1;
    }

    /**
     * 加急 0:否 1:是
     */
    public interface Urgent {
        Integer NO = 0;
        Integer YES = 1;
    }

    /**
     * 销售订单计划类型 0：总量 1：单量
     */
    public interface LogisticsOrdersPlanType {
        Integer QUANTITY = 0;
        Integer SINGLE = 1;
    }

    /**
     * 长短途 0:长途 1：短途 2：无定义
     */
    public interface LongShortTerm {
        Integer LONG = 0;
        Integer SHORT = 1;
        Integer UNDEFINED = 2;
    }

    /**
     * 运费承担方 0：自担：贸易公司/基地 1：自提：客户 2：回结：三方代收代付
     */
    public interface FreightBearer {
        Integer SELF_CARRY = 0;
        Integer SELF_PICKUP = 1;
        Integer THREE_PARTY_COLLECT = 2;
    }

    /**
     * 0:禁用 1:启用
     */
    public interface Disabled {
        Integer DISABLED = 0;
        Integer ENABLED = 1;
    }

    /**
     * 枚举类 价格类型 0：价证协议 1：锁价 2：合同
     */


}
