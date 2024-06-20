package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeBillingReference {

    protected TypeDocumentReference invoiceDocumentReference;
    protected TypeDocumentReference selfBilledInvoiceDocumentReference;
    protected TypeDocumentReference creditNoteDocumentReference;
    protected TypeDocumentReference selfBilledCreditNoteDocumentReference;
    protected TypeDocumentReference debitNoteDocumentReference;
    protected TypeDocumentReference reminderDocumentReference;
    protected TypeDocumentReference additionalDocumentReference;
    protected List<String> billingReferenceLine;
    
	public TypeDocumentReference getInvoiceDocumentReference() {
		return invoiceDocumentReference;
	}
	public void setInvoiceDocumentReference(TypeDocumentReference invoiceDocumentReference) {
		this.invoiceDocumentReference = invoiceDocumentReference;
	}
	public TypeDocumentReference getSelfBilledInvoiceDocumentReference() {
		return selfBilledInvoiceDocumentReference;
	}
	public void setSelfBilledInvoiceDocumentReference(TypeDocumentReference selfBilledInvoiceDocumentReference) {
		this.selfBilledInvoiceDocumentReference = selfBilledInvoiceDocumentReference;
	}
	public TypeDocumentReference getCreditNoteDocumentReference() {
		return creditNoteDocumentReference;
	}
	public void setCreditNoteDocumentReference(TypeDocumentReference creditNoteDocumentReference) {
		this.creditNoteDocumentReference = creditNoteDocumentReference;
	}
	public TypeDocumentReference getSelfBilledCreditNoteDocumentReference() {
		return selfBilledCreditNoteDocumentReference;
	}
	public void setSelfBilledCreditNoteDocumentReference(TypeDocumentReference selfBilledCreditNoteDocumentReference) {
		this.selfBilledCreditNoteDocumentReference = selfBilledCreditNoteDocumentReference;
	}
	public TypeDocumentReference getDebitNoteDocumentReference() {
		return debitNoteDocumentReference;
	}
	public void setDebitNoteDocumentReference(TypeDocumentReference debitNoteDocumentReference) {
		this.debitNoteDocumentReference = debitNoteDocumentReference;
	}
	public TypeDocumentReference getReminderDocumentReference() {
		return reminderDocumentReference;
	}
	public void setReminderDocumentReference(TypeDocumentReference reminderDocumentReference) {
		this.reminderDocumentReference = reminderDocumentReference;
	}
	public TypeDocumentReference getAdditionalDocumentReference() {
		return additionalDocumentReference;
	}
	public void setAdditionalDocumentReference(TypeDocumentReference additionalDocumentReference) {
		this.additionalDocumentReference = additionalDocumentReference;
	}
	public List<String> getBillingReferenceLine() {
		return billingReferenceLine;
	}
	public void setBillingReferenceLine(List<String> billingReferenceLine) {
		this.billingReferenceLine = billingReferenceLine;
	}
    
}
