package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SUNAT_ESTABLECIMIENTOS_ANEXOS", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatEstablecimientosAnexos.findAll", query = "SELECT t FROM SunatEstablecimientosAnexos t")})
public class SunatEstablecimientosAnexos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatEstablecimientosAnexosPK sunatEstablecimientosAnexosPK;
    @Column(name = "COD_TIP_ESTAB")
    private String codTipEstab;

    public SunatEstablecimientosAnexos() {
    }

    public SunatEstablecimientosAnexos(SunatEstablecimientosAnexosPK sunatEstablecimientosAnexosPK) {
        this.sunatEstablecimientosAnexosPK = sunatEstablecimientosAnexosPK;
    }

    public SunatEstablecimientosAnexos(SunatEstablecimientosAnexosPK sunatEstablecimientosAnexosPK, String codTipEstab) {
        this.sunatEstablecimientosAnexosPK = sunatEstablecimientosAnexosPK;
        this.codTipEstab = codTipEstab;
    }

    public SunatEstablecimientosAnexos(String numRuc, String codEstab) {
        this.sunatEstablecimientosAnexosPK = new SunatEstablecimientosAnexosPK(numRuc, codEstab);
    }

    public SunatEstablecimientosAnexosPK getSunatEstablecimientosAnexosPK() {
        return sunatEstablecimientosAnexosPK;
    }

    public void setTsEstablecimientosAnexosPK(SunatEstablecimientosAnexosPK sunatEstablecimientosAnexosPK) {
        this.sunatEstablecimientosAnexosPK = sunatEstablecimientosAnexosPK;
    }

    public String getCodTipEstab() {
		return codTipEstab;
	}

	public void setCodTipEstab(String codTipEstab) {
		this.codTipEstab = codTipEstab;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (sunatEstablecimientosAnexosPK != null ? sunatEstablecimientosAnexosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatParametro)) {
            return false;
        }
        SunatEstablecimientosAnexos other = (SunatEstablecimientosAnexos) object;
        if ((this.sunatEstablecimientosAnexosPK == null && other.sunatEstablecimientosAnexosPK != null) || 
        		(this.sunatEstablecimientosAnexosPK != null && 
        		!this.sunatEstablecimientosAnexosPK.equals(other.sunatEstablecimientosAnexosPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatEstablecimientosAnexosPK [sunatEstablecimientosAnexosPK=" + sunatEstablecimientosAnexosPK + ", codTipEstab="
				+ codTipEstab + "]";
	}

    
}
