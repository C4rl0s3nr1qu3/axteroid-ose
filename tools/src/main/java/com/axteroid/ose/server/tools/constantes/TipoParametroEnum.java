package com.axteroid.ose.server.tools.constantes;

public enum TipoParametroEnum {
	C03("C03","Catalogo 03"),
	ERR("ERR","Catalogo Errores"),
	RUC("RUC","Usuario - RUC"),
	CLI("CLI","Cliente - CLI"),
	SEC("SEC","Usuario - Password"),
	CNF("CNF","Configuracion"),	
	XSD("XSD","UBLPE"),
	XSL("XSL","ValidaExprReg"),
	EST("EST","Estado Cliente"),
	AFI("AFI","Afiliado Cliente");
	
	private String codigo;
	private String descripcion;
	
	private TipoParametroEnum(String codigo, String descripcion){
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
