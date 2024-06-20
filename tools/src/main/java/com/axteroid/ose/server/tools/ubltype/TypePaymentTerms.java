package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypePaymentTerms {
    protected String id;
    protected String paymentMeansID;
    protected String prepaidPaymentReferenceID;
    protected List<String> note;
    protected String referenceEventCode;
    protected String settlementDiscountPercent;
    protected String penaltySurchargePercent;
    protected String amount;
    protected String settlementPeriod;
    protected String penaltyPeriod;
    protected String paymentDueDate;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaymentMeansID() {
		return paymentMeansID;
	}
	public void setPaymentMeansID(String paymentMeansID) {
		this.paymentMeansID = paymentMeansID;
	}
	public String getPrepaidPaymentReferenceID() {
		return prepaidPaymentReferenceID;
	}
	public void setPrepaidPaymentReferenceID(String prepaidPaymentReferenceID) {
		this.prepaidPaymentReferenceID = prepaidPaymentReferenceID;
	}
	public List<String> getNote() {
		return note;
	}
	public void setNote(List<String> note) {
		this.note = note;
	}
	public String getReferenceEventCode() {
		return referenceEventCode;
	}
	public void setReferenceEventCode(String referenceEventCode) {
		this.referenceEventCode = referenceEventCode;
	}
	public String getSettlementDiscountPercent() {
		return settlementDiscountPercent;
	}
	public void setSettlementDiscountPercent(String settlementDiscountPercent) {
		this.settlementDiscountPercent = settlementDiscountPercent;
	}
	public String getPenaltySurchargePercent() {
		return penaltySurchargePercent;
	}
	public void setPenaltySurchargePercent(String penaltySurchargePercent) {
		this.penaltySurchargePercent = penaltySurchargePercent;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSettlementPeriod() {
		return settlementPeriod;
	}
	public void setSettlementPeriod(String settlementPeriod) {
		this.settlementPeriod = settlementPeriod;
	}
	public String getPenaltyPeriod() {
		return penaltyPeriod;
	}
	public void setPenaltyPeriod(String penaltyPeriod) {
		this.penaltyPeriod = penaltyPeriod;
	}
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
    
}
