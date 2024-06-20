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
@Table(name = "SUNAT_CONTRIBUYENTE_ASOCIADO_EMISOR", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatContribuyenteAsociadoEmisor.findAll", query = "SELECT t FROM SunatContribuyenteAsociadoEmisor t")})
public class SunatContribuyenteAsociadoEmisor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatContribuyenteAsociadoEmisorPK sunatContribuyenteAsociadoEmisorPK;
    @Column(name = "FEC_INICIO")
    @Temporal(TemporalType.DATE)
    private Date fecInicio;
    @Column(name = "FEC_FIN")
    @Temporal(TemporalType.DATE)
    private Date fecFin;
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

    public SunatContribuyenteAsociadoEmisor() {
    }

    public SunatContribuyenteAsociadoEmisor(SunatContribuyenteAsociadoEmisorPK sunatContribuyenteAsociadoEmisorPK) {
        this.sunatContribuyenteAsociadoEmisorPK = sunatContribuyenteAsociadoEmisorPK;
    }

    public SunatContribuyenteAsociadoEmisor(SunatContribuyenteAsociadoEmisorPK sunatContribuyenteAsociadoEmisorPK, 
    		String userCrea, Date fechaCrea) {
        this.sunatContribuyenteAsociadoEmisorPK = sunatContribuyenteAsociadoEmisorPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatContribuyenteAsociadoEmisor(long numRuc, long numRucAsociado, short indTipAsociacion) {
        this.sunatContribuyenteAsociadoEmisorPK = new SunatContribuyenteAsociadoEmisorPK(numRuc, numRucAsociado, indTipAsociacion);
    }

    public SunatContribuyenteAsociadoEmisorPK getSunatContribuyenteAsociadoEmisorPK() {
        return sunatContribuyenteAsociadoEmisorPK;
    }

    public void setTsContribuyenteAsociadoEmisorPK(SunatContribuyenteAsociadoEmisorPK sunatContribuyenteAsociadoEmisorPK) {
        this.sunatContribuyenteAsociadoEmisorPK = sunatContribuyenteAsociadoEmisorPK;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
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
        hash += (sunatContribuyenteAsociadoEmisorPK != null ? sunatContribuyenteAsociadoEmisorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatContribuyenteAsociadoEmisor)) {
            return false;
        }
        SunatContribuyenteAsociadoEmisor other = (SunatContribuyenteAsociadoEmisor) object;
        if ((this.sunatContribuyenteAsociadoEmisorPK == null && other.sunatContribuyenteAsociadoEmisorPK != null) || 
        		(this.sunatContribuyenteAsociadoEmisorPK != null && 
        		!this.sunatContribuyenteAsociadoEmisorPK.equals(other.sunatContribuyenteAsociadoEmisorPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatContribuyenteAsociadoEmisorPK [sunatContribuyenteAsociadoEmisorPK=" + sunatContribuyenteAsociadoEmisorPK
				+ "]";
	}

}
