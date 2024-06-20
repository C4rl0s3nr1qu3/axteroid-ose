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
@Table(name = "SUNAT_PADRON_CONTRIBUYENTE", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatPadronContribuyente.findAll", query = "SELECT t FROM SunatPadronContribuyente t")})
public class SunatPadronContribuyente implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatPadronContribuyentePK sunatPadronContribuyentePK;
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
    @Column(name = "FEC_INIVIG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fec_inivig;
    @Column(name = "FEC_FINVIG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fec_finvig;

    public SunatPadronContribuyente() {
    }

    public SunatPadronContribuyente(SunatPadronContribuyentePK sunatPadronContribuyentePK) {
        this.sunatPadronContribuyentePK = sunatPadronContribuyentePK;
    }

    public SunatPadronContribuyente(SunatPadronContribuyentePK sunatPadronContribuyentePK, String userCrea, Date fechaCrea) {
        this.sunatPadronContribuyentePK = sunatPadronContribuyentePK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatPadronContribuyente(long numRuc, String indPadron) {
        this.sunatPadronContribuyentePK = new SunatPadronContribuyentePK(numRuc, indPadron);
    }

    public SunatPadronContribuyentePK getSunatPadronContribuyentePK() {
        return sunatPadronContribuyentePK;
    }

    public void setTsPadronContribuyentePK(SunatPadronContribuyentePK sunatPadronContribuyentePK) {
        this.sunatPadronContribuyentePK = sunatPadronContribuyentePK;
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
        hash += (sunatPadronContribuyentePK != null ? sunatPadronContribuyentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatPadronContribuyente)) {
            return false;
        }
        SunatPadronContribuyente other = (SunatPadronContribuyente) object;
        if ((this.sunatPadronContribuyentePK == null && other.sunatPadronContribuyentePK != null) || 
        		(this.sunatPadronContribuyentePK != null && 
        		!this.sunatPadronContribuyentePK.equals(other.sunatPadronContribuyentePK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatPadronContribuyentePK [sunatPadronContribuyentePK=" + sunatPadronContribuyentePK + "]";
	}

}
