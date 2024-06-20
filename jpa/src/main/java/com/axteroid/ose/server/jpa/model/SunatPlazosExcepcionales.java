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
@Table(name = "SUNAT_PLAZOS_EXCEPCIONALES", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatPlazosExcepcionales.findAll", query = "SELECT t FROM SunatPlazosExcepcionales t")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByNumRuc", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.sunatPlazosExcepcionalesPK.numRuc = :numRuc")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByCodCpe", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.sunatPlazosExcepcionalesPK.codCpe = :codCpe")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByFecEmision", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.sunatPlazosExcepcionalesPK.fecEmision = :fecEmision")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByFecLimite", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.fecLimite = :fecLimite")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByUserCrea", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.userCrea = :userCrea")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByFechaCrea", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.fechaCrea = :fechaCrea")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByUserModi", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.userModi = :userModi")
    , @NamedQuery(name = "SunatPlazosExcepcionales.findByFechaModi", query = "SELECT t FROM SunatPlazosExcepcionales t WHERE t.fechaModi = :fechaModi")})
public class SunatPlazosExcepcionales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatPlazosExcepcionalesPK sunatPlazosExcepcionalesPK;
    @Column(name = "FEC_LIMITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecLimite;
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

    public SunatPlazosExcepcionales() {
    }

    public SunatPlazosExcepcionales(SunatPlazosExcepcionalesPK sunatPlazosExcepcionalesPK) {
        this.sunatPlazosExcepcionalesPK = sunatPlazosExcepcionalesPK;
    }

    public SunatPlazosExcepcionales(SunatPlazosExcepcionalesPK sunatPlazosExcepcionalesPK, String userCrea, Date fechaCrea) {
        this.sunatPlazosExcepcionalesPK = sunatPlazosExcepcionalesPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatPlazosExcepcionales(long numRuc, String codCpe, Date fecEmision) {
        this.sunatPlazosExcepcionalesPK = new SunatPlazosExcepcionalesPK(numRuc, codCpe, fecEmision);
    }

    public SunatPlazosExcepcionalesPK getSunatPlazosExcepcionalesPK() {
        return sunatPlazosExcepcionalesPK;
    }

    public void setTsPlazosExcepcionalesPK(SunatPlazosExcepcionalesPK sunatPlazosExcepcionalesPK) {
        this.sunatPlazosExcepcionalesPK = sunatPlazosExcepcionalesPK;
    }

    public Date getFecLimite() {
        return fecLimite;
    }

    public void setFecLimite(Date fecLimite) {
        this.fecLimite = fecLimite;
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
        hash += (sunatPlazosExcepcionalesPK != null ? sunatPlazosExcepcionalesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatPlazosExcepcionales)) {
            return false;
        }
        SunatPlazosExcepcionales other = (SunatPlazosExcepcionales) object;
        if ((this.sunatPlazosExcepcionalesPK == null && other.sunatPlazosExcepcionalesPK != null) || 
        		(this.sunatPlazosExcepcionalesPK != null && !this.sunatPlazosExcepcionalesPK.equals(other.sunatPlazosExcepcionalesPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatPlazosExcepcionalesPK [sunatPlazosExcepcionalesPK=" + sunatPlazosExcepcionalesPK + "]";
	}

    
}
