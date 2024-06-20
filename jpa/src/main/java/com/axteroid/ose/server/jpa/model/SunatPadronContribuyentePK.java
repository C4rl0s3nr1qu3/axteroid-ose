package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SunatPadronContribuyentePK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "IND_PADRON")
    private String indPadron;

    public SunatPadronContribuyentePK() {
    }

    public SunatPadronContribuyentePK(long numRuc, String indPadron) {
        this.numRuc = numRuc;
        this.indPadron = indPadron;
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
        if (!(object instanceof SunatPadronContribuyentePK)) {
            return false;
        }
        SunatPadronContribuyentePK other = (SunatPadronContribuyentePK) object;
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
		return "SunatPadronContribuyentePK [numRuc=" + numRuc + ", indPadron=" + indPadron + "]";
	}

    
}
