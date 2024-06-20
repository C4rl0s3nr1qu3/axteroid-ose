package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeAllowanceCharge {

	protected String id;
    protected String chargeIndicator;
    protected String allowanceChargeReasonCode;
    protected String allowanceChargeReason;
    protected String multiplierFactorNumeric;
    protected String prepaidIndicator;
    protected String sequenceNumeric;
    protected String amount;
    protected String baseAmount;
    protected String accountingCostCode;
    protected String accountingCost;
    protected List<String> taxCategory;
    protected String taxTotal;
    protected List<String> paymentMeans;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChargeIndicator() {
		return chargeIndicator;
	}
	public void setChargeIndicator(String chargeIndicator) {
		this.chargeIndicator = chargeIndicator;
	}
	public String getAllowanceChargeReasonCode() {
		return allowanceChargeReasonCode;
	}
	public void setAllowanceChargeReasonCode(String allowanceChargeReasonCode) {
		this.allowanceChargeReasonCode = allowanceChargeReasonCode;
	}
	public String getAllowanceChargeReason() {
		return allowanceChargeReason;
	}
	public void setAllowanceChargeReason(String allowanceChargeReason) {
		this.allowanceChargeReason = allowanceChargeReason;
	}
	public String getMultiplierFactorNumeric() {
		return multiplierFactorNumeric;
	}
	public void setMultiplierFactorNumeric(String multiplierFactorNumeric) {
		this.multiplierFactorNumeric = multiplierFactorNumeric;
	}
	public String getPrepaidIndicator() {
		return prepaidIndicator;
	}
	public void setPrepaidIndicator(String prepaidIndicator) {
		this.prepaidIndicator = prepaidIndicator;
	}
	public String getSequenceNumeric() {
		return sequenceNumeric;
	}
	public void setSequenceNumeric(String sequenceNumeric) {
		this.sequenceNumeric = sequenceNumeric;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(String baseAmount) {
		this.baseAmount = baseAmount;
	}
	public String getAccountingCostCode() {
		return accountingCostCode;
	}
	public void setAccountingCostCode(String accountingCostCode) {
		this.accountingCostCode = accountingCostCode;
	}
	public String getAccountingCost() {
		return accountingCost;
	}
	public void setAccountingCost(String accountingCost) {
		this.accountingCost = accountingCost;
	}
	public List<String> getTaxCategory() {
		return taxCategory;
	}
	public void setTaxCategory(List<String> taxCategory) {
		this.taxCategory = taxCategory;
	}
	public String getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(String taxTotal) {
		this.taxTotal = taxTotal;
	}
	public List<String> getPaymentMeans() {
		return paymentMeans;
	}
	public void setPaymentMeans(List<String> paymentMeans) {
		this.paymentMeans = paymentMeans;
	}
    
    
}
