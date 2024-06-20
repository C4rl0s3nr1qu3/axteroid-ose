package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SunatEstablecimientosAnexosPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private String numRuc;
    @Basic(optional = false)
    @Column(name = "COD_ESTAB")
    private String codEstab;

    public SunatEstablecimientosAnexosPK() {
    }

    public SunatEstablecimientosAnexosPK(String numRuc, String codEstab) {
        this.numRuc = numRuc;
        this.codEstab = codEstab;
    }

    public String getNumRuc() {
		return numRuc;
	}

	public void setNumRuc(String numRuc) {
		this.numRuc = numRuc;
	}

	public String getCodEstab() {
		return codEstab;
	}

	public void setCodEstab(String codEstab) {
		this.codEstab = codEstab;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (numRuc != null ? numRuc.hashCode() : 0);
        hash += (codEstab != null ? codEstab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatEstablecimientosAnexosPK)) {
            return false;
        }
        SunatEstablecimientosAnexosPK other = (SunatEstablecimientosAnexosPK) object;
        if ((this.numRuc == null && other.numRuc != null) || (this.numRuc != null && !this.numRuc.equals(other.numRuc))) {
            return false;
        }
        if ((this.codEstab == null && other.codEstab != null) || (this.codEstab != null && !this.codEstab.equals(other.codEstab))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatEstablecimientosAnexosPK [numRuc=" + numRuc + ", codEstab=" + codEstab + "]";
	}

    
}
