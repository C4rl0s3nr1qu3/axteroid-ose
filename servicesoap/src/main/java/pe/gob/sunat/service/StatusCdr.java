package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "StatusCdr")
public class StatusCdr {
	
	private String rucComprobante;
	private String tipoComprobante;
	private String serieComprobante;
	private String numeroComprobante;
	
	public String getRucComprobante() {
		return rucComprobante;
	}
	public void setRucComprobante(String rucComprobante) {
		this.rucComprobante = rucComprobante;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getSerieComprobante() {
		return serieComprobante;
	}
	public void setSerieComprobante(String serieComprobante) {
		this.serieComprobante = serieComprobante;
	}
	public String getNumeroComprobante() {
		return numeroComprobante;
	}
	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}
}
