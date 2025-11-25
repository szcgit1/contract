package com.xtm.contract.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copy from [order]
 */
@Slf4j
public class CityUtils {

    public static String removePCD(String fullAddress) {
        if (StrUtil.isBlank(fullAddress)) return fullAddress;

        String street = null;

        // 正则表达式模式
        Pattern pattern = Pattern.compile(
                "(?<province>[^省]+省|[^自治区]+自治区|[^市]+市|[^特别行政区]+特别行政区)?" +
                        "(?<city>[^市]+市|[^自治州]+自治州)?" +
                        "(?<district>[^区]+区|[^县]+县|[^自治县]+自治县|[^旗]+旗|[^自治旗]+自治旗|[^林区]+林区|[^特区]+特区)?" +
                        "(?<street>.*)?"
        );

        Matcher matcher = pattern.matcher(fullAddress);

        if (matcher.find()) {
            street = matcher.group("street");
        }

        return street;
    }

    public static String removePCDWithLog(String fullAddress, String operator) {
        String newAddress = removePCD(fullAddress);
        log.info("去除省市区 operator: {}, address: {} -> {}", operator, fullAddress, newAddress);
        return newAddress;
    }


//    public static String removePCDWithLog(String fullAddress) {
//        return removePCDWithLog(fullAddress, null);
//    }

}
