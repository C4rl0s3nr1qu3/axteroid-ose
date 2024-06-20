package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SUNAT_CONTRIBUYENTE", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatContribuyente.findAll", query = "SELECT t FROM SunatContribuyente t")})
public class SunatContribuyente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUM_RUC")
    private Long numRuc;
    @Column(name = "IND_ESTADO")
    private String indEstado;
    @Column(name = "IND_CONDICION")
    private String indCondicion;
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

    public SunatContribuyente() {
    }

    public SunatContribuyente(Long numRuc) {
        this.numRuc = numRuc;
    }

    public SunatContribuyente(Long numRuc, String userCrea, Date fechaCrea) {
        this.numRuc = numRuc;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public Long getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(Long numRuc) {
        this.numRuc = numRuc;
    }

    public String getIndEstado() {
        return indEstado;
    }

    public void setIndEstado(String indEstado) {
        this.indEstado = indEstado;
    }

    public String getIndCondicion() {
        return indCondicion;
    }

    public void setIndCondicion(String indCondicion) {
        this.indCondicion = indCondicion;
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
        hash += (numRuc != null ? numRuc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatContribuyente)) {
            return false;
        }
        SunatContribuyente other = (SunatContribuyente) object;
        if ((this.numRuc == null && other.numRuc != null) || (this.numRuc != null && !this.numRuc.equals(other.numRuc))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatContribuyente [numRuc=" + numRuc + ", indEstado=" + indEstado + ", indCondicion=" + indCondicion
				+ "]";
	}

    
}
