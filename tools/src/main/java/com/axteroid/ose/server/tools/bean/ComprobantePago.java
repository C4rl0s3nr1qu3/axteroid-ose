package com.axteroid.ose.server.tools.bean;

import java.sql.Timestamp;

public class ComprobantePago {
	
	private String ruc;
	private String tipo;
	private String serie;
	private Long secuencial;
	private Integer estado;
	private Double importe;
	private Timestamp fechaEmision;
	private String moneda;
	private Integer motivoTraslado;
	private Integer modTraslado;
	private Integer indicadorTransbordo;
	private Timestamp fechaInicioTraslado;
	private Integer indicadorFormaPago;
	private Integer indicadorPercepcion;	

	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Timestamp getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Timestamp fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public Integer getMotivoTraslado() {
		return motivoTraslado;
	}
	public void setMotivoTraslado(Integer motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}
	public Integer getModTraslado() {
		return modTraslado;
	}
	public void setModTraslado(Integer modTraslado) {
		this.modTraslado = modTraslado;
	}
	public Integer getIndicadorTransbordo() {
		return indicadorTransbordo;
	}
	public void setIndicadorTransbordo(Integer indicadorTransbordo) {
		this.indicadorTransbordo = indicadorTransbordo;
	}
	public Timestamp getFechaInicioTraslado() {
		return fechaInicioTraslado;
	}
	public void setFechaInicioTraslado(Timestamp fechaInicioTraslado) {
		this.fechaInicioTraslado = fechaInicioTraslado;
	}

	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public Long getSecuencial() {
		return secuencial;
	}
	public void setSecuencial(Long secuencial) {
		this.secuencial = secuencial;
	}
	public Integer getIndicadorFormaPago() {
		return indicadorFormaPago;
	}
	public void setIndicadorFormaPago(Integer indicadorFormaPago) {
		this.indicadorFormaPago = indicadorFormaPago;
	}
	public Integer getIndicadorPercepcion() {
		return indicadorPercepcion;
	}
	public void setIndicadorPercepcion(Integer indicadorPercepcion) {
		this.indicadorPercepcion = indicadorPercepcion;
	}	
	
	
}
