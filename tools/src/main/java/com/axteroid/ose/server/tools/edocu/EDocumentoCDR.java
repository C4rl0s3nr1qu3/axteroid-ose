package com.axteroid.ose.server.tools.edocu;


import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.axteroid.ose.server.tools.constantes.Constantes;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class EDocumentoCDR {

    private String versionUBL = Constantes.SUNAT_UBL_21;
    private String versionCDR = Constantes.SUNAT_CUSTOMIZA_10;
    
    private String autorización_Comprobante;
    private Date fecha_recepcion_comprobante;
    private Date hora_recepcion_comprobante;
    private Date fecha_comprobacion_comprobante;
    private Date hora_comprobacion_comprobante;
    private String ruc_emisor_pse;    
    private String tipo_documento_emisor_pse = Constantes.SUNAT_TDI_RUC;
    private String ruc_ose;
    private String tipo_documento_ose = Constantes.SUNAT_TDI_RUC;
    private String codigo_respuesta;
    private String descripcion_respuesta;
    private String codigo_observacion;
    private String descripcion_observacion;
    private String serie_numero_comprobante;
    private Date fecha_emisión_comprobante;
    private Date hora_emisión_comprobante;
    private String tipo_comprobante;
    private String hash_comprobante;
    private String numero_documento_emisor;
    private String tipo_documento_emisor;
    private String numero_documento_receptor;
    private String tipo_documento_receptor;
    private String document_response;
    private String firma_digital;
    private String ubl_Extensions;
    
	public String getVersionUBL() {
		return versionUBL;
	}

	public void setVersionUBL(String versionUBL) {
		this.versionUBL = versionUBL;
	}

	public String getVersionCDR() {
		return versionCDR;
	}

	public void setVersionCDR(String versionCDR) {
		this.versionCDR = versionCDR;
	}

	public String getAutorización_Comprobante() {
		return autorización_Comprobante;
	}

	public void setAutorización_Comprobante(String autorización_Comprobante) {
		this.autorización_Comprobante = autorización_Comprobante;
	}

	public Date getFecha_recepcion_comprobante() {
		return fecha_recepcion_comprobante;
	}

	public void setFecha_recepcion_comprobante(Date fecha_recepcion_comprobante) {
		this.fecha_recepcion_comprobante = fecha_recepcion_comprobante;
	}

	public Date getHora_recepcion_comprobante() {
		return hora_recepcion_comprobante;
	}

	public void setHora_recepcion_comprobante(Date hora_recepcion_comprobante) {
		this.hora_recepcion_comprobante = hora_recepcion_comprobante;
	}

	public Date getFecha_comprobacion_comprobante() {
		return fecha_comprobacion_comprobante;
	}

	public void setFecha_comprobacion_comprobante(Date fecha_comprobacion_comprobante) {
		this.fecha_comprobacion_comprobante = fecha_comprobacion_comprobante;
	}

	public Date getHora_comprobacion_comprobante() {
		return hora_comprobacion_comprobante;
	}

	public void setHora_comprobacion_comprobante(Date hora_comprobacion_comprobante) {
		this.hora_comprobacion_comprobante = hora_comprobacion_comprobante;
	}

	public String getRuc_emisor_pse() {
		return ruc_emisor_pse;
	}

	public void setRuc_emisor_pse(String ruc_emisor_pse) {
		this.ruc_emisor_pse = ruc_emisor_pse;
	}

	public String getTipo_documento_emisor_pse() {
		return tipo_documento_emisor_pse;
	}

	public void setTipo_documento_emisor_pse(String tipo_documento_emisor_pse) {
		this.tipo_documento_emisor_pse = tipo_documento_emisor_pse;
	}

	public String getRuc_ose() {
		return ruc_ose;
	}

	public void setRuc_ose(String ruc_ose) {
		this.ruc_ose = ruc_ose;
	}

	public String getTipo_documento_ose() {
		return tipo_documento_ose;
	}

	public void setTipo_documento_ose(String tipo_documento_ose) {
		this.tipo_documento_ose = tipo_documento_ose;
	}

	public String getCodigo_respuesta() {
		return codigo_respuesta;
	}

	public void setCodigo_respuesta(String codigo_respuesta) {
		this.codigo_respuesta = codigo_respuesta;
	}

	public String getDescripcion_respuesta() {
		return descripcion_respuesta;
	}

	public void setDescripcion_respuesta(String descripcion_respuesta) {
		this.descripcion_respuesta = descripcion_respuesta;
	}

	public String getCodigo_observacion() {
		return codigo_observacion;
	}

	public void setCodigo_observacion(String codigo_observacion) {
		this.codigo_observacion = codigo_observacion;
	}

	public String getDescripcion_observacion() {
		return descripcion_observacion;
	}

	public void setDescripcion_observacion(String descripcion_observacion) {
		this.descripcion_observacion = descripcion_observacion;
	}

	public String getSerie_numero_comprobante() {
		return serie_numero_comprobante;
	}

	public void setSerie_numero_comprobante(String serie_numero_comprobante) {
		this.serie_numero_comprobante = serie_numero_comprobante;
	}

	public String getTipo_comprobante() {
		return tipo_comprobante;
	}

	public void setTipo_comprobante(String tipo_comprobante) {
		this.tipo_comprobante = tipo_comprobante;
	}

	public String getHash_comprobante() {
		return hash_comprobante;
	}

	public void setHash_comprobante(String hash_comprobante) {
		this.hash_comprobante = hash_comprobante;
	}

	public String getNumero_documento_emisor() {
		return numero_documento_emisor;
	}

	public void setNumero_documento_emisor(String numero_documento_emisor) {
		this.numero_documento_emisor = numero_documento_emisor;
	}

	public String getTipo_documento_emisor() {
		return tipo_documento_emisor;
	}

	public void setTipo_documento_emisor(String tipo_documento_emisor) {
		this.tipo_documento_emisor = tipo_documento_emisor;
	}

	public String getNumero_documento_receptor() {
		return numero_documento_receptor;
	}

	public void setNumero_documento_receptor(String numero_documento_receptor) {
		this.numero_documento_receptor = numero_documento_receptor;
	}

	public String getTipo_documento_receptor() {
		return tipo_documento_receptor;
	}

	public void setTipo_documento_receptor(String tipo_documento_receptor) {
		this.tipo_documento_receptor = tipo_documento_receptor;
	}

	public Date getFecha_emisión_comprobante() {
		return fecha_emisión_comprobante;
	}

	public void setFecha_emisión_comprobante(Date fecha_emisión_comprobante) {
		this.fecha_emisión_comprobante = fecha_emisión_comprobante;
	}

	public Date getHora_emisión_comprobante() {
		return hora_emisión_comprobante;
	}

	public void setHora_emisión_comprobante(Date hora_emisión_comprobante) {
		this.hora_emisión_comprobante = hora_emisión_comprobante;
	}

	public String getDocument_response() {
		return document_response;
	}

	public void setDocument_response(String document_response) {
		this.document_response = document_response;
	}

	public String getFirma_digital() {
		return firma_digital;
	}

	public void setFirma_digital(String firma_digital) {
		this.firma_digital = firma_digital;
	}

	public String getUbl_Extensions() {
		return ubl_Extensions;
	}

	public void setUbl_Extensions(String ubl_Extensions) {
		this.ubl_Extensions = ubl_Extensions;
	}
}
	