package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class BeanResponse {

	private String mensaje;
	private List<String> advertencias;
	private boolean error = false;

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public List<String> getAdvertencias() {
		return advertencias;
	}

	public void setAdvertencias(List<String> advertencias) {
		this.advertencias = advertencias;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
