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
@Table(name = "SUNAT_ACUSE_RECIBO_SUNAT", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatAcuseReciboSunat.findAll", query = "SELECT t FROM SunatAcuseReciboSunat t")})
public class SunatAcuseReciboSunat implements Serializable {

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
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FECHA_ENVIO_SUNAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvioSunat;
    @Column(name = "RESPUESTA_SUNAT")
    private String respuestaSunat;
    @Column(name = "MENSAJE_SUNAT")
    private String mensajeSunat;
    @Column(name = "OBSERVA_NUMERO")
    private String observaNumero;
    @Column(name = "OBSERVA_DESCRIPCION")
    private String observaDescripcion;    
    @Column(name = "FECHA_RESPUESTA_SUNAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuestaSunat;
    @Column(name = "ERROR_LOG_SUNAT")
    private String errorLogSunat;
    @Basic(optional = false)
    @Column(name = "USER_CREA")
    private String userCrea;
    @Basic(optional = false)
    @Column(name = "FECHA_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "TICKET")
    private String ticket;
    
    public SunatAcuseReciboSunat() {
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

	public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaNumero() {
        return observaNumero;
    }

    public void setObservaNumero(String observaNumero) {
        this.observaNumero = observaNumero;
    }

    public String getObservaDescripcion() {
        return observaDescripcion;
    }

    public void setObservaDescripcion(String observaDescripcion) {
        this.observaDescripcion = observaDescripcion;
    }

    public Date getFechaEnvioSunat() {
        return fechaEnvioSunat;
    }

    public void setFechaEnvioSunat(Date fechaEnvioSunat) {
        this.fechaEnvioSunat = fechaEnvioSunat;
    }

    public String getRespuestaSunat() {
        return respuestaSunat;
    }

    public void setRespuestaSunat(String respuestaSunat) {
        this.respuestaSunat = respuestaSunat;
    }

    public String getMensajeSunat() {
        return mensajeSunat;
    }

    public void setMensajeSunat(String mensajeSunat) {
        this.mensajeSunat = mensajeSunat;
    }

    public Date getFechaRespuestaSunat() {
        return fechaRespuestaSunat;
    }

    public void setFechaRespuestaSunat(Date fechaRespuestaSunat) {
        this.fechaRespuestaSunat = fechaRespuestaSunat;
    }

    public String getErrorLogSunat() {
        return errorLogSunat;
    }

    public void setErrorLogSunat(String errorLogSunat) {
        this.errorLogSunat = errorLogSunat;
    }

    public String getUserCrea() {
        return userCrea;
    }

    public void setUserCrea(String userCrea) {
        this.userCrea = userCrea;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
    
    @Override
    public String toString() {
        return "[ ubl=" + rucEmisor+"-"+ tipoComprobante+"-"+serie+"-"+numeroCorrelativo+ " ]";
    }
}
