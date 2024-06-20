package com.axteroid.ose.server.tools.bean;

public class ErrorBean {
	 @SuppressWarnings("unused")
	private static final long serialVersionUID = -4218201282764465777L;
	 
    private String numero;
    private String descripcion ;

    public ErrorBean(){}
    
    public ErrorBean(String numero, String descripcion) {
        this.numero = numero;
        this.descripcion = descripcion;

    }

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


}
