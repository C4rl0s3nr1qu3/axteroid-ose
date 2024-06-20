package com.axteroid.ose.server.tools.ubltype;

public class TypeDelivery {
    protected String id;
    protected String quantity;
    protected String minimumQuantity;
    protected String maximumQuantity;
    protected String actualDeliveryDate;
    protected String actualDeliveryTime;
    protected String latestDeliveryDate;
    protected String latestDeliveryTime;
    protected String trackingID;
    protected TypeAddress deliveryAddress;
    protected TypeLocation deliveryLocation;
    protected String requestedDeliveryPeriod;
    protected String promisedDeliveryPeriod;
    protected String estimatedDeliveryPeriod;
    protected String deliveryParty;
    protected TypeDespatch despatch;
    protected TypeShipment shipment;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getMinimumQuantity() {
		return minimumQuantity;
	}
	public void setMinimumQuantity(String minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}
	public String getMaximumQuantity() {
		return maximumQuantity;
	}
	public void setMaximumQuantity(String maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}
	public String getActualDeliveryDate() {
		return actualDeliveryDate;
	}
	public void setActualDeliveryDate(String actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}
	public String getActualDeliveryTime() {
		return actualDeliveryTime;
	}
	public void setActualDeliveryTime(String actualDeliveryTime) {
		this.actualDeliveryTime = actualDeliveryTime;
	}
	public String getLatestDeliveryDate() {
		return latestDeliveryDate;
	}
	public void setLatestDeliveryDate(String latestDeliveryDate) {
		this.latestDeliveryDate = latestDeliveryDate;
	}
	public String getLatestDeliveryTime() {
		return latestDeliveryTime;
	}
	public void setLatestDeliveryTime(String latestDeliveryTime) {
		this.latestDeliveryTime = latestDeliveryTime;
	}
	public String getTrackingID() {
		return trackingID;
	}
	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}
	public TypeAddress getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(TypeAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public TypeLocation getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(TypeLocation deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	public String getRequestedDeliveryPeriod() {
		return requestedDeliveryPeriod;
	}
	public void setRequestedDeliveryPeriod(String requestedDeliveryPeriod) {
		this.requestedDeliveryPeriod = requestedDeliveryPeriod;
	}
	public String getPromisedDeliveryPeriod() {
		return promisedDeliveryPeriod;
	}
	public void setPromisedDeliveryPeriod(String promisedDeliveryPeriod) {
		this.promisedDeliveryPeriod = promisedDeliveryPeriod;
	}
	public String getEstimatedDeliveryPeriod() {
		return estimatedDeliveryPeriod;
	}
	public void setEstimatedDeliveryPeriod(String estimatedDeliveryPeriod) {
		this.estimatedDeliveryPeriod = estimatedDeliveryPeriod;
	}
	public String getDeliveryParty() {
		return deliveryParty;
	}
	public void setDeliveryParty(String deliveryParty) {
		this.deliveryParty = deliveryParty;
	}
	public TypeShipment getShipment() {
		return shipment;
	}
	public void setShipment(TypeShipment shipment) {
		this.shipment = shipment;
	}
	public TypeDespatch getDespatch() {
		return despatch;
	}
	public void setDespatch(TypeDespatch despatch) {
		this.despatch = despatch;
	}
}
