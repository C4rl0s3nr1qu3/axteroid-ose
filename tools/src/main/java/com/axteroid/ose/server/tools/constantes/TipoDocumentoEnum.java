package com.axteroid.ose.server.tools.constantes;

public enum TipoDocumentoEnum {
	TD01("01","Factura"),
	TD03("03","Boleta de venta"),
	TD04("04","Liquidación de compra"),
	TD07("07","Nota de crédito"),
	TD08("08","Nota de debito"),
	TD09("09","Guía de remisión remitente"),
	TD20("20","Comprobante de retención"),
	TD30("30","DAE rol adquirente en los sistemas de pago"),
	TD31("31","Guía de remisión transportista"),
	TD32("32","DAE recaudadoras de la Garantía de Red Principal"),
	TD34("34","DAE operador"),
	TD40("40","Comprobante de percepción"),
	TD42("42","DAE adquiriente pago por tarj. de crédito");
	
	private String codigo;
	private String descripcion;
	
	private TipoDocumentoEnum(String codigo, String descripcion){
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
