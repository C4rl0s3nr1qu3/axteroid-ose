package com.axteroid.ose.server.repository.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class OseValiConsBean {
	@JsonProperty("rucEmisor")
	private Long rucEmisor;
	
	@JsonProperty("tipoComprobante")
	private String tipoComprobante;	
	
	@JsonProperty("tipoDocumentoReceptor")
	private String tipoDocumentoReceptor;	
	
	@JsonProperty("numeroDocumentoReceptor")
	private String numeroDocumentoReceptor;
		
	@JsonProperty("serie")
	private String serie;
	
	@JsonProperty("numeroCorrelativo")
	private String numeroCorrelativo;	

	@JsonProperty("fechaEmision")
	private Date fechaEmision;
	
	@JsonProperty("importeTotal")
	private String importeTotal;

	public Long getRucEmisor() {
		return rucEmisor;
	}

	public void setRucEmisor(Long rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getTipoDocumentoReceptor() {
		return tipoDocumentoReceptor;
	}

	public void setTipoDocumentoReceptor(String tipoDocumentoReceptor) {
		this.tipoDocumentoReceptor = tipoDocumentoReceptor;
	}

	public String getNumeroDocumentoReceptor() {
		return numeroDocumentoReceptor;
	}

	public void setNumeroDocumentoReceptor(String numeroDocumentoReceptor) {
		this.numeroDocumentoReceptor = numeroDocumentoReceptor;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getNumeroCorrelativo() {
		return numeroCorrelativo;
	}

	public void setNumeroCorrelativo(String numeroCorrelativo) {
		this.numeroCorrelativo = numeroCorrelativo;
	}
}
