package com.axteroid.ose.server.tools.constantes;

public enum TipoTributosEnum {
	IGV("1000","IGV Impuesto General a las Ventas","IGV"),
	IVAP("1016","Impuesto a la Venta Arroz Pilado","IVAP"),
	ISC("2000","ISC Impuesto Selectivo al Consumo","ISC"),
	EXP("9995","Exportación","EXP"),
	GRA("9996","Gratuito","GRA"),
	EXO("9998","Exonerado","EXO"),
	INA("9996","Inafecto","INA"),
	OTROS("9998","Otros tributos","OTROS");
	
	private String codigo;
	private String descripcion;
	private String nombre;
	
	private TipoTributosEnum(String codigo, String descripcion, String nombre){
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.nombre = nombre;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
