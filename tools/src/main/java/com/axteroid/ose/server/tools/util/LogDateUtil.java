package com.axteroid.ose.server.tools.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogDateUtil {
	public static String FORMATO_FECHA_YYYYMMDD = "yyyyMMdd";
	public static String FORMATO_FECHA_DD_MM_YYYY = "dd/MM/yyyy";
	public static String FORMATO_FECHA_YYYY_MM_DD = "yyyy/MM/dd";
	private final static Logger log = LoggerFactory.getLogger(LogDateUtil.class);
	
	public static String dateToString(Date fecha, String formato) {
		DateFormat formatter = new SimpleDateFormat(formato);
		String s = formatter.format(fecha);
		return s;
	}

	public static Date stringToDate(String fecha, String formato) {
		DateFormat formatter = new SimpleDateFormat(formato);
		Date date = null;
		try {
			date = formatter.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static long restaFechas(Date fechaMinuendo, Date fechaSustraendo) {
		long fechaMinuendoMs = fechaMinuendo.getTime();
		long fechaSustraendoMs = fechaSustraendo.getTime();
		return (fechaMinuendoMs - fechaSustraendoMs);
	}

	public static boolean validarFechaValRango(Date fechaValor, Date fechaIni, Date fechaFin, boolean rangoFinal) {
		boolean resp = false;
		Calendar fechaCalValor = new GregorianCalendar();
		Calendar fechaCalIni = new GregorianCalendar();
		Calendar fechaCalFin = new GregorianCalendar();

		if (fechaValor != null) {
			fechaCalValor.setTime(fechaValor);
			fechaCalValor.set(Calendar.HOUR, 0);
			fechaCalValor.set(Calendar.MINUTE, 0);
			fechaCalValor.set(Calendar.SECOND, 0);
			fechaCalValor.set(Calendar.MILLISECOND, 0);
			fechaCalValor.set(Calendar.AM_PM, 0);
			fechaCalValor.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fechaIni != null) {
			fechaCalIni.setTime(fechaIni);
			fechaCalIni.set(Calendar.HOUR, 0);
			fechaCalIni.set(Calendar.MINUTE, 0);
			fechaCalIni.set(Calendar.SECOND, 0);
			fechaCalIni.set(Calendar.MILLISECOND, 0);
		}

		if (fechaFin != null) {
			fechaCalFin.setTime(fechaFin);
			fechaCalFin.set(Calendar.HOUR, 0);
			fechaCalFin.set(Calendar.MINUTE, 0);
			fechaCalFin.set(Calendar.SECOND, 0);
			fechaCalFin.set(Calendar.MILLISECOND, 0);
		}

		if (fechaCalIni != null && fechaCalValor.compareTo(fechaCalIni) >= 0) {
			if (fechaFin != null) {
				if (fechaCalValor.compareTo(fechaCalFin) <= 0) {
					resp = true;
				}
			} else {
				if (rangoFinal) {
					resp = true;
				}
			}
		}

		return resp;
	}

	public static boolean esFechaMenor(Date fecha1, Date fecha2) {
		boolean resp = false;
		Calendar fechaCal1 = new GregorianCalendar();
		Calendar fechaCal2 = new GregorianCalendar();

		if (fecha1 != null) {
			fechaCal1.setTime(fecha1);
			fechaCal1.set(Calendar.HOUR, 0);
			fechaCal1.set(Calendar.MINUTE, 0);
			fechaCal1.set(Calendar.SECOND, 0);
			fechaCal1.set(Calendar.MILLISECOND, 0);
			fechaCal1.set(Calendar.AM_PM, 0);
			fechaCal1.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha2 != null) {
			fechaCal2.setTime(fecha2);
			fechaCal2.set(Calendar.HOUR, 0);
			fechaCal2.set(Calendar.MINUTE, 0);
			fechaCal2.set(Calendar.SECOND, 0);
			fechaCal2.set(Calendar.MILLISECOND, 0);
			fechaCal2.set(Calendar.AM_PM, 0);
			fechaCal2.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha1 != null && fecha2 != null) {
			if (fecha1.compareTo(fecha2) < 0) {
				resp = true;
			}
		}

		return resp;
	}

	public static boolean esFechaMayor(Date fecha1, Date fecha2) {
		boolean resp = false;
		Calendar fechaCal1 = new GregorianCalendar();
		Calendar fechaCal2 = new GregorianCalendar();

		if (fecha1 != null) {
			fechaCal1.setTime(fecha1);
			fechaCal1.set(Calendar.HOUR, 0);
			fechaCal1.set(Calendar.MINUTE, 0);
			fechaCal1.set(Calendar.SECOND, 0);
			fechaCal1.set(Calendar.MILLISECOND, 0);
			fechaCal1.set(Calendar.AM_PM, 0);
			fechaCal1.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha2 != null) {
			fechaCal2.setTime(fecha2);
			fechaCal2.set(Calendar.HOUR, 0);
			fechaCal2.set(Calendar.MINUTE, 0);
			fechaCal2.set(Calendar.SECOND, 0);
			fechaCal2.set(Calendar.MILLISECOND, 0);
			fechaCal2.set(Calendar.AM_PM, 0);
			fechaCal2.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha1 != null && fecha2 != null) {
			if (fecha1.compareTo(fecha2) > 0) {
				resp = true;
			}
		}

		return resp;
	}

	public static boolean esFechaIgual(Date fecha1, Date fecha2) {
		boolean resp = false;
		Calendar fechaCal1 = new GregorianCalendar();
		Calendar fechaCal2 = new GregorianCalendar();

		if (fecha1 != null) {
			fechaCal1.setTime(fecha1);
			fechaCal1.set(Calendar.HOUR, 0);
			fechaCal1.set(Calendar.MINUTE, 0);
			fechaCal1.set(Calendar.SECOND, 0);
			fechaCal1.set(Calendar.MILLISECOND, 0);
			fechaCal1.set(Calendar.AM_PM, 0);
			fechaCal1.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha2 != null) {
			fechaCal2.setTime(fecha2);
			fechaCal2.set(Calendar.HOUR, 0);
			fechaCal2.set(Calendar.MINUTE, 0);
			fechaCal2.set(Calendar.SECOND, 0);
			fechaCal2.set(Calendar.MILLISECOND, 0);
			fechaCal2.set(Calendar.AM_PM, 0);
			fechaCal2.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha1 != null && fecha2 != null) {
			if (fechaCal1.compareTo(fechaCal2) == 0) {
				resp = true;
			}
		}

		return resp;
	}

	public static boolean fechaE(Date fecha1, Date fecha2) {
		boolean resp = false;
		Calendar fechaCal1 = new GregorianCalendar();
		Calendar fechaCal2 = new GregorianCalendar();

		if (fecha1 != null) {
			fechaCal1.setTime(fecha1);
			fechaCal1.set(Calendar.HOUR, 0);
			fechaCal1.set(Calendar.MINUTE, 0);
			fechaCal1.set(Calendar.SECOND, 0);
			fechaCal1.set(Calendar.MILLISECOND, 0);
			fechaCal1.set(Calendar.AM_PM, 0);
			fechaCal1.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha2 != null) {
			fechaCal2.setTime(fecha2);
			fechaCal2.set(Calendar.HOUR, 0);
			fechaCal2.set(Calendar.MINUTE, 0);
			fechaCal2.set(Calendar.SECOND, 0);
			fechaCal2.set(Calendar.MILLISECOND, 0);
			fechaCal2.set(Calendar.AM_PM, 0);
			fechaCal2.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fecha1 != null && fecha2 != null) {
			if (fecha1.compareTo(fecha2) > 0) {
				resp = true;
			}
		}

		return resp;
	}

	public static Date adicionaDiasFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_MONTH, dias);
		return calendar.getTime();
	}

	public static boolean validarFechaEnRango(Date fechaValor, Date fechaIni, Date fechaFin) {
		boolean resp = false;
		Calendar fechaCalValor = new GregorianCalendar();
		Calendar fechaCalIni = new GregorianCalendar();
		Calendar fechaCalFin = new GregorianCalendar();

		if (fechaValor != null) {
			fechaCalValor.setTime(fechaValor);
			fechaCalValor.set(Calendar.HOUR, 0);
			fechaCalValor.set(Calendar.MINUTE, 0);
			fechaCalValor.set(Calendar.SECOND, 0);
			fechaCalValor.set(Calendar.MILLISECOND, 0);
			fechaCalValor.set(Calendar.AM_PM, 0);
			fechaCalValor.set(Calendar.HOUR_OF_DAY, 0);
		}

		if (fechaIni != null) {
			fechaCalIni.setTime(fechaIni);
			fechaCalIni.set(Calendar.HOUR, 0);
			fechaCalIni.set(Calendar.MINUTE, 0);
			fechaCalIni.set(Calendar.SECOND, 0);
			fechaCalIni.set(Calendar.MILLISECOND, 0);
		}

		if (fechaFin != null) {
			fechaCalFin.setTime(fechaFin);
			fechaCalFin.set(Calendar.HOUR, 0);
			fechaCalFin.set(Calendar.MINUTE, 0);
			fechaCalFin.set(Calendar.SECOND, 0);
			fechaCalFin.set(Calendar.MILLISECOND, 0);
		}

		if (fechaCalIni != null && fechaCalValor.compareTo(fechaCalIni) >= 0) {
			if (fechaFin != null) {
				if (fechaCalValor.compareTo(fechaCalFin) <= 0) {
					resp = true;
				}
			} else {
				resp = true;
			}
		}

		return resp;
	}

	public static Integer obtenerAnnio(Date fecha) {
		Integer annio = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		annio = calendar.get(Calendar.YEAR);
		return annio;
	}

	public static boolean equal(Date f1, Date f2) {
		if (f1 == null && f2 == null)
			return true;
		if (f1 != null && f2 != null)
			if (f1.compareTo(f2) == 0)
				return true;
		return false;
	}

	public static boolean greaterThanOrEqualTo(Date f1, Date f2) {
		if (equal(f1, f2))
			return true;
		if (f1 != null && f2 != null)
			if (f1.compareTo(f2) > 0)
				return true;
		return false;
	}

	public static boolean lessThanOrEqualTo(Date f1, Date f2) {
		if (equal(f1, f2))
			return true;
		if (f1 != null && f2 != null)
			if (f1.compareTo(f2) < 0)
				return true;
		return false;
	}

	public static boolean greaterThan(Date f1, Date f2) {
		if (f1 != null && f2 != null)
			if (f1.compareTo(f2) > 0)
				return true;
		return false;
	}

	public static boolean lessThan(Date f1, Date f2) {
		if (f1 != null && f2 != null)
			if (f1.compareTo(f2) < 0)
				return true;
		return false;
	}

	public static Date dateToDateYYYYMMDD(Date fecha) {
		Calendar f = new GregorianCalendar();
		if (fecha != null) {
			f.setTime(fecha);
			f.set(Calendar.HOUR, 0);
			f.set(Calendar.MINUTE, 0);
			f.set(Calendar.SECOND, 0);
			f.set(Calendar.MILLISECOND, 0);
			f.set(Calendar.AM_PM, 0);
			f.set(Calendar.HOUR_OF_DAY, 0);
		}
		return f.getTime();
	}

	public static String dateToStringDD_MM_YYYY(Date fecha) {
		String s = null;
		if (fecha != null) {
			DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DD_MM_YYYY);
			s = formatter.format(fecha);
		}
		return s;
	}

	public static String stringToStringDD_MM_YYYY(String fecha) {
		String s = null;
		DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DD_MM_YYYY);
		try {
			if (fecha != null) {
				Date date = formatter.parse(fecha);
				s = formatter.format(date);
			} 
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static String stringToStringYYYY_MM_DD(String fecha) {
		String s = null;
		DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DD_MM_YYYY);
		try {
			if (fecha != null) {
				Date date = formatter.parse(fecha);
				DateFormat formatter2 = new SimpleDateFormat(FORMATO_FECHA_YYYY_MM_DD);
				s = formatter2.format(date);
			} 
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static Date stringToDateDD_MM_YYYY(String fecha) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DD_MM_YYYY);
		try {
			if (fecha != null) {
				date = formatter.parse(fecha);
			} 
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date stringToDateYYYY_MM_DD(String fecha) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_YYYY_MM_DD);
		try {
			if (fecha != null) {
				date = formatter.parse(fecha);
			} 
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToStringYYYY_MM_DD(Date fecha) {
		String s = null;
		if (fecha != null) {
			DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_YYYY_MM_DD);
			s = formatter.format(fecha);
		}
		return s;
	}
	public static Date getDateActual() {
		return new Date();
	}

	public static Timestamp getTimestampActual() {
		return new Timestamp((new Date()).getTime());
	}

	public static String convertDateToString(Date fecha, String formato) {
		String s = "";
		try {
			DateFormat formatter = new SimpleDateFormat(formato);
			s = formatter.format(fecha);
		} catch (Exception e) {
			s = "";
			e.printStackTrace();
		}
		return s;
	}

	public static boolean validaTraslapeRangos(Date fechaIniNueva, Date fechaFinNueva, Date fechaIniAntigua,
			Date fechaFinAntigua) {

		boolean esCorrecto = true;

		if (fechaFinNueva != null) {

			if (equal(fechaIniNueva, fechaFinNueva)) {
				esCorrecto = !(equal(fechaIniAntigua, fechaFinAntigua) && equal(fechaIniNueva, fechaIniAntigua));
			} else if (equal(fechaIniAntigua, fechaFinAntigua)) {
				esCorrecto = !(equal(fechaIniNueva, fechaFinNueva) && equal(fechaIniNueva, fechaIniAntigua));
			} else {
				if (fechaFinAntigua!= null){
					esCorrecto = lessThan(fechaFinNueva, fechaIniAntigua) || greaterThan(fechaIniNueva, fechaFinAntigua);					
				} else {
					esCorrecto = lessThan(fechaFinNueva, fechaIniAntigua);
				}
			}
		} else {
			if (fechaFinAntigua!= null){
				esCorrecto = greaterThan(fechaIniNueva, fechaFinAntigua);					
			} else {
				esCorrecto = false;
			}
		}

		return esCorrecto;
	}
	
	public static boolean enRangoBase(Date fechaIniHija, Date fechaFinHija, Date fechaIniPadre,
			Date fechaFinPadre) {

		boolean esCorrecto = true;
		
		if (fechaFinPadre != null) {
			if (fechaFinHija != null) {
				esCorrecto = greaterThanOrEqualTo(fechaIniHija, fechaIniPadre) && lessThanOrEqualTo(fechaFinHija, fechaFinPadre);
			} else {
				esCorrecto = false;
			}
		} else {
			esCorrecto = greaterThanOrEqualTo(fechaIniHija, fechaIniPadre);
		}
		
		return esCorrecto;
	}
	
	@SuppressWarnings("unused")
	public static boolean isDate(String fechax, String formato){
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
			Date fecha = formatoFecha.parse(fechax);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * Compara si las fechas son iguales en annio, mes y dia.
	 */
	public static boolean sonIgualesFechas(Calendar fecha1,Calendar fecha2){
		boolean resp=false;
		
		if(	fecha1.get(Calendar.YEAR)==fecha2.get(Calendar.YEAR)  &&
			fecha1.get(Calendar.MONTH)==fecha2.get(Calendar.MONTH)&&
			fecha1.get(Calendar.DATE)==fecha2.get(Calendar.DATE)){
			
			resp=false;
		}
		
		return resp;
	}
	
	public static int mesVigencia(Date date){
		return dateToCalendar(date).get(Calendar.MONTH);
	}
	
	public static int periodoVigencia(Date date){
		return dateToCalendar(date).get(Calendar.YEAR);
	}
	
	private static Calendar dateToCalendar(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	public static String getDateFormat(String format){
		Date date = new Date(); 
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
		return dt1.format(date);
	}
	
	public static String generarFechaIso(){
        String dat = "";
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        dat = df.format(new Date());
        return dat;
    }
	
	 public static String toSqlDateTimeFormat(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String f = "";
		 f = sdf.format(date);
		 return f;
	}
	 public static String glueDate(String time){
		 String tiempo=time.substring(0,4)+time.substring(5,7)+time.substring(8,10)+time.substring(11,13)+time.substring(14,16)+time.substring(17,19);
		 return tiempo;
	 }
	 
}