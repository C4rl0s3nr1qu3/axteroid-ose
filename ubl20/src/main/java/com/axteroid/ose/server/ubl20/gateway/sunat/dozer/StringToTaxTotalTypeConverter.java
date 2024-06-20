package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import com.axteroid.ose.server.tools.constantes.Constantes;

import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;
import java.math.BigDecimal;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class StringToTaxTotalTypeConverter implements ConfigurableCustomConverter {
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
        String value = (String)sourceFieldValue;
        if(StringUtils.isBlank(value)) return null;

        String[] values = StringUtils.split(value,"|");
        
        TaxTotalType taxTotalType = null;

        if ("TotalOtrosTributos".equals(parameter)) {
            taxTotalType = buildTaxTotalType(
            		Constantes.TIPO_TRIBUTO_OTROS_CODIGO,
            		Constantes.TIPO_TRIBUTO_OTROS_NOMBRE,
            		Constantes.TIPO_TRIBUTO_OTROS_CODIGO_INTERNACIONAL,
                    values[0] ,new BigDecimal(values[1]));
        } else if ("TotalIsc".equals(parameter)) {
            taxTotalType = buildTaxTotalType(
            		Constantes.TIPO_TRIBUTO_ISC_CODIGO,
            		Constantes.TIPO_TRIBUTO_ISC_NOMBRE,
            		Constantes.TIPO_TRIBUTO_ISC_CODIGO_INTERNACIONAL,
                    values[0] ,new BigDecimal(values[1]));
        } else if ("TotalIgv".equals(parameter)) {
            taxTotalType = buildTaxTotalType(
            		Constantes.TIPO_TRIBUTO_IGV_CODIGO,
            		Constantes.TIPO_TRIBUTO_IGV_NOMBRE,
            		Constantes.TIPO_TRIBUTO_IGV_CODIGO_INTERNACIONAL,
                    values[0] ,new BigDecimal(values[1]));
        } else if ("ImporteIgv".equals(parameter)) {
            taxTotalType = buildTaxTotalType(
            		Constantes.TIPO_TRIBUTO_IGV_CODIGO,
            		Constantes.TIPO_TRIBUTO_IGV_NOMBRE,
            		Constantes.TIPO_TRIBUTO_IGV_CODIGO_INTERNACIONAL,
                    values[0],
                    new BigDecimal(values[1])
            );
            TaxExemptionReasonCodeType taxExemptionReasonCodeType = new TaxExemptionReasonCodeType();
            taxExemptionReasonCodeType.setValue(values[2]);
            taxTotalType.getTaxSubtotal().get(0).getTaxCategory().setTaxExemptionReasonCode(taxExemptionReasonCodeType);
            taxTotalType.getTaxSubtotal().get(0).getTaxAmount().setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));

        } else if ("ImporteIsc".equals(parameter)) {

           taxTotalType = buildTaxTotalType(
        		   Constantes.TIPO_TRIBUTO_ISC_CODIGO,
        		   Constantes.TIPO_TRIBUTO_ISC_NOMBRE,
        		   Constantes.TIPO_TRIBUTO_ISC_CODIGO_INTERNACIONAL,
                    values[0],
                    new BigDecimal(values[1]));

            TierRangeType tierRangeType = new TierRangeType();
            tierRangeType.setValue(values[2]);
            taxTotalType.getTaxSubtotal().get(0).getTaxCategory().setTierRange(tierRangeType);
            taxTotalType.getTaxSubtotal().get(0).getTaxAmount().setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));
        }
        return taxTotalType;
    }

    private TaxTotalType buildTaxTotalType(String codigo,
                                           String nombre,
                                           String codigoInternacional,
                                           String tipoMoneda,
                                           BigDecimal monto) {
        TaxAmountType taxAmountType = new TaxAmountType();
        taxAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));
        taxAmountType.setValue(monto);

        TaxTypeCodeType taxTypeCodeType = new TaxTypeCodeType();
        taxTypeCodeType.setValue(codigoInternacional);

        NameType nameType = new NameType();
        nameType.setValue(nombre);

        IDType idType = new IDType();
        idType.setValue(codigo);

        TaxSchemeType taxSchemeType = new TaxSchemeType();
        taxSchemeType.setTaxTypeCode(taxTypeCodeType);
        taxSchemeType.setName(nameType);
        taxSchemeType.setID(idType);

        TaxCategoryType taxCategoryType = new TaxCategoryType();

        taxCategoryType.setTaxScheme(taxSchemeType);

        TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
        taxSubtotalType.setTaxAmount(taxAmountType);
        taxSubtotalType.setTaxCategory(taxCategoryType);

        TaxTotalType taxTotalType = new TaxTotalType();
        taxTotalType.setTaxAmount(taxAmountType);
        taxTotalType.getTaxSubtotal().add(taxSubtotalType);
        taxTotalType.getTaxAmount().setCurrencyID(CurrencyCodeContentType.valueOf(tipoMoneda));

        return taxTotalType;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
