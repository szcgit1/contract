package com.xtm.contract.constant;


/**
 * @author LiJingzhi  @date 2021/06
 **/
public class CoreLanguage {
    public static ThreadLocal<String> errorThreadLocal = new InheritableThreadLocal<>();
    public static String mysqlUrl = null;
    public static String mysqlUsername = null;
    public static String mysqlPassword = null;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int OKCODE=600000;
    public static final String hibernateError= "org.springframework.validation.BindException";
    public static final String EMPTY_ERROR = "你的状态码在数据库里面找不到对应中文提示信息，请在数据库的表 xn_static_language表里面手动添加数据";
    public static final String ZH = "zh";
    public static final String EN = "en";
}
