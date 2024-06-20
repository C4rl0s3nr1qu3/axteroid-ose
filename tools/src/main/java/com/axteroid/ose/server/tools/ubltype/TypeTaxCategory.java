package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeTaxCategory {
    protected String id;
    protected String name;
    protected String percent;
    protected String baseUnitMeasure;
    protected String perUnitAmount;
    protected String taxExemptionReasonCode;
    protected String taxExemptionReason;
    protected String tierRange;
    protected String tierRatePercent;
    protected String taxScheme;
    protected String taxSchemeId;
    protected String taxSchemeName;
    protected String taxSchemeTaxTypeCode;
    protected String taxSchemeCurrencyCode;
    protected List<String> ListTaxSchemeJurisdictionRegionAddress;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getTaxExemptionReasonCode() {
		return taxExemptionReasonCode;
	}
	public void setTaxExemptionReasonCode(String taxExemptionReasonCode) {
		this.taxExemptionReasonCode = taxExemptionReasonCode;
	}
	public String getTaxExemptionReason() {
		return taxExemptionReason;
	}
	public void setTaxExemptionReason(String taxExemptionReason) {
		this.taxExemptionReason = taxExemptionReason;
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
	public String getTaxScheme() {
		return taxScheme;
	}
	public void setTaxScheme(String taxScheme) {
		this.taxScheme = taxScheme;
	}
	public String getTaxSchemeId() {
		return taxSchemeId;
	}
	public void setTaxSchemeId(String taxSchemeId) {
		this.taxSchemeId = taxSchemeId;
	}
	public String getTaxSchemeName() {
		return taxSchemeName;
	}
	public void setTaxSchemeName(String taxSchemeName) {
		this.taxSchemeName = taxSchemeName;
	}
	public String getTaxSchemeTaxTypeCode() {
		return taxSchemeTaxTypeCode;
	}
	public void setTaxSchemeTaxTypeCode(String taxSchemeTaxTypeCode) {
		this.taxSchemeTaxTypeCode = taxSchemeTaxTypeCode;
	}
	public String getTaxSchemeCurrencyCode() {
		return taxSchemeCurrencyCode;
	}
	public void setTaxSchemeCurrencyCode(String taxSchemeCurrencyCode) {
		this.taxSchemeCurrencyCode = taxSchemeCurrencyCode;
	}
	public List<String> getListTaxSchemeJurisdictionRegionAddress() {
		return ListTaxSchemeJurisdictionRegionAddress;
	}
	public void setListTaxSchemeJurisdictionRegionAddress(List<String> listTaxSchemeJurisdictionRegionAddress) {
		ListTaxSchemeJurisdictionRegionAddress = listTaxSchemeJurisdictionRegionAddress;
	}
    
    
}
