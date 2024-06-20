package com.axteroid.ose.server.tools.constantes;

public enum TipoDocumentoOSEEnum {
	FACTURA("01","Factura Electrónica"),
	BOLETA("03","Boleta de Venta Electrónica"),
	NOTA_CREDITO_01("0701","Nota de Crédito Electrónica – Factura"),
	NOTA_DEBITO_01("0801","Nota de Debito Electrónica – Factura"),
	NOTA_CREDITO_03("0703","Nota de Crédito Electrónica – Boleta de Venta"),
	NOTA_DEBITO_03("0803","Nota de Debito Electrónica – Boleta de Venta"),
	GUIA_REMISION("09","Guía de Remisión"),
	RETENCION("20","Comprobante de retención"),
	PERCEPCION("40","Comprobante de percepción"),
	RESUMEN_DIARIO_03("RC03","Envío Resumen - Boleta de Venta"),
	RESUMEN_DIARIO_07("RC07","Envío Resumen - Nota de Crédito"),
	RESUMEN_DIARIO_08("RC08","Envío Resumen - Nota de Debito"),
	REVERSION("RR","Resumen reversión"),
	DAE("DAE","DAE");
	
	private String codigo;
	private String descripcion;
	
	private TipoDocumentoOSEEnum(String codigo, String descripcion){
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
