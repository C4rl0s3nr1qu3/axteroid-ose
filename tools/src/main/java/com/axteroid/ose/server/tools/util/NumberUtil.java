package com.axteroid.ose.server.tools.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberUtil {

    public static String nvl(BigDecimal origen, String result) {
        if (origen == null) return result;
        return String.valueOf(origen);
    }

    public static String nvl(String origen, String result) {
        if (origen == null) return result;
        return String.valueOf(origen);
    }

    public static String toFormat(BigDecimal value){
        if(value==null) return null;
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat format= new DecimalFormat("##############0.0#########", unusualSymbols);
        return format.format(value);
    }
}
