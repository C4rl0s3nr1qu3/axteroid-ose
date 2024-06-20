package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SunatContribuyenteAsociadoEmisorPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "NUM_RUC_ASOCIADO")
    private long numRucAsociado;
    @Basic(optional = false)
    @Column(name = "IND_TIP_ASOCIACION")
    private short indTipAsociacion;

    public SunatContribuyenteAsociadoEmisorPK() {
    }

    public SunatContribuyenteAsociadoEmisorPK(long numRuc, long numRucAsociado, short indTipAsociacion) {
        this.numRuc = numRuc;
        this.numRucAsociado = numRucAsociado;
        this.indTipAsociacion = indTipAsociacion;
    }

    public long getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(long numRuc) {
        this.numRuc = numRuc;
    }

    public long getNumRucAsociado() {
        return numRucAsociado;
    }

    public void setNumRucAsociado(long numRucAsociado) {
        this.numRucAsociado = numRucAsociado;
    }

    public short getIndTipAsociacion() {
        return indTipAsociacion;
    }

    public void setIndTipAsociacion(short indTipAsociacion) {
        this.indTipAsociacion = indTipAsociacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numRuc;
        hash += (int) numRucAsociado;
        hash += (int) indTipAsociacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatContribuyenteAsociadoEmisorPK)) {
            return false;
        }
        SunatContribuyenteAsociadoEmisorPK other = (SunatContribuyenteAsociadoEmisorPK) object;
        if (this.numRuc != other.numRuc) {
            return false;
        }
        if (this.numRucAsociado != other.numRucAsociado) {
            return false;
        }
        if (this.indTipAsociacion != other.indTipAsociacion) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatContribuyenteAsociadoEmisorPK [numRuc=" + numRuc + ", numRucAsociado=" + numRucAsociado
				+ ", indTipAsociacion=" + indTipAsociacion + "]";
	}

    
}
