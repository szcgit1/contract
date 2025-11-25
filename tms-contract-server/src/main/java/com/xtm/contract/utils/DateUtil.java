package com.xtm.contract.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 工具类 - 日期工具类
 *
 * @author wumenghua
 * @version V1.0
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SIGN = "yyyyMMddHHmmssSSS";


    /**
     * 获取时间yyyMMddHHmmssSSS
     */
    public static String  getFormatDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SIGN);
        return  sdf.format(new Date());
    }

    /**
     * 获取当前日期（零时零分零秒）
     *
     * @return 当前日期（零时零分零秒）
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取当前时间（yyyy-MM-dd HH:mm:ss）
     *
     * @return 当前时间
     */
    public static String getCurrentDateTimeString() {
        return day2String(getCurrentDateTime());
    }

    /**
     * date 多少分钟之前的日期
     *
     * @param date   日期
     * @param minute
     * @return
     */
    public static Date beforMinute(Date date, int minute) {
        return handleMinute(date, -minute);
    }

    /**
     * date 多少分钟后的日期
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date afterMinute(Date date, int minute) {
        return handleMinute(date, Math.abs(minute));
    }

    /**
     * 处理日期
     *
     * @param date   日期
     * @param minute 分钟
     * @return
     */
    private static Date handleMinute(Date date, int minute) {
        if (null == date) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * date 多少天之前的日期
     *
     * @param date 日期
     * @param day
     * @return
     */
    public static Date beforDay(Date date, int day) {
        Date nowDate = handleDay(date, -day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(nowDate);
        try {
            nowDate = formatter.parse(dateString);
        } catch (ParseException e) {
        }
        return nowDate;
    }

    /**
     * date 多少天之前的日期
     *
     * @param date 日期
     * @param day
     * @return
     */
    public static Date afterDay(Date date, int day) {
        Date nowDate = handleDay(date, day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(nowDate);
        try {
            nowDate = formatter.parse(dateString);
        } catch (ParseException e) {
        }
        return nowDate;
    }


    /**
     * date 多少年之后的日期
     *
     * @param date 日期
     * @param year
     * @return
     */
    public static Date afterYear(Date date, int year) {
        Date nowDate = handleYear(date, year);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(nowDate);
        try {
            nowDate = formatter.parse(dateString);
        } catch (ParseException e) {
        }
        return nowDate;
    }

    /**
     * date 多少小时之前的日期
     *
     * @param date   日期
     * @param minute
     * @return
     */
    public static Date beforHour(Date date, int minute) {
        return handleHour(date, -minute);
    }

    /**
     * date 多少小时后的日期
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date afterHour(Date date, int minute) {
        return handleHour(date, Math.abs(minute));
    }

    /**
     * 处理日期
     *
     * @param date 日期
     * @param hour 小时数
     * @return
     */
    private static Date handleHour(Date date, int hour) {
        if (null == date) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }

    /**
     * 处理日期
     *
     * @param date 日期
     * @param day  天数
     * @return
     */
    private static Date handleDay(Date date, int day) {
        if (null == date) {
            date = new Date();
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DAY_OF_YEAR, day);
        return gc.getTime();
    }

    /**
     * 处理月份
     *
     * @param date  日期
     * @param month 月数
     * @return
     */
    private static Date handleMonth(Date date, int month) {
        if (null == date) {
            date = new Date();
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, month);
        return gc.getTime();
    }

    /**
     * 处理年份
     *
     * @param date 日期
     * @param year 年数
     * @return
     */
    private static Date handleYear(Date date, int year) {
        if (null == date) {
            date = new Date();
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, year);
        return gc.getTime();
    }

    /**
     * 日期转化为固定格式的字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String day2String(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }

    /**
     * 日期转化为固定格式的字符串 yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String day2String(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 字符串转换日期
     *
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static Date stringToDate(String date, String format) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串转换日期
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date stringToDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return stringToDate(date, DATE_FORMAT);
    }



}
