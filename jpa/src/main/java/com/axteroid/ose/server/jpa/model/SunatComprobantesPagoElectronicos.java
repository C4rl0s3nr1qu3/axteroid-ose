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
@Table(name = "SUNAT_COMPROBANTES_PAGO_ELECTRONICOS", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatComprobantesPagoElectronicos.findAll", query = "SELECT t FROM SunatComprobantesPagoElectronicos t")})
public class SunatComprobantesPagoElectronicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK;
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
    @Column(name = "IND_PERCEPCION")
    private Short indPercepcion;
    @Column(name = "IND_FOR_PAG")
    private Short indForPpag;
    
    public SunatComprobantesPagoElectronicos() {
    }

    public SunatComprobantesPagoElectronicos(SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK) {
        this.sunatComprobantesPagoElectronicosPK = sunatComprobantesPagoElectronicosPK;
    }

    public SunatComprobantesPagoElectronicos(SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK, 
    		String userCrea, Date fechaCrea) {
        this.sunatComprobantesPagoElectronicosPK = sunatComprobantesPagoElectronicosPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatComprobantesPagoElectronicos(long numRuc, String codCpe, String numSerieCpe, long numCpe) {
        this.sunatComprobantesPagoElectronicosPK = new SunatComprobantesPagoElectronicosPK(numRuc, codCpe, numSerieCpe, numCpe);
    }

    public SunatComprobantesPagoElectronicosPK getSunatComprobantesPagoElectronicosPK() {
        return sunatComprobantesPagoElectronicosPK;
    }

    public void setSunatComprobantesPagoElectronicosPK(SunatComprobantesPagoElectronicosPK sunatComprobantesPagoElectronicosPK) {
        this.sunatComprobantesPagoElectronicosPK = sunatComprobantesPagoElectronicosPK;
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
        hash += (sunatComprobantesPagoElectronicosPK != null ? sunatComprobantesPagoElectronicosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatComprobantesPagoElectronicos)) {
            return false;
        }
        SunatComprobantesPagoElectronicos other = (SunatComprobantesPagoElectronicos) object;
        if ((this.sunatComprobantesPagoElectronicosPK == null && other.sunatComprobantesPagoElectronicosPK != null) || 
        		(this.sunatComprobantesPagoElectronicosPK != null && 
        		!this.sunatComprobantesPagoElectronicosPK.equals(other.sunatComprobantesPagoElectronicosPK))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return "SunatComprobantesPagoElectronicos [sunatComprobantesPagoElectronicosPK=" + sunatComprobantesPagoElectronicosPK
				+ ", indEstadoCpe=" + indEstadoCpe + ", fecEmisionCpe=" + fecEmisionCpe + ", mtoImporteCpe="
				+ mtoImporteCpe + ", codMonedaCpe=" + codMonedaCpe + ", codMotTraslado=" + codMotTraslado
				+ ", codModTraslado=" + codModTraslado + ", indTransbordo=" + indTransbordo + ", fecIniTraslado="
				+ fecIniTraslado + ", indPercepcion=" + indPercepcion
				+ ", indForPpag=" + indForPpag + "]";
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
