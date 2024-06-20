package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import com.axteroid.ose.server.tools.constantes.Constantes;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalInformationType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.AdditionalMonetaryTotalType;
import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import java.math.BigDecimal;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class String2ToUBLExtensionTypeConverter implements ConfigurableCustomConverter {
    private String parameter;

    /**
     * Invoice/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation/sac:AdditionalMonetaryTotal/cbc:ID (CÃ¡digo de tipo de monto -  CatÃ¡logo No. 14)
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

        AdditionalMonetaryTotalType ublExtensionType = null;
        if ("TotalDetraccion".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(
            		Constantes.DETRACCIONES, values[0], new BigDecimal(values[1]), null, new BigDecimal(values[2]),values[3],new BigDecimal(values[4]));
        }
        return ublExtensionType;
    }

    private AdditionalMonetaryTotalType buildUBLExtensionType(String tipo,
                                                   String tipoMoneda,
                                                   BigDecimal monto,
                                                   BigDecimal total,
                                                   BigDecimal porcentaje,
                                                   String nombre,
                                                   BigDecimal valorReferencial) {
        if (monto == null && total == null && porcentaje == null) return null;

        AdditionalMonetaryTotalType additionalMonetaryTotal = new AdditionalMonetaryTotalType();
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
        if (valorReferencial != null) {
            AmountType baseImponible = new AmountType();
            baseImponible.setValue(valorReferencial);
            baseImponible.setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));
            additionalMonetaryTotal.setReferenceAmount(baseImponible);
        }
        if (porcentaje != null) {
            PercentType percentType = new PercentType();
            percentType.setValue(porcentaje);
            additionalMonetaryTotal.setPercent(percentType);
        }
        if (StringUtils.isNotBlank(nombre)) {
            NameType nameType = new NameType();
            nameType.setValue(nombre);
            additionalMonetaryTotal.setName(nameType);
        }
        return additionalMonetaryTotal;
    }

    private AdditionalMonetaryTotalType buildUBLExtensionType(String tipo, String tipoMoneda, BigDecimal monto, BigDecimal total) {
        if (monto == null && total == null) return null;

        AdditionalMonetaryTotalType additionalMonetaryTotal = new AdditionalMonetaryTotalType();
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
        AdditionalInformationType additionalInformationType = new AdditionalInformationType();
        additionalInformationType.getAdditionalMonetaryTotal().add(additionalMonetaryTotal);

        ExtensionContentType extensionContentType = new ExtensionContentType();
        extensionContentType.setAny(additionalInformationType);
        UBLExtensionType ublExtensionType = new UBLExtensionType();
        ublExtensionType.setExtensionContent(extensionContentType);
        return additionalMonetaryTotal;
    }

    private AdditionalMonetaryTotalType buildUBLExtensionType(String tipo, String tipoMoneda, BigDecimal monto) {
        return buildUBLExtensionType(tipo, tipoMoneda, monto, null);

    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public static void main(String[] args) {
        String[] values = StringUtils.split("x-x-x-", "-");
    }
}
