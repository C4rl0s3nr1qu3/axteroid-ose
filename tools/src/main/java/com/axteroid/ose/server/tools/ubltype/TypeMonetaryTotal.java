package com.axteroid.ose.server.tools.ubltype;

import java.math.BigDecimal;

public class TypeMonetaryTotal {
    protected BigDecimal lineExtensionAmount;
    protected BigDecimal taxExclusiveAmount;
    protected BigDecimal taxInclusiveAmount;
    protected BigDecimal allowanceTotalAmount;
    protected BigDecimal chargeTotalAmount;
    protected BigDecimal prepaidAmount;
    protected BigDecimal payableRoundingAmount;
    protected BigDecimal payableAmount;
    
	public BigDecimal getLineExtensionAmount() {
		return lineExtensionAmount;
	}
	public void setLineExtensionAmount(BigDecimal lineExtensionAmount) {
		this.lineExtensionAmount = lineExtensionAmount;
	}
	public BigDecimal getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}
	public void setTaxExclusiveAmount(BigDecimal taxExclusiveAmount) {
		this.taxExclusiveAmount = taxExclusiveAmount;
	}
	public BigDecimal getTaxInclusiveAmount() {
		return taxInclusiveAmount;
	}
	public void setTaxInclusiveAmount(BigDecimal taxInclusiveAmount) {
		this.taxInclusiveAmount = taxInclusiveAmount;
	}
	public BigDecimal getAllowanceTotalAmount() {
		return allowanceTotalAmount;
	}
	public void setAllowanceTotalAmount(BigDecimal allowanceTotalAmount) {
		this.allowanceTotalAmount = allowanceTotalAmount;
	}
	public BigDecimal getChargeTotalAmount() {
		return chargeTotalAmount;
	}
	public void setChargeTotalAmount(BigDecimal chargeTotalAmount) {
		this.chargeTotalAmount = chargeTotalAmount;
	}
	public BigDecimal getPrepaidAmount() {
		return prepaidAmount;
	}
	public void setPrepaidAmount(BigDecimal prepaidAmount) {
		this.prepaidAmount = prepaidAmount;
	}
	public BigDecimal getPayableRoundingAmount() {
		return payableRoundingAmount;
	}
	public void setPayableRoundingAmount(BigDecimal payableRoundingAmount) {
		this.payableRoundingAmount = payableRoundingAmount;
	}
	public BigDecimal getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
    
}
