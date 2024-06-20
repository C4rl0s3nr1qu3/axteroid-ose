package com.axteroid.ose.server.ubl20.gateway.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * User: RAC
 * Date: 14/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResumenItem", propOrder = {
        "indicador",
        "numeroFila",
        "tipoDocumento",
        "tipoMoneda",
        "serieDocumentoBaja",
        "numeroDocumentoBaja",
        "motivoBaja",
        "serieGrupoDocumento",
        "numeroCorrelativoInicio",
        "numeroCorrelativoFin",
        "totalValorVentaOpGravadasConIgv",
        "totalValorVentaOpInafectasIgv",
        "totalValorVentaOpGratuitas",
        "totalOtrosTributos",
        "totalIgv",
        "totalVenta",
        "totalIsc",
        "totalOtrosCargos",
        "totalValorVentaOpExoneradasIgv"
})
public class EResumenItemCliente {
    /**
     * parametro
     * sac:SummaryDocumentsLine/cbc:LineID
     */
    protected Long numeroFila;

    /**
     * parametro
     * sac:SummaryDocumentsLine/cbc:DocumentTypeCode
     */
    protected  String indicador;
    protected  String tipoDocumento;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:DocumentSerialID
     */
    protected  String serieGrupoDocumento;

    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:DocumentSerialID
     */
    protected  String serieDocumentoBaja;
    /**
     * parametro
     * /sac:VoidedDocumentsLine/sac:DocumentNumberID
     */
    protected  String numeroDocumentoBaja;

    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:VoidReasonDescription
     */
    protected  String motivoBaja;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:StartDocumentNumberID
     */
    protected  String numeroCorrelativoInicio;

    protected  String tipoMoneda;


    protected  BigDecimal totalValorVentaOpGratuitas;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:EndDocumentNumberID
     */
    protected  String numeroCorrelativoFin;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */

    protected  BigDecimal totalValorVentaOpGravadasConIgv;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */

    protected  BigDecimal totalValorVentaOpExoneradasIgv;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */

    protected  BigDecimal totalValorVentaOpInafectasIgv;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */
//    protected  BigDecimal totalValorVentaExportacion;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total ISC del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total ISC del item)
     */

    protected  BigDecimal totalIsc;

//    protected  BigDecimal totalValorVentaOpExoneradasConIgv;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total IGV del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total IGV del item)
     */

    protected  BigDecimal totalIgv;

    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total Otros Tributos del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total Otros Tributos del item)
     */

    public BigDecimal totalOtrosTributos;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:TotalAmount/@currencyID
     */

    public BigDecimal totalVenta;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:AllowanceCharge/cbc:Amount/@currencyID (Monto de otros cargos)
     */

    public BigDecimal totalOtrosCargos;


    public EResumenItemCliente() {
    }

    public EResumenItemCliente(String s, String s1) {
        this.setTipoDocumento(s);
        this.setSerieDocumentoBaja(s1);
    }


    public Long getNumeroFila() {
        return numeroFila;
    }

    public void setNumeroFila(Long numeroFila) {
        this.numeroFila = numeroFila;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getSerieGrupoDocumento() {
        return serieGrupoDocumento;
    }

    public void setSerieGrupoDocumento(String serieGrupoDocumento) {
        this.serieGrupoDocumento = serieGrupoDocumento;
    }

    public String getNumeroCorrelativoInicio() {
        return numeroCorrelativoInicio;
    }

    public void setNumeroCorrelativoInicio(String numeroCorrelativoInicio) {
        this.numeroCorrelativoInicio = numeroCorrelativoInicio;
    }

    public String getNumeroCorrelativoFin() {
        return numeroCorrelativoFin;
    }

    public void setNumeroCorrelativoFin(String numeroCorrelativoFin) {
        this.numeroCorrelativoFin = numeroCorrelativoFin;
    }

    public BigDecimal getTotalValorVentaOpGravadasConIgv() {
        return totalValorVentaOpGravadasConIgv;
    }

    public void setTotalValorVentaOpGravadasConIgv(BigDecimal totalValorVentaOpGravadasConIgv) {
        this.totalValorVentaOpGravadasConIgv = totalValorVentaOpGravadasConIgv;
    }

    public String getSerieDocumentoBaja() {
        return serieDocumentoBaja;
    }

    public void setSerieDocumentoBaja(String serieDocumentoBaja) {
        this.serieDocumentoBaja = serieDocumentoBaja;
    }

    public String getNumeroDocumentoBaja() {
        return numeroDocumentoBaja;
    }

    public void setNumeroDocumentoBaja(String numeroDocumentoBaja) {
        this.numeroDocumentoBaja = numeroDocumentoBaja;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public BigDecimal getTotalValorVentaOpExoneradasIgv() {
        return totalValorVentaOpExoneradasIgv;
    }

    public void setTotalValorVentaOpExoneradasIgv(BigDecimal totalValorVentaOpExoneradasIgv) {
        this.totalValorVentaOpExoneradasIgv = totalValorVentaOpExoneradasIgv;
    }

    public BigDecimal getTotalValorVentaOpInafectasIgv() {
        return totalValorVentaOpInafectasIgv;
    }

    public void setTotalValorVentaOpInafectasIgv(BigDecimal totalValorVentaOpInafectasIgv) {
        this.totalValorVentaOpInafectasIgv = totalValorVentaOpInafectasIgv;
    }

/*
    public BigDecimal getTotalValorVentaExportacion() {
        return totalValorVentaExportacion;
    }

    public void setTotalValorVentaExportacion(BigDecimal totalValorVentaExportacion) {
        this.totalValorVentaExportacion = totalValorVentaExportacion;
    }
*/

    public BigDecimal getTotalIsc() {
        return totalIsc;
    }

    public void setTotalIsc(BigDecimal totalIsc) {
        this.totalIsc = totalIsc;
    }

    public BigDecimal getTotalIgv() {
        return totalIgv;
    }

    public void setTotalIgv(BigDecimal totalIgv) {
        this.totalIgv = totalIgv;
    }

    public BigDecimal getTotalOtrosTributos() {
        return totalOtrosTributos;
    }

    public void setTotalOtrosTributos(BigDecimal totalOtrosTributos) {
        this.totalOtrosTributos = totalOtrosTributos;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public BigDecimal getTotalOtrosCargos() {
        return totalOtrosCargos;
    }

    public void setTotalOtrosCargos(BigDecimal totalOtrosCargos) {
        this.totalOtrosCargos = totalOtrosCargos;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

/*
    public BigDecimal getTotalValorVentaOpExoneradasConIgv() {
        return totalValorVentaOpExoneradasConIgv;
    }

    public void setTotalValorVentaOpExoneradasConIgv(BigDecimal totalValorVentaOpExoneradasConIgv) {
        this.totalValorVentaOpExoneradasConIgv = totalValorVentaOpExoneradasConIgv;
    }
*/



    public BigDecimal getTotalValorVentaOpGratuitas() {
        return totalValorVentaOpGratuitas;
    }

    public void setTotalValorVentaOpGratuitas(BigDecimal totalValorVentaOpGratuitas) {
        this.totalValorVentaOpGratuitas = totalValorVentaOpGratuitas;
    }
}
