package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ComprobantesPagoElectronicosPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "COD_CPE")
    private String codCpe;
    @Basic(optional = false)
    @Column(name = "NUM_SERIE_CPE")
    private String numSerieCpe;
    @Basic(optional = false)
    @Column(name = "NUM_CPE")
    private long numCpe;

    public ComprobantesPagoElectronicosPK() {
    }

    public ComprobantesPagoElectronicosPK(long numRuc, String codCpe, String numSerieCpe, long numCpe) {
        this.numRuc = numRuc;
        this.codCpe = codCpe;
        this.numSerieCpe = numSerieCpe;
        this.numCpe = numCpe;
    }

    public long getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(long numRuc) {
        this.numRuc = numRuc;
    }

    public String getCodCpe() {
        return codCpe;
    }

    public void setCodCpe(String codCpe) {
        this.codCpe = codCpe;
    }

    public String getNumSerieCpe() {
        return numSerieCpe;
    }

    public void setNumSerieCpe(String numSerieCpe) {
        this.numSerieCpe = numSerieCpe;
    }

    public long getNumCpe() {
        return numCpe;
    }

    public void setNumCpe(long numCpe) {
        this.numCpe = numCpe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numRuc;
        hash += (codCpe != null ? codCpe.hashCode() : 0);
        hash += (numSerieCpe != null ? numSerieCpe.hashCode() : 0);
        hash += (int) numCpe;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComprobantesPagoElectronicosPK)) {
            return false;
        }
        ComprobantesPagoElectronicosPK other = (ComprobantesPagoElectronicosPK) object;
        if (this.numRuc != other.numRuc) {
            return false;
        }
        if ((this.codCpe == null && other.codCpe != null) || (this.codCpe != null && !this.codCpe.equals(other.codCpe))) {
            return false;
        }
        if ((this.numSerieCpe == null && other.numSerieCpe != null) || (this.numSerieCpe != null && !this.numSerieCpe.equals(other.numSerieCpe))) {
            return false;
        }
        if (this.numCpe != other.numCpe) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return "ComprobantesPagoElectronicosPK [numRuc=" + numRuc + ", codCpe=" + codCpe + ", numSerieCpe="
				+ numSerieCpe + ", numCpe=" + numCpe + "]";
	}
    
}
