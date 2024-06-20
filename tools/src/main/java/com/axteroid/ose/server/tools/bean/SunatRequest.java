package com.axteroid.ose.server.tools.bean;

public class SunatRequest {
	
	private String numRuc;
	private String codComp;
	private String numeroSerie;
	private String numero;
	private String fechaEmision;
	private String monto;
	private byte[] content;
	private String filename;
    private String hash;
    private String contentString;
	
	public String getNumRuc() {
		return numRuc;
	}

	public void setNumRuc(String numRuc) {
		this.numRuc = numRuc;
	}
	public String getCodComp() {
		return codComp;
	}
	public void setCodComp(String codComp) {
		this.codComp = codComp;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}

	@Override
	public String toString() {
		return "SunatConsult [numRuc=" + numRuc + ", codComp=" + codComp + ", numeroSerie=" + numeroSerie + ", numero="
				+ numero + ", fechaEmision=" + fechaEmision + ", monto=" + monto + ", filename=" + filename + ", hash="
				+ hash + "]";
	}
}
