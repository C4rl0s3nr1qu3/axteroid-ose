package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeShipment {
    protected String id;
    protected String shippingPriorityLevelCode;
    protected String handlingCode;
    protected List<String> handlingInstructions;
    protected List<String> information;
    protected String grossWeightMeasure;
    protected String netWeightMeasure;
    protected String netNetWeightMeasure;
    protected String grossVolumeMeasure;
    protected String netVolumeMeasure;
    protected String totalGoodsItemQuantity;
    protected String totalTransportHandlingUnitQuantity;
    protected String insuranceValueAmount;
    protected String declaredCustomsValueAmount;
    protected String declaredForCarriageValueAmount;
    protected String declaredStatisticsValueAmount;
    protected String freeOnBoardValueAmount;
    protected List<String> specialInstructions;
    protected List<String> deliveryInstructions;
    protected String splitConsignmentIndicator;
    protected String consignmentQuantity;
    protected List<TypeConsignment> consignment;
    protected List<String> goodsItem;
    protected List<String> shipmentStage;
    protected TypeDelivery delivery;
    protected List<String> transportHandlingUnit;
    protected TypeAddress returnAddress;
    protected TypeAddress originAddress;
    protected TypeLocation firstArrivalPortLocation;
    protected TypeLocation lastExitPortLocation;
    protected String exportCountry;
    protected List<String> freightAllowanceCharge;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShippingPriorityLevelCode() {
		return shippingPriorityLevelCode;
	}
	public void setShippingPriorityLevelCode(String shippingPriorityLevelCode) {
		this.shippingPriorityLevelCode = shippingPriorityLevelCode;
	}
	public String getHandlingCode() {
		return handlingCode;
	}
	public void setHandlingCode(String handlingCode) {
		this.handlingCode = handlingCode;
	}
	public List<String> getHandlingInstructions() {
		return handlingInstructions;
	}
	public void setHandlingInstructions(List<String> handlingInstructions) {
		this.handlingInstructions = handlingInstructions;
	}
	public List<String> getInformation() {
		return information;
	}
	public void setInformation(List<String> information) {
		this.information = information;
	}
	public String getGrossWeightMeasure() {
		return grossWeightMeasure;
	}
	public void setGrossWeightMeasure(String grossWeightMeasure) {
		this.grossWeightMeasure = grossWeightMeasure;
	}
	public String getNetWeightMeasure() {
		return netWeightMeasure;
	}
	public void setNetWeightMeasure(String netWeightMeasure) {
		this.netWeightMeasure = netWeightMeasure;
	}
	public String getNetNetWeightMeasure() {
		return netNetWeightMeasure;
	}
	public void setNetNetWeightMeasure(String netNetWeightMeasure) {
		this.netNetWeightMeasure = netNetWeightMeasure;
	}
	public String getGrossVolumeMeasure() {
		return grossVolumeMeasure;
	}
	public void setGrossVolumeMeasure(String grossVolumeMeasure) {
		this.grossVolumeMeasure = grossVolumeMeasure;
	}
	public String getNetVolumeMeasure() {
		return netVolumeMeasure;
	}
	public void setNetVolumeMeasure(String netVolumeMeasure) {
		this.netVolumeMeasure = netVolumeMeasure;
	}
	public String getTotalGoodsItemQuantity() {
		return totalGoodsItemQuantity;
	}
	public void setTotalGoodsItemQuantity(String totalGoodsItemQuantity) {
		this.totalGoodsItemQuantity = totalGoodsItemQuantity;
	}
	public String getTotalTransportHandlingUnitQuantity() {
		return totalTransportHandlingUnitQuantity;
	}
	public void setTotalTransportHandlingUnitQuantity(String totalTransportHandlingUnitQuantity) {
		this.totalTransportHandlingUnitQuantity = totalTransportHandlingUnitQuantity;
	}
	public String getInsuranceValueAmount() {
		return insuranceValueAmount;
	}
	public void setInsuranceValueAmount(String insuranceValueAmount) {
		this.insuranceValueAmount = insuranceValueAmount;
	}
	public String getDeclaredCustomsValueAmount() {
		return declaredCustomsValueAmount;
	}
	public void setDeclaredCustomsValueAmount(String declaredCustomsValueAmount) {
		this.declaredCustomsValueAmount = declaredCustomsValueAmount;
	}
	public String getDeclaredForCarriageValueAmount() {
		return declaredForCarriageValueAmount;
	}
	public void setDeclaredForCarriageValueAmount(String declaredForCarriageValueAmount) {
		this.declaredForCarriageValueAmount = declaredForCarriageValueAmount;
	}
	public String getDeclaredStatisticsValueAmount() {
		return declaredStatisticsValueAmount;
	}
	public void setDeclaredStatisticsValueAmount(String declaredStatisticsValueAmount) {
		this.declaredStatisticsValueAmount = declaredStatisticsValueAmount;
	}
	public String getFreeOnBoardValueAmount() {
		return freeOnBoardValueAmount;
	}
	public void setFreeOnBoardValueAmount(String freeOnBoardValueAmount) {
		this.freeOnBoardValueAmount = freeOnBoardValueAmount;
	}
	public List<String> getSpecialInstructions() {
		return specialInstructions;
	}
	public void setSpecialInstructions(List<String> specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	public List<String> getDeliveryInstructions() {
		return deliveryInstructions;
	}
	public void setDeliveryInstructions(List<String> deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}
	public String getSplitConsignmentIndicator() {
		return splitConsignmentIndicator;
	}
	public void setSplitConsignmentIndicator(String splitConsignmentIndicator) {
		this.splitConsignmentIndicator = splitConsignmentIndicator;
	}
	public String getConsignmentQuantity() {
		return consignmentQuantity;
	}
	public void setConsignmentQuantity(String consignmentQuantity) {
		this.consignmentQuantity = consignmentQuantity;
	}
	public List<String> getGoodsItem() {
		return goodsItem;
	}
	public void setGoodsItem(List<String> goodsItem) {
		this.goodsItem = goodsItem;
	}
	public List<String> getShipmentStage() {
		return shipmentStage;
	}
	public void setShipmentStage(List<String> shipmentStage) {
		this.shipmentStage = shipmentStage;
	}
	public TypeDelivery getDelivery() {
		return delivery;
	}
	public void setDelivery(TypeDelivery delivery) {
		this.delivery = delivery;
	}
	public List<String> getTransportHandlingUnit() {
		return transportHandlingUnit;
	}
	public void setTransportHandlingUnit(List<String> transportHandlingUnit) {
		this.transportHandlingUnit = transportHandlingUnit;
	}
	public TypeAddress getReturnAddress() {
		return returnAddress;
	}
	public void setReturnAddress(TypeAddress returnAddress) {
		this.returnAddress = returnAddress;
	}
	public TypeAddress getOriginAddress() {
		return originAddress;
	}
	public void setOriginAddress(TypeAddress originAddress) {
		this.originAddress = originAddress;
	}
	public TypeLocation getFirstArrivalPortLocation() {
		return firstArrivalPortLocation;
	}
	public void setFirstArrivalPortLocation(TypeLocation firstArrivalPortLocation) {
		this.firstArrivalPortLocation = firstArrivalPortLocation;
	}
	public TypeLocation getLastExitPortLocation() {
		return lastExitPortLocation;
	}
	public void setLastExitPortLocation(TypeLocation lastExitPortLocation) {
		this.lastExitPortLocation = lastExitPortLocation;
	}
	public String getExportCountry() {
		return exportCountry;
	}
	public void setExportCountry(String exportCountry) {
		this.exportCountry = exportCountry;
	}
	public List<String> getFreightAllowanceCharge() {
		return freightAllowanceCharge;
	}
	public void setFreightAllowanceCharge(List<String> freightAllowanceCharge) {
		this.freightAllowanceCharge = freightAllowanceCharge;
	}
	public List<TypeConsignment> getConsignment() {
		return consignment;
	}
	public void setConsignment(List<TypeConsignment> consignment) {
		this.consignment = consignment;
	}
}
