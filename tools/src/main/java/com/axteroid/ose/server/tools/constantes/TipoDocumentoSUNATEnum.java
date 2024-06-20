package com.axteroid.ose.server.tools.constantes;

public enum TipoDocumentoSUNATEnum {
	FACTURA("01","Factura"),
	BOLETA("03","Boleta de venta"),
	NOTA_CREDITO("07","Nota de crédito"),
	NOTA_DEBITO("08","Nota de debito"),
	GUIA_REMISION("09","Guía de Remisión Remitente"),
	TICKET("12","Ticket"),
	DOCU_CONTROL_SBS("13","Documento Control SBS"),
	RECIBO_SERV_PUBL("14","Servicio publico"),
	RETENCION("20","Comprobante de retención"),
	DAE_ROL_ADQUIRIENTE_SISTEMAS_PAGO("30","DAE ROL ADQUIRIENTE SISTEMAS PAGO"),
	DAE_OPERADOR("34","DAE OPERADOR"),
	DAE_ADQUIRIENTE_SISTEMAS_PAGO("42","DAE ADQUIRIENTE SISTEMAS PAGO"),
	PERCEPCION("40","Comprobante de percepción"),
	COMUNICACION_BAJAS("RA","Comunicación de baja"),
	RESUMEN_DIARIO("RC","Resumen diario"),
	REVERSION("RR","Resumen reversión");
	
	private String codigo;
	private String descripcion;
	
	private TipoDocumentoSUNATEnum(String codigo, String descripcion){
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
