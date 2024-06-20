package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class SunatPadronVigenciaPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "IND_PADRON")
    private String indPadron;
    @Column(name = "FEC_INIVIG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fec_inivig;
    
    public SunatPadronVigenciaPK() {
    }

    public SunatPadronVigenciaPK(long numRuc, String indPadron, Date fec_inivig) {
        this.numRuc = numRuc;
        this.indPadron = indPadron;
        this.fec_inivig = fec_inivig;
    }

    public long getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(long numRuc) {
        this.numRuc = numRuc;
    }

    public String getIndPadron() {
        return indPadron;
    }

    public void setIndPadron(String indPadron) {
        this.indPadron = indPadron;
    }

    public Date getFec_inivig() {
		return fec_inivig;
	}

	public void setFec_inivig(Date fec_inivig) {
		this.fec_inivig = fec_inivig;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numRuc;
        hash += (indPadron != null ? indPadron.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatPadronVigenciaPK)) {
            return false;
        }
        SunatPadronVigenciaPK other = (SunatPadronVigenciaPK) object;
        if (this.numRuc != other.numRuc) {
            return false;
        }
        if ((this.indPadron == null && other.indPadron != null) || (this.indPadron != null && !this.indPadron.equals(other.indPadron))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatPadronVigenciaPK [numRuc=" + numRuc + ", indPadron=" + indPadron + "]";
	}

    
}
