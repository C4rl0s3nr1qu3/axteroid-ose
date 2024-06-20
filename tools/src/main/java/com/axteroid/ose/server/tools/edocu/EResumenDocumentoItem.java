package com.axteroid.ose.server.tools.edocu;

import com.axteroid.ose.server.tools.ubltype.TypePayment;
import com.axteroid.ose.server.tools.ubltype.TypeTaxSubtotal;
import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.validation.ValidateFormatNumber;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: RAC
 * Date: 14/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EResumenDocumentoItem {
    /**
     * parametro
     * sac:SummaryDocumentsLine/cbc:LineID
     */
    @NotNull
    private Long numeroFila;

    /**
     * parametro
     * sac:SummaryDocumentsLine/cbc:DocumentTypeCode
     */
    @NotBlank(message = "7041")
    private String tipoDocumento;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:DocumentSerialID
     */

    @NotBlank(groups = DocumentSummary.class)
    @Length(max = 4, groups = DocumentSummary.class)
    private String serieGrupoDocumento;

    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:DocumentSerialID
     */
    @NotBlank(groups = DocumentVoid.class)
    @Length(max = 4, groups = DocumentVoid.class)
    private String serieDocumentoBaja;


    private String tipoMoneda;

    //@Transient
    private String serieNumeroBaja;

    /**
     * parametro
     * /sac:VoidedDocumentsLine/sac:DocumentNumberID
     */
    @NotBlank(groups = DocumentVoid.class)
    private String numeroDocumentoBaja;

    /**
     * parametro
     * sac:VoidedDocumentsLine/sac:VoidReasonDescription
     */
    @NotBlank(groups = DocumentVoid.class)
    private String motivoBaja;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:StartDocumentNumberID
     */

    @NotBlank(groups = DocumentSummary.class)
    private String numeroCorrelativoInicio;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:EndDocumentNumberID
     */

    @NotBlank(groups = DocumentSummary.class)
    private String numeroCorrelativoFin;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */

    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalValorVentaOpGravadasConIgv;

    /**
     * default:01
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:InstructionID (Cdigo de tipo de valor de venta - Catlogo No 11)
     */
    private String tipoTotalValorVentaOpGravadasConIgv;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalValorVentaOpExoneradasIgv;

    /**
     * default:02
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:InstructionID (Cdigo de tipo de valor de venta - Catlogo No 11)
     */
    private String tipoTotalValorVentaOpExoneradasIgv;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalValorVentaOpInafectasIgv;

    /**
     * default:03
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:InstructionID (Cdigo de tipo de valor de venta - Catlogo No 11)
     */
    private String tipoTotalValorVentaOpInafectasIgv;

    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:PaidAmount/@currencyID (Monto)
     */
//    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalValorVentaExportacion;

    /**
     * default:04
     * sac:SummaryDocumentsLine/sac:BillingPayment/cbc:InstructionID (Cdigo de tipo de valor de venta - Catlogo No 11)
     */

    private String tipoTotalValorVentaExportacion;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total ISC del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total ISC del item)
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalIsc;

    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    protected  BigDecimal totalValorVentaOpGratuitas;

    /**
     * default:2000
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID (Cdigo de tributo - Catlogo No. 05)
     */
    private String codigoTributoIsc;

    /**
     * default: ISC    IMPUESTO SELECTIVO AL CONSUMO
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name (Nombre de tributo - Catlogo No. 05)
     */
    private String nombreTributoIsc;

    /**
     * default:EXC
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode (Cdigo internacional tributo - Catlogo No. 05)
     */
    private String codigoInternacionalTributoIsc;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total IGV del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total IGV del item)
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal totalIgv;

    /**
     * default : 1000
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total IGV del item)
     */
    private String codigoTributoIgv;

    /**
     * default : IGV    IMPUESTO GENERAL A LAS VENTAS
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name (Nombre de tributo - Catlogo No. 05)
     */
    private String nombreTributoIgv;

    /**
     * default : VAT
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode (Cdigo internacional tributo - Catlogo No. 05)
     */
    private String codigoInternacionalTributoIgv;
    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto Total Otros Tributos del item)
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto Total Otros Tributos del item)
     */
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    public BigDecimal totalOtrosTributos;
    /**
     * default : 9999
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID (Cdigo de tributo - Catlogo No. 05)
     */
    private String codigoOtrosTributos;

    /**
     * default : OTROS CONCEPTOS DE PAGO
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name (Nombre de tributo - Catlogo No. 05)
     */
    private String nombreOtrosTributos;

    /**
     * default : OTH
     * sac:SummaryDocumentsLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode (Cdigo internacional tributo - Catlogo No. 05)
     */
    private String codigoInternacionalOtrosTributos;


    /**
     * parametro
     * sac:SummaryDocumentsLine/sac:TotalAmount/@currencyID
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal totalVenta;


    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:AllowanceCharge/cbc:Amount/@currencyID (Monto de otros cargos)
     */
    @NotNull(groups = DocumentSummary.class)
//    @XmlJavaTypeAdapter(NumberAdapter.class)
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    public BigDecimal totalOtrosCargos;

    /**
     * parametro
     * sac:SummaryDocumentsLine/cac:AllowanceCharge/cbc:Amount/@currencyID (Indicador de descuento)
     */
/*
    @NotNull(groups = DocumentSummary.class)
    public BigDecimal totalOtrosDescuentos;        
*/
    private List<TypePayment> listTypePayment = new ArrayList<TypePayment>();
    
    private List<TypeTaxSubtotal> listTypeTaxSubtotal = new ArrayList<TypeTaxSubtotal>();
    
    private String id;
    private String lineID;
    private String documentTypeCode;
    private int status;
    private String brID;
    private String brDocumentTypeCode;
    private Boolean sunatPerceptionSummaryDocumentReference = false;
    private String acpCustomerAssignedAccountID;
    private String acpAdditionalAccountID;    
    //private String codigoOperacionItem;
    
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

    public String getTipoTotalValorVentaOpGravadasConIgv() {
        return tipoTotalValorVentaOpGravadasConIgv;
    }

    public void setTipoTotalValorVentaOpGravadasConIgv(String tipoTotalValorVentaOpGravadasConIgv) {
        this.tipoTotalValorVentaOpGravadasConIgv = tipoTotalValorVentaOpGravadasConIgv;
    }

    public BigDecimal getTotalValorVentaOpExoneradasIgv() {
        return totalValorVentaOpExoneradasIgv;
    }

    public void setTotalValorVentaOpExoneradasIgv(BigDecimal totalValorVentaOpExoneradasIgv) {
        this.totalValorVentaOpExoneradasIgv = totalValorVentaOpExoneradasIgv;
    }

    public String getTipoTotalValorVentaOpExoneradasIgv() {
        return tipoTotalValorVentaOpExoneradasIgv;
    }

    public void setTipoTotalValorVentaOpExoneradasIgv(String tipoTotalValorVentaOpExoneradasIgv) {
        this.tipoTotalValorVentaOpExoneradasIgv = tipoTotalValorVentaOpExoneradasIgv;
    }

    public BigDecimal getTotalValorVentaOpInafectasIgv() {
        return totalValorVentaOpInafectasIgv;
    }

    public void setTotalValorVentaOpInafectasIgv(BigDecimal totalValorVentaOpInafectasIgv) {
        this.totalValorVentaOpInafectasIgv = totalValorVentaOpInafectasIgv;
    }

    public String getTipoTotalValorVentaOpInafectasIgv() {
        return tipoTotalValorVentaOpInafectasIgv;
    }

    public void setTipoTotalValorVentaOpInafectasIgv(String tipoTotalValorVentaOpInafectasIgv) {
        this.tipoTotalValorVentaOpInafectasIgv = tipoTotalValorVentaOpInafectasIgv;
    }

    public BigDecimal getTotalValorVentaExportacion() {
        return totalValorVentaExportacion;
    }

    public void setTotalValorVentaExportacion(BigDecimal totalValorVentaExportacion) {
        this.totalValorVentaExportacion = totalValorVentaExportacion;
    }

    public String getTipoTotalValorVentaExportacion() {
        return tipoTotalValorVentaExportacion;
    }

    public void setTipoTotalValorVentaExportacion(String tipoTotalValorVentaExportacion) {
        this.tipoTotalValorVentaExportacion = tipoTotalValorVentaExportacion;
    }

    public BigDecimal getTotalIsc() {
        return totalIsc;
    }

    public void setTotalIsc(BigDecimal totalIsc) {
        this.totalIsc = totalIsc;
    }

    public String getCodigoTributoIsc() {
        return codigoTributoIsc;
    }

    public void setCodigoTributoIsc(String codigoTributoIsc) {
        this.codigoTributoIsc = codigoTributoIsc;
    }

    public String getNombreTributoIsc() {
        return nombreTributoIsc;
    }

    public void setNombreTributoIsc(String nombreTributoIsc) {
        this.nombreTributoIsc = nombreTributoIsc;
    }

    public String getCodigoInternacionalTributoIsc() {
        return codigoInternacionalTributoIsc;
    }

    public void setCodigoInternacionalTributoIsc(String codigoInternacionalTributoIsc) {
        this.codigoInternacionalTributoIsc = codigoInternacionalTributoIsc;
    }

    public BigDecimal getTotalIgv() {
        return totalIgv;
    }

    public void setTotalIgv(BigDecimal totalIgv) {
        this.totalIgv = totalIgv;
    }

    public String getCodigoTributoIgv() {
        return codigoTributoIgv;
    }

    public void setCodigoTributoIgv(String codigoTributoIgv) {
        this.codigoTributoIgv = codigoTributoIgv;
    }

    public String getNombreTributoIgv() {
        return nombreTributoIgv;
    }

    public void setNombreTributoIgv(String nombreTributoIgv) {
        this.nombreTributoIgv = nombreTributoIgv;
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

    public String getCodigoInternacionalTributoIgv() {
        return codigoInternacionalTributoIgv;
    }

    public void setCodigoInternacionalTributoIgv(String codigoInternacionalTributoIgv) {
        this.codigoInternacionalTributoIgv = codigoInternacionalTributoIgv;
    }

    public BigDecimal getTotalOtrosTributos() {
        return totalOtrosTributos;
    }

    public void setTotalOtrosTributos(BigDecimal totalOtrosTributos) {
        this.totalOtrosTributos = totalOtrosTributos;
    }

    public String getCodigoOtrosTributos() {
        return codigoOtrosTributos;
    }

    public void setCodigoOtrosTributos(String codigoOtrosTributos) {
        this.codigoOtrosTributos = codigoOtrosTributos;
    }

    public String getNombreOtrosTributos() {
        return nombreOtrosTributos;
    }

    public void setNombreOtrosTributos(String nombreOtrosTributos) {
        this.nombreOtrosTributos = nombreOtrosTributos;
    }

    public String getCodigoInternacionalOtrosTributos() {
        return codigoInternacionalOtrosTributos;
    }

    public void setCodigoInternacionalOtrosTributos(String codigoInternacionalOtrosTributos) {
        this.codigoInternacionalOtrosTributos = codigoInternacionalOtrosTributos;
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

    public String getSerieNumeroBaja() {
        return serieDocumentoBaja + "-" + numeroDocumentoBaja;
    }

    public String getIdDocumento() {
        return serieDocumentoBaja + "-" + numeroDocumentoBaja;
    }

    public String getIdDocumentoResumen() {
        return  getSerieGrupoDocumento()+"-"+ getNumeroCorrelativoInicio()+ "-" + getNumeroCorrelativoFin();
    }

    public void setSerieNumeroBaja(String serieNumeroBaja) {
        if (StringUtils.isBlank(serieNumeroBaja)) return;
        String[] values = StringUtils.split(serieNumeroBaja, "-");
        serieDocumentoBaja = values[0];
        numeroDocumentoBaja = values[1];
    }

    public String buildTotalIgv() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalIgv);
    }

    public String buildTotalIsc() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalIsc == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalIsc);
    }

    public String buildTotalOtrosTributos() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalOtrosTributos == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalOtrosTributos);
    }

    public String buildTotalValorVentaOpInafectasIgv() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaOpInafectasIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaOpInafectasIgv);
    }

    public String buildTotalValorVentaExportacion() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaExportacion == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaExportacion);
    }

    public String buildTotalValorVentaOpGravadasConIgv() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaOpGravadasConIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaOpGravadasConIgv);
    }

    public String buildTotalValorVentaOpExoneradasIgv() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaOpExoneradasIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaOpExoneradasIgv);
    }

    public String buildTotalValorVentaOpGratuitas() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaOpGratuitas== null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaOpGratuitas);
    }

    public String buildTotalVenta() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalVenta == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalVenta);
    }

    public String buildTotalOtrosCargos() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalOtrosCargos == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalOtrosCargos);
    }

/*
    public String buildTotalOtrosDescuentos() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalOtrosDescuentos == null) return null;
        return tipoMoneda + "-" + totalOtrosDescuentos;
    }
*/

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public int totalItemsResumenDiario() {
        return Integer.parseInt(numeroCorrelativoFin) - Integer.parseInt(numeroCorrelativoInicio)+1;
    }


    public String getIdentificador(){
        return tipoDocumento+"-"+getIdDocumento();
    }


    @Override
    public String toString() {
        return "EResumenDocumentoItem{" +
                "numeroFila=" + numeroFila +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", serieGrupoDocumento='" + serieGrupoDocumento + '\'' +
                ", serieDocumentoBaja='" + serieDocumentoBaja + '\'' +
                ", tipoMoneda='" + tipoMoneda + '\'' +
                ", serieNumeroBaja='" + serieNumeroBaja + '\'' +
                ", numeroDocumentoBaja='" + numeroDocumentoBaja + '\'' +
                ", motivoBaja='" + motivoBaja + '\'' +
                ", numeroCorrelativoInicio='" + numeroCorrelativoInicio + '\'' +
                ", numeroCorrelativoFin='" + numeroCorrelativoFin + '\'' +
                ", totalValorVentaOpGravadasConIgv=" + totalValorVentaOpGravadasConIgv +
                ", tipoTotalValorVentaOpGravadasConIgv='" + tipoTotalValorVentaOpGravadasConIgv + '\'' +
                ", totalValorVentaOpExoneradasIgv=" + totalValorVentaOpExoneradasIgv +
                ", tipoTotalValorVentaOpExoneradasIgv='" + tipoTotalValorVentaOpExoneradasIgv + '\'' +
                ", totalValorVentaOpInafectasIgv=" + totalValorVentaOpInafectasIgv +
                ", tipoTotalValorVentaOpInafectasIgv='" + tipoTotalValorVentaOpInafectasIgv + '\'' +
                ", totalValorVentaExportacion=" + totalValorVentaExportacion +
                ", tipoTotalValorVentaExportacion='" + tipoTotalValorVentaExportacion + '\'' +
                ", totalIsc=" + totalIsc +
                ", codigoTributoIsc='" + codigoTributoIsc + '\'' +
                ", nombreTributoIsc='" + nombreTributoIsc + '\'' +
                ", codigoInternacionalTributoIsc='" + codigoInternacionalTributoIsc + '\'' +
                ", totalIgv=" + totalIgv +
                ", codigoTributoIgv='" + codigoTributoIgv + '\'' +
                ", nombreTributoIgv='" + nombreTributoIgv + '\'' +
                ", codigoInternacionalTributoIgv='" + codigoInternacionalTributoIgv + '\'' +
                ", totalOtrosTributos=" + totalOtrosTributos +
                ", codigoOtrosTributos='" + codigoOtrosTributos + '\'' +
                ", nombreOtrosTributos='" + nombreOtrosTributos + '\'' +
                ", codigoInternacionalOtrosTributos='" + codigoInternacionalOtrosTributos + '\'' +
                ", totalVenta=" + totalVenta +
                ", totalOtrosCargos=" + totalOtrosCargos +
                '}';
    }


    public BigDecimal getTotalValorVentaOpGratuitas() {
        return totalValorVentaOpGratuitas;
    }

    public void setTotalValorVentaOpGratuitas(BigDecimal totalValorVentaOpGratuitas) {
        this.totalValorVentaOpGratuitas = totalValorVentaOpGratuitas;
    }

	public List<TypePayment> getListTypePayment() {
		return listTypePayment;
	}

	public void setListTypePayment(List<TypePayment> listTypePayment) {
		this.listTypePayment = listTypePayment;
	}

	public List<TypeTaxSubtotal> getListTypeTaxSubtotal() {
		return listTypeTaxSubtotal;
	}

	public void setListTypeTaxSubtotal(List<TypeTaxSubtotal> listTypeTaxSubtotal) {
		this.listTypeTaxSubtotal = listTypeTaxSubtotal;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public String getBrID() {
		return brID;
	}

	public void setBrID(String brID) {
		this.brID = brID;
	}

	public String getBrDocumentTypeCode() {
		return brDocumentTypeCode;
	}

	public void setBrDocumentTypeCode(String brDocumentTypeCode) {
		this.brDocumentTypeCode = brDocumentTypeCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getSunatPerceptionSummaryDocumentReference() {
		return sunatPerceptionSummaryDocumentReference;
	}

	public void setSunatPerceptionSummaryDocumentReference(Boolean sunatPerceptionSummaryDocumentReference) {
		this.sunatPerceptionSummaryDocumentReference = sunatPerceptionSummaryDocumentReference;
	}

	public String getAcpCustomerAssignedAccountID() {
		return acpCustomerAssignedAccountID;
	}

	public void setAcpCustomerAssignedAccountID(String acpCustomerAssignedAccountID) {
		this.acpCustomerAssignedAccountID = acpCustomerAssignedAccountID;
	}

	public String getAcpAdditionalAccountID() {
		return acpAdditionalAccountID;
	}

	public void setAcpAdditionalAccountID(String acpAdditionalAccountID) {
		this.acpAdditionalAccountID = acpAdditionalAccountID;
	}


}
