
package com.axteroid.ose.server.tools.constantes;


public enum EstadoRegType {
	ACTIVO("1","Activo"),
	INACTIVO("0","Inactivo");
	
	private String codigo;
	private String descripcion;
	
	private EstadoRegType(String codigo, String descripcion){
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