package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
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
@Table(name = "SUNAT_CERTIFICADO_EMISOR", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatCertificadoEmisor.findAll", query = "SELECT t FROM SunatCertificadoEmisor t")})
public class SunatCertificadoEmisor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatCertificadoEmisorPK sunatCertificadoEmisorPK;
    @Column(name = "FEC_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAlta;
    @Column(name = "FEC_BAJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecBaja;
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

    public SunatCertificadoEmisor() {
    }

    public SunatCertificadoEmisor(SunatCertificadoEmisorPK sunatCertificadoEmisorPK) {
        this.sunatCertificadoEmisorPK = sunatCertificadoEmisorPK;
    }

    public SunatCertificadoEmisor(SunatCertificadoEmisorPK sunatCertificadoEmisorPK, String userCrea, Date fechaCrea) {
        this.sunatCertificadoEmisorPK = sunatCertificadoEmisorPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatCertificadoEmisor(long numRuc, String numIdCa, String numIdCd) {
        this.sunatCertificadoEmisorPK = new SunatCertificadoEmisorPK(numRuc, numIdCa, numIdCd);
    }

    public SunatCertificadoEmisorPK getSunatCertificadoEmisorPK() {
        return sunatCertificadoEmisorPK;
    }

    public void setSunatCertificadoEmisorPK(SunatCertificadoEmisorPK sunatCertificadoEmisorPK) {
        this.sunatCertificadoEmisorPK = sunatCertificadoEmisorPK;
    }

    public Date getFecAlta() {
        return fecAlta;
    }

    public void setFecAlta(Date fecAlta) {
        this.fecAlta = fecAlta;
    }

    public Date getFecBaja() {
        return fecBaja;
    }

    public void setFecBaja(Date fecBaja) {
        this.fecBaja = fecBaja;
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
        hash += (sunatCertificadoEmisorPK != null ? sunatCertificadoEmisorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatCertificadoEmisor)) {
            return false;
        }
        SunatCertificadoEmisor other = (SunatCertificadoEmisor) object;
        if ((this.sunatCertificadoEmisorPK == null && other.sunatCertificadoEmisorPK != null) || 
        		(this.sunatCertificadoEmisorPK != null && !this.sunatCertificadoEmisorPK.equals(other.sunatCertificadoEmisorPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatCertificadoEmisorPK [sunatCertificadoEmisorPK=" + sunatCertificadoEmisorPK + "]";
	}

    
}
