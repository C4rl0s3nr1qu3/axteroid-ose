package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.math.BigDecimal;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AllowanceTotalAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ChargeTotalAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaidAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PrepaidAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TotalInvoiceAmountType;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatNetTotalCashedType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatNetTotalPaidType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatPerceptionAmountType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatRetentionAmountType;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.AmountType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToAmountTypeConverter implements ConfigurableCustomConverter {
    private String parameter;
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        String value = (String)source;
        if(StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value,"|");

        AmountType idType = null;
        if("PayableAmountType".equals(parameter)){
            idType= new PayableAmountType();
        }else if ("PrepaidAmountType".equals(parameter)){
            idType= new PrepaidAmountType();
        }else if ("ChargeTotalAmountType".equals(parameter)){
            idType= new ChargeTotalAmountType();
        }else if ("AllowanceTotalAmountType".equals(parameter)){
            idType= new AllowanceTotalAmountType();
        }else if ("ImporteUnitarioSinImpuesto".equals(parameter)){
            idType= new PriceAmountType();
        }else if ("ImporteTotalSinImpuesto".equals(parameter)){
            idType= new LineExtensionAmountType();
        }else if ("LineExtensionAmountType".equals(parameter)){
            idType= new LineExtensionAmountType();
        }else if ("AmountType".equals(parameter)){
            idType= new oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType();
        }else if("PaidAmountType".equals(parameter)){
        	idType = new PaidAmountType();
        }else if("TotalInvoiceAmountType".equals(parameter)){
        	idType = new TotalInvoiceAmountType();
        }else if("SunatRetentionAmountType".equals(parameter)){
        	idType = new SunatRetentionAmountType();
        }else if("SunatNetTotalPaidType".equals(parameter)){
        	idType = new SunatNetTotalPaidType();
        }else if("SunatPerceptionAmountType".equals(parameter)){
        	idType = new SunatPerceptionAmountType();
        }else if("SunatNetTotalCashedType".equals(parameter)){
        	idType = new SunatNetTotalCashedType();
        }
        idType.setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));
        idType.setValue(new BigDecimal(values[1]));
        return idType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
