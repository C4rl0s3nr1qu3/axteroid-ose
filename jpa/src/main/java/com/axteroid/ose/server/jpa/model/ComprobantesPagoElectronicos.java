package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COMPROBANTES_PAGO_ELECTRONICOS", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "ComprobantesPagoElectronicos.findAll", query = "SELECT t FROM ComprobantesPagoElectronicos t")})
public class ComprobantesPagoElectronicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComprobantesPagoElectronicosPK comprobantesPagoElectronicosPK;
    @Column(name = "IND_ESTADO_CPE")
    private Short indEstadoCpe;
    @Column(name = "FEC_EMISION_CPE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmisionCpe;
    @Column(name = "MTO_IMPORTE_CPE")
    private BigDecimal mtoImporteCpe;
    @Column(name = "COD_MONEDA_CPE")
    private String codMonedaCpe;
    @Column(name = "COD_MOT_TRASLADO")
    private Short codMotTraslado;
    @Column(name = "COD_MOD_TRASLADO")
    private Short codModTraslado;
    @Column(name = "IND_TRANSBORDO")
    private Short indTransbordo;
    @Column(name = "FEC_INI_TRASLADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIniTraslado;
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
    @Column(name = "ID")
    private Long id;
    @Column(name = "IND_PERCEPCION")
    private Short indPercepcion;
    @Column(name = "IND_FOR_PAG")
    private Short indForPpag;
    
    public ComprobantesPagoElectronicos() {
    }

    public ComprobantesPagoElectronicos(ComprobantesPagoElectronicosPK comprobantesPagoElectronicosPK) {
        this.comprobantesPagoElectronicosPK = comprobantesPagoElectronicosPK;
    }

    public ComprobantesPagoElectronicos(ComprobantesPagoElectronicosPK comprobantesPagoElectronicosPK, String userCrea, Date fechaCrea) {
        this.comprobantesPagoElectronicosPK = comprobantesPagoElectronicosPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public ComprobantesPagoElectronicos(long numRuc, String codCpe, String numSerieCpe, Long numCpe) {
        this.comprobantesPagoElectronicosPK = new ComprobantesPagoElectronicosPK(numRuc, codCpe, numSerieCpe, numCpe);
    }

    public ComprobantesPagoElectronicosPK getComprobantesPagoElectronicosPK() {
        return comprobantesPagoElectronicosPK;
    }

    public void setComprobantesPagoElectronicosPK(ComprobantesPagoElectronicosPK comprobantesPagoElectronicosPK) {
        this.comprobantesPagoElectronicosPK = comprobantesPagoElectronicosPK;
    }

    public Short getIndEstadoCpe() {
        return indEstadoCpe;
    }

    public void setIndEstadoCpe(Short indEstadoCpe) {
        this.indEstadoCpe = indEstadoCpe;
    }

    public Date getFecEmisionCpe() {
        return fecEmisionCpe;
    }

    public void setFecEmisionCpe(Date fecEmisionCpe) {
        this.fecEmisionCpe = fecEmisionCpe;
    }

    public BigDecimal getMtoImporteCpe() {
        return mtoImporteCpe;
    }

    public void setMtoImporteCpe(BigDecimal mtoImporteCpe) {
        this.mtoImporteCpe = mtoImporteCpe;
    }

    public String getCodMonedaCpe() {
        return codMonedaCpe;
    }

    public void setCodMonedaCpe(String codMonedaCpe) {
        this.codMonedaCpe = codMonedaCpe;
    }

    public Short getCodMotTraslado() {
        return codMotTraslado;
    }

    public void setCodMotTraslado(Short codMotTraslado) {
        this.codMotTraslado = codMotTraslado;
    }

    public Short getCodModTraslado() {
        return codModTraslado;
    }

    public void setCodModTraslado(Short codModTraslado) {
        this.codModTraslado = codModTraslado;
    }

    public Short getIndTransbordo() {
        return indTransbordo;
    }

    public void setIndTransbordo(Short indTransbordo) {
        this.indTransbordo = indTransbordo;
    }

    public Date getFecIniTraslado() {
        return fecIniTraslado;
    }

    public void setFecIniTraslado(Date fecIniTraslado) {
        this.fecIniTraslado = fecIniTraslado;
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
        hash += (comprobantesPagoElectronicosPK != null ? comprobantesPagoElectronicosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprobantesPagoElectronicos)) {
            return false;
        }
        ComprobantesPagoElectronicos other = (ComprobantesPagoElectronicos) object;
        if ((this.comprobantesPagoElectronicosPK == null && other.comprobantesPagoElectronicosPK != null) || 
        		(this.comprobantesPagoElectronicosPK != null && 
        		!this.comprobantesPagoElectronicosPK.equals(other.comprobantesPagoElectronicosPK))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return "ComprobantesPagoElectronicos [ComprobantesPagoElectronicosPK=" + comprobantesPagoElectronicosPK
				+ ", indEstadoCpe=" + indEstadoCpe + ", fecEmisionCpe=" + fecEmisionCpe + ", mtoImporteCpe="
				+ mtoImporteCpe + ", codMonedaCpe=" + codMonedaCpe + ", codMotTraslado=" + codMotTraslado
				+ ", codModTraslado=" + codModTraslado + ", indTransbordo=" + indTransbordo + ", fecIniTraslado="
				+ fecIniTraslado + ", id=" + id + ", indPercepcion=" + indPercepcion
				+ ", indForPpag=" + indForPpag + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getIndPercepcion() {
		return indPercepcion;
	}

	public void setIndPercepcion(Short indPercepcion) {
		this.indPercepcion = indPercepcion;
	}

	public Short getIndForPpag() {
		return indForPpag;
	}

	public void setIndForPpag(Short indForPpag) {
		this.indForPpag = indForPpag;
	}
    
}
