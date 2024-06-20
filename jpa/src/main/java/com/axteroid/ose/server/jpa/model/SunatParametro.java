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
@Table(name = "SUNAT_PARAMETRO", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatParametro.findAll", query = "SELECT t FROM SunatParametro t")})
public class SunatParametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatParametroPK sunatParametroPK;
    @Column(name = "DES_ARGUMENTO")
    private String desArgumento;
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

    public SunatParametro() {
    }

    public SunatParametro(SunatParametroPK sunatParametroPK) {
        this.sunatParametroPK = sunatParametroPK;
    }

    public SunatParametro(SunatParametroPK sunatParametroPK, String userCrea, Date fechaCrea) {
        this.sunatParametroPK = sunatParametroPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatParametro(String codParametro, String codArgumento) {
        this.sunatParametroPK = new SunatParametroPK(codParametro, codArgumento);
    }

    public SunatParametroPK getSunatParametroPK() {
        return sunatParametroPK;
    }

    public void setSunatParametroPK(SunatParametroPK sunatParametroPK) {
        this.sunatParametroPK = sunatParametroPK;
    }

    public String getDesArgumento() {
        return desArgumento;
    }

    public void setDesArgumento(String desArgumento) {
        this.desArgumento = desArgumento;
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
        hash += (sunatParametroPK != null ? sunatParametroPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatParametro)) {
            return false;
        }
        SunatParametro other = (SunatParametro) object;
        if ((this.sunatParametroPK == null && other.sunatParametroPK != null) || 
        		(this.sunatParametroPK != null && !this.sunatParametroPK.equals(other.sunatParametroPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatParametroPK [sunatParametroPK=" + sunatParametroPK + ", desArgumento=" + desArgumento + "]";
	}

    
}
