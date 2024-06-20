package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DespatchAdviceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.HandlingCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.OrderTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ShippingPriorityLevelCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.SourceCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TargetCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TransportMeansTypeCodeType;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatPerceptionSystemCodeType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatRetentionSystemCodeType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.CodeType;

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
        } else if("SunatRetentionSystemCodeType".equals(parameter)) {
        	result = new SunatRetentionSystemCodeType();
        } else if("SourceCurrencyCodeType".equals(parameter)){
        	result = new SourceCurrencyCodeType();
        } else if("TargetCurrencyCodeType".equals(parameter)){
        	result = new TargetCurrencyCodeType();
        } else if("ShippingPriorityLevelCodeType".equals(parameter)) {
        	result = new ShippingPriorityLevelCodeType();
        } else if("SunatPerceptionSystemCodeType".equals(parameter)) {
        	result = new SunatPerceptionSystemCodeType();
        }
        result.setValue(valueAsString);

        return result;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
