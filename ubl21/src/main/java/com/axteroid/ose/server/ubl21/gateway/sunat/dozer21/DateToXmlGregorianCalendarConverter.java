package com.axteroid.ose.server.ubl21.gateway.sunat.dozer21;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

//import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ResponseDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2_1.ResponseTimeType;


public class DateToXmlGregorianCalendarConverter implements ConfigurableCustomConverter {
	//private static final Logger log = Logger.getLogger(DateToXmlGregorianCalendarConverter.class);
	private static final Logger log = LoggerFactory.getLogger(DateToXmlGregorianCalendarConverter.class);
    protected String parameter;

    @SuppressWarnings("rawtypes")
	public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        Date value = ((Date) source);
        if(value==null) return null;
       	//log.info("21 value --> "+value);
        //log.info("21 parameter --> "+parameter);
        
        if("IssueTimeType".equals(parameter)) {
        	IssueTimeType issueTimeType = new IssueTimeType();
        	issueTimeType.setValue(convertTimeToXMLGC(value)); 
            return issueTimeType;
        }else if("ResponseDateType".equals(parameter)) {
        	ResponseDateType responseDateType = new ResponseDateType();
        	responseDateType.setValue(convertDateToXMLGC(value)); 
            return responseDateType;
        }else if("ResponseTimeType".equals(parameter)) {
        	ResponseTimeType responseTimeType = new ResponseTimeType();
        	responseTimeType.setValue(convertTimeToXMLGC(value)); 
            return responseTimeType;            
        } else if("IssueDateType".equals(parameter)) {
        	IssueDateType issueDateType = new IssueDateType(); 	
        	issueDateType.setValue(convertDateToXMLGC(value));
            return issueDateType;
        }
        return null;
    }
    
    public XMLGregorianCalendar convertDateToXMLGC(Date value){
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(value);
    	XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), 
					gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
			//log.info("convertDateToXMLGC --> "+xgc.toString());
		} catch (DatatypeConfigurationException e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("convertDateToXMLGC -- Exception \n"+errors);
		}
		return xgc;
    }
    
    public XMLGregorianCalendar convertTimeToXMLGC(Date value){
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(value);    	
    	gc.setTimeZone(TimeZone.getTimeZone("America/Lima"));
    	XMLGregorianCalendar xgc = null;   
		try {
			int mili = gc.get(Calendar.MILLISECOND);
			//log.info("mili ---> "+mili);
			if(mili == 0)
				mili = 1;
			BigDecimal bg1 = new BigDecimal(mili);
		    BigDecimal bg2 = new BigDecimal(100000);			
			BigDecimal fractionalSecond = bg1.divide(bg2, 5, BigDecimal.ROUND_DOWN);	
			//log.info("fractionalSecond --> "+fractionalSecond);
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY), 
					gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), fractionalSecond, DatatypeConstants.FIELD_UNDEFINED);			
			//log.info("convertTimeToXMLGC --> "+xgc.toString());
			
			
/*			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY), 
					gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), gc.get(Calendar.MILLISECOND), DatatypeConstants.FIELD_UNDEFINED);			
			log.info("xgc --> "+xgc.toString());
			
			XMLGregorianCalendar xgc2 = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY), 
					gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), gc.get(Calendar.MILLISECOND), Calendar.AM_PM);			
			log.info("xgc2 --> "+xgc2.toString());
			String fs = String.valueOf("0."+gc.get(Calendar.MILLISECOND))+"00";
			log.info("fs --> "+fs);
			BigDecimal fractionalSecond = new BigDecimal(fs);
			XMLGregorianCalendar xgc3 = DatatypeFactory.newInstance().newXMLGregorianCalendarTime(gc.get(Calendar.HOUR_OF_DAY), 
					gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND), fractionalSecond, DatatypeConstants.FIELD_UNDEFINED );			
			log.info("xgc3 --> "+xgc3.toString());*/
			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("convertTimeToXMLGC -- Exception \n"+errors);			
		}
		return xgc;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
