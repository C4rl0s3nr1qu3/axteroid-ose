package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeDespatch {
	    protected String id;
	    protected String requestedDespatchDate;
	    protected String requestedDespatchTime;
	    protected String estimatedDespatchDate;
	    protected String estimatedDespatchTime;
	    protected String actualDespatchDate;
	    protected String actualDespatchTime;
	    protected String guaranteedDespatchDate;
	    protected String guaranteedDespatchTime;
	    protected String releaseID;
	    protected List<String> instructions;
	    protected TypeAddress despatchAddress;
	    protected TypeLocation despatchLocation;
	    protected TypeParty despatchParty;
	    protected TypeParty carrierParty;
	    protected List<TypeParty> notifyParty;
	    protected String contact;
	    protected String estimatedDespatchPeriod;
	    protected String requestedDespatchPeriod;
	    
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getRequestedDespatchDate() {
			return requestedDespatchDate;
		}
		public void setRequestedDespatchDate(String requestedDespatchDate) {
			this.requestedDespatchDate = requestedDespatchDate;
		}
		public String getRequestedDespatchTime() {
			return requestedDespatchTime;
		}
		public void setRequestedDespatchTime(String requestedDespatchTime) {
			this.requestedDespatchTime = requestedDespatchTime;
		}
		public String getEstimatedDespatchDate() {
			return estimatedDespatchDate;
		}
		public void setEstimatedDespatchDate(String estimatedDespatchDate) {
			this.estimatedDespatchDate = estimatedDespatchDate;
		}
		public String getEstimatedDespatchTime() {
			return estimatedDespatchTime;
		}
		public void setEstimatedDespatchTime(String estimatedDespatchTime) {
			this.estimatedDespatchTime = estimatedDespatchTime;
		}
		public String getActualDespatchDate() {
			return actualDespatchDate;
		}
		public void setActualDespatchDate(String actualDespatchDate) {
			this.actualDespatchDate = actualDespatchDate;
		}
		public String getActualDespatchTime() {
			return actualDespatchTime;
		}
		public void setActualDespatchTime(String actualDespatchTime) {
			this.actualDespatchTime = actualDespatchTime;
		}
		public String getGuaranteedDespatchDate() {
			return guaranteedDespatchDate;
		}
		public void setGuaranteedDespatchDate(String guaranteedDespatchDate) {
			this.guaranteedDespatchDate = guaranteedDespatchDate;
		}
		public String getGuaranteedDespatchTime() {
			return guaranteedDespatchTime;
		}
		public void setGuaranteedDespatchTime(String guaranteedDespatchTime) {
			this.guaranteedDespatchTime = guaranteedDespatchTime;
		}
		public String getReleaseID() {
			return releaseID;
		}
		public void setReleaseID(String releaseID) {
			this.releaseID = releaseID;
		}
		public List<String> getInstructions() {
			return instructions;
		}
		public void setInstructions(List<String> instructions) {
			this.instructions = instructions;
		}
		public TypeAddress getDespatchAddress() {
			return despatchAddress;
		}
		public void setDespatchAddress(TypeAddress despatchAddress) {
			this.despatchAddress = despatchAddress;
		}
		public TypeLocation getDespatchLocation() {
			return despatchLocation;
		}
		public void setDespatchLocation(TypeLocation despatchLocation) {
			this.despatchLocation = despatchLocation;
		}
		public TypeParty getDespatchParty() {
			return despatchParty;
		}
		public void setDespatchParty(TypeParty despatchParty) {
			this.despatchParty = despatchParty;
		}
		public TypeParty getCarrierParty() {
			return carrierParty;
		}
		public void setCarrierParty(TypeParty carrierParty) {
			this.carrierParty = carrierParty;
		}
		public List<TypeParty> getNotifyParty() {
			return notifyParty;
		}
		public void setNotifyParty(List<TypeParty> notifyParty) {
			this.notifyParty = notifyParty;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		public String getEstimatedDespatchPeriod() {
			return estimatedDespatchPeriod;
		}
		public void setEstimatedDespatchPeriod(String estimatedDespatchPeriod) {
			this.estimatedDespatchPeriod = estimatedDespatchPeriod;
		}
		public String getRequestedDespatchPeriod() {
			return requestedDespatchPeriod;
		}
		public void setRequestedDespatchPeriod(String requestedDespatchPeriod) {
			this.requestedDespatchPeriod = requestedDespatchPeriod;
		}
}
