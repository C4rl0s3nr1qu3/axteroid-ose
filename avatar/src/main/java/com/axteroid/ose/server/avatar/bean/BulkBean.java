package com.axteroid.ose.server.avatar.bean;

import java.io.Serializable;

public class BulkBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String rutaArchivoAMigrar;
	private String delimitador;
	private String rowTerminator;
	private char bdFg;
	
	public String getRutaArchivoAMigrar() {
		return rutaArchivoAMigrar;
	}
	public void setRutaArchivoAMigrar(String rutaArchivoAMigrar) {
		this.rutaArchivoAMigrar = rutaArchivoAMigrar;
	}
	public String getDelimitador() {
		return delimitador;
	}
	public void setDelimitador(String delimitador) {
		this.delimitador = delimitador;
	}
	public String getRowTerminator() {
		return rowTerminator;
	}
	public void setRowTerminator(String rowTerminator) {
		this.rowTerminator = rowTerminator;
	}
	public char getBdFg() {
		return bdFg;
	}
	public void setBdFg(char bdFg) {
		this.bdFg = bdFg;
	}
	
}
