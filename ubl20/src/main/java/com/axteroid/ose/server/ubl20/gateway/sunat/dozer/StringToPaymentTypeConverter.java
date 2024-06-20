package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PaymentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InstructionIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaidAmountType;
import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;

import com.axteroid.ose.server.tools.exception.GeneralException;

import un.unece.uncefact.codelist.specification._54217._2001.CurrencyCodeContentType;

import java.math.BigDecimal;

public class StringToPaymentTypeConverter implements ConfigurableCustomConverter {

    protected String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Object convert(Object destinationFieldValue, Object sourceFieldValue,
                          Class destinationClass, Class sourceClass) {
        String value = (String) sourceFieldValue;
        if (StringUtils.isBlank(value)) return null;


        String[] values = StringUtils.split(value, "|");

        InstructionIDType instructionIDType = new InstructionIDType();

        if ("totalValorVentaOpExoneradasIgv".equals(parameter)) {
            instructionIDType.setValue("02");
        } else if ("totalValorVentaOpInafectasIgv".equals(parameter)) {
            instructionIDType.setValue("03");
        } else if ("totalValorVentaOpGratuitas".equals(parameter)) {
            instructionIDType.setValue("05");
        } else if ("totalValorVentaOpGravadasConIgv".equals(parameter)) {
            instructionIDType.setValue("01");
        } else if ("TotalDocumentoAnticipo".equals(parameter)) {
            //   tipoMoneda + "|"+tipoDocumentoAnticipo + "|" + serieNumeroDocumentoAnticipo
            //   StringUtil.nvl(tipoDocumentoEmisorAnticipo,"") + "|" + StringUtil.nvl(numeroDocumentoEmisorAnticipo,"") + "|" + NumberUtil.toFormat(totalPrepagadoAnticipo);
            IDType idType= new IDType();
            idType.setSchemeID(values[1]);
            idType.setValue(values[2]);

            instructionIDType.setValue(values[4]);
            instructionIDType.setSchemeID(values[3]);

            PaidAmountType paidAmountType = new PaidAmountType();
            paidAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));
            paidAmountType.setValue(new BigDecimal(values[5]));


            PaymentType paymentType = new PaymentType();
            paymentType.setPaidAmount(paidAmountType);
            paymentType.setInstructionID(instructionIDType);
            paymentType.setID(idType);
            return paymentType;

        } else if ("totalValorVentaExportacion".equals(parameter)) {
            instructionIDType.setValue("04");
            throw new GeneralException("7123", "Campo totalValorVentaExportacion ya no es soportado por Sunat");
        } else {
            instructionIDType.setValue("00");
        }


        PaidAmountType paidAmountType = new PaidAmountType();
        paidAmountType.setCurrencyID(CurrencyCodeContentType.valueOf(values[0]));
        paidAmountType.setValue(new BigDecimal(values[1]));

        PaymentType paymentType = new PaymentType();
        paymentType.setPaidAmount(paidAmountType);
        paymentType.setInstructionID(instructionIDType);

        return paymentType;
    }

}
