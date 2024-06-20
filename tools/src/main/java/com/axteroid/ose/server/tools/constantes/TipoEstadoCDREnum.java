package com.axteroid.ose.server.tools.constantes;

public enum TipoEstadoCDREnum {
	CDR_PROCESO("10","SUNAT CDR PROCESO"),
	CDR_ERRADO("20","SUNAT CDR ERRADO"),
	CDR_GENERADO("30","SUNAT CDR GENERADO"),
	CDR_ENVIADO("40","SUNAT CDR ENVIADO"),
	CDR_SERVICIO_INHABILITADO("50","SUNAT CDR SERVICIO INHABILITADO"),
	CDR_ERROR("60","SUNAT CDR ERROR"),
	CDR_SIN_RETORNO("70","SUNAT CDR SIN RETORNO"),
	CDR_RECHAZADO("80","SUNAT CDR RECHAZADO"),
	CDR_AUTORIZADO("90","SUNAT CDR AUTORIZADO");
	
	private String codigo;
	private String descripcion;
	
	private TipoEstadoCDREnum(String codigo, String descripcion){
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
