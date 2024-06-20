package com.axteroid.ose.server.ubl20.config;

public enum ConfiguracionEnum {
	C01("01","ZZZ"),
	C02("02","YYY");
	
	private String codigo;
	private String descripcion;
	
	private ConfiguracionEnum(String codigo, String descripcion){
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
