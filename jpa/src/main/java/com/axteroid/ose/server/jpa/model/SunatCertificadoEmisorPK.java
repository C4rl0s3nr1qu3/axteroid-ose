package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class SunatCertificadoEmisorPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "NUM_ID_CA")
    private String numIdCa;
    @Basic(optional = false)
    @Column(name = "NUM_ID_CD")
    private String numIdCd;

    public SunatCertificadoEmisorPK() {
    }

    public SunatCertificadoEmisorPK(long numRuc, String numIdCa, String numIdCd) {
        this.numRuc = numRuc;
        this.numIdCa = numIdCa;
        this.numIdCd = numIdCd;
    }

    public long getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(long numRuc) {
        this.numRuc = numRuc;
    }

    public String getNumIdCa() {
        return numIdCa;
    }

    public void setNumIdCa(String numIdCa) {
        this.numIdCa = numIdCa;
    }

    public String getNumIdCd() {
        return numIdCd;
    }

    public void setNumIdCd(String numIdCd) {
        this.numIdCd = numIdCd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numRuc;
        hash += (numIdCa != null ? numIdCa.hashCode() : 0);
        hash += (numIdCd != null ? numIdCd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatCertificadoEmisorPK)) {
            return false;
        }
        SunatCertificadoEmisorPK other = (SunatCertificadoEmisorPK) object;
        if (this.numRuc != other.numRuc) {
            return false;
        }
        if ((this.numIdCa == null && other.numIdCa != null) || (this.numIdCa != null && !this.numIdCa.equals(other.numIdCa))) {
            return false;
        }
        if ((this.numIdCd == null && other.numIdCd != null) || (this.numIdCd != null && !this.numIdCd.equals(other.numIdCd))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatCertificadoEmisorPK [numRuc=" + numRuc + ", numIdCa=" + numIdCa + ", numIdCd=" + numIdCd + "]";
	}

    
}
