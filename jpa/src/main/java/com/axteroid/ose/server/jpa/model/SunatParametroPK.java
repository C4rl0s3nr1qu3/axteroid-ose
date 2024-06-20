package com.axteroid.ose.server.jpa.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SunatParametroPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "COD_PARAMETRO")
    private String codParametro;
    @Basic(optional = false)
    @Column(name = "COD_ARGUMENTO")
    private String codArgumento;

    public SunatParametroPK() {
    }

    public SunatParametroPK(String codParametro, String codArgumento) {
        this.codParametro = codParametro;
        this.codArgumento = codArgumento;
    }

    public String getCodParametro() {
        return codParametro;
    }

    public void setCodParametro(String codParametro) {
        this.codParametro = codParametro;
    }

    public String getCodArgumento() {
        return codArgumento;
    }

    public void setCodArgumento(String codArgumento) {
        this.codArgumento = codArgumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codParametro != null ? codParametro.hashCode() : 0);
        hash += (codArgumento != null ? codArgumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SunatParametroPK)) {
            return false;
        }
        SunatParametroPK other = (SunatParametroPK) object;
        if ((this.codParametro == null && other.codParametro != null) || (this.codParametro != null && !this.codParametro.equals(other.codParametro))) {
            return false;
        }
        if ((this.codArgumento == null && other.codArgumento != null) || (this.codArgumento != null && !this.codArgumento.equals(other.codArgumento))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SunatParametroPK [codParametro=" + codParametro + ", codArgumento=" + codArgumento + "]";
	}

    
}
