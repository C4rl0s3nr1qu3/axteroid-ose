package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeCustomerParty {
    protected String customerAssignedAccountID;
    protected String supplierAssignedAccountID;
    protected List<String> additionalAccountID;
    protected TypeParty party;
    protected String deliveryContact;
    protected String accountingContact;
    protected String buyerContact;
    
	public String getCustomerAssignedAccountID() {
		return customerAssignedAccountID;
	}
	public void setCustomerAssignedAccountID(String customerAssignedAccountID) {
		this.customerAssignedAccountID = customerAssignedAccountID;
	}
	public String getSupplierAssignedAccountID() {
		return supplierAssignedAccountID;
	}
	public void setSupplierAssignedAccountID(String supplierAssignedAccountID) {
		this.supplierAssignedAccountID = supplierAssignedAccountID;
	}
	public List<String> getAdditionalAccountID() {
		return additionalAccountID;
	}
	public void setAdditionalAccountID(List<String> additionalAccountID) {
		this.additionalAccountID = additionalAccountID;
	}
	public TypeParty getParty() {
		return party;
	}
	public void setParty(TypeParty party) {
		this.party = party;
	}
	public String getDeliveryContact() {
		return deliveryContact;
	}
	public void setDeliveryContact(String deliveryContact) {
		this.deliveryContact = deliveryContact;
	}
	public String getAccountingContact() {
		return accountingContact;
	}
	public void setAccountingContact(String accountingContact) {
		this.accountingContact = accountingContact;
	}
	public String getBuyerContact() {
		return buyerContact;
	}
	public void setBuyerContact(String buyerContact) {
		this.buyerContact = buyerContact;
	}
    
}
