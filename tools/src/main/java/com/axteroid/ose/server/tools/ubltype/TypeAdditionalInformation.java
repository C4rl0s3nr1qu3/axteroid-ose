package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeAdditionalInformation {
	protected String id;
    protected List<TypeAdditionalMonetaryTotal> listTypeAdditionalMonetaryTotal;
    protected List<String> additionalProperty;
    protected String sunatEmbededDespatchAdvice;
    protected String sunatTransaction;
    protected String sunatCosts;
    protected String sunatDeductions;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getAdditionalProperty() {
		return additionalProperty;
	}
	public void setAdditionalProperty(List<String> additionalProperty) {
		this.additionalProperty = additionalProperty;
	}
	public String getSunatEmbededDespatchAdvice() {
		return sunatEmbededDespatchAdvice;
	}
	public void setSunatEmbededDespatchAdvice(String sunatEmbededDespatchAdvice) {
		this.sunatEmbededDespatchAdvice = sunatEmbededDespatchAdvice;
	}
	public String getSunatTransaction() {
		return sunatTransaction;
	}
	public void setSunatTransaction(String sunatTransaction) {
		this.sunatTransaction = sunatTransaction;
	}
	public String getSunatCosts() {
		return sunatCosts;
	}
	public void setSunatCosts(String sunatCosts) {
		this.sunatCosts = sunatCosts;
	}
	public String getSunatDeductions() {
		return sunatDeductions;
	}
	public void setSunatDeductions(String sunatDeductions) {
		this.sunatDeductions = sunatDeductions;
	}
	public List<TypeAdditionalMonetaryTotal> getListTypeAdditionalMonetaryTotal() {
		return listTypeAdditionalMonetaryTotal;
	}
	public void setListTypeAdditionalMonetaryTotal(List<TypeAdditionalMonetaryTotal> listTypeAdditionalMonetaryTotal) {
		this.listTypeAdditionalMonetaryTotal = listTypeAdditionalMonetaryTotal;
	}
    
}
