package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ChargeIndicatorType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import java.math.BigDecimal;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToAllowanceChargeTypeConverter implements ConfigurableCustomConverter {


    private String parameter;

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String value = (String)sourceFieldValue;
        if(StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value,"|");
        

        ChargeIndicatorType chargeIndicatorType = new ChargeIndicatorType();
        if("totalOtrosCargos".equals(parameter)) {
            chargeIndicatorType.setValue(Boolean.TRUE);
        }/*else{
            chargeIndicatorType.setValue(Boolean.FALSE);
        }*/


        AmountType amountType = new AmountType();
        amountType.setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));
        amountType.setValue(new BigDecimal(values[1]));

        AllowanceChargeType allowanceChargeType= new AllowanceChargeType();
        allowanceChargeType.setChargeIndicator(chargeIndicatorType);
        allowanceChargeType.setAmount(amountType);


        return allowanceChargeType;
    }

    public void setParameter(String parameter) {
        this.parameter=parameter;
    }
}
