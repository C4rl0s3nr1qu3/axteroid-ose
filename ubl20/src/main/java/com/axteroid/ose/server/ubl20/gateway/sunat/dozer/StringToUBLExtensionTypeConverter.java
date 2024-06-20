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

public class StringToUBLExtensionTypeConverter implements ConfigurableCustomConverter {
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

        AdditionalMonetaryTotalType ublExtensionType = null;
        if ("TotalValorVentaNetoOpGravadas".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_OPERACIONES_GRAVADAS, values[0], new BigDecimal(values[1]));
        } else if ("TotalValorVentaNetoOpNoGravada".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_OPERACIONES_NO_GRAVADAS, values[0], new BigDecimal(values[1]));
        } else if ("TotalValorVentaNetoOpExoneradas".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_OPERACIONES_EXONERADAS, values[0], new BigDecimal(values[1]));
        }else if ("TotalValorVentaNetoOpGratuitas".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_OPERACIONES_GRATUITAS, values[0], new BigDecimal(values[1]));
        }else if ("TotalDescuentos".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_TOTAL_DESCUENTOS, values[0], new BigDecimal(values[1]));
        }else if ("SubTotalVenta".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_SUBTOTAL_VENTA, values[0], new BigDecimal(values[1]));
        }else if ("TotalPercepcion".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.PERCEPCIONES, values[0], new BigDecimal(values[1]), "*".equals(values[2])?null:new BigDecimal(values[2]), "*".equals(values[3])?null:new BigDecimal(values[3]),null,"*".equals(values[4])?null:new BigDecimal(values[4]));
        } else if ("TotalRetencion".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(
            		Constantes.RETENCIONES, values[0], new BigDecimal(values[1]), null, new BigDecimal(values[2]),null,null);
        } else if ("TotalDetraccion".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(
            		Constantes.DETRACCIONES, values[0], new BigDecimal(values[1]), null, new BigDecimal(values[2]),values[3],null);
        } else if ("TotalBonificacion".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(
            		Constantes.BONIFICACIONES, values[0], new BigDecimal(values[1]), null, null,null,null);
        } else if ("TotalValorReferencial".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(
            		Constantes.BONIFICACIONES, values[0], null, null, null,null,new BigDecimal(values[1]));
        }else if ("TotalFondoInclusionSocial".equals(parameter)) {
            ublExtensionType = buildUBLExtensionType(Constantes.TIPO_OPERACIONES_FIS, values[0], new BigDecimal(values[1]));
        }
        return ublExtensionType;
    }

    private AdditionalMonetaryTotalType buildUBLExtensionType(String tipo,
                                                   String tipoMoneda,
                                                   BigDecimal monto,
                                                   BigDecimal total,
                                                   BigDecimal porcentaje,
                                                   String nombre,
                                                   BigDecimal base) {
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
        if (base != null) {
            AmountType baseImponible = new AmountType();
            baseImponible.setValue(base);
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
