package com.axteroid.ose.server.repository.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class OseResponseBean {
	
	@JsonProperty("ID")
	private Long id;
	
	@JsonProperty("ID_COMPROBANTE")
	private String idComprobante;
	    
	@JsonProperty("RUC_EMISOR")
	private long rucEmisor;
		
	@JsonProperty("TIPO_COMPROBANTE")
	private String tipoComprobante;
	
	@JsonProperty("ESTADO")
	private String estado;
		
	@JsonProperty("SERIE")
    private String serie;
	
	@JsonProperty("NUMERO_CORRELATIVO")
    private String numeroCorrelativo;

	@JsonProperty("FECHA_EMISION")
	//private Date fechaEmision;
	private String fechaEmision;
	
	@JsonProperty("TIPO_DOCUMENTO_EMISOR")
	private String tipoDocumentoEmisor;
	
	@JsonProperty("RAZON_SOCIAL_EMISOR")
	private String razonSocialEmisor;
	
	@JsonProperty("DIRECCION_EMISOR")
	private String direccionEmisor;

	@JsonProperty("NUMERO_DOCUMENTO_CLIENTE")
	private String numeroDocumentoCliente;
	
	@JsonProperty("TIPO_DOCUMENTO_CLIENTE")
	private String tipoDocumentoCliente;
	
	@JsonProperty("RAZON_SOCIAL_CLIENTE")
	private String razonSocialCliente;
	
	@JsonProperty("DIRECCION_CLIENTE")
	private String direccionCliente;
	
	@JsonProperty("TIPO_MONEDA")
	private String tipoMoneda;
	
	@JsonProperty("FECHA_VENCIMIENTO")
	private String fechaVencimiento;
	
	@JsonProperty("OP_GRAVADA")
	private String opGravada;
	
	@JsonProperty("IGV")
	private String igv;
	
	@JsonProperty("OP_INAFECTA")
	private String opInafecta;
	
	@JsonProperty("OP_EXONERADA")
	private String opExonerada;
	
	@JsonProperty("OP_EXPORTACION")
	private String opExportacion;
	
	@JsonProperty("ISC")
	private String isc;
	
	@JsonProperty("DESCUENTOS_GLOBALES_AFECTOS")
	private String descuentosGlobalesAfectos;
	
	@JsonProperty("CARGOS_GLOBALES_AFECTOS")
	private String cargosGlobalesAfectos;
	
	@JsonProperty("IMPORTE_TOTAL")
	private String importeTotal;	
	
	@JsonProperty("DESCUENTOS_GLOBALES_NO_AFECTOS")
	private String descuentosGlobalesNoAfectos;
	
	@JsonProperty("CARGOS_GLOBALES_NO_AFECTOS")
	private String cargosGlobalesNoAfectos;
	
	@JsonProperty("PDF")
	private byte[] pdf;
	
	@JsonProperty("UBL")
	private byte[] ubl;
	
	@JsonProperty("CDR")
	private byte[] cdr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdComprobante() {
		return idComprobante;
	}

	public void setIdComprobante(String idComprobante) {
		this.idComprobante = idComprobante;
	}

	public long getRucEmisor() {
		return rucEmisor;
	}

	public void setRucEmisor(long rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumeroCorrelativo() {
		return numeroCorrelativo;
	}

	public void setNumeroCorrelativo(String numeroCorrelativo) {
		this.numeroCorrelativo = numeroCorrelativo;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getTipoDocumentoEmisor() {
		return tipoDocumentoEmisor;
	}

	public void setTipoDocumentoEmisor(String tipoDocumentoEmisor) {
		this.tipoDocumentoEmisor = tipoDocumentoEmisor;
	}

	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}

	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}

	public String getDireccionEmisor() {
		return direccionEmisor;
	}

	public void setDireccionEmisor(String direccionEmisor) {
		this.direccionEmisor = direccionEmisor;
	}

	public String getNumeroDocumentoCliente() {
		return numeroDocumentoCliente;
	}

	public void setNumeroDocumentoCliente(String numeroDocumentoCliente) {
		this.numeroDocumentoCliente = numeroDocumentoCliente;
	}

	public String getTipoDocumentoCliente() {
		return tipoDocumentoCliente;
	}

	public void setTipoDocumentoCliente(String tipoDocumentoCliente) {
		this.tipoDocumentoCliente = tipoDocumentoCliente;
	}

	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	public String getDireccionCliente() {
		return direccionCliente;
	}

	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getOpGravada() {
		return opGravada;
	}

	public void setOpGravada(String opGravada) {
		this.opGravada = opGravada;
	}

	public String getIgv() {
		return igv;
	}

	public void setIgv(String igv) {
		this.igv = igv;
	}

	public String getOpInafecta() {
		return opInafecta;
	}

	public void setOpInafecta(String opInafecta) {
		this.opInafecta = opInafecta;
	}

	public String getOpExonerada() {
		return opExonerada;
	}

	public void setOpExonerada(String opExonerada) {
		this.opExonerada = opExonerada;
	}

	public String getOpExportacion() {
		return opExportacion;
	}

	public void setOpExportacion(String opExportacion) {
		this.opExportacion = opExportacion;
	}

	public String getIsc() {
		return isc;
	}

	public void setIsc(String isc) {
		this.isc = isc;
	}

	public String getDescuentosGlobalesAfectos() {
		return descuentosGlobalesAfectos;
	}

	public void setDescuentosGlobalesAfectos(String descuentosGlobalesAfectos) {
		this.descuentosGlobalesAfectos = descuentosGlobalesAfectos;
	}

	public String getCargosGlobalesAfectos() {
		return cargosGlobalesAfectos;
	}

	public void setCargosGlobalesAfectos(String cargosGlobalesAfectos) {
		this.cargosGlobalesAfectos = cargosGlobalesAfectos;
	}

	public String getDescuentosGlobalesNoAfectos() {
		return descuentosGlobalesNoAfectos;
	}

	public void setDescuentosGlobalesNoAfectos(String descuentosGlobalesNoAfectos) {
		this.descuentosGlobalesNoAfectos = descuentosGlobalesNoAfectos;
	}

	public String getCargosGlobalesNoAfectos() {
		return cargosGlobalesNoAfectos;
	}

	public void setCargosGlobalesNoAfectos(String cargosGlobalesNoAfectos) {
		this.cargosGlobalesNoAfectos = cargosGlobalesNoAfectos;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

	public byte[] getUbl() {
		return ubl;
	}

	public void setUbl(byte[] ubl) {
		this.ubl = ubl;
	}

	public byte[] getCdr() {
		return cdr;
	}

	public void setCdr(byte[] cdr) {
		this.cdr = cdr;
	}

	public String getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}
}
