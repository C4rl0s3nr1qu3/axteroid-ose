package com.axteroid.ose.server.ubl20.gateway.batch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User: HNA
 * Date: 01/12/15
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Retencion", propOrder = {
		"indicador",
		"correoEmisor",
		"correoAdquiriente",
		"inHabilitado",
		"serieNumeroRetencion",
		"fechaEmision",
		"tipoDocumento",
		"numeroDocumentoEmisor",
		"tipoDocumentoEmisor",
		"nombreComercialEmisor",
		"ubigeoEmisor",
		"direccionEmisor",
		"urbanizacionEmisor",
		"provinciaEmisor",
		"departamentoEmisor",
		"distritoEmisor",
		"codigoPaisEmisor",
		"razonSocialEmisor",
		"numeroDocumentoProveedor",
		"tipoDocumentoProveedor",
		"nombreComercialProveedor",
		"ubigeoProveedor",
		"direccionProveedor",
		"urbanizacionProveedor",
		"provinciaProveedor",
		"departamentoProveedor",
		"distritoProveedor",
		"codigoPaisProveedor",
		"razonSocialProveedor",
		"regimenRetencion",
		"tasaRetencion",
		"observaciones",
		"importeTotalRetenido",
		"tipoMonedaTotalRetenido",
		"importeTotalPagado",
		"tipoMonedaTotalPagado",
        "codigoAuxiliar100_1",
        "textoAuxiliar100_1",
        "codigoAuxiliar100_2",
        "textoAuxiliar100_2",
        "codigoAuxiliar100_3",
        "textoAuxiliar100_3",
        "codigoAuxiliar100_4",
        "textoAuxiliar100_4",
        "codigoAuxiliar100_5",
        "textoAuxiliar100_5",
        "codigoAuxiliar100_6",
        "textoAuxiliar100_6",
        "codigoAuxiliar100_7",
        "textoAuxiliar100_7",
        "codigoAuxiliar100_8",
        "textoAuxiliar100_8",
        "codigoAuxiliar100_9",
        "textoAuxiliar100_9",
        "codigoAuxiliar100_10",
        "textoAuxiliar100_10",
        "codigoAuxiliar40_1",
        "textoAuxiliar40_1",
        "codigoAuxiliar40_2",
        "textoAuxiliar40_2",
        "codigoAuxiliar40_3",
        "textoAuxiliar40_3",
        "codigoAuxiliar40_4",
        "textoAuxiliar40_4",
        "codigoAuxiliar40_5",
        "textoAuxiliar40_5",
        "codigoAuxiliar40_6",
        "textoAuxiliar40_6",
        "codigoAuxiliar40_7",
        "textoAuxiliar40_7",
        "codigoAuxiliar40_8",
        "textoAuxiliar40_8",
        "codigoAuxiliar40_9",
        "textoAuxiliar40_9",
        "codigoAuxiliar40_10",
        "textoAuxiliar40_10",
        "codigoAuxiliar500_1",
        "textoAuxiliar500_1",
        "codigoAuxiliar500_2",
        "textoAuxiliar500_2",
        "codigoAuxiliar500_3",
        "textoAuxiliar500_3",
        "codigoAuxiliar500_4",
        "textoAuxiliar500_4",
        "codigoAuxiliar500_5",
        "textoAuxiliar500_5",
        "codigoAuxiliar500_6",
        "textoAuxiliar500_6",
        "codigoAuxiliar500_7",
        "textoAuxiliar500_7",
        "codigoAuxiliar500_8",
        "textoAuxiliar500_8",
        "codigoAuxiliar500_9",
        "textoAuxiliar500_9",
        "codigoAuxiliar500_10",
        "textoAuxiliar500_10",
        "codigoAuxiliar250_1",
        "textoAuxiliar250_1",
        "codigoAuxiliar250_2",
        "textoAuxiliar250_2",
        "codigoAuxiliar250_3",
        "textoAuxiliar250_3",
        "codigoAuxiliar250_4",
        "textoAuxiliar250_4",
        "codigoAuxiliar250_5",
        "textoAuxiliar250_5",
        "codigoAuxiliar250_6",
        "textoAuxiliar250_6",
        "codigoAuxiliar250_7",
        "textoAuxiliar250_7",
        "codigoAuxiliar250_8",
        "textoAuxiliar250_8",
        "codigoAuxiliar250_9",
        "textoAuxiliar250_9",
        "codigoAuxiliar250_10",
        "textoAuxiliar250_10",
		"items"
})

public class ERetencionCliente implements Cloneable {
	
	@XmlElement(required = true)
    public String indicador = "C";
	public String correoEmisor;
	public String correoAdquiriente;
	public String inHabilitado;
	public String serieNumeroRetencion;
	public Date fechaEmision;
	public String tipoDocumento;
	public String numeroDocumentoEmisor;
	public String tipoDocumentoEmisor;
	public String nombreComercialEmisor;
	public String ubigeoEmisor;
	public String direccionEmisor;
	public String urbanizacionEmisor;
	public String provinciaEmisor;
	public String departamentoEmisor;
	public String distritoEmisor;
	public String codigoPaisEmisor;
	public String razonSocialEmisor;
	public String numeroDocumentoProveedor;
	public String tipoDocumentoProveedor;
	public String nombreComercialProveedor;
	public String ubigeoProveedor;
	public String direccionProveedor;
	public String urbanizacionProveedor;
	public String provinciaProveedor;
	public String departamentoProveedor;
	public String distritoProveedor;
	public String codigoPaisProveedor;
	public String razonSocialProveedor;
	public String regimenRetencion;
	//@XmlJavaTypeAdapter(Number2Adapter.class)
	public BigDecimal tasaRetencion;
	public String observaciones;
	//@XmlJavaTypeAdapter(Number2Adapter.class)
	public BigDecimal importeTotalRetenido;
	public String tipoMonedaTotalRetenido;
	//@XmlJavaTypeAdapter(Number2Adapter.class)
	public BigDecimal importeTotalPagado;
	public String tipoMonedaTotalPagado;

    @XmlElement(name = "codigoAuxiliar100_1")
    private String codigoAuxiliar100_1;
    @XmlElement(name = "textoAuxiliar100_1")
    private String textoAuxiliar100_1;
    @XmlElement(name = "codigoAuxiliar100_2")
    private String codigoAuxiliar100_2;
    @XmlElement(name = "textoAuxiliar100_2")
    private String textoAuxiliar100_2;
    @XmlElement(name = "codigoAuxiliar100_3")
    private String codigoAuxiliar100_3;
    @XmlElement(name = "textoAuxiliar100_3")
    private String textoAuxiliar100_3;
    @XmlElement(name = "codigoAuxiliar100_4")
    private String codigoAuxiliar100_4;
    @XmlElement(name = "textoAuxiliar100_4")
    private String textoAuxiliar100_4;
    @XmlElement(name = "codigoAuxiliar100_5")
    private String codigoAuxiliar100_5;
    @XmlElement(name = "textoAuxiliar100_5")
    private String textoAuxiliar100_5;
    @XmlElement(name = "codigoAuxiliar100_6")
    private String codigoAuxiliar100_6;
    @XmlElement(name = "textoAuxiliar100_6")
    private String textoAuxiliar100_6;
    @XmlElement(name = "codigoAuxiliar100_7")
    private String codigoAuxiliar100_7;
    @XmlElement(name = "textoAuxiliar100_7")
    private String textoAuxiliar100_7;
    @XmlElement(name = "codigoAuxiliar100_8")
    private String codigoAuxiliar100_8;
    @XmlElement(name = "textoAuxiliar100_8")
    private String textoAuxiliar100_8;
    @XmlElement(name = "codigoAuxiliar100_9")
    private String codigoAuxiliar100_9;
    @XmlElement(name = "textoAuxiliar100_9")
    private String textoAuxiliar100_9;
    @XmlElement(name = "codigoAuxiliar100_10")
    private String codigoAuxiliar100_10;
    @XmlElement(name = "textoAuxiliar100_10")
    private String textoAuxiliar100_10;
    
    @XmlElement(name = "codigoAuxiliar40_1")
    private String codigoAuxiliar40_1;
    @XmlElement(name = "textoAuxiliar40_1")
    private String textoAuxiliar40_1;
    @XmlElement(name = "codigoAuxiliar40_2")
    private String codigoAuxiliar40_2;
    @XmlElement(name = "textoAuxiliar40_2")
    private String textoAuxiliar40_2;
    @XmlElement(name = "codigoAuxiliar40_3")
    private String codigoAuxiliar40_3;
    @XmlElement(name = "textoAuxiliar40_3")
    private String textoAuxiliar40_3;
    @XmlElement(name = "codigoAuxiliar40_4")
    private String codigoAuxiliar40_4;
    @XmlElement(name = "textoAuxiliar40_4")
    private String textoAuxiliar40_4;
    @XmlElement(name = "codigoAuxiliar40_5")
    private String codigoAuxiliar40_5;
    @XmlElement(name = "textoAuxiliar40_5")
    private String textoAuxiliar40_5;
    @XmlElement(name = "codigoAuxiliar40_6")
    private String codigoAuxiliar40_6;
    @XmlElement(name = "textoAuxiliar40_6")
    private String textoAuxiliar40_6;
    @XmlElement(name = "codigoAuxiliar40_7")
    private String codigoAuxiliar40_7;
    @XmlElement(name = "textoAuxiliar40_7")
    private String textoAuxiliar40_7;
    @XmlElement(name = "codigoAuxiliar40_8")
    private String codigoAuxiliar40_8;
    @XmlElement(name = "textoAuxiliar40_8")
    private String textoAuxiliar40_8;
    @XmlElement(name = "codigoAuxiliar40_9")
    private String codigoAuxiliar40_9;
    @XmlElement(name = "textoAuxiliar40_9")
    private String textoAuxiliar40_9;
    @XmlElement(name = "codigoAuxiliar40_10")
    private String codigoAuxiliar40_10;
    @XmlElement(name = "textoAuxiliar40_10")
    private String textoAuxiliar40_10;
    
    @XmlElement(name = "codigoAuxiliar500_1")
    private String codigoAuxiliar500_1;
    @XmlElement(name = "textoAuxiliar500_1")
    private String textoAuxiliar500_1;
    @XmlElement(name = "codigoAuxiliar500_2")
    private String codigoAuxiliar500_2;
    @XmlElement(name = "textoAuxiliar500_2")
    private String textoAuxiliar500_2;
    @XmlElement(name = "codigoAuxiliar500_3")
    private String codigoAuxiliar500_3;
    @XmlElement(name = "textoAuxiliar500_3")
    private String textoAuxiliar500_3;
    @XmlElement(name = "codigoAuxiliar500_4")
    private String codigoAuxiliar500_4;
    @XmlElement(name = "textoAuxiliar500_4")
    private String textoAuxiliar500_4;
    @XmlElement(name = "codigoAuxiliar500_5")
    private String codigoAuxiliar500_5;
    @XmlElement(name = "textoAuxiliar500_5")
    private String textoAuxiliar500_5;
    @XmlElement(name = "codigoAuxiliar500_6")
    private String codigoAuxiliar500_6;
    @XmlElement(name = "textoAuxiliar500_6")
    private String textoAuxiliar500_6;
    @XmlElement(name = "codigoAuxiliar500_7")
    private String codigoAuxiliar500_7;
    @XmlElement(name = "textoAuxiliar500_7")
    private String textoAuxiliar500_7;
    @XmlElement(name = "codigoAuxiliar500_8")
    private String codigoAuxiliar500_8;
    @XmlElement(name = "textoAuxiliar500_8")
    private String textoAuxiliar500_8;
    @XmlElement(name = "codigoAuxiliar500_9")
    private String codigoAuxiliar500_9;
    @XmlElement(name = "textoAuxiliar500_9")
    private String textoAuxiliar500_9;
    @XmlElement(name = "codigoAuxiliar500_10")
    private String codigoAuxiliar500_10;
    @XmlElement(name = "textoAuxiliar500_10")
    private String textoAuxiliar500_10;
    
    @XmlElement(name = "codigoAuxiliar250_1")
    private String codigoAuxiliar250_1;
    @XmlElement(name = "textoAuxiliar250_1")
    private String textoAuxiliar250_1;
    @XmlElement(name = "codigoAuxiliar250_2")
    private String codigoAuxiliar250_2;
    @XmlElement(name = "textoAuxiliar250_2")
    private String textoAuxiliar250_2;
    @XmlElement(name = "codigoAuxiliar250_3")
    private String codigoAuxiliar250_3;
    @XmlElement(name = "textoAuxiliar250_3")
    private String textoAuxiliar250_3;
    @XmlElement(name = "codigoAuxiliar250_4")
    private String codigoAuxiliar250_4;
    @XmlElement(name = "textoAuxiliar250_4")
    private String textoAuxiliar250_4;
    @XmlElement(name = "codigoAuxiliar250_5")
    private String codigoAuxiliar250_5;
    @XmlElement(name = "textoAuxiliar250_5")
    private String textoAuxiliar250_5;
    @XmlElement(name = "codigoAuxiliar250_6")
    private String codigoAuxiliar250_6;
    @XmlElement(name = "textoAuxiliar250_6")
    private String textoAuxiliar250_6;
    @XmlElement(name = "codigoAuxiliar250_7")
    private String codigoAuxiliar250_7;
    @XmlElement(name = "textoAuxiliar250_7")
    private String textoAuxiliar250_7;
    @XmlElement(name = "codigoAuxiliar250_8")
    private String codigoAuxiliar250_8;
    @XmlElement(name = "textoAuxiliar250_8")
    private String textoAuxiliar250_8;
    @XmlElement(name = "codigoAuxiliar250_9")
    private String codigoAuxiliar250_9;
    @XmlElement(name = "textoAuxiliar250_9")
    private String textoAuxiliar250_9;
    @XmlElement(name = "codigoAuxiliar250_10")
    private String codigoAuxiliar250_10;
    @XmlElement(name = "textoAuxiliar250_10")
    private String textoAuxiliar250_10;    
   
	@XmlElement(name = "RetencionItem", nillable = true)
	public List<ERetencionItemCliente> items = new ArrayList<ERetencionItemCliente>();
	
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
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
	public String getInHabilitado() {
		return inHabilitado;
	}
	public void setInHabilitado(String inHabilitado) {
		this.inHabilitado = inHabilitado;
	}
	public String getSerieNumeroRetencion() {
		return serieNumeroRetencion;
	}
	public void setSerieNumeroRetencion(String serieNumeroRetencion) {
		this.serieNumeroRetencion = serieNumeroRetencion;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
	public String getNombreComercialEmisor() {
		return nombreComercialEmisor;
	}
	public void setNombreComercialEmisor(String nombreComercialEmisor) {
		this.nombreComercialEmisor = nombreComercialEmisor;
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
	public String getUrbanizacionEmisor() {
		return urbanizacionEmisor;
	}
	public void setUrbanizacionEmisor(String urbanizacionEmisor) {
		this.urbanizacionEmisor = urbanizacionEmisor;
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
	public String getCodigoPaisEmisor() {
		return codigoPaisEmisor;
	}
	public void setCodigoPaisEmisor(String codigoPaisEmisor) {
		this.codigoPaisEmisor = codigoPaisEmisor;
	}
	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}
	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}
	public String getNumeroDocumentoProveedor() {
		return numeroDocumentoProveedor;
	}
	public void setNumeroDocumentoProveedor(String numeroDocumentoProveedor) {
		this.numeroDocumentoProveedor = numeroDocumentoProveedor;
	}
	public String getTipoDocumentoProveedor() {
		return tipoDocumentoProveedor;
	}
	public void setTipoDocumentoProveedor(String tipoDocumentoProveedor) {
		this.tipoDocumentoProveedor = tipoDocumentoProveedor;
	}
	public String getNombreComercialProveedor() {
		return nombreComercialProveedor;
	}
	public void setNombreComercialProveedor(String nombreComercialProveedor) {
		this.nombreComercialProveedor = nombreComercialProveedor;
	}
	public String getUbigeoProveedor() {
		return ubigeoProveedor;
	}
	public void setUbigeoProveedor(String ubigeoProveedor) {
		this.ubigeoProveedor = ubigeoProveedor;
	}
	public String getDireccionProveedor() {
		return direccionProveedor;
	}
	public void setDireccionProveedor(String direccionProveedor) {
		this.direccionProveedor = direccionProveedor;
	}
	public String getUrbanizacionProveedor() {
		return urbanizacionProveedor;
	}
	public void setUrbanizacionProveedor(String urbanizacionProveedor) {
		this.urbanizacionProveedor = urbanizacionProveedor;
	}
	public String getProvinciaProveedor() {
		return provinciaProveedor;
	}
	public void setProvinciaProveedor(String provinciaProveedor) {
		this.provinciaProveedor = provinciaProveedor;
	}
	public String getDepartamentoProveedor() {
		return departamentoProveedor;
	}
	public void setDepartamentoProveedor(String departamentoProveedor) {
		this.departamentoProveedor = departamentoProveedor;
	}
	public String getDistritoProveedor() {
		return distritoProveedor;
	}
	public void setDistritoProveedor(String distritoProveedor) {
		this.distritoProveedor = distritoProveedor;
	}
	public String getCodigoPaisProveedor() {
		return codigoPaisProveedor;
	}
	public void setCodigoPaisProveedor(String codigoPaisProveedor) {
		this.codigoPaisProveedor = codigoPaisProveedor;
	}
	public String getRazonSocialProveedor() {
		return razonSocialProveedor;
	}
	public void setRazonSocialProveedor(String razonSocialProveedor) {
		this.razonSocialProveedor = razonSocialProveedor;
	}
	public String getRegimenRetencion() {
		return regimenRetencion;
	}
	public void setRegimenRetencion(String regimenRetencion) {
		this.regimenRetencion = regimenRetencion;
	}
	public BigDecimal getTasaRetencion() {
		return tasaRetencion;
	}
	public void setTasaRetencion(BigDecimal tasaRetencion) {
		this.tasaRetencion = tasaRetencion;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public BigDecimal getImporteTotalRetenido() {
		return importeTotalRetenido;
	}
	public void setImporteTotalRetenido(BigDecimal importeTotalRetenido) {
		this.importeTotalRetenido = importeTotalRetenido;
	}
	public String getTipoMonedaTotalRetenido() {
		return tipoMonedaTotalRetenido;
	}
	public void setTipoMonedaTotalRetenido(String tipoMonedaTotalRetenido) {
		this.tipoMonedaTotalRetenido = tipoMonedaTotalRetenido;
	}
	public BigDecimal getImporteTotalPagado() {
		return importeTotalPagado;
	}
	public void setImporteTotalPagado(BigDecimal importeTotalPagado) {
		this.importeTotalPagado = importeTotalPagado;
	}
	public String getTipoMonedaTotalPagado() {
		return tipoMonedaTotalPagado;
	}
	public void setTipoMonedaTotalPagado(String tipoMonedaTotalPagado) {
		this.tipoMonedaTotalPagado = tipoMonedaTotalPagado;
	}
	public List<ERetencionItemCliente> getItems() {
		return items;
	}
	public void setItems(List<ERetencionItemCliente> items) {
		this.items = items;
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
}
