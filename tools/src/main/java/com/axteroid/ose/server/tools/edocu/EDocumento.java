package com.axteroid.ose.server.tools.edocu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.ubltype.TypeAdditionalInformation;
import com.axteroid.ose.server.tools.ubltype.TypeAllowanceCharge;
import com.axteroid.ose.server.tools.ubltype.TypeBillingReference;
import com.axteroid.ose.server.tools.ubltype.TypeCustomerParty;
import com.axteroid.ose.server.tools.ubltype.TypeDelivery;
import com.axteroid.ose.server.tools.ubltype.TypeDocumentReference;
import com.axteroid.ose.server.tools.ubltype.TypeMonetaryTotal;
import com.axteroid.ose.server.tools.ubltype.TypePaymentTerms;
import com.axteroid.ose.server.tools.ubltype.TypeResponse;
import com.axteroid.ose.server.tools.ubltype.TypeSignature;
import com.axteroid.ose.server.tools.ubltype.TypeSupplierParty;
import com.axteroid.ose.server.tools.ubltype.TypeTaxTotal;
import com.axteroid.ose.server.tools.util.NumberUtil;
import com.axteroid.ose.server.tools.util.StringUtil;

public class EDocumento implements IDocumento, IEDocumento {
	
    private Integer inHabilitado = 1;
    private String correoEmisor;
    private String version = Constantes.SUNAT_UBL_20;
    private String versionUBL = Constantes.SUNAT_CUSTOMIZA_10;
    private String correoAdquiriente;
    private String codigoRazonExoneracion;
    private String tipoSistemaImpuestoISC;
    private BigDecimal importeTotalSinImpuesto;
    private String numeroDocumentoEmisor;
    private String tipoDocumentoEmisor;
    private String tipoDocumento;
    private String razonSocialEmisor;
    private String nombreComercialEmisor;
    private String serieNumero;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String ubigeoEmisor;
    private String direccionEmisor;
    private String urbanizacion;
    private String provinciaEmisor;
    private String departamentoEmisor;
    private String distritoEmisor;
    private String paisEmisor;
    private String numeroDocumentoAdquiriente;
    private String tipoDocumentoAdquiriente;
    private String razonSocialAdquiriente;
    private String lugarDestino;
    private String ubigeoAdquiriente;
    private String direccionAdquiriente;
    private String urbanizacionAdquiriente;
    private String provinciaAdquiriente;
    private String departamentoAdquiriente;
    private String distritoAdquiriente;
    private String paisAdquiriente;
    private String tipoMoneda;
    
    private String tipoTotalValorVentaNetoOpGravadas;
    private BigDecimal totalValorVentaNetoOpGravadas;
    private String tipoTotalValorVentaNetoOpNoGravada;
    private String tipoOperacionFactura;
    private BigDecimal totalDocumentoAnticipo;
    private BigDecimal totalValorVentaNetoOpNoGravada;
    private String tipoTotalValorVentaNetoOpExoneradas;
    private String coTipoEmision;
    private BigDecimal totalValorVentaNetoOpExoneradas;
    private String tipoTotalValorVentaNetoOpGratutitas;
    private BigDecimal totalValorVentaNetoOpGratuitas;
    
    private BigDecimal descuentosGlobales;
    private BigDecimal baseImponiblePercepcion;
       
    private BigDecimal subTotal;   
    private BigDecimal totalFondoInclusionSocial;
    private BigDecimal totalOtrosCargos;
    private BigDecimal totalDescuentos;
    private BigDecimal totalVenta;
    
    private String textoNombreMatriculaEmbarcacion; // 3002
    private String textoTipoCantidadEspecieVendida; /// 300
    private String serieNumeroAfectado;    // ok

    private String codigoSerieNumeroAfectado;    // ok
    private String motivoDocumento;
    private String tipoDocumentoReferenciaPrincipal;
    private String numeroDocumentoReferenciaPrincipal;
    private String tipoDocumentoReferenciaCorregido;
    private String numeroDocumentoReferenciaCorregido;
    private String codigoTributoIgv;
    private String nombreTributoIgv;
    private String codigoInternacionalTributoIgv;
    private String codigoTributoIsc;
    private String nombreTributoIsc;
    private String codigoInternacionalTributoIsc;
    private String codigoOtrosTributos;
    private String nombreOtrosTributos;
    private String codigoInternacionalOtrosTributos;
    private String ubigeoDireccionPtoPartida;
    private String direccionCompletaPtoPartida;
    private String urbanizacionPtoPartida;
    private String provinciaPtoPartida;
    private String departamentoPtoPartida;
    private String distritoPtoPartida;
    private String paisPtoPartida;
    private String ubigeoDireccionPtoLlegada;
    private String direccionCompletaPtoLlegada;
    private String urbanizacionPtoLlegada;
    private String provinciaPtoLlegada;
    private String departamentoPtoLlegada;
    private String distritoPtoLlegada;
    private String paisPtoLlegada;
    private String marcaVehiculo;
    private String placaVehiculo;
    private String numeroConstanciaVehiculo;
    private String numeroLicenciaConducir;
    private String numeroRucTransportista;
    private String numeroRucTransportistaCuenta;
    private String razonSocialTransportista;
    private String ubigeoLugarEntrega;
    private String direccionLugarEntrega;
    private String urbanizacionLugarEntrega;
    private String provinciaLugarEntrega;
    private String departamentoLugarEntrega;
    private String distritoLugarEntrega;
    private String paisLugarEntrega;
    private String modalidadTransporte;
    private BigDecimal totalPesoBruto;
    private String unidadMedidaPesoBruto;
    private String placaVehiculoRenta;
    private String matriculaEmbarcacion;
    private String nombreEmbarcacion;
    private String descripcionEspecieVendida;
    private String lugarDescarga;
    private Date fechaDescarga;
    private BigDecimal montoReferencial;
    private BigDecimal montoReferencialPreliminar;
    private String factorRetornoViaje;
    private String puntoOrigenViaje;
    private String puntoDestinoViaje;
    private BigDecimal cargaEfectivaVehiculo;
    private String unidadMedidaCargaEfectiva;
    private BigDecimal montoReferencialVehiculo;
    private String configuracionVehicular;
    private BigDecimal cargaUtilTnVehiculo;
    private String unidadMedidaCargaUtilTn;
    private BigDecimal montoReferencialTM;
    private String ordenCompra;
    
    private List<EDocumentoItem> items = new ArrayList<EDocumentoItem>();
    private List<EDocumentoReferencia> referencias = new ArrayList<EDocumentoReferencia>();
    private List<EDocumentoAnticipo> anticipos = new ArrayList<EDocumentoAnticipo>();

    // OSE  
    private String tipoFondoInclusionSocialEnergetico;
    //@Min(value = 0, message = "7157")
    //@ValidateFormatNumber(presicion = 15,scale = 2,message = "7214")
    private BigDecimal totalFondoInclusionSocialEnergetico;    
    
    protected List<TypeBillingReference> billingReference;
    private List<TypeTaxTotal> listTypeTaxTotal = new ArrayList<TypeTaxTotal>();      
    private List<TypeResponse> listTypeResponse = new ArrayList<TypeResponse>();
    private List<TypeAdditionalInformation> listTypeAdditionalInformation = new ArrayList<TypeAdditionalInformation>();            
    private List<TypeDocumentReference> listTypeDocumentReference = new ArrayList<TypeDocumentReference>();    
    private List<TypeDocumentReference> listTypeAdditionalDocumentReference = new ArrayList<TypeDocumentReference>();    
    private List<TypeDelivery> delivery;
    private List<TypeResponse> discrepancyResponse;
    private List<TypeAllowanceCharge> listTypeAllowanceCharge;
    private List<TypePaymentTerms> listTypePaymentTerms;
    private TypeSignature typeSignature = new TypeSignature();
    private String hashCode;
    private String numIdCd;
    private String issueTime;
    private TypeMonetaryTotal typeLegalMonetaryTotal;
    private TypeMonetaryTotal typeRequestedMonetaryTotal;
    private TypeCustomerParty accountingCustomerParty;
    private TypeSupplierParty accountingSupplierParty;    
    private Date dategetNotBefore;
    private Date dateNotAfter;
    
    private BigDecimal totalOPGravada;
    private BigDecimal totalIgv;
    private BigDecimal totalIsc;
    private BigDecimal totalOtrosTributos;
    private BigDecimal totalOPInafecta;
    private BigDecimal totalOPExonerada;
    private BigDecimal totalOPExportacion;  
    private BigDecimal descuentosGlobalesAfectos;
    private BigDecimal descuentosGlobalesNoAfectos;
    private BigDecimal cargosGlobalesAfectos;
    private BigDecimal cargosGlobalesNoAfectos;

//  private String rucRazonSocialEmisor;
//  private String documentoAfectado;
//  private String documentoReferenciaPrincipal;
//  private String documentoReferenciaCorregido;
    
//  protected BigDecimal monto1;
//  protected BigDecimal monto2;
//  protected BigDecimal monto3;
//  protected BigDecimal monto4;
//  protected BigDecimal monto5;    
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal totalVentaConPercepcion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal totalPercepcion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal porcentajePercepcion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal totalRetencion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal porcentajeRetencion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal totalDetraccion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal valorReferencialDetraccion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal porcentajeDetraccion;
//    private String descripcionDetraccion;
//    @ValidateFormatNumber(presicion = 12,scale = 2,message = "7214")
//    private BigDecimal totalBonificacion;

//    private String rutaAdjunto_1;
//    private String rutaAdjunto_2;
//    private String rutaAdjunto_3;
//    private String rutaAdjunto_4;
//    private String rutaAdjunto_5;
//    @XmlTransient
//    private byte[] adjunto_1;
//    @XmlTransient
//    private byte[] adjunto_2;
//    @XmlTransient
//    private byte[] adjunto_3;
//    @XmlTransient
//    private byte[] adjunto_4;
//    @XmlTransient
//    private byte[] adjunto_5;
//    private String nombreAdjunto_1;
//    private String nombreAdjunto_2;
//    private String nombreAdjunto_3;
//    private String nombreAdjunto_4;
//    private String nombreAdjunto_5;

    public Integer getInHabilitado() {
        return inHabilitado;
    }

    public void setInHabilitado(Integer inHabilitado) {
        this.inHabilitado = inHabilitado;
    }
    
    public String getCorreoEmisor() {
        return correoEmisor;
    }

    public void setCorreoEmisor(String correoEmisor) {
        this.correoEmisor = correoEmisor;
    }

    public String getCorreoAdquiriente() {
        return correoAdquiriente;
    }

    public void setCorreoAdquiriente(String correoAdquiriente) {
        this.correoAdquiriente = correoAdquiriente;
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

    public String getNumeroDocumentoEmisor() {
        return numeroDocumentoEmisor;
    }

    public void setNumeroDocumentoEmisor(String numeroDocumentoEmisor) {
        this.numeroDocumentoEmisor = numeroDocumentoEmisor;
    }

    public String getTipoDocumentoEmisor() {
        return tipoDocumentoEmisor;
    }

    public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
        this.tipoDocumentoEmisor = tipoDocumentoEmisor;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getSerieDocumento() {
        return getIdDocumento().substring(0, getIdDocumento().lastIndexOf('-'));
    }

    public String getNumeroDocumento() {
        return getIdDocumento().substring(getIdDocumento().lastIndexOf('-') + 1, getIdDocumento().length());
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRazonSocialEmisor() {
        return razonSocialEmisor;
    }

    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    public String getNombreComercialEmisor() {
        return nombreComercialEmisor;
    }

    public void setNombreComercialEmisor(String nombreComercialEmisor) {
        this.nombreComercialEmisor = nombreComercialEmisor;
    }

    public String getSerieNumero() {
        return serieNumero;
    }

    public void setSerieNumero(String serieNumero) {
        this.serieNumero = serieNumero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUbigeoEmisor() {
        return ubigeoEmisor;
    }

    public void setUbigeoEmisor(String ubigeoEmisor) {
        this.ubigeoEmisor = ubigeoEmisor;
    }

    public String getDireccionEmisor() {
        return direccionEmisor;
    }

    public void setDireccionEmisor(String direccionEmisor) {
        this.direccionEmisor = direccionEmisor;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public String getProvinciaEmisor() {
        return provinciaEmisor;
    }

    public void setProvinciaEmisor(String provinciaEmisor) {
        this.provinciaEmisor = provinciaEmisor;
    }

    public String getDepartamentoEmisor() {
        return departamentoEmisor;
    }

    public void setDepartamentoEmisor(String departamentoEmisor) {
        this.departamentoEmisor = departamentoEmisor;
    }

    public String getDistritoEmisor() {
        return distritoEmisor;
    }

    public void setDistritoEmisor(String distritoEmisor) {
        this.distritoEmisor = distritoEmisor;
    }

    public String getPaisEmisor() {
        return paisEmisor;
    }

    public void setPaisEmisor(String paisEmisor) {
        this.paisEmisor = paisEmisor;
    }

    public String getNumeroDocumentoAdquiriente() {
        return numeroDocumentoAdquiriente;
    }

    public void setNumeroDocumentoAdquiriente(String numeroDocumentoAdquiriente) {
        this.numeroDocumentoAdquiriente = numeroDocumentoAdquiriente;
    }

    public String getTipoDocumentoAdquiriente() {
        return tipoDocumentoAdquiriente;
    }

    public void setTipoDocumentoAdquiriente(String tipoDocumentoAdquiriente) {
        this.tipoDocumentoAdquiriente = tipoDocumentoAdquiriente;
    }

    public String getRazonSocialAdquiriente() {
        return razonSocialAdquiriente;
    }

    public void setRazonSocialAdquiriente(String razonSocialAdquiriente) {
        this.razonSocialAdquiriente = razonSocialAdquiriente;
    }

    public String getLugarDestino() {
        return lugarDestino;
    }

    public boolean isDireccionAdquirienteValido(){

        return  StringUtils.isNotBlank(ubigeoAdquiriente) ||
                StringUtils.isNotBlank(direccionAdquiriente) ||
                StringUtils.isNotBlank(urbanizacionAdquiriente) ||
                StringUtils.isNotBlank(distritoAdquiriente) ||
                StringUtils.isNotBlank(provinciaAdquiriente) ||
                StringUtils.isNotBlank(departamentoAdquiriente) ||
                StringUtils.isNotBlank(paisAdquiriente) ;
    }

    public void setLugarDestino(String lugarDestino) {
        this.lugarDestino = lugarDestino;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public String getTipoTotalValorVentaNetoOpGravadas() {
        return tipoTotalValorVentaNetoOpGravadas;
    }

    public void setTipoTotalValorVentaNetoOpGravadas(String tipoTotalValorVentaNetoOpGravadas) {
        this.tipoTotalValorVentaNetoOpGravadas = tipoTotalValorVentaNetoOpGravadas;
    }

    public BigDecimal getTotalValorVentaNetoOpGravadas() {
        return totalValorVentaNetoOpGravadas;
    }

    public void setTotalValorVentaNetoOpGravadas(BigDecimal totalValorVentaNetoOpGravadas) {
        this.totalValorVentaNetoOpGravadas = totalValorVentaNetoOpGravadas;
    }

    public String getTipoTotalValorVentaNetoOpNoGravada() {
        return tipoTotalValorVentaNetoOpNoGravada;
    }

    public void setTipoTotalValorVentaNetoOpNoGravada(String tipoTotalValorVentaNetoOpNoGravada) {
        this.tipoTotalValorVentaNetoOpNoGravada = tipoTotalValorVentaNetoOpNoGravada;
    }

    public BigDecimal getTotalValorVentaNetoOpNoGravada() {
        return totalValorVentaNetoOpNoGravada;
    }

    public void setTotalValorVentaNetoOpNoGravada(BigDecimal totalValorVentaNetoOpNoGravada) {
        this.totalValorVentaNetoOpNoGravada = totalValorVentaNetoOpNoGravada;
    }

    public String getTipoTotalValorVentaNetoOpExoneradas() {
        return tipoTotalValorVentaNetoOpExoneradas;
    }

    public void setTipoTotalValorVentaNetoOpExoneradas(String tipoTotalValorVentaNetoOpExoneradas) {
        this.tipoTotalValorVentaNetoOpExoneradas = tipoTotalValorVentaNetoOpExoneradas;
    }

    public BigDecimal getTotalValorVentaNetoOpExoneradas() {
        return totalValorVentaNetoOpExoneradas;
    }

    public void setTotalValorVentaNetoOpExoneradas(BigDecimal totalValorVentaNetoOpExoneradas) {
        this.totalValorVentaNetoOpExoneradas = totalValorVentaNetoOpExoneradas;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotalOtrosCargos() {
        return totalOtrosCargos;
    }

    public void setTotalOtrosCargos(BigDecimal totalOtrosCargos) {
        this.totalOtrosCargos = totalOtrosCargos;
    }

    public BigDecimal getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getSerieNumeroAfectado() {
        return serieNumeroAfectado;
    }

    public void setSerieNumeroAfectado(String serieNumeroAfectado) {
        this.serieNumeroAfectado = serieNumeroAfectado;
    }

    public String getCodigoSerieNumeroAfectado() {
        return codigoSerieNumeroAfectado;
    }

    public void setCodigoSerieNumeroAfectado(String codigoSerieNumeroAfectado) {
        this.codigoSerieNumeroAfectado = codigoSerieNumeroAfectado;
    }

    public String getMotivoDocumento() {
        return motivoDocumento;
    }

    public void setMotivoDocumento(String motivoDocumento) {
        this.motivoDocumento = motivoDocumento;
    }

    public String getTipoDocumentoReferenciaPrincipal() {
        return tipoDocumentoReferenciaPrincipal;
    }

    public void setTipoDocumentoReferenciaPrincipal(String tipoDocumentoReferenciaPrincipal) {
        this.tipoDocumentoReferenciaPrincipal = tipoDocumentoReferenciaPrincipal;
    }

    public String getTipoDocumentoReferenciaCorregido() {
        return tipoDocumentoReferenciaCorregido;
    }

    public void setTipoDocumentoReferenciaCorregido(String tipoDocumentoReferenciaCorregido) {
        this.tipoDocumentoReferenciaCorregido = tipoDocumentoReferenciaCorregido;
    }

    public String getNumeroDocumentoReferenciaPrincipal() {
        return numeroDocumentoReferenciaPrincipal;
    }

    public void setNumeroDocumentoReferenciaPrincipal(String numeroDocumentoReferenciaPrincipal) {
        this.numeroDocumentoReferenciaPrincipal = numeroDocumentoReferenciaPrincipal;
    }

    public String getNumeroDocumentoReferenciaCorregido() {
        return numeroDocumentoReferenciaCorregido;
    }

    public void setNumeroDocumentoReferenciaCorregido(String numeroDocumentoReferenciaCorregido) {
        this.numeroDocumentoReferenciaCorregido = numeroDocumentoReferenciaCorregido;
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
    
    public List<EDocumentoReferencia> getReferencias() {
        return referencias;
    }

    public void setReferencias(List<EDocumentoReferencia> referencias) {
        this.referencias = referencias;
    }
    
    public List<EDocumentoAnticipo> getAnticipos() {
        return anticipos;
    }

    public void setAnticipos(List<EDocumentoAnticipo> anticipos) {
        this.anticipos = anticipos;
    }

    public List<EDocumentoItem> getItems() {
        return items;
    }

    public void setItems(List<EDocumentoItem> items) {
        this.items = items;
    }

    public String getIdDocumento() {
        return getSerieNumero();
    }

    public String getRucRazonSocialEmisor() {
        return numeroDocumentoEmisor + "|" + razonSocialEmisor;
    }

    public String getDocumentoAfectado() {
        if (StringUtils.isNotBlank(serieNumeroAfectado)
                && StringUtils.isNotBlank(codigoSerieNumeroAfectado)
                && StringUtils.isNotBlank(motivoDocumento)) {
            return serieNumeroAfectado + "|" + codigoSerieNumeroAfectado + "|" + motivoDocumento;
        }
        return null;
    }

    public String getDocumentoAfectadoV2() {
        return StringUtil.nvl(serieNumeroAfectado, " ") + "|" + StringUtil.nvl(codigoSerieNumeroAfectado, " ") + "|" + StringUtil.nvl(motivoDocumento, " ");
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionUBL() {
        return versionUBL;
    }

    public void setVersionUBL(String versionUBL) {
        this.versionUBL = versionUBL;
    }


    public String getEstado() {
        return null;  //Todo implementar
    }

    public Date getFechaDocumento() {
        return getFechaEmision();
    }
    
    public BigDecimal getImporteTotalSinImpuesto() {
        return importeTotalSinImpuesto;
    }

    public void setImporteTotalSinImpuesto(BigDecimal importeTotalSinImpuesto) {
        this.importeTotalSinImpuesto = importeTotalSinImpuesto;
    }

    public String buildTotalValorVentaNetoOpGravadas() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaNetoOpGravadas == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaNetoOpGravadas);
    }

    public String buildTotalValorVentaNetoOpNoGravadas() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaNetoOpNoGravada == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaNetoOpNoGravada);
    }

    public String buildTotalValorVentaNetoOpExoneradas() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaNetoOpExoneradas == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaNetoOpExoneradas);
    }

    public String buildTotalValorVentaNetoOpGratuitas() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalValorVentaNetoOpGratuitas == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalValorVentaNetoOpGratuitas);
    }

    public String buildDescuentoGlobal() {
        if (StringUtils.isBlank(tipoMoneda) ||
                descuentosGlobales == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(descuentosGlobales);
    }

    public String buildTotalDocumentoAnticipo() {
        if (StringUtils.isBlank(tipoMoneda) || totalDocumentoAnticipo==null ) return null;
        return tipoMoneda  + "|" +NumberUtil.toFormat(totalDocumentoAnticipo);
    }

    public String buildTotalDescuentos() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalDescuentos == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalDescuentos);
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

    public String buildTotalFondoInclusionSocial() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalFondoInclusionSocial == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalFondoInclusionSocial);
    }

    public String buildTotalIgv() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalIgv == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalIgv);
    }

    public String buildDireccionPtoPartida() {
        if (StringUtils.isBlank(ubigeoDireccionPtoPartida) ||
                StringUtils.isBlank(direccionCompletaPtoPartida) ||
                StringUtils.isBlank(urbanizacionPtoPartida) ||
                StringUtils.isBlank(provinciaPtoPartida) ||
                StringUtils.isBlank(departamentoPtoPartida) ||
                StringUtils.isBlank(distritoPtoPartida) ||
                StringUtils.isBlank(paisPtoPartida)) {
            return null;
        }

        return StringUtil.blank(ubigeoDireccionPtoPartida) + "|" +
                StringUtil.blank(direccionCompletaPtoPartida) + "|" +
                StringUtil.blank(urbanizacionPtoPartida) + "|" +
                StringUtil.blank(provinciaPtoPartida) + "|" +
                StringUtil.blank(departamentoPtoPartida) + "|" +
                StringUtil.blank(distritoPtoPartida) + "|" +
                StringUtil.blank(paisPtoPartida)
                ;
    }

    public String buildDireccionPtoLlegada() {
        if (StringUtils.isBlank(ubigeoDireccionPtoLlegada) ||
                StringUtils.isBlank(direccionCompletaPtoLlegada) ||
                StringUtils.isBlank(urbanizacionPtoLlegada) ||
                StringUtils.isBlank(provinciaPtoLlegada) ||
                StringUtils.isBlank(departamentoPtoLlegada) ||
                StringUtils.isBlank(distritoPtoLlegada) ||
                StringUtils.isBlank(paisPtoLlegada)) {
            return null;
        }

        return StringUtil.blank(ubigeoDireccionPtoLlegada) + "|" +
                StringUtil.blank(direccionCompletaPtoLlegada) + "|" +
                StringUtil.blank(urbanizacionPtoLlegada) + "|" +
                StringUtil.blank(provinciaPtoLlegada) + "|" +
                StringUtil.blank(departamentoPtoLlegada) + "|" +
                StringUtil.blank(distritoPtoLlegada) + "|" +
                StringUtil.blank(paisPtoLlegada)
                ;
    }

    public String buildMarcaPlacaVehiculo() {
        if (StringUtils.isBlank(placaVehiculo) ||
                StringUtils.isBlank(numeroConstanciaVehiculo) ||
                StringUtils.isBlank(marcaVehiculo)) {
            return null;
        }

        return StringUtil.blank(placaVehiculo) + "|" +
                StringUtil.blank(numeroConstanciaVehiculo) + "|" +
                StringUtil.blank(marcaVehiculo)
                ;
    }

    public String buildTransportista() {
        if (StringUtils.isBlank(numeroRucTransportista) ||
                StringUtils.isBlank(numeroRucTransportistaCuenta) ||
                StringUtils.isBlank(razonSocialTransportista)) {
            return null;
        }

        return StringUtil.blank(numeroRucTransportista) + "|" +
                StringUtil.blank(numeroRucTransportistaCuenta) + "|" +
                StringUtil.blank(razonSocialTransportista)
                ;
    }

    public String buildTotalOtrosCargos() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalOtrosCargos == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalOtrosCargos);
    }





    public String buildTotalVenta() {
        if (StringUtils.isBlank(tipoMoneda) ||
                totalVenta == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(totalVenta);
    }


    public String buildSubTotal() {
        if (StringUtils.isBlank(tipoMoneda) ||
                subTotal == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(subTotal);
    }

    public void populateItem() {
        if (StringUtils.isBlank(getTipoDocumento())) {
            setTipoDocumento("SN");
        }
        if (StringUtils.isBlank(getIdDocumento())) {
            setSerieNumero("SN" + new Date().getTime());
        }
        for (EDocumentoItem documentoItem : items) {
            documentoItem.setTipoMoneda(tipoMoneda);
        }
    }
    
    //HNA - 28OCt2015: RS185
    public String buildDireccionLugarEntrega() {
        if (StringUtils.isBlank(ubigeoLugarEntrega) ||
                StringUtils.isBlank(direccionLugarEntrega) ||
                StringUtils.isBlank(urbanizacionLugarEntrega) ||
                StringUtils.isBlank(provinciaLugarEntrega) ||
                StringUtils.isBlank(departamentoLugarEntrega) ||
                StringUtils.isBlank(distritoLugarEntrega) ||
                StringUtils.isBlank(paisLugarEntrega)) {
            return null;
        }

        return StringUtil.blank(ubigeoLugarEntrega) + "|" +
                StringUtil.blank(direccionLugarEntrega) + "|" +
                StringUtil.blank(urbanizacionLugarEntrega) + "|" +
                StringUtil.blank(provinciaLugarEntrega) + "|" +
                StringUtil.blank(departamentoLugarEntrega) + "|" +
                StringUtil.blank(distritoLugarEntrega) + "|" +
                StringUtil.blank(paisLugarEntrega)
                ;
    }
    
    public String buildTotalPesoBruto() {
        if (StringUtils.isBlank(unidadMedidaPesoBruto) ||
                totalPesoBruto == null) return null;
        return unidadMedidaPesoBruto + "|" + NumberUtil.toFormat(totalPesoBruto);
    }
    
    public String buildMaritimeTransport() {
    	if(StringUtils.isBlank(matriculaEmbarcacion) || 
    			StringUtils.isBlank(nombreEmbarcacion)) return null;
    	return matriculaEmbarcacion + "|" + nombreEmbarcacion;
    }
    
    public String buildMontoReferencial(){
            if (StringUtils.isBlank(tipoMoneda) ||
            		montoReferencial == null) return null;
            return tipoMoneda + "|" + NumberUtil.toFormat(montoReferencial);
    }
    
    public String buildMontoReferencialPreliminar(){
        if (StringUtils.isBlank(tipoMoneda) ||
        		montoReferencialPreliminar == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(montoReferencialPreliminar);
    }
    
    public String buildCargaEfectivaVehiculo() {
        if (StringUtils.isBlank(unidadMedidaCargaEfectiva) ||
        		cargaEfectivaVehiculo == null) return null;
        return unidadMedidaCargaEfectiva + "|" + NumberUtil.toFormat(cargaEfectivaVehiculo);
    }
    
    public String buildMontoReferencialVehiculo(){
        if (StringUtils.isBlank(tipoMoneda) ||
        		montoReferencialVehiculo == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(montoReferencialVehiculo);
    }
    
    public String buildCargaUtilTnVehiculo() {
        if (StringUtils.isBlank(unidadMedidaCargaUtilTn) ||
        		cargaUtilTnVehiculo == null) return null;
        return unidadMedidaCargaUtilTn + "|" + NumberUtil.toFormat(cargaUtilTnVehiculo);
    }
    
    public String buildMontoReferencialTM(){
        if (StringUtils.isBlank(tipoMoneda) ||
        		montoReferencialTM == null) return null;
        return tipoMoneda + "|" + NumberUtil.toFormat(montoReferencialTM);
    }

//
//    public String getRutaAdjunto_1() {
//        return rutaAdjunto_1;
//    }
//
//    public void setRutaAdjunto_1(String rutaAdjunto_1) {
//        this.rutaAdjunto_1 = rutaAdjunto_1;
//    }
//
//    public String getRutaAdjunto_2() {
//        return rutaAdjunto_2;
//    }
//
//    public void setRutaAdjunto_2(String rutaAdjunto_2) {
//        this.rutaAdjunto_2 = rutaAdjunto_2;
//    }
//
//    public String getRutaAdjunto_3() {
//        return rutaAdjunto_3;
//    }
//
//    public void setRutaAdjunto_3(String rutaAdjunto_3) {
//        this.rutaAdjunto_3 = rutaAdjunto_3;
//    }
//
//    public String getRutaAdjunto_4() {
//        return rutaAdjunto_4;
//    }
//
//    public void setRutaAdjunto_4(String rutaAdjunto_4) {
//        this.rutaAdjunto_4 = rutaAdjunto_4;
//    }
//
//    public String getRutaAdjunto_5() {
//        return rutaAdjunto_5;
//    }
//
//    public void setRutaAdjunto_5(String rutaAdjunto_5) {
//        this.rutaAdjunto_5 = rutaAdjunto_5;
//    }
//
//    public BigDecimal getTotalVentaConPercepcion() {
//        return totalVentaConPercepcion;
//    }
//
//    public void setTotalVentaConPercepcion(BigDecimal totalVentaConPercepcion) {
//        this.totalVentaConPercepcion = totalVentaConPercepcion;
//    }
//    public BigDecimal getTotalPercepcion() {
//        return totalPercepcion;
//    }
//
//    public void setTotalPercepcion(BigDecimal totalPercepcion) {
//        this.totalPercepcion = totalPercepcion;
//    }
//    
//    public String buildTotalPercepcion() {
//        if (StringUtils.isBlank(tipoMoneda) ||
//                totalPercepcion == null
//                ) return null;
//        return tipoMoneda + "|" +
//                NumberUtil.toFormat(totalPercepcion) + "|" +
//                NumberUtil.nvl(NumberUtil.toFormat(totalVentaConPercepcion), "*") + "|" +
//                NumberUtil.nvl(NumberUtil.toFormat(porcentajePercepcion), "*") + "|" +
//                NumberUtil.nvl(NumberUtil.toFormat(baseImponiblePercepcion), "*");
//    }
//
//    public String buildTotalRetencion() {
//        if (StringUtils.isBlank(tipoMoneda) ||
//                totalRetencion == null ||
//                porcentajeRetencion == null) return null;
//        return tipoMoneda + "|" + NumberUtil.toFormat(totalRetencion) + "|" + NumberUtil.toFormat(porcentajeRetencion);
//    }
//
//    public String buildTotalDetraccion() {
//        if (StringUtils.isBlank(tipoMoneda) || StringUtils.isBlank(descripcionDetraccion) ||
//                totalDetraccion == null ||
//                valorReferencialDetraccion == null ||
//                porcentajeDetraccion == null) return null;
//        return tipoMoneda + "|" + NumberUtil.toFormat(totalDetraccion) + "|" + NumberUtil.toFormat(porcentajeDetraccion) + "|" + descripcionDetraccion + "|" + NumberUtil.toFormat(valorReferencialDetraccion);
//    }
//
//    public String buildTotalBonificacion() {
//        if (StringUtils.isBlank(tipoMoneda) ||
//                totalBonificacion == null) return null;
//        return tipoMoneda + "|" + NumberUtil.toFormat(totalBonificacion);
//    }
//    
//    public BigDecimal getPorcentajePercepcion() {
//        return porcentajePercepcion;
//    }
//
//    public void setPorcentajePercepcion(BigDecimal porcentajePercepcion) {
//        this.porcentajePercepcion = porcentajePercepcion;
//    }
//
//    public BigDecimal getTotalRetencion() {
//        return totalRetencion;
//    }
//
//    public void setTotalRetencion(BigDecimal totalRetencion) {
//        this.totalRetencion = totalRetencion;
//    }
//
//    public BigDecimal getPorcentajeRetencion() {
//        return porcentajeRetencion;
//    }
//
//    public void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
//        this.porcentajeRetencion = porcentajeRetencion;
//    }
//
//    public BigDecimal getTotalDetraccion() {
//        return totalDetraccion;
//    }
//
//    public void setTotalDetraccion(BigDecimal totalDetraccion) {
//        this.totalDetraccion = totalDetraccion;
//    }
//
//    public BigDecimal getPorcentajeDetraccion() {
//        return porcentajeDetraccion;
//    }
//
//    public void setPorcentajeDetraccion(BigDecimal porcentajeDetraccion) {
//        this.porcentajeDetraccion = porcentajeDetraccion;
//    }
//
//    public String getDescripcionDetraccion() {
//        return descripcionDetraccion;
//    }
//
//    public void setDescripcionDetraccion(String descripcionDetraccion) {
//        this.descripcionDetraccion = descripcionDetraccion;
//    }
//
//    public BigDecimal getTotalBonificacion() {
//        return totalBonificacion;
//    }
//
//    public void setTotalBonificacion(BigDecimal totalBonificacion) {
//        this.totalBonificacion = totalBonificacion;
//    }
//
//
//    public BigDecimal getValorReferencialDetraccion() {
//        return valorReferencialDetraccion;
//    }
//
//    public void setValorReferencialDetraccion(BigDecimal valorReferencialDetraccion) {
//        this.valorReferencialDetraccion = valorReferencialDetraccion;
//    }
//
//    public BigDecimal getMonto1() {
//        return monto1;
//    }
//
//    public void setMonto1(BigDecimal monto1) {
//        this.monto1 = monto1;
//    }
//
//    public BigDecimal getMonto2() {
//        return monto2;
//    }
//
//    public void setMonto2(BigDecimal monto2) {
//        this.monto2 = monto2;
//    }
//
//    public BigDecimal getMonto3() {
//        return monto3;
//    }
//
//    public void setMonto3(BigDecimal monto3) {
//        this.monto3 = monto3;
//    }
//
//    public BigDecimal getMonto4() {
//        return monto4;
//    }
//
//    public void setMonto4(BigDecimal monto4) {
//        this.monto4 = monto4;
//    }
//
//    public BigDecimal getMonto5() {
//        return monto5;
//    }
//
//    public void setMonto5(BigDecimal monto5) {
//        this.monto5 = monto5;
//    }
//    
//    private boolean referenciaValida(String tipoReferencia, String numeroDocumentoReferencia) {
//        boolean existeTipo = StringUtils.isNotBlank(tipoReferencia);
//        boolean existeNumero = StringUtils.isNotBlank(numeroDocumentoReferencia);
//        if (existeTipo && existeNumero) {
//            return true;
//        } else if (existeTipo || existeNumero) {
//            return false;
//        }
//        return true;
//    }
//
//    public void setDocumentoAfectado(String documentoAfectado) {
//        this.documentoAfectado = documentoAfectado;
//    }
//
//    public void setDocumentoReferenciaCorregido(String documentoReferenciaCorregido) {
//        this.documentoReferenciaCorregido = documentoReferenciaCorregido;
//    }
//    
//    public void setRucRazonSocialEmisor(String rucRazonSocialEmisor) {
//        this.rucRazonSocialEmisor = rucRazonSocialEmisor;
//    }
//    
//    public void setDocumentoReferenciaPrincipal(String documentoReferenciaPrincipal) {
//        this.documentoReferenciaPrincipal = documentoReferenciaPrincipal;
//    }
//    
//    public List<File> obtenerListaAdjuntosEnLan(String rutaParcial) {
//        List<File> result = new ArrayList<File>();
//        if (StringUtils.isNotBlank(getRutaAdjunto_1())) {
//            result.add(new File(rutaParcial + getRutaAdjunto_1()));
//        }
//        if (StringUtils.isNotBlank(getRutaAdjunto_2())) {
//            result.add(new File(rutaParcial + getRutaAdjunto_2()));
//        }
//        if (StringUtils.isNotBlank(getRutaAdjunto_3())) {
//            result.add(new File(rutaParcial + getRutaAdjunto_3()));
//        }
//        if (StringUtils.isNotBlank(getRutaAdjunto_4())) {
//            result.add(new File(rutaParcial + getRutaAdjunto_4()));
//        }
//        if (StringUtils.isNotBlank(getRutaAdjunto_5())) {
//            result.add(new File(rutaParcial + getRutaAdjunto_5()));
//        }
//        return result;
//    }

//    public byte[] getAdjunto_1() {
//        return adjunto_1;
//    }
//
//    public void setAdjunto_1(byte[] adjunto_1) {
//        this.adjunto_1 = adjunto_1;
//    }
//
//    public byte[] getAdjunto_2() {
//        return adjunto_2;
//    }
//
//    public void setAdjunto_2(byte[] adjunto_2) {
//        this.adjunto_2 = adjunto_2;
//    }
//
//    public byte[] getAdjunto_3() {
//        return adjunto_3;
//    }
//
//    public void setAdjunto_3(byte[] adjunto_3) {
//        this.adjunto_3 = adjunto_3;
//    }
//
//    public byte[] getAdjunto_4() {
//        return adjunto_4;
//    }
//
//    public void setAdjunto_4(byte[] adjunto_4) {
//        this.adjunto_4 = adjunto_4;
//    }
//
//    public byte[] getAdjunto_5() {
//        return adjunto_5;
//    }
//
//    public void setAdjunto_5(byte[] adjunto_5) {
//        this.adjunto_5 = adjunto_5;
//    }
//
//    public String getNombreAdjunto_1() {
//        return nombreAdjunto_1;
//    }
//
//    public void setNombreAdjunto_1(String nombreAdjunto_1) {
//        this.nombreAdjunto_1 = nombreAdjunto_1;
//    }
//
//    public String getNombreAdjunto_2() {
//        return nombreAdjunto_2;
//    }
//
//    public void setNombreAdjunto_2(String nombreAdjunto_2) {
//        this.nombreAdjunto_2 = nombreAdjunto_2;
//    }
//
//    public String getNombreAdjunto_3() {
//        return nombreAdjunto_3;
//    }
//
//    public void setNombreAdjunto_3(String nombreAdjunto_3) {
//        this.nombreAdjunto_3 = nombreAdjunto_3;
//    }
//
//    public String getNombreAdjunto_4() {
//        return nombreAdjunto_4;
//    }
//
//    public void setNombreAdjunto_4(String nombreAdjunto_4) {
//        this.nombreAdjunto_4 = nombreAdjunto_4;
//    }
//
//    public String getNombreAdjunto_5() {
//        return nombreAdjunto_5;
//    }
//
//    public void setNombreAdjunto_5(String nombreAdjunto_5) {
//        this.nombreAdjunto_5 = nombreAdjunto_5;
//    }    
//
//    public List<File> obtenerListaAdjuntosEnServer(EDocumento documento) {
//        List<File> result = new ArrayList<File>();
//
//        try {
//            if (getAdjunto_1() != null) {
//                result.add(FileUtil.writeToFile(getNombreAdjunto_1(), getAdjunto_1()));
//            }
//            if (getAdjunto_2() != null) {
//                result.add(FileUtil.writeToFile(getNombreAdjunto_2(), getAdjunto_2()));
//            }
//            if (getAdjunto_3() != null) {
//                result.add(FileUtil.writeToFile(getNombreAdjunto_3(), getAdjunto_3()));
//            }
//            if (getAdjunto_4() != null) {
//                result.add(FileUtil.writeToFile(getNombreAdjunto_4(), getAdjunto_4()));
//            }
//            if (getAdjunto_5() != null) {
//                result.add(FileUtil.writeToFile(getNombreAdjunto_5(), getAdjunto_5()));
//            }
//        } catch (Exception e) {
//            throw new GeneralException("7182", "No se pudo convertir los adjuntos a archivos");
//
//        }
//        return result;
//    }
//
//    public void validarNombreArchivoAdjunto(EDocumento documento) {
//        List<String> result = new ArrayList<String>();
//
//
//        if (getNombreAdjunto_1() != null && getNombreAdjunto_1().length() <= 20) {
//            result.add(getNombreAdjunto_1());
//        }
//        if (getNombreAdjunto_2() != null && getNombreAdjunto_2().length() <= 20) {
//            result.add(getNombreAdjunto_2());
//        }
//        if (getNombreAdjunto_3() != null && getNombreAdjunto_3().length() <= 20) {
//            result.add(getNombreAdjunto_3());
//        }
//        if (getNombreAdjunto_4() != null && getNombreAdjunto_4().length() <= 20) {
//            result.add(getNombreAdjunto_4());
//        }
//        if (getNombreAdjunto_5() != null && getNombreAdjunto_5().length() <= 20) {
//            result.add(getNombreAdjunto_5());
//        }
//
//        if (result.size() > 0) {
//            //throw new GeneralException("7183", "El nombre de los archivos adjuntos supera el límite permitido de 20 caracteres: " + ListUtil.getRepeatList(new HashSet<String>(result), result));
//        	throw new GeneralException("7183", "El nombre de los archivos adjuntos supera el límite permitido de 20 caracteres: " );
//        }
//
//        return;
//    }


    public boolean isExportacion() {

        List<EDocumentoItem> detalle = getItems();
        boolean result = false;
        for (Iterator<EDocumentoItem> iterator = detalle.iterator(); iterator.hasNext(); ) {
            EDocumentoItem documentoItem = iterator.next();
            if ("40".equals(documentoItem.getCodigoRazonExoneracion())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean isNotaDebitoPorPenalidad() {
        return Constantes.SUNAT_NOTA_CREDITO.equals(getTipoDocumento()) && Constantes.SUNAT_NOTA_DEBITO_PENALIDAD.equals(getCodigoSerieNumeroAfectado());
    }

    public String getTipoTotalValorVentaNetoOpGratutitas() {
        return tipoTotalValorVentaNetoOpGratutitas;
    }

    public void setTipoTotalValorVentaNetoOpGratutitas(String tipoTotalValorVentaNetoOpGratutitas) {
        this.tipoTotalValorVentaNetoOpGratutitas = tipoTotalValorVentaNetoOpGratutitas;
    }

    public BigDecimal getTotalValorVentaNetoOpGratuitas() {
        return totalValorVentaNetoOpGratuitas;
    }

    public void setTotalValorVentaNetoOpGratuitas(BigDecimal totalValorVentaNetoOpGratuitas) {
        this.totalValorVentaNetoOpGratuitas = totalValorVentaNetoOpGratuitas;
    }

    public BigDecimal getDescuentosGlobales() {
        return descuentosGlobales;
    }

    public void setDescuentosGlobales(BigDecimal descuentosGlobales) {
        this.descuentosGlobales = descuentosGlobales;
    }

    public BigDecimal getBaseImponiblePercepcion() {
        return baseImponiblePercepcion;
    }

    public void setBaseImponiblePercepcion(BigDecimal baseImponiblePercepcion) {
        this.baseImponiblePercepcion = baseImponiblePercepcion;
    }

    public String getSignatureValue() {
        return null;
    }



    public String getCoTipoEmision() {
        return coTipoEmision;
    }

    public void setCoTipoEmision(String coTipoEmision) {
        this.coTipoEmision = coTipoEmision;
    }

    public String getUbigeoDireccionPtoPartida() {
        return ubigeoDireccionPtoPartida;
    }

    public void setUbigeoDireccionPtoPartida(String ubigeoDireccionPtoPartida) {
        this.ubigeoDireccionPtoPartida = ubigeoDireccionPtoPartida;
    }

    public String getDireccionCompletaPtoPartida() {
        return direccionCompletaPtoPartida;
    }

    public void setDireccionCompletaPtoPartida(String direccionCompletaPtoPartida) {
        this.direccionCompletaPtoPartida = direccionCompletaPtoPartida;
    }

    public String getUrbanizacionPtoPartida() {
        return urbanizacionPtoPartida;
    }

    public void setUrbanizacionPtoPartida(String urbanizacionPtoPartida) {
        this.urbanizacionPtoPartida = urbanizacionPtoPartida;
    }

    public String getProvinciaPtoPartida() {
        return provinciaPtoPartida;
    }

    public void setProvinciaPtoPartida(String provinciaPtoPartida) {
        this.provinciaPtoPartida = provinciaPtoPartida;
    }

    public String getDepartamentoPtoPartida() {
        return departamentoPtoPartida;
    }

    public void setDepartamentoPtoPartida(String departamentoPtoPartida) {
        this.departamentoPtoPartida = departamentoPtoPartida;
    }

    public String getDistritoPtoPartida() {
        return distritoPtoPartida;
    }

    public void setDistritoPtoPartida(String distritoPtoPartida) {
        this.distritoPtoPartida = distritoPtoPartida;
    }

    public String getPaisPtoPartida() {
        return paisPtoPartida;
    }

    public void setPaisPtoPartida(String paisPtoPartida) {
        this.paisPtoPartida = paisPtoPartida;
    }

    public String getUbigeoDireccionPtoLlegada() {
        return ubigeoDireccionPtoLlegada;
    }

    public void setUbigeoDireccionPtoLlegada(String ubigeoDireccionPtoLlegada) {
        this.ubigeoDireccionPtoLlegada = ubigeoDireccionPtoLlegada;
    }

    public String getDireccionCompletaPtoLlegada() {
        return direccionCompletaPtoLlegada;
    }

    public void setDireccionCompletaPtoLlegada(String direccionCompletaPtoLlegada) {
        this.direccionCompletaPtoLlegada = direccionCompletaPtoLlegada;
    }

    public String getUrbanizacionPtoLlegada() {
        return urbanizacionPtoLlegada;
    }

    public void setUrbanizacionPtoLlegada(String urbanizacionPtoLlegada) {
        this.urbanizacionPtoLlegada = urbanizacionPtoLlegada;
    }

    public String getProvinciaPtoLlegada() {
        return provinciaPtoLlegada;
    }

    public void setProvinciaPtoLlegada(String provinciaPtoLlegada) {
        this.provinciaPtoLlegada = provinciaPtoLlegada;
    }

    public String getDepartamentoPtoLlegada() {
        return departamentoPtoLlegada;
    }

    public void setDepartamentoPtoLlegada(String departamentoPtoLlegada) {
        this.departamentoPtoLlegada = departamentoPtoLlegada;
    }

    public String getDistritoPtoLlegada() {
        return distritoPtoLlegada;
    }

    public void setDistritoPtoLlegada(String distritoPtoLlegada) {
        this.distritoPtoLlegada = distritoPtoLlegada;
    }

    public String getPaisPtoLlegada() {
        return paisPtoLlegada;
    }

    public void setPaisPtoLlegada(String paisPtoLlegada) {
        this.paisPtoLlegada = paisPtoLlegada;
    }

    public String getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getNumeroConstanciaVehiculo() {
        return numeroConstanciaVehiculo;
    }

    public void setNumeroConstanciaVehiculo(String numeroConstanciaVehiculo) {
        this.numeroConstanciaVehiculo = numeroConstanciaVehiculo;
    }

    public String getNumeroLicenciaConducir() {
        return numeroLicenciaConducir;
    }

    public void setNumeroLicenciaConducir(String numeroLicenciaConducir) {
        this.numeroLicenciaConducir = numeroLicenciaConducir;
    }

    public String getNumeroRucTransportista() {
        return numeroRucTransportista;
    }

    public void setNumeroRucTransportista(String numeroRucTransportista) {
        this.numeroRucTransportista = numeroRucTransportista;
    }

    public String getNumeroRucTransportistaCuenta() {
        return numeroRucTransportistaCuenta;
    }

    public void setNumeroRucTransportistaCuenta(String numeroRucTransportistaCuenta) {
        this.numeroRucTransportistaCuenta = numeroRucTransportistaCuenta;
    }

    public String getRazonSocialTransportista() {
        return razonSocialTransportista;
    }

    public void setRazonSocialTransportista(String razonSocialTransportista) {
        this.razonSocialTransportista = razonSocialTransportista;
    }

    public String getTextoNombreMatriculaEmbarcacion() {
        return textoNombreMatriculaEmbarcacion;
    }

    public void setTextoNombreMatriculaEmbarcacion(String textoNombreMatriculaEmbarcacion) {
        this.textoNombreMatriculaEmbarcacion = textoNombreMatriculaEmbarcacion;
    }

    public String getTextoTipoCantidadEspecieVendida() {
        return textoTipoCantidadEspecieVendida;
    }

    public void setTextoTipoCantidadEspecieVendida(String textoTipoCantidadEspecieVendida) {
        this.textoTipoCantidadEspecieVendida = textoTipoCantidadEspecieVendida;
    }

    public BigDecimal getTotalFondoInclusionSocial() {
        return totalFondoInclusionSocial;
    }

    public void setTotalFondoInclusionSocial(BigDecimal totalFondoInclusionSocial) {
        this.totalFondoInclusionSocial = totalFondoInclusionSocial;
    }

    public String getTipoOperacionFactura() {
        return tipoOperacionFactura;
    }

    public void setTipoOperacionFactura(String tipoOperacionFactura) {
        this.tipoOperacionFactura = tipoOperacionFactura;
    }

    public BigDecimal getTotalDocumentoAnticipo() {
        return totalDocumentoAnticipo;
    }

    public void setTotalDocumentoAnticipo(BigDecimal totalDocumentoAnticipo) {
        this.totalDocumentoAnticipo = totalDocumentoAnticipo;
    }


    public String getUbigeoAdquiriente() {
        return ubigeoAdquiriente;
    }

    public void setUbigeoAdquiriente(String ubigeoAdquiriente) {
        this.ubigeoAdquiriente = ubigeoAdquiriente;
    }

    public String getDireccionAdquiriente() {
        return direccionAdquiriente;
    }

    public void setDireccionAdquiriente(String direccionAdquiriente) {
        this.direccionAdquiriente = direccionAdquiriente;
    }

    public String getUrbanizacionAdquiriente() {
        return urbanizacionAdquiriente;
    }

    public void setUrbanizacionAdquiriente(String urbanizacionAdquiriente) {
        this.urbanizacionAdquiriente = urbanizacionAdquiriente;
    }

    public String getProvinciaAdquiriente() {
        return provinciaAdquiriente;
    }

    public void setProvinciaAdquiriente(String provinciaAdquiriente) {
        this.provinciaAdquiriente = provinciaAdquiriente;
    }

    public String getDepartamentoAdquiriente() {
        return departamentoAdquiriente;
    }

    public void setDepartamentoAdquiriente(String departamentoAdquiriente) {
        this.departamentoAdquiriente = departamentoAdquiriente;
    }

    public String getDistritoAdquiriente() {
        return distritoAdquiriente;
    }

    public void setDistritoAdquiriente(String distritoAdquiriente) {
        this.distritoAdquiriente = distritoAdquiriente;
    }

    public String getPaisAdquiriente() {
        return paisAdquiriente;
    }

    public void setPaisAdquiriente(String paisAdquiriente) {
        this.paisAdquiriente = paisAdquiriente;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    //HNA - 28OCt2015: RS185
	public String getUbigeoLugarEntrega() {
		return ubigeoLugarEntrega;
	}

	public void setUbigeoLugarEntrega(String ubigeoLugarEntrega) {
		this.ubigeoLugarEntrega = ubigeoLugarEntrega;
	}

	public String getDireccionLugarEntrega() {
		return direccionLugarEntrega;
	}

	public void setDireccionLugarEntrega(String direccionLugarEntrega) {
		this.direccionLugarEntrega = direccionLugarEntrega;
	}

	public String getUrbanizacionLugarEntrega() {
		return urbanizacionLugarEntrega;
	}

	public void setUrbanizacionLugarEntrega(String urbanizacionLugarEntrega) {
		this.urbanizacionLugarEntrega = urbanizacionLugarEntrega;
	}

	public String getProvinciaLugarEntrega() {
		return provinciaLugarEntrega;
	}

	public void setProvinciaLugarEntrega(String provinciaLugarEntrega) {
		this.provinciaLugarEntrega = provinciaLugarEntrega;
	}

	public String getDepartamentoLugarEntrega() {
		return departamentoLugarEntrega;
	}

	public void setDepartamentoLugarEntrega(String departamentoLugarEntrega) {
		this.departamentoLugarEntrega = departamentoLugarEntrega;
	}

	public String getDistritoLugarEntrega() {
		return distritoLugarEntrega;
	}

	public void setDistritoLugarEntrega(String distritoLugarEntrega) {
		this.distritoLugarEntrega = distritoLugarEntrega;
	}

	public String getPaisLugarEntrega() {
		return paisLugarEntrega;
	}

	public void setPaisLugarEntrega(String paisLugarEntrega) {
		this.paisLugarEntrega = paisLugarEntrega;
	}

	public String getModalidadTransporte() {
		return modalidadTransporte;
	}

	public void setModalidadTransporte(String modalidadTransporte) {
		this.modalidadTransporte = modalidadTransporte;
	}

	public BigDecimal getTotalPesoBruto() {
		return totalPesoBruto;
	}

	public void setTotalPesoBruto(BigDecimal totalPesoBruto) {
		this.totalPesoBruto = totalPesoBruto;
	}

	public String getUnidadMedidaPesoBruto() {
		return unidadMedidaPesoBruto;
	}

	public void setUnidadMedidaPesoBruto(String unidadMedidaPesoBruto) {
		this.unidadMedidaPesoBruto = unidadMedidaPesoBruto;
	}

	public String getPlacaVehiculoRenta() {
		return placaVehiculoRenta;
	}

	public void setPlacaVehiculoRenta(String placaVehiculoRenta) {
		this.placaVehiculoRenta = placaVehiculoRenta;
	}

	public String getMatriculaEmbarcacion() {
		return matriculaEmbarcacion;
	}

	public void setMatriculaEmbarcacion(String matriculaEmbarcacion) {
		this.matriculaEmbarcacion = matriculaEmbarcacion;
	}

	public String getNombreEmbarcacion() {
		return nombreEmbarcacion;
	}

	public void setNombreEmbarcacion(String nombreEmbarcacion) {
		this.nombreEmbarcacion = nombreEmbarcacion;
	}

	public String getDescripcionEspecieVendida() {
		return descripcionEspecieVendida;
	}

	public void setDescripcionEspecieVendida(String descripcionEspecieVendida) {
		this.descripcionEspecieVendida = descripcionEspecieVendida;
	}

	public String getLugarDescarga() {
		return lugarDescarga;
	}

	public void setLugarDescarga(String lugarDescarga) {
		this.lugarDescarga = lugarDescarga;
	}

	public Date getFechaDescarga() {
		return fechaDescarga;
	}

	public void setFechaDescarga(Date fechaDescarga) {
		this.fechaDescarga = fechaDescarga;
	}

	public BigDecimal getMontoReferencial() {
		return montoReferencial;
	}

	public void setMontoReferencial(BigDecimal montoReferencial) {
		this.montoReferencial = montoReferencial;
	}

	public BigDecimal getMontoReferencialPreliminar() {
		return montoReferencialPreliminar;
	}

	public void setMontoReferencialPreliminar(BigDecimal montoReferencialPreliminar) {
		this.montoReferencialPreliminar = montoReferencialPreliminar;
	}

	public String getFactorRetornoViaje() {
		return factorRetornoViaje;
	}

	public void setFactorRetornoViaje(String factorRetornoViaje) {
		this.factorRetornoViaje = factorRetornoViaje;
	}

	public String getPuntoOrigenViaje() {
		return puntoOrigenViaje;
	}

	public void setPuntoOrigenViaje(String puntoOrigenViaje) {
		this.puntoOrigenViaje = puntoOrigenViaje;
	}

	public String getPuntoDestinoViaje() {
		return puntoDestinoViaje;
	}

	public void setPuntoDestinoViaje(String puntoDestinoViaje) {
		this.puntoDestinoViaje = puntoDestinoViaje;
	}

	public BigDecimal getCargaEfectivaVehiculo() {
		return cargaEfectivaVehiculo;
	}

	public void setCargaEfectivaVehiculo(BigDecimal cargaEfectivaVehiculo) {
		this.cargaEfectivaVehiculo = cargaEfectivaVehiculo;
	}

	public String getUnidadMedidaCargaEfectiva() {
		return unidadMedidaCargaEfectiva;
	}

	public void setUnidadMedidaCargaEfectiva(String unidadMedidaCargaEfectiva) {
		this.unidadMedidaCargaEfectiva = unidadMedidaCargaEfectiva;
	}

	public BigDecimal getMontoReferencialVehiculo() {
		return montoReferencialVehiculo;
	}

	public void setMontoReferencialVehiculo(BigDecimal montoReferencialVehiculo) {
		this.montoReferencialVehiculo = montoReferencialVehiculo;
	}

	public String getConfiguracionVehicular() {
		return configuracionVehicular;
	}

	public void setConfiguracionVehicular(String configuracionVehicular) {
		this.configuracionVehicular = configuracionVehicular;
	}

	public BigDecimal getCargaUtilTnVehiculo() {
		return cargaUtilTnVehiculo;
	}

	public void setCargaUtilTnVehiculo(BigDecimal cargaUtilTnVehiculo) {
		this.cargaUtilTnVehiculo = cargaUtilTnVehiculo;
	}

	public String getUnidadMedidaCargaUtilTn() {
		return unidadMedidaCargaUtilTn;
	}

	public void setUnidadMedidaCargaUtilTn(String unidadMedidaCargaUtilTn) {
		this.unidadMedidaCargaUtilTn = unidadMedidaCargaUtilTn;
	}

	public BigDecimal getMontoReferencialTM() {
		return montoReferencialTM;
	}

	public void setMontoReferencialTM(BigDecimal montoReferencialTM) {
		this.montoReferencialTM = montoReferencialTM;
	}

	public String getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}
	
    public List<String> buildDireccionAdquiriente() {
        if (StringUtils.isBlank(ubigeoAdquiriente) &&
                StringUtils.isBlank(direccionAdquiriente) &&
                StringUtils.isBlank(urbanizacionAdquiriente) &&
                StringUtils.isBlank(provinciaAdquiriente) &&
                StringUtils.isBlank(departamentoAdquiriente) &&
                StringUtils.isBlank(distritoAdquiriente) &&
                StringUtils.isBlank(paisAdquiriente)) {
            return null;
        }

        List<String> diAdquiriente = new ArrayList<String>();
        
        diAdquiriente.add(ubigeoAdquiriente);
        diAdquiriente.add(direccionAdquiriente);
        diAdquiriente.add(urbanizacionAdquiriente);
        diAdquiriente.add(provinciaAdquiriente);
        diAdquiriente.add(departamentoAdquiriente);
        diAdquiriente.add(distritoAdquiriente);
        diAdquiriente.add(paisAdquiriente);
        
        return diAdquiriente;
    }

	public String getTipoFondoInclusionSocialEnergetico() {
		return tipoFondoInclusionSocialEnergetico;
	}

	public void setTipoFondoInclusionSocialEnergetico(String tipoFondoInclusionSocialEnergetico) {
		this.tipoFondoInclusionSocialEnergetico = tipoFondoInclusionSocialEnergetico;
	}

	public BigDecimal getTotalFondoInclusionSocialEnergetico() {
		return totalFondoInclusionSocialEnergetico;
	}

	public void setTotalFondoInclusionSocialEnergetico(BigDecimal totalFondoInclusionSocialEnergetico) {
		this.totalFondoInclusionSocialEnergetico = totalFondoInclusionSocialEnergetico;
	}

	public List<TypeResponse> getListTypeResponse() {
		return listTypeResponse;
	}

	public void setListTypeResponse(List<TypeResponse> listTypeResponse) {
		this.listTypeResponse = listTypeResponse;
	}

	public List<TypeAdditionalInformation> getListTypeAdditionalInformation() {
		return listTypeAdditionalInformation;
	}

	public void setListTypeAdditionalInformation(List<TypeAdditionalInformation> listTypeAdditionalInformation) {
		this.listTypeAdditionalInformation = listTypeAdditionalInformation;
	}

	public List<TypeDocumentReference> getListTypeDocumentReference() {
		return listTypeDocumentReference;
	}

	public void setListTypeDocumentReference(List<TypeDocumentReference> listTypeDocumentReference) {
		this.listTypeDocumentReference = listTypeDocumentReference;
	}

	public List<TypeBillingReference> getBillingReference() {
		if (billingReference == null) {
			billingReference = new ArrayList<TypeBillingReference>();
        }
		return billingReference;
	}

	public void setBillingReference(List<TypeBillingReference> billingReference) {
		this.billingReference = billingReference;
	}

	public TypeSignature getTypeSignature() {
		return typeSignature;
	}

	public void setTypeSignature(TypeSignature typeSignature) {
		this.typeSignature = typeSignature;
	}

	public String getNumIdCd() {
		return numIdCd;
	}

	public void setNumIdCd(String numIdCd) {
		this.numIdCd = numIdCd;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public List<TypeTaxTotal> getListTypeTaxTotal() {
		return listTypeTaxTotal;
	}

	public void setListTypeTaxTotal(List<TypeTaxTotal> listTypeTaxTotal) {
		this.listTypeTaxTotal = listTypeTaxTotal;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public TypeMonetaryTotal getTypeLegalMonetaryTotal() {
		return typeLegalMonetaryTotal;
	}

	public void setTypeLegalMonetaryTotal(TypeMonetaryTotal typeLegalMonetaryTotal) {
		this.typeLegalMonetaryTotal = typeLegalMonetaryTotal;
	}

	public TypeCustomerParty getAccountingCustomerParty() {
		return accountingCustomerParty;
	}

	public void setAccountingCustomerParty(TypeCustomerParty accountingCustomerParty) {
		this.accountingCustomerParty = accountingCustomerParty;
	}

	public Date getDategetNotBefore() {
		return dategetNotBefore;
	}

	public void setDategetNotBefore(Date dategetNotBefore) {
		this.dategetNotBefore = dategetNotBefore;
	}

	public Date getDateNotAfter() {
		return dateNotAfter;
	}

	public void setDateNotAfter(Date dateNotAfter) {
		this.dateNotAfter = dateNotAfter;
	}

	public List<TypeDocumentReference> getListTypeAdditionalDocumentReference() {
		return listTypeAdditionalDocumentReference;
	}

	public void setListTypeAdditionalDocumentReference(List<TypeDocumentReference> listTypeAdditionalDocumentReference) {
		this.listTypeAdditionalDocumentReference = listTypeAdditionalDocumentReference;
	}

	public TypeMonetaryTotal getTypeRequestedMonetaryTotal() {
		return typeRequestedMonetaryTotal;
	}

	public void setTypeRequestedMonetaryTotal(TypeMonetaryTotal typeRequestedMonetaryTotal) {
		this.typeRequestedMonetaryTotal = typeRequestedMonetaryTotal;
	}

	public BigDecimal getTotalIgv() {
		return totalIgv;
	}

	public void setTotalIgv(BigDecimal totalIgv) {
		this.totalIgv = totalIgv;
	}

	public BigDecimal getTotalIsc() {
		return totalIsc;
	}

	public void setTotalIsc(BigDecimal totalIsc) {
		this.totalIsc = totalIsc;
	}

	public BigDecimal getTotalOtrosTributos() {
		return totalOtrosTributos;
	}

	public void setTotalOtrosTributos(BigDecimal totalOtrosTributos) {
		this.totalOtrosTributos = totalOtrosTributos;
	}

	public BigDecimal getTotalOPGravada() {
		return totalOPGravada;
	}

	public void setTotalOPGravada(BigDecimal totalOPGravada) {
		this.totalOPGravada = totalOPGravada;
	}

	public BigDecimal getTotalOPInafecta() {
		return totalOPInafecta;
	}

	public void setTotalOPInafecta(BigDecimal totalOPInafecta) {
		this.totalOPInafecta = totalOPInafecta;
	}

	public BigDecimal getTotalOPExonerada() {
		return totalOPExonerada;
	}

	public void setTotalOPExonerada(BigDecimal totalOPExonerada) {
		this.totalOPExonerada = totalOPExonerada;
	}

	public BigDecimal getTotalOPExportacion() {
		return totalOPExportacion;
	}

	public void setTotalOPExportacion(BigDecimal totalOPExportacion) {
		this.totalOPExportacion = totalOPExportacion;
	}

	public BigDecimal getDescuentosGlobalesAfectos() {
		return descuentosGlobalesAfectos;
	}

	public void setDescuentosGlobalesAfectos(BigDecimal descuentosGlobalesAfectos) {
		this.descuentosGlobalesAfectos = descuentosGlobalesAfectos;
	}

	public BigDecimal getDescuentosGlobalesNoAfectos() {
		return descuentosGlobalesNoAfectos;
	}

	public void setDescuentosGlobalesNoAfectos(BigDecimal descuentosGlobalesNoAfectos) {
		this.descuentosGlobalesNoAfectos = descuentosGlobalesNoAfectos;
	}

	public BigDecimal getCargosGlobalesAfectos() {
		return cargosGlobalesAfectos;
	}

	public void setCargosGlobalesAfectos(BigDecimal cargosGlobalesAfectos) {
		this.cargosGlobalesAfectos = cargosGlobalesAfectos;
	}

	public BigDecimal getCargosGlobalesNoAfectos() {
		return cargosGlobalesNoAfectos;
	}

	public void setCargosGlobalesNoAfectos(BigDecimal cargosGlobalesNoAfectos) {
		this.cargosGlobalesNoAfectos = cargosGlobalesNoAfectos;
	}

	public TypeSupplierParty getAccountingSupplierParty() {
		return accountingSupplierParty;
	}

	public void setAccountingSupplierParty(TypeSupplierParty accountingSupplierParty) {
		this.accountingSupplierParty = accountingSupplierParty;
	}

	public List<TypeDelivery> getDelivery() {
		return delivery;
	}

	public void setDelivery(List<TypeDelivery> delivery) {
		this.delivery = delivery;
	}

	public List<TypeResponse> getDiscrepancyResponse() {
		return discrepancyResponse;
	}

	public void setDiscrepancyResponse(List<TypeResponse> discrepancyResponse) {
		this.discrepancyResponse = discrepancyResponse;
	}

	public List<TypeAllowanceCharge> getListTypeAllowanceCharge() {
		return listTypeAllowanceCharge;
	}

	public void setListTypeAllowanceCharge(List<TypeAllowanceCharge> listTypeAllowanceCharge) {
		this.listTypeAllowanceCharge = listTypeAllowanceCharge;
	}
    
    public List<TypePaymentTerms> getListTypePaymentTerms() {
		return listTypePaymentTerms;
	}

	public void setListTypePaymentTerms(List<TypePaymentTerms> listTypePaymentTerms) {
		this.listTypePaymentTerms = listTypePaymentTerms;
	}

	public String toString() {
        return "EDocumento{" +
                "tipoDocumento='" + tipoDocumento + '\'' +
                ", serieNumero='" + serieNumero + '\'' +
                ", numeroDocumentoEmisor='" + numeroDocumentoEmisor + '\'' +
                ", razonSocialEmisor='" + razonSocialEmisor + '\'' +
                '}';
    }
    
}