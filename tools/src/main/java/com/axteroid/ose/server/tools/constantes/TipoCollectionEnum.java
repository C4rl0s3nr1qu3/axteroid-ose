package com.axteroid.ose.server.tools.constantes;

public enum TipoCollectionEnum {
	TD01("01","TT_OSE_INVOICE_RESPONSE"),
	TD03("03","TT_OSE_VOUCHER_RESPONSE"),
	TD04("04","TT_OSE_ERROR"),
	TD07("07","TT_OSE_CREDIT_NOTE_RESPONSE"),
	TD08("08","TT_OSE_DEBIT_NOTE_RESPONSE"),
	TD09("09","TT_OSE_DESPATCH_ADVICE_RESPONSE"),
	TD20("20","TT_OSE_RETENTION_RESPONSE"),
	TD30("30","TT_OSE_DAE_RESPONSE"),
	TD34("34","TT_OSE_ERROR"),
	TD40("40","TT_OSE_PERCEPTION_RESPONSE"),
	TD42("42","TT_OSE_ERROR");
	
	private String codigo;
	private String descripcion;
	
	private TipoCollectionEnum(String codigo, String descripcion){
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
