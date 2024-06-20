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
@Table(name = "SUNAT_AUTORIZACION_COMPROB_PAGO_FISICO", schema="AXTEROIDOSE")
@NamedQueries({
    @NamedQuery(name = "SunatAutorizacionComprobPagoFisico.findAll", query = "SELECT t FROM SunatAutorizacionComprobPagoFisico t")})
public class SunatAutorizacionComprobPagoFisico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SunatAutorizacionComprobPagoFisicoPK sunatAutorizacionComprobPagoFisicoPK;
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

    public SunatAutorizacionComprobPagoFisico() {
    }

    public SunatAutorizacionComprobPagoFisico(SunatAutorizacionComprobPagoFisicoPK sunatAutorizacionComprobPagoFisicoPK) {
        this.sunatAutorizacionComprobPagoFisicoPK = sunatAutorizacionComprobPagoFisicoPK;
    }

    public SunatAutorizacionComprobPagoFisico(SunatAutorizacionComprobPagoFisicoPK sunatAutorizacionComprobPagoFisicoPK, 
    		String userCrea, Date fechaCrea) {
        this.sunatAutorizacionComprobPagoFisicoPK = sunatAutorizacionComprobPagoFisicoPK;
        this.userCrea = userCrea;
        this.fechaCrea = fechaCrea;
    }

    public SunatAutorizacionComprobPagoFisico(long numRuc, String codCpe, String numSerieCpe, int numIniCpe) {
        this.sunatAutorizacionComprobPagoFisicoPK = new SunatAutorizacionComprobPagoFisicoPK(numRuc, codCpe, numSerieCpe, numIniCpe);
    }

    public SunatAutorizacionComprobPagoFisicoPK getSunatAutorizacionComprobPagoFisicoPK() {
        return sunatAutorizacionComprobPagoFisicoPK;
    }

    public void setSunatAutorizacionComprobPagoFisicoPK(SunatAutorizacionComprobPagoFisicoPK sunatAutorizacionComprobPagoFisicoPK) {
        this.sunatAutorizacionComprobPagoFisicoPK = sunatAutorizacionComprobPagoFisicoPK;
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
        hash += (sunatAutorizacionComprobPagoFisicoPK != null ? sunatAutorizacionComprobPagoFisicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatAutorizacionComprobPagoFisico)) {
            return false;
        }
        SunatAutorizacionComprobPagoFisico other = (SunatAutorizacionComprobPagoFisico) object;
        if ((this.sunatAutorizacionComprobPagoFisicoPK == null && other.sunatAutorizacionComprobPagoFisicoPK != null) || 
        		(this.sunatAutorizacionComprobPagoFisicoPK != null && 
        		!this.sunatAutorizacionComprobPagoFisicoPK.equals(other.sunatAutorizacionComprobPagoFisicoPK))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatAutorizacionComprobPagoFisicoPK [sunatAutorizacionComprobPagoFisicoPK=" + 
				sunatAutorizacionComprobPagoFisicoPK + "]";
	}

    
}
