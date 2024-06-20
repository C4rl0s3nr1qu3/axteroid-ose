package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeSupplierParty {
	
    protected String customerAssignedAccountID;
    protected List<String> additionalAccountID;
    protected String dataSendingCapability;
	protected TypeParty party;
    protected String despatchContact;
    protected String accountingContact;
    protected String sellerContact;
    
	public String getCustomerAssignedAccountID() {
		return customerAssignedAccountID;
	}
	public void setCustomerAssignedAccountID(String customerAssignedAccountID) {
		this.customerAssignedAccountID = customerAssignedAccountID;
	}
	public List<String> getAdditionalAccountID() {
		return additionalAccountID;
	}
	public void setAdditionalAccountID(List<String> additionalAccountID) {
		this.additionalAccountID = additionalAccountID;
	}
	public String getDataSendingCapability() {
		return dataSendingCapability;
	}
	public void setDataSendingCapability(String dataSendingCapability) {
		this.dataSendingCapability = dataSendingCapability;
	}
	public TypeParty getParty() {
		return party;
	}
	public void setParty(TypeParty party) {
		this.party = party;
	}
	public String getDespatchContact() {
		return despatchContact;
	}
	public void setDespatchContact(String despatchContact) {
		this.despatchContact = despatchContact;
	}
	public String getAccountingContact() {
		return accountingContact;
	}
	public void setAccountingContact(String accountingContact) {
		this.accountingContact = accountingContact;
	}
	public String getSellerContact() {
		return sellerContact;
	}
	public void setSellerContact(String sellerContact) {
		this.sellerContact = sellerContact;
	}
	
}
