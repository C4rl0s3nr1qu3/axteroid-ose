package com.axteroid.ose.server.ubl20.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class Number15Adapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String result) throws Exception {
        int p=result.indexOf(".");
        return new BigDecimal(result.substring(0,p+3));
    }

    @Override
    public String marshal(BigDecimal v) throws Exception {
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        NumberFormat nf= new DecimalFormat("##############0.00#",unusualSymbols);
        String result= nf.format(v);
        int p=result.indexOf(".");
        return result.substring(0,p+3);
    }

    public static void main(String[] args) {

        String s="2454.458";
        System.out.println(s.substring(0,s.indexOf(".")+3));

    }
}
