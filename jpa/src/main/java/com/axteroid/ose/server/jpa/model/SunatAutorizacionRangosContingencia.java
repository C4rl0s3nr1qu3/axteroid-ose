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
@Table(name = "SUNAT_AUTORIZACION_RANGOS_CONTINGENCIA", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatAutorizacionRangosContingencia.findAll", query = "SELECT t FROM SunatAutorizacionRangosContingencia t")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByNumRuc", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.sunatAutorizacionRangosContingenciaPK.numRuc = :numRuc")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByCodCpe", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.sunatAutorizacionRangosContingenciaPK.codCpe = :codCpe")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByNumSerieCpe", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.sunatAutorizacionRangosContingenciaPK.numSerieCpe = :numSerieCpe")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByNumIniCpe", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.sunatAutorizacionRangosContingenciaPK.numIniCpe = :numIniCpe")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByNumFinCpe", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.numFinCpe = :numFinCpe")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByUserCrea", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.userCrea = :userCrea")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByFechaCrea", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.fechaCrea = :fechaCrea")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByUserModi", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.userModi = :userModi")
    , @NamedQuery(name = "SunatAutorizacionRangosContingencia.findByFechaModi", query = "SELECT t FROM SunatAutorizacionRangosContingencia t WHERE t.fechaModi = :fechaModi")})
public class SunatAutorizacionRangosContingencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatAutorizacionRangosContingenciaPK sunatAutorizacionRangosContingenciaPK;
    @Column(name = "NUM_FIN_CPE")
    private Integer numFinCpe;
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

    public SunatAutorizacionRangosContingencia() {
    }

    public SunatAutorizacionRangosContingencia(SunatAutorizacionRangosContingenciaPK sunatAutorizacionRangosContingenciaPK) {
        this.sunatAutorizacionRangosContingenciaPK = sunatAutorizacionRangosContingenciaPK;
    }

    public SunatAutorizacionRangosContingencia(SunatAutorizacionRangosContingenciaPK sunatAutorizacionRangosContingenciaPK, 
    		String userCrea, Date fechaCrea) {
        this.sunatAutorizacionRangosContingenciaPK = sunatAutorizacionRangosContingenciaPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatAutorizacionRangosContingencia(long numRuc, String codCpe, String numSerieCpe, int numIniCpe) {
        this.sunatAutorizacionRangosContingenciaPK = new SunatAutorizacionRangosContingenciaPK(numRuc, codCpe, numSerieCpe, numIniCpe);
    }

    public SunatAutorizacionRangosContingenciaPK getSunatAutorizacionRangosContingenciaPK() {
        return sunatAutorizacionRangosContingenciaPK;
    }

    public void setSunatAutorizacionRangosContingenciaPK(SunatAutorizacionRangosContingenciaPK sunatAutorizacionRangosContingenciaPK) {
        this.sunatAutorizacionRangosContingenciaPK = sunatAutorizacionRangosContingenciaPK;
    }

    public Integer getNumFinCpe() {
        return numFinCpe;
    }

    public void setNumFinCpe(Integer numFinCpe) {
        this.numFinCpe = numFinCpe;
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
        hash += (sunatAutorizacionRangosContingenciaPK != null ? sunatAutorizacionRangosContingenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatAutorizacionRangosContingencia)) {
            return false;
        }
        SunatAutorizacionRangosContingencia other = (SunatAutorizacionRangosContingencia) object;
        if ((this.sunatAutorizacionRangosContingenciaPK == null && other.sunatAutorizacionRangosContingenciaPK != null) ||
        		(this.sunatAutorizacionRangosContingenciaPK != null && 
        		!this.sunatAutorizacionRangosContingenciaPK.equals(other.sunatAutorizacionRangosContingenciaPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatAutorizacionRangosContingenciaPK [sunatAutorizacionRangosContingenciaPK="
				+ sunatAutorizacionRangosContingenciaPK + "]";
	}

    
}
