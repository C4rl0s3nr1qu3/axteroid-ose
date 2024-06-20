package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class SunatBeanResponseGRE {
	private String cod;
	private String msg;
	private String exc;	
	private String numTicket;
	private String fecRecepcion;	
	private String codRespuesta;
	private String arcCdr;
	private String indCdrGenerado;
	private List<Error> errors;
	private Error error;
	
	public String getNumTicket() {
		return numTicket;
	}

	public void setNumTicket(String numTicket) {
		this.numTicket = numTicket;
	}

	public String getFecRecepcion() {
		return fecRecepcion;
	}

	public void setFecRecepcion(String fecRecepcion) {
		this.fecRecepcion = fecRecepcion;
	}

	public String getCodRespuesta() {
		return codRespuesta;
	}

	public void setCodRespuesta(String codRespuesta) {
		this.codRespuesta = codRespuesta;
	}

	public String getArcCdr() {
		return arcCdr;
	}

	public void setArcCdr(String arcCdr) {
		this.arcCdr = arcCdr;
	}

	public String getIndCdrGenerado() {
		return indCdrGenerado;
	}

	public void setIndCdrGenerado(String indCdrGenerado) {
		this.indCdrGenerado = indCdrGenerado;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getExc() {
		return exc;
	}

	public void setExc(String exc) {
		this.exc = exc;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
	public class Error {
		private String numError;
		private String desError;
		private String cod;
		private String msg;
		
		public String getNumError() {
			return numError;
		}
		public void setNumError(String numError) {
			this.numError = numError;
		}
		public String getDesError() {
			return desError;
		}
		public void setDesError(String desError) {
			this.desError = desError;
		}
		public String getCod() {
			return cod;
		}
		public void setCod(String cod) {
			this.cod = cod;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		@Override
		public String toString() {
			return "Error [numError=" + numError + ", desError=" + desError + ", cod=" + cod + ", msg=" + msg + "]";
		}				
		
	}

	@Override
	public String toString() {
		return "SunatBeanResponseGRE [cod=" + cod + ", msg=" + msg + ", exc=" + exc + ", numTicket=" + numTicket
				+ ", fecRecepcion=" + fecRecepcion + ", codRespuesta=" + codRespuesta + ", arcCdr=" + arcCdr
				+ ", indCdrGenerado=" + indCdrGenerado + ", error=" + error+ ", listErrors=" + (errors != null && errors.size()>0 ? errors.toString() : "")+ "]";
	}	
	
	public String toStringRes() {
		return "SunatBeanResponseGRE [cod=" + cod + ", msg=" + msg + ", exc=" + exc + ", numTicket=" + numTicket
				+ ", fecRecepcion=" + fecRecepcion + ", codRespuesta=" + codRespuesta 
				+ ", indCdrGenerado=" + indCdrGenerado + ", error=" + error+ ", listErrors=" + (errors != null && errors.size()>0 ? errors.toString() : "S/D")+ "]";
	}	
}
