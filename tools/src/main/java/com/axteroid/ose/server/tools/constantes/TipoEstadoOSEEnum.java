package com.axteroid.ose.server.tools.constantes;

public enum TipoEstadoOSEEnum {
	CONTENT_FALSE("1","CPE CONTENT FALSE"),
	CONTENT_TRUE("0","CPE CONTENT TRUE"),
	CONTENT_PROCESO("98","CPE CONTENT PROCESO"),
	CONTENT_REPETIDO("3","CPE CONTENT REPETIDO"),
	CONTENT_ERROR_DB("4","CPE CONTENT ERROR_DB"),
	CONTENT_PROCESO_SUNAT("0098","CPE CONTENT_PROCESO_SUNAT");
	
	private String codigo;
	private String descripcion;
	
	private TipoEstadoOSEEnum(String codigo, String descripcion){
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
