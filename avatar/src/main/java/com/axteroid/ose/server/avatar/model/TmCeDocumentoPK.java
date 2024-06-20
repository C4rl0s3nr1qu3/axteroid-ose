package com.axteroid.ose.server.avatar.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TM_CE_DOCUMENTO database table.
 * 
 */
@Embeddable
public class TmCeDocumentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="\"ID\"")
	private long id;

	private String idemisor;

	private String idorigen;

	public TmCeDocumentoPK() {
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdemisor() {
		return this.idemisor;
	}
	public void setIdemisor(String idemisor) {
		this.idemisor = idemisor;
	}
	public String getIdorigen() {
		return this.idorigen;
	}
	public void setIdorigen(String idorigen) {
		this.idorigen = idorigen;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TmCeDocumentoPK)) {
			return false;
		}
		TmCeDocumentoPK castOther = (TmCeDocumentoPK)other;
		return 
			(this.id == castOther.id)
			&& this.idemisor.equals(castOther.idemisor)
			&& this.idorigen.equals(castOther.idorigen);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.id ^ (this.id >>> 32)));
		hash = hash * prime + this.idemisor.hashCode();
		hash = hash * prime + this.idorigen.hashCode();
		
		return hash;
	}
}