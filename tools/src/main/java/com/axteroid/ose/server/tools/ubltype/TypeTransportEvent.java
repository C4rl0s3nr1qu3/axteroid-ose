package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeTransportEvent {
    protected String identificationID;
    protected String occurrenceDate;
    protected String occurrenceTime;
    protected String transportEventTypeCode;
    protected List<String> description;
    protected String completionIndicator;
    protected String reportedShipment;
    protected List<String> currentStatus;
    protected List<String> contact;
    protected TypeLocation location;
    protected TypeSignature signature;
    protected List<String> period;
    
	public String getIdentificationID() {
		return identificationID;
	}
	public void setIdentificationID(String identificationID) {
		this.identificationID = identificationID;
	}
	public String getOccurrenceDate() {
		return occurrenceDate;
	}
	public void setOccurrenceDate(String occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}
	public String getOccurrenceTime() {
		return occurrenceTime;
	}
	public void setOccurrenceTime(String occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}
	public String getTransportEventTypeCode() {
		return transportEventTypeCode;
	}
	public void setTransportEventTypeCode(String transportEventTypeCode) {
		this.transportEventTypeCode = transportEventTypeCode;
	}
	public List<String> getDescription() {
		return description;
	}
	public void setDescription(List<String> description) {
		this.description = description;
	}
	public String getCompletionIndicator() {
		return completionIndicator;
	}
	public void setCompletionIndicator(String completionIndicator) {
		this.completionIndicator = completionIndicator;
	}
	public String getReportedShipment() {
		return reportedShipment;
	}
	public void setReportedShipment(String reportedShipment) {
		this.reportedShipment = reportedShipment;
	}
	public List<String> getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(List<String> currentStatus) {
		this.currentStatus = currentStatus;
	}
	public List<String> getContact() {
		return contact;
	}
	public void setContact(List<String> contact) {
		this.contact = contact;
	}
	public TypeLocation getLocation() {
		return location;
	}
	public void setLocation(TypeLocation location) {
		this.location = location;
	}
	public TypeSignature getSignature() {
		return signature;
	}
	public void setSignature(TypeSignature signature) {
		this.signature = signature;
	}
	public List<String> getPeriod() {
		return period;
	}
	public void setPeriod(List<String> period) {
		this.period = period;
	}
    
}
