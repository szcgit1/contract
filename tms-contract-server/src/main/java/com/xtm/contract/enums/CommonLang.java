package com.xtm.contract.enums;

import com.xtm.contract.constant.CoreLanguage;

import java.util.Locale;

/**
 * @author LiJingzhi  @date 2021/06
 *
 * 正确 code 规范： 前三位 全部是 200，表示 正确状态码，后面两位依次递增。 一共是5位数
 * 错误 code 规范： 前三位 全部是 500，表示 错误状态码，后面两位依次递增。 一共是5位数
 * 枚举规范：成功的用 _SUCCESS 做为后缀，失败的 用 _FAIL作为后缀，异常提示语不触发异常机制的用 _WARN 后缀
 **/
public enum CommonLang {
    // 作为参数用在 XiaoniuResult.of(CommonLang lang) 方法里面
    SUCCESS(200001,"操作成功","Operation is successful"),
    IMPORT_SUCCESS(200002,"导入成功","Import success"),
    UPLOAD_SUCCESS(200003,"上传成功","upload success"),
    PARAM_VALID_SUCCESS(200004,"参数校验成功","PARAM VALID SUCCESS"),
    UPDATE_SUCCESS(200005,"修改成功","UPDATE SUCCESS"),
    INSERT5_SUCCESS(200006,"新增成功","INSERT5 SUCCESS"),
    QUERY_SUCCESS(200007,"查询成功","QUERY SUCCESS"),
    PUBLISH_SUCCESS(200008,"发布成功","PUBLISH SUCCESS"),
    COLLECTION_SUCCESS(200009,"集合成功","COLLECTION SUCCESS"),
    UN_COLLECTION_SUCCESS(200010,"解除集合成功","UN COLLECTION SUCCESS"),
    ADD_SUCCESS(200011,"添加成功","ADD SUCCESS"),
    REMOVE_SUCCESS(200012,"移除成功","REMOVE SUCCESS"),
    AUDIT_SUCCESS(200013,"审核成功","AUDIT SUCCESS"),
    QUOTA_SUCCESS(200014,"报价成功","QUOTA SUCCESS"),
    ENTRUST_SUCCESS(200015,"委托成功","ENTRUST SUCCESS"),
    CARRIAGE_SUCCESS(200016,"承运成功","CARRIAGE SUCCESS"),
    DISPATCH_SUCCESS(200017,"调度成功","DISPATCH SUCCESS"),
    DELETE_SUCCESS(200018,"删除成功","Delete the success"),
    GOODS_INVALID_SUCCESS(200019,"货源作废成功","Supply cancelled successfully"),
    EXPORT_SUCCESS(200020,"导出成功","Export success"),
    ORDER_SAVE_SUCCESS(200021,"订单保存成功","Order saved successfully"),
    ORDER_DELETE_SUCCESS(200022,"订单删除成功","Order deleted successfully"),
    ORDER_VOID_SUCCESS(200023,"订单作废成功","The order was cancelled successfully"),
    ORDER_BALANCE_SUCCESS(200024,"订单余量完结成功","Order margin completed successfully"),
    ORDER_FINISH_SUCCESS(200025,"订单手动完结成功","Manual order completion succeeded. Procedure"),
    ORDER_SUBCONTRACT_SUCCESS(200026,"订单转包成功","Order subcontracted successfully"),
    ORDER_CHANGE_SUCCESS(200027,"修改结算价成功","Succeeded in modifying the settlement price"),
    ORDER_EXPORT_SUCCESS(200028,"订单导出成功","Order export successful"),


    // JDK系统异常 用在全局异常类里面，开发人员不需要添加此异常 由于是 JDK 系统异常里的运行时异常，一旦页面发生此异常，需要后端程序员或者是前端程序员修复，
    SYS_FAIL(500100,"系统错误","System error"),OPERATE_FAIL(500102,"操作失败","The operation failure"),
    NULL_POINT_FAIL(500103,"系统错误(空指针异常)","System error (null pointer exception)"),
    NUMBER_FORMAT_FAIL(500105,"系统错误(数字转换异常)","System error (digital conversion exception)"),
    DATABASE_SQL_FAIL(500106,"系统错误(数据库SQL异常)","System error (database SQL exception)"),
    PARAM_VALID_FAIL(500107,"参数校验失败","Parameter verification failure"),
    ARITHMETIC_FAIL(500108,"算术异常","Arithmetic exception"),
    ARRAYINDEXOUTOFBOUNDS_FAIL(500109,"数组下标越界异常","Array index out of bounds exception"),
    CLASSCAST_FAIL(500110,"类型强制转换异常","Type cast exception"),
    FILENOTFOUND_FAIL(500111,"系统错误(文件未找到异常)","System error (file not found exception)"),
    SQL_FAIL(500116,"系统错误(操作数据库异常）","System error (operating database exception)"),
    IOEXCEPTION_FAIL(500117,"系统错误(输入输出异常)","System error (input/output exception)"),
    NOSUCHMETHOD_FAIL(500118,"系统错误(方法未找到异常)","System error (method does not find exception)"),
    NOCLASSDEFFOUND_FAIL(500119,"系统错误(未找到类定义错误)","System error (method does not find exception)"),
    INTERRUPTED_FAIL(500120,"系统错误(线程打断异常)","System error (thread interruption exception)"),
    NOSUCHFIELD_FAIL(500121,"系统错误(不存在的属性异常)","System error (non-existent attribute exception)"),
    UNSUPPORTEDOPERATION_FAIL(500122,"系统错误(不支持的方法异常)","System error (unsupported method exception)"),
    TYPENOTPRESENT_FAIL(5000123,"系统错误(类型不存在异常)","System error (type without exception)"),
    STRINGINDEXOUTOFBOUNDS(500124,"系统错误(字符串索引越界异常)","System error (string index out of bounds exception)"),
    ILLEGALACCESS_FAIL(5000125,"系统错误(违法的访问异常)","System error (illegal access exception)"),
    SERVLET_FAIL(500126,"系统错误(SERVLET异常)","System error (SERVLET exception)"),
    HIBERNATE_FAIL(500127,"系统错误(参数验证异常)","System error (parameter validation exception)"),



    a0001(-1,"货源编号获取失败","Failed to obtain source number"),
    a0002(-1,"当前状态不能作废","Current status cannot be voided"),
    a0003(-1,"转发布货源,再次重定向就不在允许,再次转发布","republish the source of goods. Re redirection is not allowed. republish"),
    a0004(-1,"存在报价的货源,禁止修改审核状态","It is forbidden to modify the approval document when downward inquiry calls the goods source of order with different quotation"),
    a0005(-1, "货主不能进行报价","he consignor cannot quote"),
    a0006(-1,"当前公司已存在报价","There is a quotation in the current company"),
    a0007(-1,"已经超过竞价截止时间,不在允许报价","The bidding deadline has been exceeded, and no quotation is allowed"),
    a0008(-1,"审核未通过,禁止报价","No quotation if the audit fails"),
    a0009(-1, "当前状态禁止报价", "Quotation forbidden in current status"),
    a0010(-1,"当前公司不存在报价信息,不允许修改","Quotation information does not exist in the current company and cannot be modified"),
    a0011(-1,"当前非报价状态,禁止修改","The current non quotation status cannot be modified"),
    a0012(-1,"已经超过竞价截止时间,不在允许修改","It has exceeded the deadline of bidding and cannot be modified"),
    a0013(-1,"货源剩余量不够进行进行承运或者委托生成订单","The remaining quantity of the goods source is not enough to carry or entrust to generate orders"),
    a0014(-1,"向下询价调用订单异常","Inquiry down call order exception"),
    a0015(-1,"扩展字段枚举类或者多枚举id不能为空","The extended field enumeration class or multiple enumeration ID cannot be empty"),
    a0016(-1,"页面已经过期，请重新刷新页面！","The page has expired, please refresh the page again!"),
    a0017(-1,"实际出发时间不能早于调度时间","The actual departure time cannot be earlier than the scheduled time"),
    a0018(-1,"实际到货时间不能早于调度时间","The actual arrival time cannot be earlier than the scheduled time"),
    a0019(-1,"实际到货时间不能早于出发时间","The actual arrival time should not be earlier than the departure time"),
    a0020(-1,"已装运的单子不可再次装运","A list already shipped cannot be shipped again"),
    a0021(-1,"登录异常，请输入验证码！","Login exception, please enter verification code!"),
    a0022(-1,"平台已限定不能下单给未认证的司机！","The platform has been restricted from placing orders to uncertified drivers!"),
    a0023(-1,"晚上8点后支付订单，请明天再试","Order will be paid after 8pm, please try again tomorrow"),
    a0024(-1,"用户名或密码输错次数过多，请15分钟后重试！","Too many incorrect user names or passwords have been entered. Please try again 15 minutes later.");



    private int code;
    private String message;
    private String enMessage;

    CommonLang(int code, String message, String enMessage) {
        this.code = code;
        this.message = message;
        this.enMessage = enMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEnMessage() {
        return enMessage;
    }

    public void setEnMessage(String enMessage) {
        this.enMessage = enMessage;
    }
    public String translateMessage(Locale locale){
        String lastVal = null;
        String language = locale.getLanguage();
        CommonLang[] langs = CommonLang.values();
        for (int i = 0; i < langs.length; i++) {
            CommonLang comm = langs[i];
            if(comm.name().equals(this.name())){
                if(language.equals(CoreLanguage.ZH)){
                    lastVal = comm.getMessage();
                }else {
                    lastVal = comm.getEnMessage();
                }
            }
            break;
        }
        return lastVal;
        /*Map<String, String> props = LOCAL_CACHE_STATIC.get(language);
        if (null != props && props.containsKey(lang)) {
            return props.get(lang);
        } else {
            //修改从内存拿不到数据则分成功和失败默认两个返回消息
            return CoreLanguage.EMPTY_ERROR;
        }*/
    }


}
