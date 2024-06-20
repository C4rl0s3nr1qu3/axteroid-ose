package com.axteroid.ose.server.avatar.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DOCUMENTO", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "Documento", query = "SELECT t FROM Documento t")})
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "ID_COMPROBANTE")
    private String idComprobante;
    @Basic(optional = false)
    @Column(name = "RUC_EMISOR")
    private long rucEmisor;
    @Column(name = "TIPO_COMPROBANTE")
    private String tipoComprobante;
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "NUMERO_CORRELATIVO")
    private String numeroCorrelativo;
    @Column(name = "CORRELATIVO")
    private long correlativo;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "LONGITUD_NOMBRE")
    private Integer longitudNombre;
    @Column(name = "DIRECCION")
    private String direccion;
    @Lob
    @Column(name = "UBL")
    private byte[] ubl;
    @Column(name = "UBL_VERSION")
    private String ublVersion;
    @Column(name = "CUSTOMIZA_VERSION")
    private String customizaVersion;
    @Lob
    @Column(name = "CDR")
    private byte[] cdr;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Basic(optional = false)
    @Column(name = "ERROR_COMPROBANTE")
    private Character errorComprobante;
    @Column(name = "ERROR_NUMERO")
    private String errorNumero;
    @Column(name = "ERROR_DESCRIPCION")
    private String errorDescripcion;
    @Column(name = "ERROR_LOG")
    private String errorLog;
    @Column(name = "OBSERVA_NUMERO")
    private String observaNumero;
    @Column(name = "OBSERVA_DESCRIPCION")
    private String observaDescripcion;
    @Column(name = "ADVERTENCIA")
    private String advertencia;
    @Column(name = "FECHA_ENVIO_SUNAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvioSunat;
    @Column(name = "RESPUESTA_SUNAT")
    private String respuestaSunat;
    @Lob
    @Column(name = "MENSAJE_SUNAT")
    private byte[] mensajeSunat;
    @Column(name = "FECHA_RESPUESTA_SUNAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuestaSunat;
    @Column(name = "ERROR_LOG_SUNAT")
    private String errorLogSunat;
    @Column(name = "FEC_INI_PROC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIniProc;
    @Column(name = "FEC_FIN_PROC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFinProc;
    @Basic(optional = false)
    @Column(name = "USER_CREA")
    private String userCrea;
    @Basic(optional = false)
    @Column(name = "FECHA_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "USER_MODI")
    private String userModi;
    @Column(name = "FECHA_MODI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModi;

    public Documento() {
    }

    public Documento(Long id) {
        this.id = id;
    }

    public Documento(Long id, String idComprobante, long rucEmisor, String nombre, String estado, Character errorComprobante, String userCrea, Date fechaCrea) {
        this.id = id;
        this.idComprobante = idComprobante;
        this.rucEmisor = rucEmisor;
        this.nombre = nombre;
        this.estado = estado;
        this.errorComprobante = errorComprobante;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(String idComprobante) {
        this.idComprobante = idComprobante;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getLongitudNombre() {
        return longitudNombre;
    }

    public void setLongitudNombre(Integer longitudNombre) {
        this.longitudNombre = longitudNombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getUbl() {
        return ubl;
    }

    public void setUbl(byte[] ubl) {
        this.ubl = ubl;
    }

    public byte[] getCdr() {
        return cdr;
    }

    public void setCdr(byte[] cdr) {
        this.cdr = cdr;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Character getErrorComprobante() {
        return errorComprobante;
    }

    public void setErrorComprobante(Character errorComprobante) {
        this.errorComprobante = errorComprobante;
    }

    public String getErrorNumero() {
        return errorNumero;
    }

    public void setErrorNumero(String errorNumero) {
        this.errorNumero = errorNumero;
    }

    public String getErrorDescripcion() {
        return errorDescripcion;
    }

    public void setErrorDescripcion(String errorDescripcion) {
        this.errorDescripcion = errorDescripcion;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
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

    public String getAdvertencia() {
        return advertencia;
    }

    public void setAdvertencia(String advertencia) {
        this.advertencia = advertencia;
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

    public byte[] getMensajeSunat() {
        return mensajeSunat;
    }

    public void setMensajeSunat(byte[] mensajeSunat) {
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

    public Date getFecIniProc() {
        return fecIniProc;
    }

    public void setFecIniProc(Date fecIniProc) {
        this.fecIniProc = fecIniProc;
    }

    public Date getFecFinProc() {
        return fecFinProc;
    }

    public void setFecFinProc(Date fecFinProc) {
        this.fecFinProc = fecFinProc;
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

    public String getUserModi() {
        return userModi;
    }

    public void setUserModi(String userModi) {
        this.userModi = userModi;
    }

    public Date getFechaModi() {
        return fechaModi;
    }

    public void setFechaModi(Date fechaModi) {
        this.fechaModi = fechaModi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	public String getUblVersion() {
		return ublVersion;
	}

	public void setUblVersion(String ublVersion) {
		this.ublVersion = ublVersion;
	}

	public String getCustomizaVersion() {
		return customizaVersion;
	}

	public void setCustomizaVersion(String customizaVersion) {
		this.customizaVersion = customizaVersion;
	}

	@Override
	public String toString() {
		return "TbComprobante [id=" + id + ", rucEmisor=" + rucEmisor + ", tipoComprobante=" + tipoComprobante
				+ ", serie=" + serie + ", numeroCorrelativo=" + numeroCorrelativo + "]";
	}
    
}
