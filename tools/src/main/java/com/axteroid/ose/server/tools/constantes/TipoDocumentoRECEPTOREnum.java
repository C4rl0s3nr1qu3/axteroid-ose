package com.axteroid.ose.server.tools.constantes;

public enum TipoDocumentoRECEPTOREnum {
	DOC_TRIB_NO_DOM_SIN_RUC("0","DOC.TRIB.NO.DOM.SIN.RUC"),
	DOC_NACIONAL_DE_IDENTIDAD("1","DOC. NACIONAL DE IDENTIDAD"),
	CARNET_DE_EXTRANJERIA("4","CARNET DE EXTRANJERIA"),
	REG_UNICO_DE_CONTRIBUYENTES("6","REG.UNICO DE CONTRIBUYENTES"),
	PASAPORTE("7","PASAPORTE"),
	CED_DIPLOMATICA_DE_IDENTIDAD("A","CED_DIPLOMATICA_DE_IDENTIDAD");
	
	private String codigo;
	private String descripcion;
	
	private TipoDocumentoRECEPTOREnum(String codigo, String descripcion){
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
	
	public TipoDocumentoSUNATEnum[] getValues() {
        return TipoDocumentoSUNATEnum.values();
    }
}
