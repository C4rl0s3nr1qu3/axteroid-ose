package com.axteroid.ose.server.ubl21.gateway.request.sunat.dozer;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DespatchAdviceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.HandlingCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.OrderTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ShippingPriorityLevelCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.SourceCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.TargetCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.TransportMeansTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2_1.CodeType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToCodeTypeConverter implements ConfigurableCustomConverter {

    private String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String valueAsString = ((String) source);

        if(StringUtils.isBlank(valueAsString)) return null;

        CodeType result = null;


        if ("InvoiceTypeCodeType".equals(parameter)) {
            result = new InvoiceTypeCodeType();
        } else if ("DocumentCurrencyCodeType".equals(parameter)) {
            result = new DocumentCurrencyCodeType();
        } else if ("DocumentTypeCodeType".equals(parameter)) {
            result = new DocumentTypeCodeType();
        } else if ("TransportMeansTypeCode".equals(parameter)){
        	result = new TransportMeansTypeCodeType();
        } else if("DespatchAdviceTypeCodeType".equals(parameter)) {
        	result = new DespatchAdviceTypeCodeType();
        } else if("HandlingCodeType".equals(parameter)) {
        	result = new HandlingCodeType();
        } else if("OrderTypeCodeType".equals(parameter)) {
        	result = new OrderTypeCodeType();
/*        } else if("SunatRetentionSystemCodeType".equals(parameter)) {
        	result = new SunatRetentionSystemCodeType();*/
        } else if("SourceCurrencyCodeType".equals(parameter)){
        	result = new SourceCurrencyCodeType();
        } else if("TargetCurrencyCodeType".equals(parameter)){
        	result = new TargetCurrencyCodeType();
        } else if("ShippingPriorityLevelCodeType".equals(parameter)) {
        	result = new ShippingPriorityLevelCodeType();
/*        } else if("SunatPerceptionSystemCodeType".equals(parameter)) {
        	result = new SunatPerceptionSystemCodeType();*/
        }
        result.setValue(valueAsString);

        return result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
