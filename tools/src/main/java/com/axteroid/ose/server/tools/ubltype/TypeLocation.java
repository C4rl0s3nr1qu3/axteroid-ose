package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeLocation {
    protected String id;
    protected String description;
    protected String conditions;
    protected String countrySubentity;
    protected String countrySubentityCode;
    protected List<String> validityPeriod;
    protected TypeAddress address;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public String getCountrySubentity() {
		return countrySubentity;
	}
	public void setCountrySubentity(String countrySubentity) {
		this.countrySubentity = countrySubentity;
	}
	public String getCountrySubentityCode() {
		return countrySubentityCode;
	}
	public void setCountrySubentityCode(String countrySubentityCode) {
		this.countrySubentityCode = countrySubentityCode;
	}
	public List<String> getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(List<String> validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public TypeAddress getAddress() {
		return address;
	}
	public void setAddress(TypeAddress address) {
		this.address = address;
	}
    
}
