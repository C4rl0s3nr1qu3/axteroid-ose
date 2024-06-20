package com.axteroid.ose.server.repository.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class OseQueryBean {
	
	@JsonProperty("listRucEmisor")
	private List<String> listRucEmisor;
	
	@JsonProperty("numeroDocumentoCliente")
	private String numeroDocumentoCliente;
	
	@JsonProperty("tipoComprobante")
	private String tipoComprobante;
	
	@JsonProperty("estadoOse")
	private String estadoOse;
		
	@JsonProperty("serieNumeroDesde")
	private String serieNumeroDesde;
	
	@JsonProperty("serieNumeroHasta")
	private String serieNumeroHasta;	

	@JsonProperty("fechaEmisionDesde")
	private Date fechaEmisionDesde;
	
	@JsonProperty("fechaEmisionHasta")
	private Date fechaEmisionHasta;

	public List<String> getListRucEmisor() {
		return listRucEmisor;
	}

	public void setListRucEmisor(List<String> listRucEmisor) {
		this.listRucEmisor = listRucEmisor;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getEstadoOse() {
		return estadoOse;
	}

	public void setEstadoOse(String estadoOse) {
		this.estadoOse = estadoOse;
	}

	public String getSerieNumeroDesde() {
		return serieNumeroDesde;
	}

	public void setSerieNumeroDesde(String serieNumeroDesde) {
		this.serieNumeroDesde = serieNumeroDesde;
	}

	public String getSerieNumeroHasta() {
		return serieNumeroHasta;
	}

	public void setSerieNumeroHasta(String serieNumeroHasta) {
		this.serieNumeroHasta = serieNumeroHasta;
	}

	public Date getFechaEmisionDesde() {
		return fechaEmisionDesde;
	}

	public void setFechaEmisionDesde(Date fechaEmisionDesde) {
		this.fechaEmisionDesde = fechaEmisionDesde;
	}

	public Date getFechaEmisionHasta() {
		return fechaEmisionHasta;
	}

	public void setFechaEmisionHasta(Date fechaEmisionHasta) {
		this.fechaEmisionHasta = fechaEmisionHasta;
	}

	public String getNumeroDocumentoCliente() {
		return numeroDocumentoCliente;
	}

	public void setNumeroDocumentoCliente(String numeroDocumentoCliente) {
		this.numeroDocumentoCliente = numeroDocumentoCliente;
	}

}
