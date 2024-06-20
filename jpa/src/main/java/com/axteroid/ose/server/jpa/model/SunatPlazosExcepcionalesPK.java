package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class SunatPlazosExcepcionalesPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "NUM_RUC")
    private long numRuc;
    @Basic(optional = false)
    @Column(name = "COD_CPE")
    private String codCpe;
    @Basic(optional = false)
    @Column(name = "FEC_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmision;

    public SunatPlazosExcepcionalesPK() {
    }

    public SunatPlazosExcepcionalesPK(long numRuc, String codCpe, Date fecEmision) {
        this.numRuc = numRuc;
        this.codCpe = codCpe;
        this.fecEmision = fecEmision;
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

    public Date getFecEmision() {
        return fecEmision;
    }

    public void setFecEmision(Date fecEmision) {
        this.fecEmision = fecEmision;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numRuc;
        hash += (codCpe != null ? codCpe.hashCode() : 0);
        hash += (fecEmision != null ? fecEmision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatPlazosExcepcionalesPK)) {
            return false;
        }
        SunatPlazosExcepcionalesPK other = (SunatPlazosExcepcionalesPK) object;
        if (this.numRuc != other.numRuc) {
            return false;
        }
        if ((this.codCpe == null && other.codCpe != null) || (this.codCpe != null && !this.codCpe.equals(other.codCpe))) {
            return false;
        }
        if ((this.fecEmision == null && other.fecEmision != null) || (this.fecEmision != null && !this.fecEmision.equals(other.fecEmision))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatPlazosExcepcionalesPK [numRuc=" + numRuc + ", codCpe=" + codCpe + ", fecEmision=" + fecEmision + "]";
	}

    
}
