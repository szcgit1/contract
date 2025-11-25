package com.xtm.contract.utils;

import java.util.regex.Pattern;

public class DateTimeChecker {
    // 匹配时分秒相关模式的正则表达式
    private static final Pattern TIME_PATTERN = Pattern.compile(
            "([0-2]?[0-9]:[0-5][0-9](:[0-5][0-9])?)|"+  // 匹配 HH:mm 或 HH:mm:ss
                    "([0-2]?[0-9]时[0-5][0-9]分([0-5][0-9]秒)?)"  // 匹配 HH时mm分 或 HH时mm分ss秒
    );

    /**
     * 判断日期字符串是否包含时分秒信息
     * @param dateStr 日期字符串
     * @return 包含时分秒返回true，否则返回false
     */
    public static boolean hasTimeInfo(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }

        // 使用正则表达式检查是否包含时分秒模式
        return TIME_PATTERN.matcher(dateStr).find();
    }

    public static void main(String[] args) {
        // 测试案例
        String[] testCases = {
                "2023-10-05",                // 无时分秒
                "2023-10-05 14:30",          // 有时分
                "2023-10-05 14:30:25",       // 有时分秒
                "2023年10月05日",            // 无时分秒
                "2023年10月05日 14时30分",   // 有时分
                "2023年10月05日 14时30分25秒", // 有时分秒
                "10/05/2023",                // 无时分秒
                "10/05/2023 14:30"           // 有时分
        };

        for (String testCase : testCases) {
            boolean hasTime = hasTimeInfo(testCase);
            System.out.printf("日期: %-30s 包含时分秒: %b%n", testCase, hasTime);
        }
    }
}
