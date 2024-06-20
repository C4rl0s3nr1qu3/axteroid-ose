package com.axteroid.ose.server.tools.ubltype;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypePayment {
    protected String id;
    protected BigDecimal paidAmount;
    protected String receivedDate;
    protected String paidDate;
    protected String paidTime;
    protected String instructionID;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	public String getPaidTime() {
		return paidTime;
	}
	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}
	public String getInstructionID() {
		return instructionID;
	}
	public void setInstructionID(String instructionID) {
		this.instructionID = instructionID;
	}

}
