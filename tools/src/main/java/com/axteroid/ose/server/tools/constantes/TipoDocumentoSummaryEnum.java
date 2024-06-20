package com.axteroid.ose.server.tools.constantes;

public enum TipoDocumentoSummaryEnum {
	TDRC("RC","Resumen diario de Boletas"),
	TDRA("RA","Comunicación de Bajas"),
	TDRR("RR","Resumen de Reversión (para CRE y CPE)");
	
	private String codigo;
	private String descripcion;
	
	private TipoDocumentoSummaryEnum(String codigo, String descripcion){
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
