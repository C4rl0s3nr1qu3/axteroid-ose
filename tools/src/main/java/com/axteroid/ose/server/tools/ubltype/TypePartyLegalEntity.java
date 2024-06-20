package com.axteroid.ose.server.tools.ubltype;

public class TypePartyLegalEntity {
    protected String registrationName;
    protected String companyID;
    protected TypeAddress registrationAddress;
    protected String corporateRegistrationScheme;
    
	public String getRegistrationName() {
		return registrationName;
	}
	public void setRegistrationName(String registrationName) {
		this.registrationName = registrationName;
	}
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public TypeAddress getRegistrationAddress() {
		return registrationAddress;
	}
	public void setRegistrationAddress(TypeAddress registrationAddress) {
		this.registrationAddress = registrationAddress;
	}
	public String getCorporateRegistrationScheme() {
		return corporateRegistrationScheme;
	}
	public void setCorporateRegistrationScheme(String corporateRegistrationScheme) {
		this.corporateRegistrationScheme = corporateRegistrationScheme;
	}
    
}
