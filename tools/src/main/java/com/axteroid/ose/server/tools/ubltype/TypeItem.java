package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)

public class TypeItem {

    protected List<String> description;
    protected String packQuantity;
    protected String packSizeNumeric;
    protected String catalogueIndicator;
    protected String name;
    protected String hazardousRiskIndicator;
    protected String additionalInformation;
    protected List<String> keyword;
    protected List<String> brandName;
    protected List<String> modelName;    
    protected String buyersItemIdentification;    
    protected String sellersItemIdentification;    
    protected List<String> manufacturersItemIdentification;    
    protected String standardItemIdentification;    
    protected String catalogueItemIdentification;    
    protected List<String> additionalItemIdentification;    
    protected String catalogueDocumentReference;    
    protected List<String> itemSpecificationDocumentReference;    
    protected String originCountry;    
    protected List<TypeCommodityClassification> commodityClassification;    
    protected List<String> transactionConditions;    
    protected List<String> hazardousItem;    
    protected List<String> classifiedTaxCategory;    
    protected List<TypeItemProperty> additionalItemProperty;    
    protected List<String> manufacturerParty;    
    protected String informationContentProviderParty;    
    protected List<String> originAddress;    
    protected List<String> itemInstance;
    
	public List<String> getDescription() {
		return description;
	}
	public void setDescription(List<String> description) {
		this.description = description;
	}
	public String getPackQuantity() {
		return packQuantity;
	}
	public void setPackQuantity(String packQuantity) {
		this.packQuantity = packQuantity;
	}
	public String getPackSizeNumeric() {
		return packSizeNumeric;
	}
	public void setPackSizeNumeric(String packSizeNumeric) {
		this.packSizeNumeric = packSizeNumeric;
	}
	public String getCatalogueIndicator() {
		return catalogueIndicator;
	}
	public void setCatalogueIndicator(String catalogueIndicator) {
		this.catalogueIndicator = catalogueIndicator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHazardousRiskIndicator() {
		return hazardousRiskIndicator;
	}
	public void setHazardousRiskIndicator(String hazardousRiskIndicator) {
		this.hazardousRiskIndicator = hazardousRiskIndicator;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public List<String> getKeyword() {
		return keyword;
	}
	public void setKeyword(List<String> keyword) {
		this.keyword = keyword;
	}
	public List<String> getBrandName() {
		return brandName;
	}
	public void setBrandName(List<String> brandName) {
		this.brandName = brandName;
	}
	public List<String> getModelName() {
		return modelName;
	}
	public void setModelName(List<String> modelName) {
		this.modelName = modelName;
	}
	public String getBuyersItemIdentification() {
		return buyersItemIdentification;
	}
	public void setBuyersItemIdentification(String buyersItemIdentification) {
		this.buyersItemIdentification = buyersItemIdentification;
	}
	public String getSellersItemIdentification() {
		return sellersItemIdentification;
	}
	public void setSellersItemIdentification(String sellersItemIdentification) {
		this.sellersItemIdentification = sellersItemIdentification;
	}
	public List<String> getManufacturersItemIdentification() {
		return manufacturersItemIdentification;
	}
	public void setManufacturersItemIdentification(List<String> manufacturersItemIdentification) {
		this.manufacturersItemIdentification = manufacturersItemIdentification;
	}
	public String getStandardItemIdentification() {
		return standardItemIdentification;
	}
	public void setStandardItemIdentification(String standardItemIdentification) {
		this.standardItemIdentification = standardItemIdentification;
	}
	public String getCatalogueItemIdentification() {
		return catalogueItemIdentification;
	}
	public void setCatalogueItemIdentification(String catalogueItemIdentification) {
		this.catalogueItemIdentification = catalogueItemIdentification;
	}
	public List<String> getAdditionalItemIdentification() {
		return additionalItemIdentification;
	}
	public void setAdditionalItemIdentification(List<String> additionalItemIdentification) {
		this.additionalItemIdentification = additionalItemIdentification;
	}
	public String getCatalogueDocumentReference() {
		return catalogueDocumentReference;
	}
	public void setCatalogueDocumentReference(String catalogueDocumentReference) {
		this.catalogueDocumentReference = catalogueDocumentReference;
	}
	public List<String> getItemSpecificationDocumentReference() {
		return itemSpecificationDocumentReference;
	}
	public void setItemSpecificationDocumentReference(List<String> itemSpecificationDocumentReference) {
		this.itemSpecificationDocumentReference = itemSpecificationDocumentReference;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public List<TypeCommodityClassification> getCommodityClassification() {
		return commodityClassification;
	}
	public void setCommodityClassification(List<TypeCommodityClassification> commodityClassification) {
		this.commodityClassification = commodityClassification;
	}
	public List<String> getTransactionConditions() {
		return transactionConditions;
	}
	public void setTransactionConditions(List<String> transactionConditions) {
		this.transactionConditions = transactionConditions;
	}
	public List<String> getHazardousItem() {
		return hazardousItem;
	}
	public void setHazardousItem(List<String> hazardousItem) {
		this.hazardousItem = hazardousItem;
	}
	public List<String> getClassifiedTaxCategory() {
		return classifiedTaxCategory;
	}
	public void setClassifiedTaxCategory(List<String> classifiedTaxCategory) {
		this.classifiedTaxCategory = classifiedTaxCategory;
	}
	public List<String> getManufacturerParty() {
		return manufacturerParty;
	}
	public void setManufacturerParty(List<String> manufacturerParty) {
		this.manufacturerParty = manufacturerParty;
	}
	public String getInformationContentProviderParty() {
		return informationContentProviderParty;
	}
	public void setInformationContentProviderParty(String informationContentProviderParty) {
		this.informationContentProviderParty = informationContentProviderParty;
	}
	public List<String> getOriginAddress() {
		return originAddress;
	}
	public void setOriginAddress(List<String> originAddress) {
		this.originAddress = originAddress;
	}
	public List<String> getItemInstance() {
		return itemInstance;
	}
	public void setItemInstance(List<String> itemInstance) {
		this.itemInstance = itemInstance;
	}
	public List<TypeItemProperty> getAdditionalItemProperty() {
		return additionalItemProperty;
	}
	public void setAdditionalItemProperty(List<TypeItemProperty> additionalItemProperty) {
		this.additionalItemProperty = additionalItemProperty;
	}
    
}
