package com.axteroid.ose.server.tools.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: RAC
 * Date: 06/06/12
 */
public class DateUtil {
    //private static final Logger log = Logger.getLogger(DateUtil.class);
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    
    public static SimpleDateFormat FMT_N4() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("en"));
    }

    public static SimpleDateFormat FMT_N4_QRY() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("es"));
    }

    public static SimpleDateFormat FMT_FULL() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat FMT_DAY() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static SimpleDateFormat FMT_MIN() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public static SimpleDateFormat FMT_WEB() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public static SimpleDateFormat FMT_WEB_DAY() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static SimpleDateFormat FMT_WEB_FULL() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    public static String dateFormat(Date date, String format) {
        String dateFormat = null;

        if (date != null) {
            DateFormat simpleDateFormat = new SimpleDateFormat(format);
            dateFormat = simpleDateFormat.format(date);
        }
        return dateFormat;
    }

    public static String dateFormat(Date date, Locale locale, String format) throws Exception {
        String dateFormat = null;        
        if (date != null) {
            DateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
            dateFormat = simpleDateFormat.format(date);
        }
        return dateFormat;
    }

    public static String dateFormat(Date date, SimpleDateFormat simpleDateFormat) {
        if (date == null) return null;
        return simpleDateFormat.format(date);
    }

    public static Date parseDate(String dateStr, String format) throws Exception {
        Date parseDate = null;

        if (dateStr != null) {
            DateFormat simpleDateFormat = new SimpleDateFormat(format);
            parseDate = simpleDateFormat.parse(dateStr);
        }
        return parseDate;
    }

    public static String getMonth(Locale locale, int indexMonth) throws Exception {
        String month = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, indexMonth);
        month = dateFormat(calendar.getTime(), locale, "MMMMM");
        return month;
    }

    public static String getYear(Date fecha) throws Exception {
        return dateFormat(fecha, "yyyy");
    }

    public static Timestamp parseTimestamp(Date date) throws Exception {
        Timestamp parseTimestamp = null;
        parseTimestamp = new Timestamp(date.getTime());
        return parseTimestamp;
    }

    public static Timestamp parseTimestamp(String dateStr, String format) throws Exception {
        Timestamp parseTimestamp = null;
        Date parseDate = parseDate(dateStr, format);
        parseTimestamp = new Timestamp(parseDate.getTime());
        return parseTimestamp;
    }

    public static Date truncDay(Date date) {
        if (date == null) return null;
        Calendar cal = buildCal(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date truncMinute(Date date) {
        if (date == null) return null;
        Calendar cal = buildCal(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Date add(Date date, int field, int timeUnits) {
        if (date == null) return null;
        Calendar cal = buildCal(date);
        cal.add(field, timeUnits);
        return cal.getTime();
    }

    public static Date addDays(Date date, int timeUnits) {
        return add(date, Calendar.DAY_OF_YEAR, timeUnits);
    }

    private static Calendar buildCal(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Date joinDateAndTime(Date date, Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHH:mm");
        String dateInfo = dateFormat.format(date);
        String timeInfo = timeFormat.format(time);
        try {
            return dateTimeFormat.parse(dateInfo + timeInfo);
        } catch (ParseException e) {
            log.error("Error en el sistema", e);
            throw new IllegalStateException("Problems joining:date:<" + dateInfo + "> time:<" + timeInfo + ">");
        }
    }

    public static int compareWithoutTimeInfo(Date date1, Date date2) {
        Date date1WithoutTimeInfo = truncDay(date1);
        Date date2WithoutTimeInfo = truncDay(date2);
        return date1WithoutTimeInfo.compareTo(date2WithoutTimeInfo);
    }

    public static Date min(Date d1, Date d2) {
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.before(d2) ? d1 : d2;
    }

    public static Date max(Date d1, Date d2) {
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return d1.after(d2) ? d1 : d2;
    }

    public static boolean inOrder(Date d1, Date d2) {
        if (d1 == null && d2 == null) return true;
        if (d1 == null && d2 != null) return false;
        if (d1 != null && d2 == null) return true;
        return !d1.after(d2);
    }

    public static Date newDate(int year, int month, int day) {
        Calendar date = buildCal(new Date());
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1 + Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, day);
        return truncDay(date.getTime());
    }

    public static Date newDate(int year, int month, int day, int hour, int min) {
        Calendar date = buildCal(new Date());
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1 + Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, min);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();

    }

    public static Date to235959(Date date) {
        if (date == null) return date;
        Date dateTrunc = DateUtil.truncDay(date);
        Date dateEnd = DateUtil.add(dateTrunc, Calendar.DATE, 1);
        dateEnd = DateUtil.add(dateEnd, Calendar.SECOND, -1); // DATE 23:59:59
        return dateEnd;
    }

    public static Date parseDate(SimpleDateFormat fmt_full, String value) {
        try {
            return StringUtil.hasText(value) ? fmt_full.parse(value) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date nearest(Date pivot, Date date1, Date date2) {
        long delta1 = pivot.getTime() - date1.getTime();
        long delta2 = pivot.getTime() - date2.getTime();
        if (Math.abs(delta1) > Math.abs(delta2)) return date2;
        else return date1;
    }

    public static Date nvl(Date d1, Date d2) {
        return d1 != null ? d1 : d2;
    }

    public static int compareNullSafe(Date d1, Date d2) {
        if (d1 == null && d2 == null) return 0;
        if (d1 == null) return -1;
        if (d2 == null) return 1;
        return d1.compareTo(d2);
    }

    public static long deltaMilisNullSafe(Date d1, Date d2) {
        if (d1 == null && d2 == null) return 0;
        if (d1 == null) return d2.getTime();
        if (d2 == null) return -1 * d1.getTime();
        return d2.getTime() - d1.getTime();
    }

    public static Date toDate(Object date) {
        return (Date) date;
    }

    public static Long deltaDays(Date dateBase, Date dateSubstract) {
        if (dateSubstract == null || dateBase == null) return null;
        long milis = truncDay(dateBase).getTime() - truncDay(dateSubstract).getTime();
        return milis / (1000 * 60 * 60 * 24);
    }
    
    public static Long deltaHoras(Date dateBase, Date dateSubstract) {
    	//log.info("deltaHoras: dateBase: "+dateBase+" | dateSubstract "+dateSubstract);					
        if (dateSubstract == null || dateBase == null) return null;
        long milis = truncMinute(dateBase).getTime() - truncMinute(dateSubstract).getTime();
        //log.info("milis: "+milis);
        //log.info("segundos: "+milis / (1000 ));
        //log.info("minutos: "+milis / (1000 * 60));
        //log.info("horas: "+milis / (1000 * 60 * 60));
        return milis / (1000 * 60 * 60);
    }
    
    public static Long deltaMinutos(Date dateBase, Date dateSubstract) {
    	//log.info("deltaHoras: dateBase: "+dateBase+" | dateSubstract "+dateSubstract);					
        if (dateSubstract == null || dateBase == null) return null;
        long milis = truncMinute(dateBase).getTime() - truncMinute(dateSubstract).getTime();
        //log.info("milis: "+milis);
        //log.info("segundos: "+milis / (1000 ));
        log.info("minutos: "+milis / (1000 * 60));
        //log.info("horas: "+milis / (1000 * 60 * 60));
        return milis / (1000 * 60);
    }

    public static Date substractDaysToDate(Date date, Integer numberDays) {
        if (date == null || numberDays == null || numberDays < 0) return null;
        GregorianCalendar gcal = (GregorianCalendar) GregorianCalendar.getInstance();
        gcal.setTime(date);
        gcal.add(Calendar.DATE, (-1 * numberDays));
        return gcal.getTime();
    }

    public static boolean inRageIgnoreNull(Date date, Date startDate, Date endDate) {
        if (date == null) throw new IllegalArgumentException("Fecha no puede ser nulo");
        if (startDate != null && startDate.after(date)) return false;
        if (endDate != null && endDate.before(date)) return false;
        return true;
    }


    public static int getWeekOfYear(Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }


    public static int getDayOfWeek(Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfWeekName(Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static void demo() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        System.out.println(sdf.parse(sdf.format(new Date())));


        Calendar calendar = GregorianCalendar.getInstance();
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        // print out a bunch of interesting things
        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("DAY_OF_WEEK_IN_MONTH: "
                + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("ZONE_OFFSET: "
                + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000)));
        System.out.println("DST_OFFSET: "
                + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000)));

        System.out.println("Current Time, with hour reset to 3");
        calendar.clear(Calendar.HOUR_OF_DAY); // so doesn't override
        calendar.set(Calendar.HOUR, 3);
        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("DAY_OF_WEEK_IN_MONTH: "
                + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("ZONE_OFFSET: "
                + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000))); // in hours
        System.out.println("DST_OFFSET: "
                + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000))); // in hours
    }

    public static Date addDaysBussinesDays(Date base, int days) {
        Calendar cal = buildCal(base);
        int delta = days > 0 ? -1 : +1;
        while (days != 0) {
            cal.add(Calendar.DAY_OF_YEAR, -delta);
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                days += delta;
        }
        return cal.getTime();
    }

    public static String fomatAsHourMin(long elapsedMilis) {
        String sign = elapsedMilis < 0 ? "-" : "";
        elapsedMilis = Math.abs(elapsedMilis);
        Long elapsedHoras = elapsedMilis / (1000 * 60) / 60;
        Long elapsedMin = elapsedMilis / (1000 * 60) % 60;
        Long elapsedSegs = elapsedMilis / 1000 % 60;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return sign + elapsedHoras + ":" + decimalFormat.format(elapsedMin) + ":" + decimalFormat.format(elapsedSegs);
    }

    public static DecimalFormat buildDecimalFormat() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("es"));
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        return new DecimalFormat("#,###,###,##0.00", symbols);
    }

    public static boolean equals(Date fecha1, Date fecha2) {
        if (fecha1 == null) {
            fecha1 = new Date(10000);
        }
        if (fecha2 == null) fecha2 = new Date(10000);
        return fecha1.equals(fecha2);
    }

    public static String formatAsCurrentDateByDay(int day, String format) {
        Date currentDate = new Date();
        Integer currentYear = new Integer(dateFormat(currentDate, "yyyy"));
        Integer currentMesVar = new Integer(dateFormat(currentDate, "MM"));
        Date currentDate2 = newDate(currentYear, currentMesVar, day);
        return DateUtil.dateFormat(currentDate2, format);
    }
    
    public static Timestamp getCurrentTimestamp(){
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());	
		return currentTimestamp;
    }
	 
    public static boolean compareDate(Date value1, Date value2) {
	   	XMLGregorianCalendar xgc1 = DateUtil.convertDateToXMLGC(value1);
		XMLGregorianCalendar xgc2 = DateUtil.convertDateToXMLGC(value2);
		if(xgc1.equals(xgc2))
			return true;
	   	return false;
	}
		    
    public static XMLGregorianCalendar convertDateToXMLGC(Date value){
	   	GregorianCalendar gc = new GregorianCalendar();
	   	gc.setTime(value);
	   	XMLGregorianCalendar xgc = null;
		try {
			xgc = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			StringWriter errors = new StringWriter();	
			e.printStackTrace(new PrintWriter(errors));
			log.error("convertDateToXMLGC -- Exception \n"+errors);
		}
		return xgc;
	}
    
    public static String convertXMLGregorianCalendar2String(Date value) {    	
    	XMLGregorianCalendar xgc = DateUtil.convertDateToXMLGC(value);
    	//log.info("xgc "+xgc);
    	String sxgc = xgc.toString();
    	//log.info("sxgc "+sxgc);
    	String[] asxgc = sxgc.split("-");
    	return asxgc[0]+asxgc[1]+asxgc[2];    	
    }
   
    public static String getToday(){
        int year = Calendar.getInstance().get(1);
        int mes = Calendar.getInstance().get(2);
        int dia = Calendar.getInstance().get(5);
        String sMes = String.valueOf(mes);
        int iMes = mes + 1;
        //log.info("sMes "+sMes);
        sMes = String.valueOf(iMes);
        //log.info("sMes "+sMes);
        if(iMes < 10)     
            sMes = (new StringBuilder("0")).append(iMes).toString();
        String sDia = String.valueOf(dia);
        if(dia < 10)
            sDia = (new StringBuilder("0")).append(sDia).toString();
        return(new StringBuilder(String.valueOf(year))).append(sMes).append(sDia).toString();
    }    
    
	public static Date stringToDateYYYY_MM_DD(String fecha) {
		Date date = null;
		DateFormat formatter = DateUtil.FMT_DAY();
		try {
			if (fecha != null) {
				date = formatter.parse(fecha);
			} 
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	 public static String dateToStringYYYY_MM_DD(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String f = "";
		 f = sdf.format(date);
		 return f;
	}
	 
	 public static String dateToStringDateTimeFormat(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String f = "";
		 f = sdf.format(date);
		 return f;
	}
	 	 
}