package com.axteroid.ose.server.tools.edocu;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.util.StringUtil;
import com.axteroid.ose.server.tools.validation.TipoDocumentoRelacionadoValidator;
import com.axteroid.ose.server.tools.validation.TipoMonedaValidatorType;
import com.axteroid.ose.server.tools.validation.ValidateFormatNumber;

/**
 * User: HNA
 * Date: 30/11/15
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ERetencionDocumentoItem {
    /**
     * input: parametro
     */
    @NotBlank(message = "7047")
    private String numeroOrdenItem;
	
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cbc:ID@schemeID
     */
    @NotBlank(message="7235")
    @TipoDocumentoRelacionadoValidator(message="7236")
    public String tipoDocumentoRelacionado;

    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cbc:ID
     */
    @NotBlank(message="7237")
    public String numeroDocumentoRelacionado;
    
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cbc:IssueDate
     */
    @NotNull(message = "7238")
    public Date fechaEmisionDocumentoRelacionado;
    
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cbc:TotalInvoiceAmount
     */
//    @NotNull(message = "7045")
//    @Min(value = 0,message = "7166")
    @NotNull(message = "7239")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal importeTotalDocumentoRelacionado;

    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cbc:TotalInvoiceAmount@currencyID
     * Dato: si el campo tipoMonedaDocumentoRelacionado es diferente de PEN, los campos monedaReferenciaTipoCambio,
     * monedaObjetivoTasaCambio,factorTipoCambioMoneda,fechaCambio  son mandatorios, esta validacion esta en ParamsValidatorBeanValidationImpl
     */    
    @NotNull(message = "7240")
    public String tipoMonedaDocumentoRelacionado;
    
    // DATOS DEL PAGO
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cac:Payment/cbc:PaidDate
     */    
//    @NotNull(message = "7241")
    public Date fechaPago;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cac:Payment/cbc:ID
     * se agrega:7248
     */
//    @NotBlank(message = "7242")
    public String numeroPago;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cac:Payment/cbc:PaidAmount
     * Dato: el importe de pago no debe ser mayor al importe total del comprobante relacionado, esta validacion esta en ParamsValidatorBeanValidationImpl
     */    
//    @NotNull(message = "7243")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal importePagoSinRetencion;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/cac:Payment/cbc:PaidAmount@currencyID
     */
//    @NotBlank(message = "7244")
    public String monedaPago;
    
    //DATOS DE LA RETENCION
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/sac:SUNATRetentionAmount
     */   
//    @NotNull(message = "7245")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal importeRetenido;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/sac:SUNATRetentionAmount@currencyID 
     */
//    @NotBlank(message="7246")
    @TipoMonedaValidatorType(message = "7247")
    public String monedaImporteRetenido;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/sac:SUNATRetentionDate
     */
//    @NotNull(message = "7248")
    public Date fechaRetencion;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/sac:SUNATNetTotalPaid
      */
//    @NotNull(message = "7249")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal importeTotalPagarNeto;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/sac:SUNATNetTotalPaid@currencyID 
     */   
//    @NotBlank(message="7250")
    @TipoMonedaValidatorType(message = "7268")
    public String monedaMontoNetoPagado;
    
    //TIPO DE CAMBIO -- validacion agrupada si monedaMontoNetoPagado es soles
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:SourceCurrencyCode
     */    
    public String monedaReferenciaTipoCambio;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:TargetCurrencyCode
     */
    public String monedaObjetivoTasaCambio;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:CalculationRate 
     */
    public BigDecimal factorTipoCambioMoneda;
    /**
     * parametro
     * sac:SUNATRetentionDocumentReference/sac:SUNATRetentionInformation/cac:ExchangeRate/cbc:Date 
     */
    public Date fechaCambio;
    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_1;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_1;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_2;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_2;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_3;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_3;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_4;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_4;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_5;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_5;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_6;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_6;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_7;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_7;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_8;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_8;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_9;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_9;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar100_10;
    @Length(max = 100, message = "7093")
    private String textoAuxiliar100_10;
    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_1;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_1;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_2;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_2;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_3;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_3;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_4;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_4;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_5;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_5;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_6;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_6;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_7;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_7;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_8;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_8;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_9;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_9;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar40_10;
    @Length(max = 40, message = "7094")
    private String textoAuxiliar40_10;

    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_1;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_1;    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_2;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_2;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_3;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_3;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_4;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_4;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_5;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_5;    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_6;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_6;    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_7;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_7;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_8;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_8;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_9;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_9;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar500_10;
    @Length(max = 500, message = "7139")
    private String textoAuxiliar500_10;
    
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_1;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_1;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_2;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_2;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_3;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_3;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_4;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_4;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_5;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_5;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_6;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_6;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_7;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_7;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_8;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_8;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_9;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_9;
    @Length(max = 4, message = "7092")
    private String codigoAuxiliar250_10;
    @Length(max = 250, message = "7145")
    private String textoAuxiliar250_10;    
    
	public String getNumeroOrdenItem() {
		return numeroOrdenItem;
	}
	public void setNumeroOrdenItem(String numeroOrdenItem) {
		this.numeroOrdenItem = numeroOrdenItem;
	}
	public String getTipoDocumentoRelacionado() {
		return tipoDocumentoRelacionado;
	}
	public void setTipoDocumentoRelacionado(String tipoDocumentoRelacionado) {
		this.tipoDocumentoRelacionado = tipoDocumentoRelacionado;
	}
	public String getNumeroDocumentoRelacionado() {
		return numeroDocumentoRelacionado;
	}
	public void setNumeroDocumentoRelacionado(String numeroDocumentoRelacionado) {
		this.numeroDocumentoRelacionado = numeroDocumentoRelacionado;
	}
	public Date getFechaEmisionDocumentoRelacionado() {
		return fechaEmisionDocumentoRelacionado;
	}
	public void setFechaEmisionDocumentoRelacionado(
			Date fechaEmisionDocumentoRelacionado) {
		this.fechaEmisionDocumentoRelacionado = fechaEmisionDocumentoRelacionado;
	}
	public BigDecimal getImporteTotalDocumentoRelacionado() {
		return importeTotalDocumentoRelacionado;
	}
	public void setImporteTotalDocumentoRelacionado(
			BigDecimal importeTotalDocumentoRelacionado) {
		this.importeTotalDocumentoRelacionado = importeTotalDocumentoRelacionado;
	}
	public String getTipoMonedaDocumentoRelacionado() {
		return tipoMonedaDocumentoRelacionado;
	}
	public void setTipoMonedaDocumentoRelacionado(
			String tipoMonedaDocumentoRelacionado) {
		this.tipoMonedaDocumentoRelacionado = tipoMonedaDocumentoRelacionado;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getNumeroPago() {
		return numeroPago;
	}
	public void setNumeroPago(String numeroPago) {
		this.numeroPago = numeroPago;
	}
	public BigDecimal getImportePagoSinRetencion() {
		return importePagoSinRetencion;
	}
	public void setImportePagoSinRetencion(BigDecimal importePagoSinRetencion) {
		this.importePagoSinRetencion = importePagoSinRetencion;
	}
	public String getMonedaPago() {
		return monedaPago;
	}
	public void setMonedaPago(String monedaPago) {
		this.monedaPago = monedaPago;
	}
	public BigDecimal getImporteRetenido() {
		return importeRetenido;
	}
	public void setImporteRetenido(BigDecimal importeRetenido) {
		this.importeRetenido = importeRetenido;
	}
	public String getMonedaImporteRetenido() {
		return monedaImporteRetenido;
	}
	public void setMonedaImporteRetenido(String monedaImporteRetenido) {
		this.monedaImporteRetenido = monedaImporteRetenido;
	}
	public Date getFechaRetencion() {
		return fechaRetencion;
	}
	public void setFechaRetencion(Date fechaRetencion) {
		this.fechaRetencion = fechaRetencion;
	}
	public BigDecimal getImporteTotalPagarNeto() {
		return importeTotalPagarNeto;
	}
	public void setImporteTotalPagarNeto(BigDecimal importeTotalPagarNeto) {
		this.importeTotalPagarNeto = importeTotalPagarNeto;
	}
	public String getMonedaMontoNetoPagado() {
		return monedaMontoNetoPagado;
	}
	public void setMonedaMontoNetoPagado(String monedaMontoNetoPagado) {
		this.monedaMontoNetoPagado = monedaMontoNetoPagado;
	}
	public String getMonedaReferenciaTipoCambio() {
		return monedaReferenciaTipoCambio;
	}
	public void setMonedaReferenciaTipoCambio(String monedaReferenciaTipoCambio) {
		this.monedaReferenciaTipoCambio = monedaReferenciaTipoCambio;
	}
	public String getMonedaObjetivoTasaCambio() {
		return monedaObjetivoTasaCambio;
	}
	public void setMonedaObjetivoTasaCambio(String monedaObjetivoTasaCambio) {
		this.monedaObjetivoTasaCambio = monedaObjetivoTasaCambio;
	}
	public BigDecimal getFactorTipoCambioMoneda() {
		return factorTipoCambioMoneda;
	}
	public void setFactorTipoCambioMoneda(BigDecimal factorTipoCambioMoneda) {
		this.factorTipoCambioMoneda = factorTipoCambioMoneda;
	}
	public Date getFechaCambio() {
		return fechaCambio;
	}
	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
	
    public String buildDocumentoRelacionado() {
        if (StringUtils.isBlank(numeroDocumentoRelacionado) ||
                StringUtils.isBlank(tipoDocumentoRelacionado)) {
            return null;
        }
        return StringUtil.blank(numeroDocumentoRelacionado) + "|" + StringUtil.blank(tipoDocumentoRelacionado);
    }
    
    public String buildTotalRelacionado(){
        if (StringUtils.isBlank(tipoMonedaDocumentoRelacionado) ||
        		importeTotalDocumentoRelacionado == null) return null;
        return tipoMonedaDocumentoRelacionado + "|" + NumberUtil.toFormat(importeTotalDocumentoRelacionado);
    }
    
    public String buildTotalSinRetencion(){
        if (StringUtils.isBlank(monedaPago) ||
        		importePagoSinRetencion == null) return null;
        return monedaPago + "|" + NumberUtil.toFormat(importePagoSinRetencion);
    }   
    
    public String buildTotalRetenido(){
        if (StringUtils.isBlank(monedaImporteRetenido) ||
        		importeRetenido == null) return null;
        return monedaImporteRetenido + "|" + NumberUtil.toFormat(importeRetenido);
    }
    
    public String buildTotalNetoPagado(){
        if (StringUtils.isBlank(monedaMontoNetoPagado) ||
        		importeTotalPagarNeto == null) return null;
        return monedaMontoNetoPagado + "|" + NumberUtil.toFormat(importeTotalPagarNeto);
    }
    
	public String getCodigoAuxiliar100_1() {
		return codigoAuxiliar100_1;
	}
	public void setCodigoAuxiliar100_1(String codigoAuxiliar100_1) {
		this.codigoAuxiliar100_1 = codigoAuxiliar100_1;
	}
	public String getTextoAuxiliar100_1() {
		return textoAuxiliar100_1;
	}
	public void setTextoAuxiliar100_1(String textoAuxiliar100_1) {
		this.textoAuxiliar100_1 = textoAuxiliar100_1;
	}
    public String getCodigoAuxiliar100_2() {
		return codigoAuxiliar100_2;
	}
	public void setCodigoAuxiliar100_2(String codigoAuxiliar100_2) {
		this.codigoAuxiliar100_2 = codigoAuxiliar100_2;
	}
	public String getTextoAuxiliar100_2() {
		return textoAuxiliar100_2;
	}
	public void setTextoAuxiliar100_2(String textoAuxiliar100_2) {
		this.textoAuxiliar100_2 = textoAuxiliar100_2;
	}
	public String getCodigoAuxiliar100_3() {
		return codigoAuxiliar100_3;
	}
	public void setCodigoAuxiliar100_3(String codigoAuxiliar100_3) {
		this.codigoAuxiliar100_3 = codigoAuxiliar100_3;
	}
	public String getTextoAuxiliar100_3() {
		return textoAuxiliar100_3;
	}
	public void setTextoAuxiliar100_3(String textoAuxiliar100_3) {
		this.textoAuxiliar100_3 = textoAuxiliar100_3;
	}
	public String getCodigoAuxiliar100_4() {
		return codigoAuxiliar100_4;
	}
	public void setCodigoAuxiliar100_4(String codigoAuxiliar100_4) {
		this.codigoAuxiliar100_4 = codigoAuxiliar100_4;
	}
	public String getTextoAuxiliar100_4() {
		return textoAuxiliar100_4;
	}
	public void setTextoAuxiliar100_4(String textoAuxiliar100_4) {
		this.textoAuxiliar100_4 = textoAuxiliar100_4;
	}
	public String getCodigoAuxiliar100_5() {
		return codigoAuxiliar100_5;
	}
	public void setCodigoAuxiliar100_5(String codigoAuxiliar100_5) {
		this.codigoAuxiliar100_5 = codigoAuxiliar100_5;
	}
	public String getTextoAuxiliar100_5() {
		return textoAuxiliar100_5;
	}
	public void setTextoAuxiliar100_5(String textoAuxiliar100_5) {
		this.textoAuxiliar100_5 = textoAuxiliar100_5;
	}
	public String getCodigoAuxiliar100_6() {
		return codigoAuxiliar100_6;
	}
	public void setCodigoAuxiliar100_6(String codigoAuxiliar100_6) {
		this.codigoAuxiliar100_6 = codigoAuxiliar100_6;
	}
	public String getTextoAuxiliar100_6() {
		return textoAuxiliar100_6;
	}
	public void setTextoAuxiliar100_6(String textoAuxiliar100_6) {
		this.textoAuxiliar100_6 = textoAuxiliar100_6;
	}
	public String getCodigoAuxiliar100_7() {
		return codigoAuxiliar100_7;
	}
	public void setCodigoAuxiliar100_7(String codigoAuxiliar100_7) {
		this.codigoAuxiliar100_7 = codigoAuxiliar100_7;
	}
	public String getTextoAuxiliar100_7() {
		return textoAuxiliar100_7;
	}
	public void setTextoAuxiliar100_7(String textoAuxiliar100_7) {
		this.textoAuxiliar100_7 = textoAuxiliar100_7;
	}
	public String getCodigoAuxiliar100_8() {
		return codigoAuxiliar100_8;
	}
	public void setCodigoAuxiliar100_8(String codigoAuxiliar100_8) {
		this.codigoAuxiliar100_8 = codigoAuxiliar100_8;
	}
	public String getTextoAuxiliar100_8() {
		return textoAuxiliar100_8;
	}
	public void setTextoAuxiliar100_8(String textoAuxiliar100_8) {
		this.textoAuxiliar100_8 = textoAuxiliar100_8;
	}
	public String getCodigoAuxiliar100_9() {
		return codigoAuxiliar100_9;
	}
	public void setCodigoAuxiliar100_9(String codigoAuxiliar100_9) {
		this.codigoAuxiliar100_9 = codigoAuxiliar100_9;
	}
	public String getTextoAuxiliar100_9() {
		return textoAuxiliar100_9;
	}
	public void setTextoAuxiliar100_9(String textoAuxiliar100_9) {
		this.textoAuxiliar100_9 = textoAuxiliar100_9;
	}
	public String getCodigoAuxiliar100_10() {
		return codigoAuxiliar100_10;
	}
	public void setCodigoAuxiliar100_10(String codigoAuxiliar100_10) {
		this.codigoAuxiliar100_10 = codigoAuxiliar100_10;
	}
	public String getTextoAuxiliar100_10() {
		return textoAuxiliar100_10;
	}
	public void setTextoAuxiliar100_10(String textoAuxiliar100_10) {
		this.textoAuxiliar100_10 = textoAuxiliar100_10;
	}
	public String getCodigoAuxiliar40_1() {
		return codigoAuxiliar40_1;
	}
	public void setCodigoAuxiliar40_1(String codigoAuxiliar40_1) {
		this.codigoAuxiliar40_1 = codigoAuxiliar40_1;
	}
	public String getTextoAuxiliar40_1() {
		return textoAuxiliar40_1;
	}
	public void setTextoAuxiliar40_1(String textoAuxiliar40_1) {
		this.textoAuxiliar40_1 = textoAuxiliar40_1;
	}
	public String getCodigoAuxiliar40_2() {
		return codigoAuxiliar40_2;
	}
	public void setCodigoAuxiliar40_2(String codigoAuxiliar40_2) {
		this.codigoAuxiliar40_2 = codigoAuxiliar40_2;
	}
	public String getTextoAuxiliar40_2() {
		return textoAuxiliar40_2;
	}
	public void setTextoAuxiliar40_2(String textoAuxiliar40_2) {
		this.textoAuxiliar40_2 = textoAuxiliar40_2;
	}
	public String getCodigoAuxiliar40_3() {
		return codigoAuxiliar40_3;
	}
	public void setCodigoAuxiliar40_3(String codigoAuxiliar40_3) {
		this.codigoAuxiliar40_3 = codigoAuxiliar40_3;
	}
	public String getTextoAuxiliar40_3() {
		return textoAuxiliar40_3;
	}
	public void setTextoAuxiliar40_3(String textoAuxiliar40_3) {
		this.textoAuxiliar40_3 = textoAuxiliar40_3;
	}
	public String getCodigoAuxiliar40_4() {
		return codigoAuxiliar40_4;
	}
	public void setCodigoAuxiliar40_4(String codigoAuxiliar40_4) {
		this.codigoAuxiliar40_4 = codigoAuxiliar40_4;
	}
	public String getTextoAuxiliar40_4() {
		return textoAuxiliar40_4;
	}
	public void setTextoAuxiliar40_4(String textoAuxiliar40_4) {
		this.textoAuxiliar40_4 = textoAuxiliar40_4;
	}
	public String getCodigoAuxiliar40_5() {
		return codigoAuxiliar40_5;
	}
	public void setCodigoAuxiliar40_5(String codigoAuxiliar40_5) {
		this.codigoAuxiliar40_5 = codigoAuxiliar40_5;
	}
	public String getTextoAuxiliar40_5() {
		return textoAuxiliar40_5;
	}
	public void setTextoAuxiliar40_5(String textoAuxiliar40_5) {
		this.textoAuxiliar40_5 = textoAuxiliar40_5;
	}
	public String getCodigoAuxiliar40_6() {
		return codigoAuxiliar40_6;
	}
	public void setCodigoAuxiliar40_6(String codigoAuxiliar40_6) {
		this.codigoAuxiliar40_6 = codigoAuxiliar40_6;
	}
	public String getTextoAuxiliar40_6() {
		return textoAuxiliar40_6;
	}
	public void setTextoAuxiliar40_6(String textoAuxiliar40_6) {
		this.textoAuxiliar40_6 = textoAuxiliar40_6;
	}
	public String getCodigoAuxiliar40_7() {
		return codigoAuxiliar40_7;
	}
	public void setCodigoAuxiliar40_7(String codigoAuxiliar40_7) {
		this.codigoAuxiliar40_7 = codigoAuxiliar40_7;
	}
	public String getTextoAuxiliar40_7() {
		return textoAuxiliar40_7;
	}
	public void setTextoAuxiliar40_7(String textoAuxiliar40_7) {
		this.textoAuxiliar40_7 = textoAuxiliar40_7;
	}
	public String getCodigoAuxiliar40_8() {
		return codigoAuxiliar40_8;
	}
	public void setCodigoAuxiliar40_8(String codigoAuxiliar40_8) {
		this.codigoAuxiliar40_8 = codigoAuxiliar40_8;
	}
	public String getTextoAuxiliar40_8() {
		return textoAuxiliar40_8;
	}
	public void setTextoAuxiliar40_8(String textoAuxiliar40_8) {
		this.textoAuxiliar40_8 = textoAuxiliar40_8;
	}
	public String getCodigoAuxiliar40_9() {
		return codigoAuxiliar40_9;
	}
	public void setCodigoAuxiliar40_9(String codigoAuxiliar40_9) {
		this.codigoAuxiliar40_9 = codigoAuxiliar40_9;
	}
	public String getTextoAuxiliar40_9() {
		return textoAuxiliar40_9;
	}
	public void setTextoAuxiliar40_9(String textoAuxiliar40_9) {
		this.textoAuxiliar40_9 = textoAuxiliar40_9;
	}
	public String getCodigoAuxiliar40_10() {
		return codigoAuxiliar40_10;
	}
	public void setCodigoAuxiliar40_10(String codigoAuxiliar40_10) {
		this.codigoAuxiliar40_10 = codigoAuxiliar40_10;
	}
	public String getTextoAuxiliar40_10() {
		return textoAuxiliar40_10;
	}
	public void setTextoAuxiliar40_10(String textoAuxiliar40_10) {
		this.textoAuxiliar40_10 = textoAuxiliar40_10;
	}
	public String getCodigoAuxiliar500_1() {
		return codigoAuxiliar500_1;
	}
	public void setCodigoAuxiliar500_1(String codigoAuxiliar500_1) {
		this.codigoAuxiliar500_1 = codigoAuxiliar500_1;
	}
	public String getTextoAuxiliar500_1() {
		return textoAuxiliar500_1;
	}
	public void setTextoAuxiliar500_1(String textoAuxiliar500_1) {
		this.textoAuxiliar500_1 = textoAuxiliar500_1;
	}
	public String getCodigoAuxiliar500_2() {
		return codigoAuxiliar500_2;
	}
	public void setCodigoAuxiliar500_2(String codigoAuxiliar500_2) {
		this.codigoAuxiliar500_2 = codigoAuxiliar500_2;
	}
	public String getTextoAuxiliar500_2() {
		return textoAuxiliar500_2;
	}
	public void setTextoAuxiliar500_2(String textoAuxiliar500_2) {
		this.textoAuxiliar500_2 = textoAuxiliar500_2;
	}
	public String getCodigoAuxiliar500_3() {
		return codigoAuxiliar500_3;
	}
	public void setCodigoAuxiliar500_3(String codigoAuxiliar500_3) {
		this.codigoAuxiliar500_3 = codigoAuxiliar500_3;
	}
	public String getTextoAuxiliar500_3() {
		return textoAuxiliar500_3;
	}
	public void setTextoAuxiliar500_3(String textoAuxiliar500_3) {
		this.textoAuxiliar500_3 = textoAuxiliar500_3;
	}
	public String getCodigoAuxiliar500_4() {
		return codigoAuxiliar500_4;
	}
	public void setCodigoAuxiliar500_4(String codigoAuxiliar500_4) {
		this.codigoAuxiliar500_4 = codigoAuxiliar500_4;
	}
	public String getTextoAuxiliar500_4() {
		return textoAuxiliar500_4;
	}
	public void setTextoAuxiliar500_4(String textoAuxiliar500_4) {
		this.textoAuxiliar500_4 = textoAuxiliar500_4;
	}
	public String getCodigoAuxiliar500_5() {
		return codigoAuxiliar500_5;
	}
	public void setCodigoAuxiliar500_5(String codigoAuxiliar500_5) {
		this.codigoAuxiliar500_5 = codigoAuxiliar500_5;
	}
	public String getTextoAuxiliar500_5() {
		return textoAuxiliar500_5;
	}
	public void setTextoAuxiliar500_5(String textoAuxiliar500_5) {
		this.textoAuxiliar500_5 = textoAuxiliar500_5;
	}
	public String getCodigoAuxiliar500_6() {
		return codigoAuxiliar500_6;
	}
	public void setCodigoAuxiliar500_6(String codigoAuxiliar500_6) {
		this.codigoAuxiliar500_6 = codigoAuxiliar500_6;
	}
	public String getTextoAuxiliar500_6() {
		return textoAuxiliar500_6;
	}
	public void setTextoAuxiliar500_6(String textoAuxiliar500_6) {
		this.textoAuxiliar500_6 = textoAuxiliar500_6;
	}
	public String getCodigoAuxiliar500_7() {
		return codigoAuxiliar500_7;
	}
	public void setCodigoAuxiliar500_7(String codigoAuxiliar500_7) {
		this.codigoAuxiliar500_7 = codigoAuxiliar500_7;
	}
	public String getTextoAuxiliar500_7() {
		return textoAuxiliar500_7;
	}
	public void setTextoAuxiliar500_7(String textoAuxiliar500_7) {
		this.textoAuxiliar500_7 = textoAuxiliar500_7;
	}
	public String getCodigoAuxiliar500_8() {
		return codigoAuxiliar500_8;
	}
	public void setCodigoAuxiliar500_8(String codigoAuxiliar500_8) {
		this.codigoAuxiliar500_8 = codigoAuxiliar500_8;
	}
	public String getTextoAuxiliar500_8() {
		return textoAuxiliar500_8;
	}
	public void setTextoAuxiliar500_8(String textoAuxiliar500_8) {
		this.textoAuxiliar500_8 = textoAuxiliar500_8;
	}
	public String getCodigoAuxiliar500_9() {
		return codigoAuxiliar500_9;
	}
	public void setCodigoAuxiliar500_9(String codigoAuxiliar500_9) {
		this.codigoAuxiliar500_9 = codigoAuxiliar500_9;
	}
	public String getTextoAuxiliar500_9() {
		return textoAuxiliar500_9;
	}
	public void setTextoAuxiliar500_9(String textoAuxiliar500_9) {
		this.textoAuxiliar500_9 = textoAuxiliar500_9;
	}
	public String getCodigoAuxiliar500_10() {
		return codigoAuxiliar500_10;
	}
	public void setCodigoAuxiliar500_10(String codigoAuxiliar500_10) {
		this.codigoAuxiliar500_10 = codigoAuxiliar500_10;
	}
	public String getTextoAuxiliar500_10() {
		return textoAuxiliar500_10;
	}
	public void setTextoAuxiliar500_10(String textoAuxiliar500_10) {
		this.textoAuxiliar500_10 = textoAuxiliar500_10;
	}
	public String getCodigoAuxiliar250_1() {
		return codigoAuxiliar250_1;
	}
	public void setCodigoAuxiliar250_1(String codigoAuxiliar250_1) {
		this.codigoAuxiliar250_1 = codigoAuxiliar250_1;
	}
	public String getTextoAuxiliar250_1() {
		return textoAuxiliar250_1;
	}
	public void setTextoAuxiliar250_1(String textoAuxiliar250_1) {
		this.textoAuxiliar250_1 = textoAuxiliar250_1;
	}
	public String getCodigoAuxiliar250_2() {
		return codigoAuxiliar250_2;
	}
	public void setCodigoAuxiliar250_2(String codigoAuxiliar250_2) {
		this.codigoAuxiliar250_2 = codigoAuxiliar250_2;
	}
	public String getTextoAuxiliar250_2() {
		return textoAuxiliar250_2;
	}
	public void setTextoAuxiliar250_2(String textoAuxiliar250_2) {
		this.textoAuxiliar250_2 = textoAuxiliar250_2;
	}
	public String getCodigoAuxiliar250_3() {
		return codigoAuxiliar250_3;
	}
	public void setCodigoAuxiliar250_3(String codigoAuxiliar250_3) {
		this.codigoAuxiliar250_3 = codigoAuxiliar250_3;
	}
	public String getTextoAuxiliar250_3() {
		return textoAuxiliar250_3;
	}
	public void setTextoAuxiliar250_3(String textoAuxiliar250_3) {
		this.textoAuxiliar250_3 = textoAuxiliar250_3;
	}
	public String getCodigoAuxiliar250_4() {
		return codigoAuxiliar250_4;
	}
	public void setCodigoAuxiliar250_4(String codigoAuxiliar250_4) {
		this.codigoAuxiliar250_4 = codigoAuxiliar250_4;
	}
	public String getTextoAuxiliar250_4() {
		return textoAuxiliar250_4;
	}
	public void setTextoAuxiliar250_4(String textoAuxiliar250_4) {
		this.textoAuxiliar250_4 = textoAuxiliar250_4;
	}
	public String getCodigoAuxiliar250_5() {
		return codigoAuxiliar250_5;
	}
	public void setCodigoAuxiliar250_5(String codigoAuxiliar250_5) {
		this.codigoAuxiliar250_5 = codigoAuxiliar250_5;
	}
	public String getTextoAuxiliar250_5() {
		return textoAuxiliar250_5;
	}
	public void setTextoAuxiliar250_5(String textoAuxiliar250_5) {
		this.textoAuxiliar250_5 = textoAuxiliar250_5;
	}
	public String getCodigoAuxiliar250_6() {
		return codigoAuxiliar250_6;
	}
	public void setCodigoAuxiliar250_6(String codigoAuxiliar250_6) {
		this.codigoAuxiliar250_6 = codigoAuxiliar250_6;
	}
	public String getTextoAuxiliar250_6() {
		return textoAuxiliar250_6;
	}
	public void setTextoAuxiliar250_6(String textoAuxiliar250_6) {
		this.textoAuxiliar250_6 = textoAuxiliar250_6;
	}
	public String getCodigoAuxiliar250_7() {
		return codigoAuxiliar250_7;
	}
	public void setCodigoAuxiliar250_7(String codigoAuxiliar250_7) {
		this.codigoAuxiliar250_7 = codigoAuxiliar250_7;
	}
	public String getTextoAuxiliar250_7() {
		return textoAuxiliar250_7;
	}
	public void setTextoAuxiliar250_7(String textoAuxiliar250_7) {
		this.textoAuxiliar250_7 = textoAuxiliar250_7;
	}
	public String getCodigoAuxiliar250_8() {
		return codigoAuxiliar250_8;
	}
	public void setCodigoAuxiliar250_8(String codigoAuxiliar250_8) {
		this.codigoAuxiliar250_8 = codigoAuxiliar250_8;
	}
	public String getTextoAuxiliar250_8() {
		return textoAuxiliar250_8;
	}
	public void setTextoAuxiliar250_8(String textoAuxiliar250_8) {
		this.textoAuxiliar250_8 = textoAuxiliar250_8;
	}
	public String getCodigoAuxiliar250_9() {
		return codigoAuxiliar250_9;
	}
	public void setCodigoAuxiliar250_9(String codigoAuxiliar250_9) {
		this.codigoAuxiliar250_9 = codigoAuxiliar250_9;
	}
	public String getTextoAuxiliar250_9() {
		return textoAuxiliar250_9;
	}
	public void setTextoAuxiliar250_9(String textoAuxiliar250_9) {
		this.textoAuxiliar250_9 = textoAuxiliar250_9;
	}
	public String getCodigoAuxiliar250_10() {
		return codigoAuxiliar250_10;
	}
	public void setCodigoAuxiliar250_10(String codigoAuxiliar250_10) {
		this.codigoAuxiliar250_10 = codigoAuxiliar250_10;
	}
	public String getTextoAuxiliar250_10() {
		return textoAuxiliar250_10;
	}
	public void setTextoAuxiliar250_10(String textoAuxiliar250_10) {
		this.textoAuxiliar250_10 = textoAuxiliar250_10;
	}
	
	public String getAuxiliar100_1() {
        if (StringUtils.isBlank(codigoAuxiliar100_1) || StringUtils.isBlank(textoAuxiliar100_1)) return null;
        return codigoAuxiliar100_1 + "|" + textoAuxiliar100_1;
    }

	public String getAuxiliar100_2() {
        if (StringUtils.isBlank(codigoAuxiliar100_2) || StringUtils.isBlank(textoAuxiliar100_2)) return null;
        return codigoAuxiliar100_2 + "|" + textoAuxiliar100_2;
    }

	public String getAuxiliar100_3() {
        if (StringUtils.isBlank(codigoAuxiliar100_3) || StringUtils.isBlank(textoAuxiliar100_3)) return null;
        return codigoAuxiliar100_3 + "|" + textoAuxiliar100_3;
    }

	public String getAuxiliar100_4() {
        if (StringUtils.isBlank(codigoAuxiliar100_4) || StringUtils.isBlank(textoAuxiliar100_4)) return null;
        return codigoAuxiliar100_4 + "|" + textoAuxiliar100_4;
    }

    public String getAuxiliar100_5() {
        if (StringUtils.isBlank(codigoAuxiliar100_5) || StringUtils.isBlank(textoAuxiliar100_5)) return null;
        return codigoAuxiliar100_5 + "|" + textoAuxiliar100_5;
    }

    public String getAuxiliar100_6() {
        if (StringUtils.isBlank(codigoAuxiliar100_6) || StringUtils.isBlank(textoAuxiliar100_6)) return null;
        return codigoAuxiliar100_6 + "|" + textoAuxiliar100_6;
    }

    public String getAuxiliar100_7() {
        if (StringUtils.isBlank(codigoAuxiliar100_7) || StringUtils.isBlank(textoAuxiliar100_7)) return null;
        return codigoAuxiliar100_7 + "|" + textoAuxiliar100_7;
    }

    public String getAuxiliar100_8() {
        if (StringUtils.isBlank(codigoAuxiliar100_8) || StringUtils.isBlank(textoAuxiliar100_8)) return null;
        return codigoAuxiliar100_8 + "|" + textoAuxiliar100_8;
    }

    public String getAuxiliar100_9() {
        if (StringUtils.isBlank(codigoAuxiliar100_9) || StringUtils.isBlank(textoAuxiliar100_9)) return null;
        return codigoAuxiliar100_9 + "|" + textoAuxiliar100_9;
    }

    public String getAuxiliar100_10() {
        if (StringUtils.isBlank(codigoAuxiliar100_10) || StringUtils.isBlank(textoAuxiliar100_10)) return null;
        return codigoAuxiliar100_10 + "|" + textoAuxiliar100_10;
    }

    public String getAuxiliar40_1() {
        if (StringUtils.isBlank(codigoAuxiliar40_1) || StringUtils.isBlank(textoAuxiliar40_1)) return null;
        return codigoAuxiliar40_1 + "|" + textoAuxiliar40_1;
    }

    public String getAuxiliar40_2() {
        if (StringUtils.isBlank(codigoAuxiliar40_2) || StringUtils.isBlank(textoAuxiliar40_2)) return null;
        return codigoAuxiliar40_2 + "|" + textoAuxiliar40_2;
    }

    public String getAuxiliar40_3() {
        if (StringUtils.isBlank(codigoAuxiliar40_3) || StringUtils.isBlank(textoAuxiliar40_3)) return null;
        return codigoAuxiliar40_3 + "|" + textoAuxiliar40_3;
    }

    public String getAuxiliar40_4() {
        if (StringUtils.isBlank(codigoAuxiliar40_4) || StringUtils.isBlank(textoAuxiliar40_4)) return null;
        return codigoAuxiliar40_4 + "|" + textoAuxiliar40_4;
    }

    public String getAuxiliar40_5() {
        if (StringUtils.isBlank(codigoAuxiliar40_5) || StringUtils.isBlank(textoAuxiliar40_5)) return null;
        return codigoAuxiliar40_5 + "|" + textoAuxiliar40_5;
    }

    public String getAuxiliar40_6() {
        if (StringUtils.isBlank(codigoAuxiliar40_6) || StringUtils.isBlank(textoAuxiliar40_6)) return null;
        return codigoAuxiliar40_6 + "|" + textoAuxiliar40_6;
    }

    public String getAuxiliar40_7() {
        if (StringUtils.isBlank(codigoAuxiliar40_7) || StringUtils.isBlank(textoAuxiliar40_7)) return null;
        return codigoAuxiliar40_7 + "|" + textoAuxiliar40_7;
    }

    public String getAuxiliar40_8() {
        if (StringUtils.isBlank(codigoAuxiliar40_8) || StringUtils.isBlank(textoAuxiliar40_8)) return null;
        return codigoAuxiliar40_8 + "|" + textoAuxiliar40_8;
    }

    public String getAuxiliar40_9() {
        if (StringUtils.isBlank(codigoAuxiliar40_9) || StringUtils.isBlank(textoAuxiliar40_9)) return null;
        return codigoAuxiliar40_9 + "|" + textoAuxiliar40_9;
    }

    public String getAuxiliar40_10() {
        if (StringUtils.isBlank(codigoAuxiliar40_10) || StringUtils.isBlank(textoAuxiliar40_10)) return null;
        return codigoAuxiliar40_10 + "|" + textoAuxiliar40_10;
    }
    
    public String getAuxiliar500_1() {
        if (StringUtils.isBlank(codigoAuxiliar500_1) || StringUtils.isBlank(textoAuxiliar500_1)) return null;
        return codigoAuxiliar500_1 + "|" + textoAuxiliar500_1;
    }
    
    public String getAuxiliar500_2() {
        if (StringUtils.isBlank(codigoAuxiliar500_2) || StringUtils.isBlank(textoAuxiliar500_2)) return null;
        return codigoAuxiliar500_2 + "|" + textoAuxiliar500_2;
    }

    public String getAuxiliar500_3() {
        if (StringUtils.isBlank(codigoAuxiliar500_3) || StringUtils.isBlank(textoAuxiliar500_3)) return null;
        return codigoAuxiliar500_3 + "|" + textoAuxiliar500_3;
    }

    public String getAuxiliar500_4() {
        if (StringUtils.isBlank(codigoAuxiliar500_4) || StringUtils.isBlank(textoAuxiliar500_4)) return null;
        return codigoAuxiliar500_4 + "|" + textoAuxiliar500_4;
    }

    public String getAuxiliar500_5() {
        if (StringUtils.isBlank(codigoAuxiliar500_5) || StringUtils.isBlank(textoAuxiliar500_5)) return null;
        return codigoAuxiliar500_5 + "|" + textoAuxiliar500_5;
    }
    
    public String getAuxiliar500_6() {
        if (StringUtils.isBlank(codigoAuxiliar500_6) || StringUtils.isBlank(textoAuxiliar500_6)) return null;
        return codigoAuxiliar500_6 + "|" + textoAuxiliar500_6;
    }
    
    public String getAuxiliar500_7() {
        if (StringUtils.isBlank(codigoAuxiliar500_7) || StringUtils.isBlank(textoAuxiliar500_7)) return null;
        return codigoAuxiliar500_7 + "|" + textoAuxiliar500_7;
    }

    public String getAuxiliar500_8() {
        if (StringUtils.isBlank(codigoAuxiliar500_8) || StringUtils.isBlank(textoAuxiliar500_8)) return null;
        return codigoAuxiliar500_8 + "|" + textoAuxiliar500_8;
    }

    public String getAuxiliar500_9() {
        if (StringUtils.isBlank(codigoAuxiliar500_9) || StringUtils.isBlank(textoAuxiliar500_9)) return null;
        return codigoAuxiliar500_9 + "|" + textoAuxiliar500_9;
    }

    public String getAuxiliar500_10() {
        if (StringUtils.isBlank(codigoAuxiliar500_10) || StringUtils.isBlank(textoAuxiliar500_10)) return null;
        return codigoAuxiliar500_10 + "|" + textoAuxiliar500_10;
    }    
    
    public String getAuxiliar250_1() {
        if (StringUtils.isBlank(codigoAuxiliar250_1) || StringUtils.isBlank(textoAuxiliar250_1)) return null;
        return codigoAuxiliar250_1 + "|" + textoAuxiliar250_1;
    }

    public String getAuxiliar250_2() {
        if (StringUtils.isBlank(codigoAuxiliar250_2) || StringUtils.isBlank(textoAuxiliar250_2)) return null;
        return codigoAuxiliar250_2 + "|" + textoAuxiliar250_2;
    }

    public String getAuxiliar250_3() {
        if (StringUtils.isBlank(codigoAuxiliar250_3) || StringUtils.isBlank(textoAuxiliar250_3)) return null;
        return codigoAuxiliar250_3 + "|" + textoAuxiliar250_3;
    }

    public String getAuxiliar250_4() {
        if (StringUtils.isBlank(codigoAuxiliar250_4) || StringUtils.isBlank(textoAuxiliar250_4)) return null;
        return codigoAuxiliar250_4 + "|" + textoAuxiliar250_4;
    }

    public String getAuxiliar250_5() {
        if (StringUtils.isBlank(codigoAuxiliar250_5) || StringUtils.isBlank(textoAuxiliar250_5)) return null;
        return codigoAuxiliar250_5 + "|" + textoAuxiliar250_5;
    }

    public String getAuxiliar250_6() {
        if (StringUtils.isBlank(codigoAuxiliar250_6) || StringUtils.isBlank(textoAuxiliar250_6)) return null;
        return codigoAuxiliar250_6 + "|" + textoAuxiliar250_6;
    }

    public String getAuxiliar250_7() {
        if (StringUtils.isBlank(codigoAuxiliar250_7) || StringUtils.isBlank(textoAuxiliar250_7)) return null;
        return codigoAuxiliar250_7 + "|" + textoAuxiliar250_7;
    }

    public String getAuxiliar250_8() {
        if (StringUtils.isBlank(codigoAuxiliar250_8) || StringUtils.isBlank(textoAuxiliar250_8)) return null;
        return codigoAuxiliar250_8 + "|" + textoAuxiliar250_8;
    }

    public String getAuxiliar250_9() {
        if (StringUtils.isBlank(codigoAuxiliar250_9) || StringUtils.isBlank(textoAuxiliar250_9)) return null;
        return codigoAuxiliar250_9 + "|" + textoAuxiliar250_9;
    }

    public String getAuxiliar250_10() {
        if (StringUtils.isBlank(codigoAuxiliar250_10) || StringUtils.isBlank(textoAuxiliar250_10)) return null;
        return codigoAuxiliar250_10 + "|" + textoAuxiliar250_10;
    }    
}
