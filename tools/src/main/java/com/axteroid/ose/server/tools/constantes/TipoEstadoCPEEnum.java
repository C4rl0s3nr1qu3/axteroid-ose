package com.axteroid.ose.server.tools.constantes;

public enum TipoEstadoCPEEnum {
	RECHAZADO("0","Rechazado"),
	ACEPTADO("1","Aceptado"),
	ANULADO("2","Anulado"),
	ERROR("3","Error");
	
	private String codigo;
	private String descripcion;
	
	private TipoEstadoCPEEnum(String codigo, String descripcion){
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
