package com.axteroid.ose.server.tools.constantes;

public enum TipoListaSUNATEnum {
	ASOCIADOS(1,"asociados_"+Constantes.CDR_RUC_OSE,1,1, "TS_CONTRIBUYENTE_ASOCIADO_EMISOR"),
	CERTIFICADOS(2,"certificados_"+Constantes.CDR_RUC_OSE,1,1, "TS_CERTIFICADO_EMISOR"),
	CPE(3,"cpe_"+Constantes.CDR_RUC_OSE,1,1, "TS_COMPROBANTES_PAGO_ELECTRONICOS"),
	CPF(4,"autorizacion_cpf_"+Constantes.CDR_RUC_OSE,1,1, "TS_AUTORIZACION_COMPROB_PAGO_FISICO"),	
	PARAMETROS(5,"parametros",2,3, "TS_PARAMETRO"),
	PADRONES(6,"padrones",2,3, "TS_PADRON_CONTRIBUYENTE"),
	CONTRIBUYENTES(7,"contribuyentes",2,3, "TS_CONTRIBUYENTE"),	
	PLAZEXCEP(8,"plazexcep_"+Constantes.CDR_RUC_OSE,1,0, "TS_PLAZOS_EXCEPCIONALES"),
	CONTINGENCIA(9,"plazexcep_"+Constantes.CDR_RUC_OSE,1,0, "TS_AUTORIZACION_RANGOS_CONTINGENCIA"),
	ESTABLECIMIENTOS(10,"establecimientos_"+Constantes.CDR_RUC_OSE,1,1, "TS_ESTABLECIMIENTOS_ANEXOS"),
	CUADRE(20,"cuadre"+Constantes.CDR_RUC_OSE,1,4, "TS_CUADRE_DIARIO_RESUMEN"),
	CUADREDETALLE(21,"cuadredetalle_"+Constantes.CDR_RUC_OSE,1,4, "TS_CUADRE_DIARIO_DETALLE"),
	HOMOLOGA(22,"homologa_"+Constantes.CDR_RUC_OSE,2,3, "TS_HOMOLOGA"),
	USUARIOS(31,"usuarios",1,2, "");
	
	private Integer codigo;
	private String descripcion;
	private Integer ruc;
	private Integer tipoLista;
	private String tabla;
	
	private TipoListaSUNATEnum(Integer codigo, String descripcion, Integer ruc,Integer tipoLista, String tabla){
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.ruc = ruc;
		this.tipoLista = tipoLista;
		this.tabla = tabla;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getRuc() {
		return ruc;
	}

	public void setRuc(Integer ruc) {
		this.ruc = ruc;
	}

	public Integer getTipoLista() {
		return tipoLista;
	}

	public void setTipoLista(Integer tipoLista) {
		this.tipoLista = tipoLista;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
}
