package com.axteroid.ose.server.tools.bean;

public class SunatBeanResponse {
	private String success;
	private String message;	
	private String errorCode;
	private String ticket;
	private String cdr;
	private String ubl;
	private String fecRecepcion;
	private Data data;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getCdr() {
		return cdr;
	}

	public void setCdr(String cdr) {
		this.cdr = cdr;
	}

	public String getUbl() {
		return ubl;
	}

	public void setUbl(String ubl) {
		this.ubl = ubl;
	}

	public String getFecRecepcion() {
		return fecRecepcion;
	}

	public void setFecRecepcion(String fecRecepcion) {
		this.fecRecepcion = fecRecepcion;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data {
		private String estadoCp;
		private String estadoRuc;
		private String condDomiRuc;
		private String Observaciones;
		
		public String getEstadoCp() {
			return estadoCp;
		}
		public void setEstadoCp(String estadoCp) {
			this.estadoCp = estadoCp;
		}
		public String getEstadoRuc() {
			return estadoRuc;
		}
		public void setEstadoRuc(String estadoRuc) {
			this.estadoRuc = estadoRuc;
		}
		public String getCondDomiRuc() {
			return condDomiRuc;
		}
		public void setCondDomiRuc(String condDomiRuc) {
			this.condDomiRuc = condDomiRuc;
		}
		public String getObservaciones() {
			return Observaciones;
		}
		public void setObservaciones(String observaciones) {
			Observaciones = observaciones;
		}
	}	
}
