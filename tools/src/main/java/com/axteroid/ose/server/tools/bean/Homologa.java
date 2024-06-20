package com.axteroid.ose.server.tools.bean;

public class Homologa {
	
	private String codigo_Respuesta;
	private String estado_Respuesta;
	private String codigo_Esperado;
	private String estado_Esperado;
	private String a_nivel_de_Codigo;
	private String a_nivel_de_Estado;
	private String resultado_Subida;
	private String evaluación_INSI;
	private String e_RazonSocial;
	private String e_RUC;
	private String e_Usuario;
	private String e_Clave;
	private String e_RutaXML;
	private String e_WSDL;
	private String estado_value_out	;
	private String ticket;
	private String e_metodo	;
	private String tipo_de_Documento;
	private String comprobante ;
	
	public Homologa() {};
	
	public Homologa(String comprobante, String codigo_Esperado, String estado_Esperado, String a_nivel_de_Codigo, String a_nivel_de_Estado,
			String resultado_Subida) {
		super();
		this.codigo_Esperado = codigo_Esperado;
		this.estado_Esperado = estado_Esperado;
		this.a_nivel_de_Codigo = a_nivel_de_Codigo;
		this.a_nivel_de_Estado = a_nivel_de_Estado;
		this.resultado_Subida = resultado_Subida;
		this.comprobante = comprobante;
	}	
	
	public String getCodigo_Respuesta() {
		return codigo_Respuesta;
	}
	public void setCodigo_Respuesta(String codigo_Respuesta) {
		this.codigo_Respuesta = codigo_Respuesta;
	}
	public String getEstado_Respuesta() {
		return estado_Respuesta;
	}
	public void setEstado_Respuesta(String estado_Respuesta) {
		this.estado_Respuesta = estado_Respuesta;
	}
	public String getCodigo_Esperado() {
		return codigo_Esperado;
	}
	public void setCodigo_Esperado(String codigo_Esperado) {
		this.codigo_Esperado = codigo_Esperado;
	}
	public String getEstado_Esperado() {
		return estado_Esperado;
	}
	public void setEstado_Esperado(String estado_Esperado) {
		this.estado_Esperado = estado_Esperado;
	}
	public String getA_nivel_de_Codigo() {
		return a_nivel_de_Codigo;
	}
	public void setA_nivel_de_Codigo(String a_nivel_de_Codigo) {
		this.a_nivel_de_Codigo = a_nivel_de_Codigo;
	}
	public String getA_nivel_de_Estado() {
		return a_nivel_de_Estado;
	}
	public void setA_nivel_de_Estado(String a_nivel_de_Estado) {
		this.a_nivel_de_Estado = a_nivel_de_Estado;
	}
	public String getResultado_Subida() {
		return resultado_Subida;
	}
	public void setResultado_Subida(String resultado_Subida) {
		this.resultado_Subida = resultado_Subida;
	}
	public String getEvaluación_INSI() {
		return evaluación_INSI;
	}
	public void setEvaluación_INSI(String evaluación_INSI) {
		this.evaluación_INSI = evaluación_INSI;
	}
	public String getE_RazonSocial() {
		return e_RazonSocial;
	}
	public void setE_RazonSocial(String e_RazonSocial) {
		this.e_RazonSocial = e_RazonSocial;
	}
	public String getE_RUC() {
		return e_RUC;
	}
	public void setE_RUC(String e_RUC) {
		this.e_RUC = e_RUC;
	}
	public String getE_Usuario() {
		return e_Usuario;
	}
	public void setE_Usuario(String e_Usuario) {
		this.e_Usuario = e_Usuario;
	}
	public String getE_Clave() {
		return e_Clave;
	}
	public void setE_Clave(String e_Clave) {
		this.e_Clave = e_Clave;
	}
	public String getE_RutaXML() {
		return e_RutaXML;
	}
	public void setE_RutaXML(String e_RutaXML) {
		this.e_RutaXML = e_RutaXML;
	}
	public String getE_WSDL() {
		return e_WSDL;
	}
	public void setE_WSDL(String e_WSDL) {
		this.e_WSDL = e_WSDL;
	}
	public String getEstado_value_out() {
		return estado_value_out;
	}
	public void setEstado_value_out(String estado_value_out) {
		this.estado_value_out = estado_value_out;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getE_metodo() {
		return e_metodo;
	}
	public void setE_metodo(String e_metodo) {
		this.e_metodo = e_metodo;
	}
	public String getTipo_de_Documento() {
		return tipo_de_Documento;
	}
	public void setTipo_de_Documento(String tipo_de_Documento) {
		this.tipo_de_Documento = tipo_de_Documento;
	}
	public String getComprobante() {
		return comprobante;
	}
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	@Override
	public String toString() {
		return "Homologa [ comprobante=" + comprobante+" | codigo_Esperado=" + codigo_Esperado + " | estado_Esperado=" + estado_Esperado
				+ " | a_nivel_de_Codigo=" + a_nivel_de_Codigo + " | a_nivel_de_Estado=" + a_nivel_de_Estado
				+ " | resultado_Subida=" + resultado_Subida + "]";
	}
	
}
