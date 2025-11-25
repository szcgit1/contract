package com.xtm.contract;

import cn.hutool.core.date.DateUtil;
import com.xtm.contract.utils.NumberUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Test01A {

    @Test
    public void test01() {
        LocalDateTime effectiveTime = LocalDateTime.now();
        String reMonth = DateUtil.format(effectiveTime,"yyyy-MM");
        System.out.println(reMonth);
    }

    @Test
    public void test02() {
        try {
            new BigDecimal("abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03() {
        String toString = NumberUtil.decimalToString(new BigDecimal("123.12300000"));
        System.out.println(toString);
        System.out.println(NumberUtil.decimalToString("123.12356900"));
    }
}
