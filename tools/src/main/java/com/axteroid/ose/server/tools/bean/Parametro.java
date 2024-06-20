package com.axteroid.ose.server.tools.bean;

public class Parametro {


	private String codigoParametro;
	private String codigoArgumento;	
	private String descripcion;

	
	public Parametro() {
		super();
	}

	public Parametro(String codigoParametro, String codigoArgumento) {
		super();
		this.codigoParametro = codigoParametro;
		this.codigoArgumento = codigoArgumento;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getCodigoParametro() {
		return codigoParametro;
	}
	public void setCodigoParametro(String codigoParametro) {
		this.codigoParametro = codigoParametro;
	}
	public String getCodigoArgumento() {
		return codigoArgumento;
	}
	public void setCodigoArgumento(String codigoArgumento) {
		this.codigoArgumento = codigoArgumento;
	}
	
	
	
}
