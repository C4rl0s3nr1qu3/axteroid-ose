package com.axteroid.ose.server.ubl20.gateway.sunat.dozer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DeliveryDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaidDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentDueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReferenceDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ResponseDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ResponseTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StartDateType;

//import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatPerceptionDateType;
import sunat.names.specification.ubl.peru.schema.xsd.sunataggregatecomponents_1.SunatRetentionDateType;

/**
 * User: RAC
 * Date: 16/02/12
 */
public class DateToXmlGregorianCalendarConverter implements ConfigurableCustomConverter {
	//private static final Logger log = Logger.getLogger(DateToXmlGregorianCalendarConverter.class);
	private static final Logger log = LoggerFactory.getLogger(DateToXmlGregorianCalendarConverter.class);
    protected String parameter;

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        Date value = ((Date) source);

        if(value==null) return null;

        if("referenceDate".equals(parameter)){
            ReferenceDateType referenceDateType= new ReferenceDateType();
            referenceDateType.setValue(value);
            return referenceDateType;
        } else if("paymentDueDate".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);			
			PaymentDueDateType pddType = new PaymentDueDateType();
			pddType.setValue(xgc);
			return pddType;
        } else if("deliveryDate".equals(parameter)){
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	DeliveryDateType deliveryDateType = new DeliveryDateType();
        	deliveryDateType.setValue(xgc);
        	return deliveryDateType;
        } else if("startDate".equals(parameter)){
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	StartDateType startDateType = new StartDateType();
			startDateType.setValue(xgc);
        	return startDateType;
        } else if("paidDate".equals(parameter)){
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	PaidDateType paidDateType = new PaidDateType();
			paidDateType.setValue(xgc);
        	return paidDateType;
        }  else if("sunatRetentionDate".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	SunatRetentionDateType sunatRetentionDate = new SunatRetentionDateType();
        	sunatRetentionDate.setValue(xgc);
        	return sunatRetentionDate;
        } else if("sunatPerceptionDate".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	SunatPerceptionDateType sunatPerceptionDate = new SunatPerceptionDateType();
        	sunatPerceptionDate.setValue(xgc);
        	return sunatPerceptionDate;
        }else if("Date".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	DateType date = new DateType();
        	date.setValue(xgc);
        	return date;
        }else if("IssueTimeType".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertTimeToXMLGC(value);
        	IssueTimeType idType = new IssueTimeType();
            idType.setValue(xgc); 
            return idType;
        }else if("ResponseDateType".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertDateToXMLGC(value);
        	ResponseDateType idType = new ResponseDateType();
            idType.setValue(xgc); 
            return idType;
        }else if("ResponseTimeType".equals(parameter)) {
        	XMLGregorianCalendar xgc = convertTimeToXMLGC(value);
        	ResponseTimeType idType = new ResponseTimeType();
            idType.setValue(xgc); 
            return idType;            
        } else {
        	IssueDateType idType = new IssueDateType();
            idType.setValue(value);
            return idType;
        }
    }
    
    public XMLGregorianCalendar convertDateToXMLGC(Date value){
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(value);
    	XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}
		return xgc;
    }
    
    public XMLGregorianCalendar convertTimeToXMLGC(Date value){
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(value);    	
    	//TimeZone timeZone = TimeZone.getTimeZone("America/Lima");
    	gc.setTimeZone(TimeZone.getTimeZone("UTC"));
    	XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY), gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), gc.get(Calendar.MILLISECOND), gc.get(Calendar.ZONE_OFFSET));			
		} catch (DatatypeConfigurationException e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);			
		}
		return xgc;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
