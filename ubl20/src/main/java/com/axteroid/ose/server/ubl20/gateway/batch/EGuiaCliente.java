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
 * Date: 04/11/15
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Guia", propOrder = {
		"indicador",
		"correoEmisor",
		"correoAdquiriente",
		"inHabilitado",
		"serieNumeroGuia",
		"fechaEmisionGuia",
		"tipoDocumentoGuia",
		"observaciones",
		"serieGuiaBaja",
		"codigoGuiaBaja",
		"tipoGuiaBaja",
		"numeroDocumentoRelacionado",
		"codigoDocumentoRelacionado",
		"numeroDocumentoRemitente",
		"tipoDocumentoRemitente",
		"razonSocialRemitente",
		"numeroDocumentoDestinatario",
		"tipoDocumentoDestinatario",
		"razonSocialDestinatario",
		"numeroDocumentoEstablecimiento",
		"tipoDocumentoEstablecimiento",
		"razonSocialEstablecimiento",
		"motivoTraslado",
		"descripcionMotivoTraslado",
		"indTransbordoProgramado",
		"pesoBrutoTotalBienes",
		"unidadMedidaPesoBruto",
		"numeroBultos",
		"modalidadTraslado",
		"fechaInicioTraslado",
		"numeroRucTransportista",
		"tipoDocumentoTransportista",
		"razonSocialTransportista",
		"numeroPlacaVehiculo",
		"numeroDocumentoConductor",
		"tipoDocumentoConductor",
		"ubigeoPtoLLegada",
		"direccionPtoLLegada",
		"numeroContenedor",
		"ubigeoPtoPartida",
		"direccionPtoPartida",
		"codigoPuerto",
		"nombreAdjunto_1",
	    "nombreAdjunto_2",
	    "nombreAdjunto_3",
	    "nombreAdjunto_4",
	    "nombreAdjunto_5",
	    "adjunto_1",
	    "adjunto_2",
	    "adjunto_3",
	    "adjunto_4",
	    "adjunto_5",
	    "rutaAdjunto_1",
	    "rutaAdjunto_2",
	    "rutaAdjunto_3",
	    "rutaAdjunto_4",
	    "rutaAdjunto_5",
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

public class EGuiaCliente implements Cloneable  {
	
	@XmlElement(required = true)
    public String indicador = "C";
	public String serieNumeroGuia;
	public Date fechaEmisionGuia;
	public String tipoDocumentoGuia;
	public String observaciones;
	public String serieGuiaBaja;
	public String codigoGuiaBaja;
	public String tipoGuiaBaja;
	public String numeroDocumentoRelacionado;
	public String codigoDocumentoRelacionado;
	public String numeroDocumentoRemitente;
	public String tipoDocumentoRemitente;
	public String razonSocialRemitente;
	public String numeroDocumentoDestinatario;
	public String tipoDocumentoDestinatario;
	public String razonSocialDestinatario;	
	public String numeroDocumentoEstablecimiento;
	public String tipoDocumentoEstablecimiento;
	public String razonSocialEstablecimiento;	 
	public String motivoTraslado;
	public String descripcionMotivoTraslado;
	public boolean indTransbordoProgramado;
	public BigDecimal pesoBrutoTotalBienes;
	public String unidadMedidaPesoBruto;
	public BigDecimal numeroBultos;
	public String modalidadTraslado;
	public Date fechaInicioTraslado;
	public String numeroRucTransportista;
	public String tipoDocumentoTransportista;
	public String razonSocialTransportista;
	public String numeroPlacaVehiculo;
	public String numeroDocumentoConductor;
	public String tipoDocumentoConductor;
	public String ubigeoPtoLLegada;
	public String direccionPtoLLegada;
	public String numeroContenedor;
	public String ubigeoPtoPartida;
	public String direccionPtoPartida;
	public String codigoPuerto;
	public String correoEmisor;
	
	private String rutaAdjunto_1;
    private String rutaAdjunto_2;
    private String rutaAdjunto_3;
    private String rutaAdjunto_4;
    private String rutaAdjunto_5;
    private String nombreAdjunto_1;
    private String nombreAdjunto_2;
    private String nombreAdjunto_3;
    private String nombreAdjunto_4;
    private String nombreAdjunto_5;
    private String adjunto_1;
    private String adjunto_2;
    private String adjunto_3;
    private String adjunto_4;
    private String adjunto_5;
    
	public String correoAdquiriente;
	public String inHabilitado;
	
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
	
	@XmlElement(name = "GuiaItem", nillable = true)
	public List<EGuiaItemCliente> items = new ArrayList<EGuiaItemCliente>();
	
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public String getSerieNumeroGuia() {
		return serieNumeroGuia;
	}
	public void setSerieNumeroGuia(String serieNumeroGuia) {
		this.serieNumeroGuia = serieNumeroGuia;
	}
	public Date getFechaEmisionGuia() {
		return fechaEmisionGuia;
	}
	public void setFechaEmisionGuia(Date fechaEmisionGuia) {
		this.fechaEmisionGuia = fechaEmisionGuia;
	}
	public String getTipoDocumentoGuia() {
		return tipoDocumentoGuia;
	}
	public void setTipoDocumentoGuia(String tipoDocumentoGuia) {
		this.tipoDocumentoGuia = tipoDocumentoGuia;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getSerieGuiaBaja() {
		return serieGuiaBaja;
	}
	public void setSerieGuiaBaja(String serieGuiaBaja) {
		this.serieGuiaBaja = serieGuiaBaja;
	}
	public String getCodigoGuiaBaja() {
		return codigoGuiaBaja;
	}
	public void setCodigoGuiaBaja(String codigoGuiaBaja) {
		this.codigoGuiaBaja = codigoGuiaBaja;
	}
	public String getTipoGuiaBaja() {
		return tipoGuiaBaja;
	}
	public void setTipoGuiaBaja(String tipoGuiaBaja) {
		this.tipoGuiaBaja = tipoGuiaBaja;
	}
	public String getNumeroDocumentoRelacionado() {
		return numeroDocumentoRelacionado;
	}
	public void setNumeroDocumentoRelacionado(String numeroDocumentoRelacionado) {
		this.numeroDocumentoRelacionado = numeroDocumentoRelacionado;
	}
	public String getCodigoDocumentoRelacionado() {
		return codigoDocumentoRelacionado;
	}
	public void setCodigoDocumentoRelacionado(String codigoDocumentoRelacionado) {
		this.codigoDocumentoRelacionado = codigoDocumentoRelacionado;
	}
	public String getNumeroDocumentoRemitente() {
		return numeroDocumentoRemitente;
	}
	public void setNumeroDocumentoRemitente(String numeroDocumentoRemitente) {
		this.numeroDocumentoRemitente = numeroDocumentoRemitente;
	}
	public String getTipoDocumentoRemitente() {
		return tipoDocumentoRemitente;
	}
	public void setTipoDocumentoRemitente(String tipoDocumentoRemitente) {
		this.tipoDocumentoRemitente = tipoDocumentoRemitente;
	}
	public String getRazonSocialRemitente() {
		return razonSocialRemitente;
	}
	public void setRazonSocialRemitente(String razonSocialRemitente) {
		this.razonSocialRemitente = razonSocialRemitente;
	}
	public String getNumeroDocumentoDestinatario() {
		return numeroDocumentoDestinatario;
	}
	public void setNumeroDocumentoDestinatario(String numeroDocumentoDestinatario) {
		this.numeroDocumentoDestinatario = numeroDocumentoDestinatario;
	}
	public String getTipoDocumentoDestinatario() {
		return tipoDocumentoDestinatario;
	}
	public void setTipoDocumentoDestinatario(String tipoDocumentoDestinatario) {
		this.tipoDocumentoDestinatario = tipoDocumentoDestinatario;
	}
	public String getRazonSocialDestinatario() {
		return razonSocialDestinatario;
	}
	public void setRazonSocialDestinatario(String razonSocialDestinatario) {
		this.razonSocialDestinatario = razonSocialDestinatario;
	}
	public String getNumeroDocumentoEstablecimiento() {
		return numeroDocumentoEstablecimiento;
	}
	public void setNumeroDocumentoEstablecimiento(
			String numeroDocumentoEstablecimiento) {
		this.numeroDocumentoEstablecimiento = numeroDocumentoEstablecimiento;
	}
	public String getTipoDocumentoEstablecimiento() {
		return tipoDocumentoEstablecimiento;
	}
	public void setTipoDocumentoEstablecimiento(String tipoDocumentoEstablecimiento) {
		this.tipoDocumentoEstablecimiento = tipoDocumentoEstablecimiento;
	}
	public String getRazonSocialEstablecimiento() {
		return razonSocialEstablecimiento;
	}
	public void setRazonSocialEstablecimiento(String razonSocialEstablecimiento) {
		this.razonSocialEstablecimiento = razonSocialEstablecimiento;
	}
	public String getMotivoTraslado() {
		return motivoTraslado;
	}
	public void setMotivoTraslado(String motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}
	public String getDescripcionMotivoTraslado() {
		return descripcionMotivoTraslado;
	}
	public void setDescripcionMotivoTraslado(String descripcionMotivoTraslado) {
		this.descripcionMotivoTraslado = descripcionMotivoTraslado;
	}
	public boolean isIndTransbordoProgramado() {
		return indTransbordoProgramado;
	}
	public void setIndTransbordoProgramado(boolean indTransbordoProgramado) {
		this.indTransbordoProgramado = indTransbordoProgramado;
	}
	public BigDecimal getPesoBrutoTotalBienes() {
		return pesoBrutoTotalBienes;
	}
	public void setPesoBrutoTotalBienes(BigDecimal pesoBrutoTotalBienes) {
		this.pesoBrutoTotalBienes = pesoBrutoTotalBienes;
	}
	public String getUnidadMedidaPesoBruto() {
		return unidadMedidaPesoBruto;
	}
	public void setUnidadMedidaPesoBruto(String unidadMedidaPesoBruto) {
		this.unidadMedidaPesoBruto = unidadMedidaPesoBruto;
	}
	public BigDecimal getNumeroBultos() {
		return numeroBultos;
	}
	public void setNumeroBultos(BigDecimal numeroBultos) {
		this.numeroBultos = numeroBultos;
	}
	public String getModalidadTraslado() {
		return modalidadTraslado;
	}
	public void setModalidadTraslado(String modalidadTraslado) {
		this.modalidadTraslado = modalidadTraslado;
	}
	public Date getFechaInicioTraslado() {
		return fechaInicioTraslado;
	}
	public void setFechaInicioTraslado(Date fechaInicioTraslado) {
		this.fechaInicioTraslado = fechaInicioTraslado;
	}
	public String getNumeroRucTransportista() {
		return numeroRucTransportista;
	}
	public void setNumeroRucTransportista(String numeroRucTransportista) {
		this.numeroRucTransportista = numeroRucTransportista;
	}
	public String getTipoDocumentoTransportista() {
		return tipoDocumentoTransportista;
	}
	public void setTipoDocumentoTransportista(String tipoDocumentoTransportista) {
		this.tipoDocumentoTransportista = tipoDocumentoTransportista;
	}
	public String getRazonSocialTransportista() {
		return razonSocialTransportista;
	}
	public void setRazonSocialTransportista(String razonSocialTransportista) {
		this.razonSocialTransportista = razonSocialTransportista;
	}
	public String getNumeroPlacaVehiculo() {
		return numeroPlacaVehiculo;
	}
	public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
		this.numeroPlacaVehiculo = numeroPlacaVehiculo;
	}
	public String getNumeroDocumentoConductor() {
		return numeroDocumentoConductor;
	}
	public void setNumeroDocumentoConductor(String numeroDocumentoConductor) {
		this.numeroDocumentoConductor = numeroDocumentoConductor;
	}
	public String getTipoDocumentoConductor() {
		return tipoDocumentoConductor;
	}
	public void setTipoDocumentoConductor(String tipoDocumentoConductor) {
		this.tipoDocumentoConductor = tipoDocumentoConductor;
	}
	public String getUbigeoPtoLLegada() {
		return ubigeoPtoLLegada;
	}
	public void setUbigeoPtoLLegada(String ubigeoPtoLLegada) {
		this.ubigeoPtoLLegada = ubigeoPtoLLegada;
	}
	public String getDireccionPtoLLegada() {
		return direccionPtoLLegada;
	}
	public void setDireccionPtoLLegada(String direccionPtoLLegada) {
		this.direccionPtoLLegada = direccionPtoLLegada;
	}
	public String getNumeroContenedor() {
		return numeroContenedor;
	}
	public void setNumeroContenedor(String numeroContenedor) {
		this.numeroContenedor = numeroContenedor;
	}
	public String getUbigeoPtoPartida() {
		return ubigeoPtoPartida;
	}
	public void setUbigeoPtoPartida(String ubigeoPtoPartida) {
		this.ubigeoPtoPartida = ubigeoPtoPartida;
	}
	public String getDireccionPtoPartida() {
		return direccionPtoPartida;
	}
	public void setDireccionPtoPartida(String direccionPtoPartida) {
		this.direccionPtoPartida = direccionPtoPartida;
	}
	public String getCodigoPuerto() {
		return codigoPuerto;
	}
	public void setCodigoPuerto(String codigoPuerto) {
		this.codigoPuerto = codigoPuerto;
	}
	public List<EGuiaItemCliente> getItems() {
		return items;
	}
	public void setItems(List<EGuiaItemCliente> items) {
		this.items = items;
	}
	public String getCorreoEmisor() {
		return correoEmisor;
	}
	
	public String getRutaAdjunto_1() {
		return rutaAdjunto_1;
	}

	public void setRutaAdjunto_1(String rutaAdjunto_1) {
		this.rutaAdjunto_1 = rutaAdjunto_1;
	}

	public String getRutaAdjunto_2() {
		return rutaAdjunto_2;
	}

	public void setRutaAdjunto_2(String rutaAdjunto_2) {
		this.rutaAdjunto_2 = rutaAdjunto_2;
	}

	public String getRutaAdjunto_3() {
		return rutaAdjunto_3;
	}

	public void setRutaAdjunto_3(String rutaAdjunto_3) {
		this.rutaAdjunto_3 = rutaAdjunto_3;
	}

	public String getRutaAdjunto_4() {
		return rutaAdjunto_4;
	}

	public void setRutaAdjunto_4(String rutaAdjunto_4) {
		this.rutaAdjunto_4 = rutaAdjunto_4;
	}

	public String getRutaAdjunto_5() {
		return rutaAdjunto_5;
	}

	public void setRutaAdjunto_5(String rutaAdjunto_5) {
		this.rutaAdjunto_5 = rutaAdjunto_5;
	}
	
	public String getNombreAdjunto_1() {
		return nombreAdjunto_1;
	}

	public void setNombreAdjunto_1(String nombreAdjunto_1) {
		this.nombreAdjunto_1 = nombreAdjunto_1;
	}

	public String getNombreAdjunto_2() {
		return nombreAdjunto_2;
	}

	public void setNombreAdjunto_2(String nombreAdjunto_2) {
		this.nombreAdjunto_2 = nombreAdjunto_2;
	}

	public String getNombreAdjunto_3() {
		return nombreAdjunto_3;
	}

	public void setNombreAdjunto_3(String nombreAdjunto_3) {
		this.nombreAdjunto_3 = nombreAdjunto_3;
	}

	public String getNombreAdjunto_4() {
		return nombreAdjunto_4;
	}

	public void setNombreAdjunto_4(String nombreAdjunto_4) {
		this.nombreAdjunto_4 = nombreAdjunto_4;
	}

	public String getNombreAdjunto_5() {
		return nombreAdjunto_5;
	}

	public void setNombreAdjunto_5(String nombreAdjunto_5) {
		this.nombreAdjunto_5 = nombreAdjunto_5;
	}

	public String getAdjunto_1() {
		return adjunto_1;
	}

	public void setAdjunto_1(String adjunto_1) {
		this.adjunto_1 = adjunto_1;
	}

	public String getAdjunto_2() {
		return adjunto_2;
	}

	public void setAdjunto_2(String adjunto_2) {
		this.adjunto_2 = adjunto_2;
	}

	public String getAdjunto_3() {
		return adjunto_3;
	}

	public void setAdjunto_3(String adjunto_3) {
		this.adjunto_3 = adjunto_3;
	}

	public String getAdjunto_4() {
		return adjunto_4;
	}

	public void setAdjunto_4(String adjunto_4) {
		this.adjunto_4 = adjunto_4;
	}

	public String getAdjunto_5() {
		return adjunto_5;
	}

	public void setAdjunto_5(String adjunto_5) {
		this.adjunto_5 = adjunto_5;
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
	public void setInHabilitado(String inHabilitado2) {
		this.inHabilitado = inHabilitado2;
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
