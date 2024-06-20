package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceTypeCodeType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToPriceTypeConverter implements ConfigurableCustomConverter {

    private String parameter;

    public static List<String> codigosExonerados = Arrays.asList("11", "12", "13", "14", "15", "16", "31", "32", "33", "34", "35", "36");

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String value = (String) source;
        if (StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value, "|");

        String moneda = "";
        String monto = "";
        String codigo = "01";
        if (values.length == 3) {
            moneda = values[1];
            monto = values[2];
            codigo = values[0];
        } else {
            moneda = values[0];
            monto = values[1];
        }

        PriceType idType = new PriceType();
        PriceAmountType priceAmountType = new PriceAmountType();
        priceAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(moneda));
        priceAmountType.setValue(new BigDecimal(monto));
        idType.setPriceAmount(priceAmountType);
        PriceTypeCodeType priceTypeCodeType = new PriceTypeCodeType();
        priceTypeCodeType.setValue(codigo);
/*
        if (codigosExonerados.contains(codigo)) {
            priceTypeCodeType.setValue(EbizConstantes.TIPO_PRECIO_ONEROSA);
        } else {
            priceTypeCodeType.setValue(EbizConstantes.TIPO_PRECIO);
        }
*/
        idType.setPriceTypeCode(priceTypeCodeType);
        return idType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
