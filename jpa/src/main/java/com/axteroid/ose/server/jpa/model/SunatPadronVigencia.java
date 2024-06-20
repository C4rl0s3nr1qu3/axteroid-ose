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
@Table(name = "SUNAT_PADRON_VIGENCIA", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatPadronVigencia.findAll", query = "SELECT t FROM SunatPadronVigencia t")})
public class SunatPadronVigencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatPadronVigenciaPK sunatPadronVigenciaPK;
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
//    @Column(name = "FEC_INIVIG")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fec_inivig;
    @Column(name = "FEC_FINVIG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fec_finvig;

    public SunatPadronVigencia() {
    }

    public SunatPadronVigencia(SunatPadronVigenciaPK sunatPadronVigenciaPK) {
        this.sunatPadronVigenciaPK = sunatPadronVigenciaPK;
    }

    public SunatPadronVigencia(SunatPadronVigenciaPK sunatPadronVigenciaPK, String userCrea, Date fechaCrea) {
        this.sunatPadronVigenciaPK = sunatPadronVigenciaPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatPadronVigencia(long numRuc, String indPadron, Date fec_inivig) {
        this.sunatPadronVigenciaPK = new SunatPadronVigenciaPK(numRuc, indPadron, fec_inivig);
    }

    public void setSunatPadronVigenciaPK(SunatPadronVigenciaPK sunatPadronVigenciaPK) {
		this.sunatPadronVigenciaPK = sunatPadronVigenciaPK;
	}

	public SunatPadronVigenciaPK getSunatPadronVigenciaPK() {
        return sunatPadronVigenciaPK;
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

    public Date getFec_finvig() {
		return fec_finvig;
	}

	public void setFec_finvig(Date fec_finvig) {
		this.fec_finvig = fec_finvig;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (sunatPadronVigenciaPK != null ? sunatPadronVigenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatPadronContribuyente)) {
            return false;
        }
        SunatPadronVigencia other = (SunatPadronVigencia) object;
        if ((this.sunatPadronVigenciaPK == null && other.sunatPadronVigenciaPK != null) || 
        		(this.sunatPadronVigenciaPK != null && 
        		!this.sunatPadronVigenciaPK.equals(other.sunatPadronVigenciaPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatPadronVigenciaPK [sunatPadronVigenciaPK=" + sunatPadronVigenciaPK + "]";
	}

}
