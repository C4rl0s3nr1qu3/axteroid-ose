package com.axteroid.ose.server.tools.edocu;



import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.axteroid.ose.server.tools.ubltype.TypeDelivery;
import com.axteroid.ose.server.tools.ubltype.TypeItem;
import com.axteroid.ose.server.tools.ubltype.TypeParty;
import com.axteroid.ose.server.tools.ubltype.TypePricingReference;
import com.axteroid.ose.server.tools.ubltype.TypeTaxSubtotal;
import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.validation.ValidateFormatNumber;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13/02/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EDocumentoItem {

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cbc:ID
     */
    @NotBlank(message = "7047")
    private String numeroOrdenItem;

    /**
     * input: parametro
     * xml: cac:Item/cac:SellersItemIdentification/cbc:ID
     */
    private String codigoProducto;

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cac:Item/cbc:Description
     */
    @NotBlank(groups = {DocumentInvoice.class, DocumentBoleta.class}, message = "7050")
    private String descripcion;

    /**
     * input: parametro
     * xml: cac:CreditNoteLine/cbc:CreditedQuantity
     * xml: cac:InvoiceLine/cbc:InvoicedQuantity
     */
    @Min(value = 0,message = "7166")
    @NotNull(groups = {DocumentInvoice.class, DocumentBoleta.class},message = "7046")
    @ValidateFormatNumber(presicion = 14,scale = 10,message = "7215")
    private BigDecimal cantidad=BigDecimal.ZERO;

    /**
     * input: parametro
     * xml: cac:CreditNoteLine/cbc:CreditedQuantity
     * xml: cac:InvoiceLine/cbc:InvoicedQuantity/@unitCode (Unidad de medida - Católogo No. 03)
     */
    @NotBlank(groups = {DocumentInvoice.class, DocumentBoleta.class},message = "7043")
    private String unidadMedida;


    private String unidadMedidaComercial;
    /**
     * input: parametro
     * xml: cac:InvoiceLine/cbc:LineExtensionAmount/@currencyID
     */
    @NotNull(message = "7045")
    @Min(value = 0,message = "7166")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal importeTotalSinImpuesto;

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cac:Price/cbc:PriceAmount/@currencyID
     */
    @NotNull(message = "7044")
    @Min(value = 0,message = "7166")
    @ValidateFormatNumber(presicion = 14,scale = 10,message = "7215")
    private BigDecimal importeUnitarioSinImpuesto;

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceAmount/@currencyID (Monto de precio de venta)
     */
    @NotNull(groups = {DocumentInvoice.class, DocumentBoleta.class},message = "7042")
    @DecimalMin(value = "0.00",message = "7169")
    @ValidateFormatNumber(presicion = 12,scale = 10,message = "7215")
    private BigDecimal importeUnitarioConImpuesto;


    @NotBlank(groups = {DocumentInvoice.class, DocumentBoleta.class}, message = "7206")
    protected  String  codigoImporteUnitarioConImpuesto;

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto de IGV de la lónea)
     * xml: cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto de IGV de la lónea)
     */

    @Min(value = 0,message = "7166")
    @NotNull(groups = {DocumentInvoice.class, DocumentBoleta.class}, message = "7052")
    private BigDecimal importeIgv;

    private String tipoMoneda;

    @NotBlank(groups = {DocumentInvoice.class, DocumentBoleta.class}, message = "7051")
    private String codigoRazonExoneracion;


    private String codigoImporteReferencial;

    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal importeReferencial;

    /**
     * input: parametro
     * xml: cac:InvoiceLine/cac:TaxTotal/cbc:TaxAmount/@currencyID (Monto de ISC de la lónea)
     * xml: cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cbc:TaxAmount/@currencyID (Monto de ISC de la lónea)
     */
    @Min(value = 0,message = "7166")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal importeIsc;

    private String tipoSistemaImpuestoISC;

    @Min(value = 0,message = "7166")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal importeDescuento;

    @Min(value = 0,message = "7166")
    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7215")
    private BigDecimal importeCargo;


    //  xml: cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode (Afectación al IGV - Católogo No. 07)

    /**
     * default : 01
     * cac:InvoiceLine/cac:PricingReference/cac:AlternativeConditionPrice/cbc:PriceTypeCode (Código de tipo de precio - Católogo No. 16)
     */

    private String tipoPrecio;

    /**
     * default : 1000
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID (Código de tributo - Católogo No. 05)
     */
    private String codigoTributoIgv;

    /**
     * default : IGV    IMPUESTO GENERAL A LAS VENTAS
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name (Nombre de tributo - Católogo No. 05)
     */
    private String nombreTributoIgv;

    /**
     * default : VAT
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode (Código internacional tributo - Católogo No. 05)"
     */
    private String codigoInternacionalTributoIgv;


    /**
     * default : 03
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cbc:TaxExemptionReasonCode (Afectación al IGV - Católogo No. 07)
     */
    private String codigoAfectacionIsc;

    /**
     * default : 2000
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:ID (Código de tributo - Católogo No. 05)
     */
    private String codigoTributoIsc;

    /**
     * default : ISC    IMPUESTO SELECTIVO AL CONSUMO
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:Name (Nombre de tributo - Católogo No. 05)
     */
    private String nombreTributoIsc;

    /**
     * default : EXC
     * cac:InvoiceLine/cac:TaxTotal/cac:TaxSubtotal/cac:TaxCategory/cac:TaxScheme/cbc:TaxTypeCode (Código internacional tributo - Católogo No. 05)"
     */
    private String codigoInternacionalTributoIsc;


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
    
    private String lineID;
    private List<TypePricingReference> listTypePricingReference = new ArrayList<TypePricingReference>();
    private List<TypeTaxSubtotal> listTypeTaxSubtotal = new ArrayList<TypeTaxSubtotal>();
    private List<TypeDelivery> delivery = new ArrayList<TypeDelivery>();    
    private TypeItem item = new TypeItem();
    private TypeParty originatorParty = new TypeParty();
    
    public String getNumeroOrdenItem() {
        return numeroOrdenItem;
    }

    public void setNumeroOrdenItem(String numeroOrdenItem) {
        this.numeroOrdenItem = numeroOrdenItem;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public BigDecimal getImporteUnitarioConImpuesto() {
        return importeUnitarioConImpuesto;
    }

    public void setImporteUnitarioConImpuesto(BigDecimal importeUnitarioConImpuesto) {
        this.importeUnitarioConImpuesto = importeUnitarioConImpuesto;
    }

    public BigDecimal getImporteIgv() {
        return importeIgv;
    }

    public void setImporteIgv(BigDecimal importeIgv) {
        this.importeIgv = importeIgv;
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

    public String getCodigoInternacionalTributoIgv() {
        return codigoInternacionalTributoIgv;
    }

    public void setCodigoInternacionalTributoIgv(String codigoInternacionalTributoIgv) {
        this.codigoInternacionalTributoIgv = codigoInternacionalTributoIgv;
    }

    public BigDecimal getImporteIsc() {
        return importeIsc;
    }

    public void setImporteIsc(BigDecimal importeIsc) {
        this.importeIsc = importeIsc;
    }

    public String getCodigoAfectacionIsc() {
        return codigoAfectacionIsc;
    }

    public void setCodigoAfectacionIsc(String codigoAfectacionIsc) {
        this.codigoAfectacionIsc = codigoAfectacionIsc;
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

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public BigDecimal getImporteUnitarioSinImpuesto() {
        return importeUnitarioSinImpuesto;
    }

    public void setImporteUnitarioSinImpuesto(BigDecimal importeUnitarioSinImpuesto) {
        this.importeUnitarioSinImpuesto = importeUnitarioSinImpuesto;
    }

    public BigDecimal getImporteTotalSinImpuesto() {
        return importeTotalSinImpuesto;
    }

    public void setImporteTotalSinImpuesto(BigDecimal importeTotalSinImpuesto) {
        this.importeTotalSinImpuesto = importeTotalSinImpuesto;
    }

    public BigDecimal getImporteDescuento() {
        return importeDescuento;
    }

    public void setImporteDescuento(BigDecimal importeDescuento) {
        this.importeDescuento = importeDescuento;
    }

    public BigDecimal getImporteCargo() {
        return importeCargo;
    }

    public void setImporteCargo(BigDecimal importeCargo) {
        this.importeCargo = importeCargo;
    }


    public String getCodigoRazonExoneracion() {
        return codigoRazonExoneracion;
    }

    public void setCodigoRazonExoneracion(String codigoRazonExoneracion) {
        this.codigoRazonExoneracion = codigoRazonExoneracion;
    }

    public String getTipoSistemaImpuestoISC() {
        return tipoSistemaImpuestoISC;
    }

    public void setTipoSistemaImpuestoISC(String tipoSistemaImpuestoISC) {
        this.tipoSistemaImpuestoISC = tipoSistemaImpuestoISC;
    }

    public String buildImporteIgv() {
        if (StringUtils.isBlank(codigoRazonExoneracion) ||
                StringUtils.isBlank(tipoMoneda)
                || importeIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeIgv) + "|" + codigoRazonExoneracion;
    }

    public String buildImporteUnitarioConImpuesto() {
        if (StringUtils.isBlank(tipoMoneda) ||
                importeUnitarioConImpuesto == null ||
                StringUtils.isBlank(codigoImporteUnitarioConImpuesto)) return null;
        return codigoImporteUnitarioConImpuesto+"|"+tipoMoneda + "|" + NumberUtil.toFormat(importeUnitarioConImpuesto);
    }

    public String buildImporteReferencial() {
        if (StringUtils.isBlank(tipoMoneda) ||
                importeReferencial == null ||
                StringUtils.isBlank(codigoImporteReferencial)) return null;
        return codigoImporteReferencial+"|"+tipoMoneda + "|" + NumberUtil.toFormat(importeReferencial) ;
    }


    public String buildImporteDescuento() {
        if (StringUtils.isBlank(tipoMoneda)
                || importeDescuento == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeDescuento) ;
    }

    public String buildImporteCargo() {
        if (StringUtils.isBlank(tipoMoneda)
                || importeCargo== null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeCargo);
    }



    public String buildImporteIsc() {
        if (StringUtils.isBlank(tipoSistemaImpuestoISC) ||
                StringUtils.isBlank(tipoMoneda) ||
                importeIsc == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeIsc) + "|" + tipoSistemaImpuestoISC;
    }

    public String buildUnidadComercialCodigo() {
        if (StringUtils.isBlank(unidadMedidaComercial)) return null;
        return "9005"+"|"+unidadMedidaComercial;
    }

    public String buildImporteUnitarioSinImpuesto() {
        if (StringUtils.isBlank(tipoMoneda) ||
                importeUnitarioSinImpuesto == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeUnitarioSinImpuesto);
    }

    public String buildImporteTotalSinImpuesto() {
        if (StringUtils.isBlank(tipoMoneda) ||
                importeTotalSinImpuesto == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(importeTotalSinImpuesto);
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
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

    public static void main(String[] args) {

    }

    public String getCodigoImporteUnitarioConImpuesto() {
        return codigoImporteUnitarioConImpuesto;
    }

    public void setCodigoImporteUnitarioConImpuesto(String codigoImporteUnitarioConImpuesto) {
        this.codigoImporteUnitarioConImpuesto = codigoImporteUnitarioConImpuesto;
    }

    public String getUnidadMedidaComercial() {
        return unidadMedidaComercial;
    }

    public void setUnidadMedidaComercial(String unidadMedidaComercial) {
        this.unidadMedidaComercial = unidadMedidaComercial;
    }

    public String getCodigoImporteReferencial() {
        return codigoImporteReferencial;
    }

    public void setCodigoImporteReferencial(String codigoImporteReferencial) {
        this.codigoImporteReferencial = codigoImporteReferencial;
    }

    public BigDecimal getImporteReferencial() {
        return importeReferencial;
    }

    public void setImporteReferencial(BigDecimal importeReferencial) {
        this.importeReferencial = importeReferencial;
    }

	public List<TypePricingReference> getListTypePricingReference() {
		return listTypePricingReference;
	}

	public void setListTypePricingReference(List<TypePricingReference> listTypePricingReference) {
		this.listTypePricingReference = listTypePricingReference;
	}

	public List<TypeTaxSubtotal> getListTypeTaxSubtotal() {
		return listTypeTaxSubtotal;
	}

	public void setListTypeTaxSubtotal(List<TypeTaxSubtotal> listTypeTaxSubtotal) {
		this.listTypeTaxSubtotal = listTypeTaxSubtotal;
	}

	public String getLineID() {
		return lineID;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public List<TypeDelivery> getDelivery() {
		return delivery;
	}

	public void setDelivery(List<TypeDelivery> delivery) {
		this.delivery = delivery;
	}

	public TypeItem getItem() {
		return item;
	}

	public void setItem(TypeItem item) {
		this.item = item;
	}

	public TypeParty getOriginatorParty() {
		return originatorParty;
	}

	public void setOriginatorParty(TypeParty originatorParty) {
		this.originatorParty = originatorParty;
	}

}

