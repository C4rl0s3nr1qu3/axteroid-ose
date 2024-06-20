package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class DataQuery {
	
    private String id;
    private String idComprobante;
    private String rucEmisor;
    private String tipoComprobante;
    private String serie;
    private String numeroCorrelativo;
    private String nombre;
    private String estado;
    private String errorComprobante;
    private String errorNumero;    
    private String respuestaSunat;    
    private String descripcion;
    private String year;
    private String mes;
    private String dia;    
    private String hora;    
    private String minuto;
    private String filas = "0";
    private String ticket;
    private String url;
    private String repeat;
    private List<String> collection;
  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdComprobante() {
		return idComprobante;
	}
	public void setIdComprobante(String idComprobante) {
		this.idComprobante = idComprobante;
	}
	public String getRucEmisor() {
		return rucEmisor;
	}
	public void setRucEmisor(String rucEmisor) {
		this.rucEmisor = rucEmisor;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getErrorComprobante() {
		return errorComprobante;
	}
	public void setErrorComprobante(String errorComprobante) {
		this.errorComprobante = errorComprobante;
	}
	public String getErrorNumero() {
		return errorNumero;
	}
	public void setErrorNumero(String errorNumero) {
		this.errorNumero = errorNumero;
	}
	public String getRespuestaSunat() {
		return respuestaSunat;
	}
	public void setRespuestaSunat(String respuestaSunat) {
		this.respuestaSunat = respuestaSunat;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getMinuto() {
		return minuto;
	}
	public void setMinuto(String minuto) {
		this.minuto = minuto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFilas() {
		return filas;
	}
	public void setFilas(String filas) {
		this.filas = filas;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public List<String> getCollection() {
		return collection;
	}
	public void setCollection(List<String> collection) {
		this.collection = collection;
	}

}
