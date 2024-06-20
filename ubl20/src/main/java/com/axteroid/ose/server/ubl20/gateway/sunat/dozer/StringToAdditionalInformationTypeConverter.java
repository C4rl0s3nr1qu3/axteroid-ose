package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import com.axteroid.ose.server.tools.constantes.Constantes;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalInformationType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalMonetaryTotalType;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import java.math.BigDecimal;

public class StringToAdditionalInformationTypeConverter implements ConfigurableCustomConverter {
    private String parameter;

    /**
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:ID (Cádigo de tipo de monto -  Catálogo No. 14)
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:PayableAmount (Monto)
     *
     * @param destinationFieldValue
     * @param sourceFieldValue
     * @param destinationClass
     * @param sourceClass
     * @return
     */
    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String value = (String) sourceFieldValue;
        if (StringUtils.isBlank(value)) return null;
        String[] values = StringUtils.split(value, "|");

        AdditionalInformationType additionalInformationType = null;
        if (destinationFieldValue == null) {
            additionalInformationType = new AdditionalInformationType();
        } else {
            additionalInformationType = (AdditionalInformationType) destinationFieldValue;
        }

        if ("TotalVentaConPercepcion".equals(parameter)) {
            populateUBLExtensionType(additionalInformationType,
            		Constantes.PERCEPCIONES, values[0], null, new BigDecimal(values[1]),null,null);
        } else if ("TotalPercepcion".equals(parameter)) {
            populateUBLExtensionType(additionalInformationType,
            		Constantes.PERCEPCIONES, values[0], new BigDecimal(values[1]), new BigDecimal(values[2]),new BigDecimal(values[3]),new BigDecimal(values[4]));
        }else if ("TotalRetencion".equals(parameter)) {
            populateUBLExtensionType(additionalInformationType,
            		Constantes.RETENCIONES, values[0], new BigDecimal(values[1]), new BigDecimal(values[2]));
        }else if ("TotalDetraccion".equals(parameter)) {
            populateUBLExtensionType(additionalInformationType,
            		Constantes.DETRACCIONES, values[0], new BigDecimal(values[1]), new BigDecimal(values[2]));
        }else if ("TotalBonificacion".equals(parameter)) {
            populateUBLExtensionType(additionalInformationType,
            		Constantes.BONIFICACIONES, values[0], null, new BigDecimal(values[1]));
        }
        return additionalInformationType;
    }

    private AdditionalInformationType populateUBLExtensionType(
            AdditionalInformationType additionalInformationType,
            String tipo,
            String tipoMoneda,
            BigDecimal monto,
            BigDecimal total,
            BigDecimal porcentaje,
            BigDecimal baseImponible) {

        AdditionalMonetaryTotalType additionalMonetaryTotal = null;
        if (additionalInformationType.getAdditionalMonetaryTotal().size() == 0) {
            additionalMonetaryTotal = new AdditionalMonetaryTotalType();
        } else {
            additionalMonetaryTotal = additionalInformationType.getAdditionalMonetaryTotal().get(0);
        }


        IDType idType = new IDType();
        idType.setValue(tipo);
        additionalMonetaryTotal.setID(idType);
        if (monto != null) {
            PayableAmountType payableAmountType = new PayableAmountType();
            payableAmountType.setValue(monto);
            payableAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));
            additionalMonetaryTotal.setPayableAmount(payableAmountType);
        }
        if (total != null) {
            AmountType totalAmountType = new AmountType();
            totalAmountType.setValue(total);
            totalAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));
            additionalMonetaryTotal.setTotalAmount(totalAmountType);
        }
        if (porcentaje != null) {
            PercentType percentType = new PercentType();
            percentType.setValue(porcentaje);
            additionalMonetaryTotal.setPercent(percentType);
        }
        if (baseImponible!= null) {
            AmountType amountType = new AmountType();
            amountType.setValue(porcentaje);
            amountType.setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));
            additionalMonetaryTotal.setReferenceAmount(amountType);
        }
        if (additionalInformationType.getAdditionalMonetaryTotal().size() == 0) {
            additionalInformationType.getAdditionalMonetaryTotal().add(additionalMonetaryTotal);
        }
        return additionalInformationType;
    }

    private AdditionalInformationType populateUBLExtensionType(AdditionalInformationType additionalInformationType,
                                                               String tipo,
                                                               String tipoMoneda,
                                                               BigDecimal monto,BigDecimal porcentaje) {
        return populateUBLExtensionType(additionalInformationType, tipo, tipoMoneda, monto, null,porcentaje,null);

    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
