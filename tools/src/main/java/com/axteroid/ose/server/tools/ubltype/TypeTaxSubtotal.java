package com.axteroid.ose.server.tools.ubltype;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeTaxSubtotal {
	
    protected String taxableAmount;
    protected BigDecimal taxAmount;
    protected String calculationSequenceNumeric;
    protected String transactionCurrencyTaxAmount;
    protected String percent;
    protected String baseUnitMeasure;
    protected String perUnitAmount;
    protected String tierRange;
    protected String tierRatePercent;
    protected TypeTaxCategory typeTaxCategory;
    
	public String getTaxableAmount() {
		return taxableAmount;
	}
	public void setTaxableAmount(String taxableAmount) {
		this.taxableAmount = taxableAmount;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public String getCalculationSequenceNumeric() {
		return calculationSequenceNumeric;
	}
	public void setCalculationSequenceNumeric(String calculationSequenceNumeric) {
		this.calculationSequenceNumeric = calculationSequenceNumeric;
	}
	public String getTransactionCurrencyTaxAmount() {
		return transactionCurrencyTaxAmount;
	}
	public void setTransactionCurrencyTaxAmount(String transactionCurrencyTaxAmount) {
		this.transactionCurrencyTaxAmount = transactionCurrencyTaxAmount;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getBaseUnitMeasure() {
		return baseUnitMeasure;
	}
	public void setBaseUnitMeasure(String baseUnitMeasure) {
		this.baseUnitMeasure = baseUnitMeasure;
	}
	public String getPerUnitAmount() {
		return perUnitAmount;
	}
	public void setPerUnitAmount(String perUnitAmount) {
		this.perUnitAmount = perUnitAmount;
	}
	public String getTierRange() {
		return tierRange;
	}
	public void setTierRange(String tierRange) {
		this.tierRange = tierRange;
	}
	public String getTierRatePercent() {
		return tierRatePercent;
	}
	public void setTierRatePercent(String tierRatePercent) {
		this.tierRatePercent = tierRatePercent;
	}
	public TypeTaxCategory getTypeTaxCategory() {
		return typeTaxCategory;
	}
	public void setTypeTaxCategory(TypeTaxCategory typeTaxCategory) {
		this.typeTaxCategory = typeTaxCategory;
	}
    
}
