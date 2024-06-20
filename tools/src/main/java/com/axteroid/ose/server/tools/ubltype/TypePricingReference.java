package com.axteroid.ose.server.tools.ubltype;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class TypePricingReference {
    protected BigDecimal priceAmount;
    protected String baseQuantity;
    protected List<String> priceChangeReason;
    protected String priceTypeCode;
    protected String priceType;
    protected String orderableUnitFactorRate;
    protected List<String> validityPeriod;
    protected String priceList;
    protected List<String> allowanceCharge;
    
	public BigDecimal getPriceAmount() {
		return priceAmount;
	}
	public void setPriceAmount(BigDecimal priceAmount) {
		this.priceAmount = priceAmount;
	}
	public String getBaseQuantity() {
		return baseQuantity;
	}
	public void setBaseQuantity(String baseQuantity) {
		this.baseQuantity = baseQuantity;
	}
	public List<String> getPriceChangeReason() {
		return priceChangeReason;
	}
	public void setPriceChangeReason(List<String> priceChangeReason) {
		this.priceChangeReason = priceChangeReason;
	}
	public String getPriceTypeCode() {
		return priceTypeCode;
	}
	public void setPriceTypeCode(String priceTypeCode) {
		this.priceTypeCode = priceTypeCode;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getOrderableUnitFactorRate() {
		return orderableUnitFactorRate;
	}
	public void setOrderableUnitFactorRate(String orderableUnitFactorRate) {
		this.orderableUnitFactorRate = orderableUnitFactorRate;
	}
	public List<String> getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(List<String> validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public String getPriceList() {
		return priceList;
	}
	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
	public List<String> getAllowanceCharge() {
		return allowanceCharge;
	}
	public void setAllowanceCharge(List<String> allowanceCharge) {
		this.allowanceCharge = allowanceCharge;
	}
    
}
