package com.xtm.contract.enums;

public class DicConstant {

    public final static class ORGANIZATION_TYPE {
        /**
         * 自然人
         */
        public final static Integer NATURAL_PERSON = 2091000;
        /**
         * 法人
         */
        public final static Integer LEGAL_PERSON = 2091010;
        /**
         * 平台
         */
        public final static Integer PLATFORM = 2091020;
    }

    /**
     * 找车费用汇总单模板
     */
    public static final String FIND_CAR_CHARGE_SUMMARY_TEMPL = "findCarChargeSummaryTempl.ftl";
    /**
     * 卸车费用汇总单模板
     */
    public static final String UNLOAD_CAR_CHARGE_SUMMARY_TEMPL = "uploadCarChargeSummaryTempl.ftl";

    /**
     * 技术服务费用汇总单模板
     */
    public static final String SERVICE_CHARGE_SUMMARY_TEMP = "serviceChargeTemp.ftl";
    public static class CONTRACT_SIGN_STATUS {

        /**
         * 待签署
         */
        public static final Integer WAIT = 4830010;

        /**
         * 签署成功
         */
        public static final Integer SUCCESS = 4830020;

        /**
         * 签署失败
         */
        public static final Integer FAILURE = 4830030;

        /**
         * 签署过期
         */
        public static final Integer EXPIRED = 4830040;

        public CONTRACT_SIGN_STATUS() {
        }
    }


    public static class DOCUMENT_TYPE {
        //订单
        public final static Integer ORDER = 1133000;

        public final static Integer DISPATCH_BATCH = 1133030;

        // 合同
        public final static Integer CONTRACT = 1133380;

        // 能源单
        public final static Integer ENERGY_BILL = 1133410;
        // 找车费用
        public final static Integer FIND_CAR_CHARGE = 1133420;
        // 卸车费用
        public final static Integer UNLOAD_CAR_CHARGE = 1133440;
        //联合运单
        public final static Integer JOINT_DISPATCH_BATCH = 1133450;
        // 服务费
        public final static Integer SERVICE_CHARGE = 1133470;

    }

    /**
     * 合同业务性质
     */
    public final static class CONTRACT_BUSINESS_TYPE {
        /**
         * 三方合同
         */
        public static final Integer TRIPARTITE = 5021000;
        /**
         * 双方合同
         */
        public static final Integer BOTH = 5021010;
    }

    /**
     * 是否已删除
     */
    public static class IS_DELETE {
        public final static Integer NO = 0;
        public final static Integer YES = 1;
    }

    public final static class ENABLE_STATUS {
        /** 启用 */
        // public final static Long ENABLED = 202100L;
        /** 禁用 */
        // public final static Long DISABLED = 202101L;
        /**
         * 启用
         */
        public final static Integer ENABLED = 1;
        /**
         * 禁用
         */
        public final static Integer DISABLED = 0;
    }

    /**
     * 物流合同协议 虚拟年度协议标识 0：否 1：是
     */
    public final static class LOGIS_VIRTUAL_TAG {
        /**
         * 否
         */
        public final static Integer NO = 0;

        /**
         * 是
         */
        public final static Integer YES = 1;
    }

    /**
     * 物流合同协议 系统来源 0:tms 1:nc 2:新网
     */
    public final static class LOGIS_CONTRACT_AGREEMENT_SYSTEM_SOURCE {

        /**
         * tms
         */
        public final static Integer TMS = 0;

        /**
         * nc
         */
        public final static Integer NC = 1;
    }

    /**
     * 框架合同 业务来源 0:丰南 1:中铁 2:中重 99:本平台
     */
    public final static class LOGIS_CONTRACT_AGREEMENT_BUSI_SOURCE {

        /**
         * 本平台
         */
        public final static Integer tms = 99;

        /**
         * NC
         */
        public final static Integer NC = 98;

        /**
         * 丰南
         */
        public final static Integer FenfNan = 0;

        /**
         * 中铁
         */
        public final static Integer Zhongtie = 1;

        /**
         * 中重
         */
        public final static Integer Zhongzhong = 2;

    }


    //合同类型
    public final static class CONTRACT_TYPE {
        //框架合同
        public final static Integer FRAMEWORK_CONTRACT = 2961010;
        //明细合同
        public final static Integer DETAILED_CONTRACT = 2961020;
        //授权协议
        public final static Integer LICENSE_AGREEMENT = 2961030;
        //联合运单合同
        public final static Integer JOINT_CONTRACT = 2961040;
        //批量运单合同
        public final static Integer BATCH_CONTRACT = 2961050;
    }

    //增值业务代码
    public final static class VAS_CODE {
        //票根网
        public final static Integer STUB_NETWORK = 4160010;
        //平安保险
        public final static Integer PAN_INSURANCE = 4160020;
        //众签数字签章
        public final static Integer NUMBER_SIGN = 4160070;
        //富有保险增值业务代码
        public final static Integer INSURANCE_VAS_CODE = 4160030;
        //e签宝数字签章
        public final static Integer EQB_NUMBER_SIGN = 4160100;
    }

    /**
     * 众签相关配置自定义常量
     */
    public final static class ZQSIGN_DEFINITION{

        /**框架合同模版*/
        public static final String FRA_CONTRACT = "freContractTempl.ftl";
        /**明细合同模版*/
        public static final String DTL_CONTRACT = "contractTempl.ftl";
        /**明细合同模版*/
        public static final String TRIPARTITE_DTL_CONTRACT = "tripartiteContractTempl.ftl";
    }


    public final static class CERTIFICATION_STATE {
        /**
         * 未认证
         */
        public final static Integer NO = 0;
        /**
         * 已认证
         */
        public final static Integer YES = 1;
        /**
         * 认证中
         */
        public final static Integer DOING = 2;
    }


    public static class WEIGHT_UNIT {
        /**
         * 吨
         */
        public final static Integer TON = 1002000;
        /**
         * 公斤
         */
        public final static Integer KILO = 1002010;
    }

    public static class VOLUME_UNIT {
        /**
         * 立方米
         */
        public final static Integer CUBIC_METER = 1001000;
        /**
         * 升
         */
        public final static Integer LITRE = 1001010;
    }

    /**
     * 通用
     */
    public static final Integer COMMON_ZERO = 0;

    public static class MEMBER_TYPE {
        /**
         * 托运人
         */
        public final static Integer TRUSTOR = 2505010;
        /**
         * 收货单位
         */
        public final static Long TRADER = 2505020L;
        /**
         * 承运人
         */
        public final static Long CARRIER = 2505030L;
    }

    /**
     * 公共数字枚举
     */
    public enum NumEnums {

        NUM_ZERO(0,0,0,"0","数字0"),
        NUM_ONE(1,1,1,"1","数字1"),
        NUM_TWO(2,2,2,"2","数字2"),
        NUM_THREE(3,3,3,"3","数字3"),
        NUM_FOUR(4,4,4,"4","数字4"),
        NUM_FIVE(5,5,5,"5","数字5"),
        NUM_SIX(6,6,6,"6","数字6"),
        NUM_SEVEN(7,7,7,"7","数字7"),
        NUM_EIGHT(8,8,8,"8","数字8"),
        NUM_NINE(9,9,9,"9","数字9"),
        NUM_TEN(10,10,10,"10","数字10"),
        NUM_ELEVEN(11,11,11,"11","数字11")
        ;

        /**
         * integer 枚举值
         */
        private Integer integerValue;
        /**
         * int 枚举值
         */
        private int intValue;
        /**
         * long 枚举值
         */
        private Integer longValue;
        /**
         * string  枚举值
         */
        private String stringValue;
        /**
         * 描述
         */
        private String msg;

        public Integer getIntegerValue() {
            return integerValue;
        }

        public void setIntegerValue(Integer integerValue) {
            this.integerValue = integerValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public Integer getLongValue() {
            return longValue;
        }

        public void setLongValue(Integer longValue) {
            this.longValue = longValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        NumEnums(Integer integerValue, int intValue, Integer longValue, String stringValue, String msg) {
            this.integerValue = integerValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.stringValue = stringValue;
            this.msg = msg;
        }

        /**
         * 根据code返回枚举类型,主要在switch中使用
         */
        public static DicConstant.NumEnums getByCode(Integer value) {
            for (NumEnums code : values()) {
                if (code.getIntegerValue().equals(value)) {
                    return code;
                }
            }
            return null;
        }

        /**
         * 根据code返回msg
         */
        public static String getMsgByCode(Integer value) {
            for (DicConstant.NumEnums code : values()) {
                if (code.getIntegerValue().equals(value)) {
                    return code.getMsg();
                }
            }
            return null;
        }
    }
}
