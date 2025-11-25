package com.xtm.contract.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
    private  static final DecimalFormat DF = new DecimalFormat("#.########");
    public static String decimalToString(BigDecimal decimal){
        if(decimal == null){
            return null;
        }
        return DF.format(decimal);
    }

    public static String decimalToString(String decimal){
        if(decimal == null){
            return null;
        }
        return DF.format(new BigDecimal(decimal));
    }
}
