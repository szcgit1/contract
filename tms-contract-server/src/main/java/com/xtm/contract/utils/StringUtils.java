package com.xtm.contract.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 *
 * string工具类
 *
 * @author xll
 * @version 1.0
 * @date 2021/6/15 14:09
 */
public class StringUtils {

    /**
     * 字符串为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return !isNotEmpty(s);
    }

    /**
     * 字符串不为空
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }


    public static boolean isNotEmpty(String[] str) {

        if (str != null && str.length > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取字符串位数（中文占2位，其它1位）
     */
    public static int getStrCharLen(String str) {
        int length = 0;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }

        }
        return length;
    }


    public static void main(String[] args) {
        String str = "12345bcd为中华";
        int strCharLen = getStrCharLen(str);
        System.out.println(strCharLen);
    }
}

