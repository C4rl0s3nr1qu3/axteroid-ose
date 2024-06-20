package com.axteroid.ose.server.tools.bean;

public class Archivo {
	private String nomArchivo;
	private String arcGreZip;
	private String hashZip;
	
	public String getNomArchivo() {
		return nomArchivo;
	}
	public void setNomArchivo(String nomArchivo) {
		this.nomArchivo = nomArchivo;
	}
	public String getArcGreZip() {
		return arcGreZip;
	}
	public void setArcGreZip(String arcGreZip) {
		this.arcGreZip = arcGreZip;
	}
	public String getHashZip() {
		return hashZip;
	}
	public void setHashZip(String hashZip) {
		this.hashZip = hashZip;
	}
	@Override
	public String toString() {
		return "Archivo [nomArchivo=" + nomArchivo + ", arcGreZip=" + arcGreZip + ", hashZip=" + hashZip + "]";
	}
}
