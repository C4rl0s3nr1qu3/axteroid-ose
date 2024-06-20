package com.axteroid.ose.server.tools.ubltype;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeTaxTotal {
    protected BigDecimal taxAmount;
    protected String roundingAmount;
    protected String taxEvidenceIndicator;
    protected List<TypeTaxSubtotal> ListTaxSubtotal;
    
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public String getRoundingAmount() {
		return roundingAmount;
	}
	public void setRoundingAmount(String roundingAmount) {
		this.roundingAmount = roundingAmount;
	}
	public String getTaxEvidenceIndicator() {
		return taxEvidenceIndicator;
	}
	public void setTaxEvidenceIndicator(String taxEvidenceIndicator) {
		this.taxEvidenceIndicator = taxEvidenceIndicator;
	}
	public List<TypeTaxSubtotal> getListTaxSubtotal() {
		return ListTaxSubtotal;
	}
	public void setListTaxSubtotal(List<TypeTaxSubtotal> listTaxSubtotal) {
		ListTaxSubtotal = listTaxSubtotal;
	}

    
}
