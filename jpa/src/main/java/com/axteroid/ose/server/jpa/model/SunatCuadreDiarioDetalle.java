package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SUNAT_CRUADRE_DIARIO_DETALLE", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatCuadreDiarioDetalle.findAll", query = "SELECT t FROM SunatCuadreDiarioDetalle t")})
public class SunatCuadreDiarioDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "RUC_EMISOR")
    private long rucEmisor;
    @Column(name = "TIPO_COMPROBANTE")
    private String tipoComprobante;
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "NUMERO_CORRELATIVO")
    private String numeroCorrelativo;    
    @Basic(optional = false)
    @Column(name = "ESTADO_FINAL")
    private String estadoFinal;
    @Column(name = "FECHA_RECEPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecepcion;
    @Column(name = "HORA_RECEPCION")
    //@Temporal(TemporalType.TIME)
    private String horaRecepcion; 
    
    public SunatCuadreDiarioDetalle() {
    }
    
    public Long getId() {
		return id;
	}

    public void setId(Long id) {
		this.id = id;
	}

	public long getRucEmisor() {
		return rucEmisor;
	}

	public void setRucEmisor(long rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumeroCorrelativo() {
		return numeroCorrelativo;
	}

	public void setNumeroCorrelativo(String numeroCorrelativo) {
		this.numeroCorrelativo = numeroCorrelativo;
	}
    
    public String getEstadoFinal() {
		return estadoFinal;
	}

	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}


	public String getHoraRecepcion() {
		return horaRecepcion;
	}

	public void setHoraRecepcion(String horaRecepcion) {
		this.horaRecepcion = horaRecepcion;
	}

	@Override
    public String toString() {
        return "[ cdd=" + rucEmisor+"-"+ tipoComprobante+"-"+serie+"-"+numeroCorrelativo+ " ]";
    }
}
