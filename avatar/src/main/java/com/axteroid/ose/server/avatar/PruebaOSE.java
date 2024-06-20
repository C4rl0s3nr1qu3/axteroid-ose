package com.axteroid.ose.server.avatar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.build.GetStatusBuild;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class PruebaOSE {
	private static Logger log = LoggerFactory.getLogger(PruebaOSE.class);
	
	public static void main(String[] args) {
		PruebaOSE pruebaOSE= new PruebaOSE();
		//log.info("resultado: "+pruebaOSE.getParametroErrorFechaAmplSunat());
		//pruebaOSE.revisar_FACTURACION();	
		pruebaOSE.getString2Byte();
	}

	public boolean getParametroErrorFechaAmplSunat() {	
		Date currentdate = newDate(2020, 06, 17);
		Date cbcIssueDate = newDate(2020, 3, 8);
		log.info("currentdate: "+currentdate+" - cbcIssueDate: "+cbcIssueDate);
		
		 Date d1 = newDate(2020, 3, 9);
		 Date d2 = newDate(2020, 6, 30);
		 Date d3 = PruebaOSE.newDate(2020, 7, 10);
		 long t2 = PruebaOSE.deltaDays(currentdate, cbcIssueDate);		 
		 long t4 = PruebaOSE.deltaDays(cbcIssueDate, d1);
		 long t6 = PruebaOSE.deltaDays(cbcIssueDate, d2);
		 long t8 = PruebaOSE.deltaDays(currentdate, d1);
		 long t10 = PruebaOSE.deltaDays(currentdate, d3);
		 log.info("t2: "+t2+" | t4: "+t4+" | t6: "+t6+" | t8: "+t8+" | t10: "+t10);
		 long lDiasParam = Long.parseLong("7");
		 if(t10<=0 && t8>=0) {
			 log.info("t10: "+t10+" | t8: "+t8);
			 if(t4<0 || t6>0) {
				 log.info("t4: "+t4+" | t6: "+t6);
				 if(t2>lDiasParam) {
					 log.info("1");
				 	return true;
				 }
			 }
			 log.info("t2: "+t2);
		 }else if(t2>lDiasParam) {
			 log.info("2");
			 return true;
		 }
		 log.info("3");
		 return false;
	}	
	
    public static Date newDate(int year, int month, int day) {
        Calendar date = buildCal(new Date());
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1 + Calendar.JANUARY);
        date.set(Calendar.DAY_OF_MONTH, day);
        return truncDay(date.getTime());
    }	
	
    public static Long deltaDays(Date dateBase, Date dateSubstract) {
        if (dateSubstract == null || dateBase == null) return null;
        long milis = truncDay(dateBase).getTime() - truncDay(dateSubstract).getTime();
        return milis / (1000 * 60 * 60 * 24);
    }

    private static Calendar buildCal(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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
    
    private void revisar_FACTURACION(){    
    	int proceso = 7;
    	Date fecDBInicio = DateUtil.stringToDateYYYY_MM_DD("2020-09-16");
    	Date fecDBFin = DateUtil.stringToDateYYYY_MM_DD("2020-09-17");
    	String td = "in ('RA','RC','RR')";
    	this.revisar_FACTURACION(proceso, fecDBInicio, fecDBFin, td);
    }    
    
    private void revisar_FACTURACION(int status, Date fecDBInicio, Date fecDBFin, String td){
    	try {
    		log.info("revisar_FACTURACION ");   
    		GetStatusBuild getStatusBuild = new GetStatusBuild();
			//int k=0;
			Date fecDBAct = fecDBInicio;
			while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
				//k++;
				String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
		    	String [] fecDB = strFecDBAct.split("-");
		    	int year = Integer.parseInt(fecDB[0]);
		    	int mes = Integer.parseInt(fecDB[1]);
		    	int dia = Integer.parseInt(fecDB[2]);
	    		
				fecDBAct = DateUtil.addDays(fecDBAct, 1);
				String fechaDetalle = " extract(year from FECHA_CREA::timestamp) = "+year
						+ " and extract(month from FECHA_CREA::timestamp) = "+mes
						+ " and extract(day from FECHA_CREA::timestamp) = "+dia;
				List<String> listDetalleResumen = getStatusBuild.getListDetalleResumenTDSSShort(fechaDetalle, td);
    			for(int hora = 0; hora < 24; hora++) {
    				//log.info("status: "+status+" - actualizaIBM_PG_FACTURA_CPE: "+year+"/"+mes+"/"+dia+":"+hora);
    				String sHora = ""+hora;
    				if(hora<10)
    					sHora = "0"+hora;
    				int hora2 = hora+1;
    				String sHora2 = ""+hora2;
    				if(hora2<10)
    					sHora2 = "0"+hora2;   				
    				String fecha = " FECHA_CREA>='"+strFecDBAct+" "+sHora+":00:00' and FECHA_CREA<'"+strFecDBAct+" "+sHora2+":00:00' ";   
    				if(status==7)
    					this.probarRevisarFacturaOseTDSSShort(fecha, td, listDetalleResumen);	 
    				if(hora>3)
    					return;
    			}	
	    	}	
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.info("Exception \n"+errors);
		}	  
    }
    
    public void probarRevisarFacturaOseTDSSShort(String fecha, String TD, 
    		List<String> listDetalleResumen) throws SOAPException {  
    	GetStatusBuild getStatusBuild = new GetStatusBuild();
    	List<Documento> listTbComprobante = getStatusBuild.getListOSEProduccionTD_FECHAShort(fecha, TD);
    	   
    	int i = 0;
    	int k = listTbComprobante.size();
    	for(Documento tb : listTbComprobante) {    		
    		int bol = 0;
    		String idComprobante = tb.getIdComprobante();   		
    		String resulIdComprobante = listDetalleResumen.stream()
    				.filter(x ->  idComprobante.trim().equals(x)).findAny().orElse("");
    		//log.info(i+"/"+k+") idComprobante; "+idComprobante);
    		if(resulIdComprobante.isEmpty()) {
    			i++;
    			log.info(i+"/"+k+") idComprobante; "+idComprobante+" | resulIdComprobante; "+resulIdComprobante);
    			//list.add(tb);
    		}
    		for(String id : listDetalleResumen) {
    			//log.info("Processing id: "+ id+" | idComprobante: "+idComprobante );
    			if(id.trim().equals(idComprobante.trim()))
    				bol = 1;    			 			
    		};
    		if(bol==0)
    			log.info(i+"/"+k+") ====> idComprobante: "+idComprobante );
    	}    		
    } 
    
	public void getString2Byte() {				
		log.info("getString2Byte ");
		//String contentFile = "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";
		//String contentFile = "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";
		String contentFile = "UEsDBBQACAgIAMN1M1UAAAAAAAAAAAAAAAAgAAAAMjAxOTUwMTExNjktNDAtUDAwMi0wMDAwMTc3NS5YTUzdWVlz4kqyfp6JmP/A ETcF8LWAmLxbXuitCKQBNoAMTEP2pAE2tCCEL/ ltiM2/R0n77zcq8jCKSszK8yv8zKqsLf/nGIwtbezfIgid esBf0qeXGduIEsff2pGvs8 CplRdm7JhhErtvT7WbP/3j/dvMzWw3LaBRCwLE dtTmcWveRmbxWtsRm7 mqeuHawD22yUXksrfE3drHzNbd NzNdD7rx YDxjT2eYVys4nqHgQxjE2/yX0ciLAfC8zPXMwqWSKIUux0X AW b9hk MfPgx9h3sBAlSuJHoPgN1Po9UBKq248AHUinXxTpK4JUVfVSdV6SzENwFEURdIhAHScPvD u2u6h K3pmUPhxk3aH7mwc/4sqFyaIRx0HdoszKJO3Tu0/Er7nysQtdF nM/3bzDsV50UblHk73/761/ Aj/N15fR0 BZfBd3DJ K929O/qoGHpyrzNwW77w95de3Cfcnc3IDcx0 XienV8qMkxjGGQbHU6yiW/iJ0wKhl2RB4UePgDWlwcYQhaGeIfizjXXj50aCdjDiCfns868Afu9plpvPuW9iFyzFXbsZXPhuS1f4t6dzHFpmxvk6yaL88 ufm8mN926YpK7zfKO1mRT5OgEdeG5e/E40d5GcQeZmWLrvqRCM7WQuGshmWQHj6HaZHnpcTEaK HZy4F75JLjxcH79LpM3xs8WTt1fDKMq2FNLlVjma4VhDp1ky5RDtX/oVJTjGski5ZjCwMXiQJtIMCpXK3NMjlJ33O/w8lJeOVq3Y3vj3X/9gXX  29/naEJUu 7DibOSGaz2tUcuZ1u0bnpZJxM7siOeIRzW4JDEoWv9O1VVk6U0TjOI2pJC aqTTurI4XQ 8mhuEDGA5nNseVRTc3pRojBmHNjl1nFkUF2O5obRu3a5jabnKAG6sSzthmRV/Qo8JPOTBdrcyRphr3RR6muz/cXyPHBjWSx4GQ/s3Be31p8XtpFue74UlV0lwhOFywJtvxxpgB q9o8cuwTxHx7OGxjjB04sbq2F8O6rWamf4GsvAXg/Wy5HCZE7MQLjhFIg ke0QGzniVaZ 0d/ax6e7ul5S4PTWombn1L05JAh00Xur1QblacW437LvI8tz1SFLksPVDxJPB4TawA0NVwrIw6GzQsfV4G8I9UJhqQSG 787cBN6xQEsg5C2iK3G4YQQRb7uI7wHSG9EVKRr0DQ4Mp6UlzEiQaiTljHZ8Lq X2MN6A7VmeayNd2tujsDYXTuJQIDHqrSduwEEKyOUFUp1LoYrPCZ6VSFExKhYY9FyWBabCxlashHZE A4X7q2IzUWNqYTqNE4ztb 82TJSaMdKeoFcxTBAiiRXo5VvRVK4okhttRzj5gKq1SQtqwAVye6S1viDSOu1qPG1eARHKUwaGXqS0WfZBRIOVZ7HTkU5ryj55AHHVGNZ0RhJBDl3IuYgMhoTSrLGrETSPskoX5Rl/TDXsTnDsySrh7p3gdRrghQ2jCaSzFn1IE5kdDids OZxhxYfcNAL8B1LJV1QtdQYqzVJK/Mx1NZ73qazqoq/L5ChvPxIgDeNCAZbdtQreNTWsQlWjwIR1Bc0iKOQ0gMF26NpdKQE7gqabpzpXaWEgrzsDfwMLxyybFbnmHLqi V5gLzRVhC4pkAgaZSGvKKmotVauDs1qrJjdUZV/DTpAyH79uVStarZfORrpAwq76Bx3SOdw8cDRbXYqGx8VyuAQyaISQNHGC28ekdl1DyEy4PIjXgKHXHqbzVoWVmDBQjMBYEauFEatVEYSzHqbUlNza LWx8iDmc71uLOaxPyRciaW9pFHUtdZWvaNkYT5IV7 9tCcgMScqA9jxmBlcGBeSEgs8kmGD4socf1nND5RPWtEFUL2nQtdWjpxlrM1Gwa8ubOKwdH9vreooVo Gik7thsDPLvU Xe6 37LrzvmkI9BH0 ykrr7GjMLV27qiYqr6dTw3IfsTyGy/FvIV gdSKNTZTKWm9Hy58G5GGLK4tNK6ahmG8Xoepv5a4aaq1qanStkI8pSaYDJRe5gLW6o0ocVcyOMojOxlflBdIM1sNcK/ruNgCnlutWvcp/xBoGh1MS3I5Ss028Ce9CX2UPHQSi3hcUKieJeaY6fKjfEdKSrHyMXoY0dTwAikyoeoVY5/AOotg1F PV4Q mw891BZmPJsc pW7FLh0jmFxZgb9nFa0KWHRxDjsHRN9ZNZpN8ll7cjTq2t6Vu0tBSoGAHNKgYCBi5N3RBJtCsWhPXlBkioKOLmt0n0mRSyG4O2 wx1nJoJ1w0vnGskDEqwHzAWSBCJF5qAaNR1GQTck6VVsAvThVLXmw0lZpT1ek6nepDcM68U09eH2MQMn3TVJ6xVsF4eEanuCYWRXLkcKatPJXuiQmBEdUqMmOiZcCSZsWqd6U4cbO6r2Di6lcKUdDZxBoby2aBIuVi8jPYYlZRu2Y W6fYjsoJIqg59UBqxHfSSCCTfBfdQZgZ5QDytnMc7NhVg6eHhcLYrQVYmNhaN7ozNGhY6zMReKb OSL1HE5gJpx3I1BveQHPy7QUJTqRJiJ7XxeQY7fGngwwK2k/TEoaoz/AZ4V2 vXMo2DTxjcoIEHCVVCU3fTQEquZKa1XUdP gTkvIquHlM7gKnwQaIV0h4SoJiBqJ0KE8URZBwFAVbr6yzZCUyInXOPiPLkUipJB xpcMNYdcalNaChWucADCSVFhK193dWSoJXFZwjwk3xpmnSgH3a347I2m7gt0IFox33R1hZDwFeFDRHozoonttG5VMAY8CIrA5Us2ncOMdnT29diVxJDKnhubwQAEi3W2KiDf4OUrKXR62mQp2/bIh4Rb4iQuqaWYfBeV0nK/F1LTavY2HKGzBBEzRlz396iWAq0ZmAJd0h/3tPsymUd ItGTXD42RZ43ytrg3KHlN9I/t/qQ8FLv StpyWm8N5ky5n27xdHUgo7TXQ sL5HA0UIn28chuF7inr00wSri2wKbCRuf7YbRns5HqrYvpWF3P7Q5KMW2GqfDxaqxwYjIZIOVA6O88G9sPw h6nlNzbrHdHImMVWf7wWFlTTs9fmPvRIrtdQcDJ8nC Y7zKDSV8vlwNTnIHYCo3QWyWya2aVE7iq23Veq2WfnaL/OVaoGyE7A7fM4sVgqBZfOiViNMnFTp6ijoB6K2 u35VhaW/jiYwEx1FyiiT0DkHe1pe41JS8XdSGz7etpwKWGERIhErdcRVYsDrT3qpUUCrF170xvMWcnJBySzkvB24Ebe5Vz3/VntJjyf5pD7c96ncyB8fXi1O90FkUeXwa9SeAeBN/lGND//HMLT7/gL g35Ij3pUWVeJNHlSgeF2FX1 4Fvtmnfe9roQDFPN7IJdzb6rJdk9czMivosOz3yDozndl2 oeAoNiRQDMN6ww8g5MdW1wEJXsHPIOenv/ TooEG/qlaLXgpa379aakvyotQOC// tf7GfqsiXyHgfzIbXi3CgozvEUOisK0/eh0427GG Kz2AzvrlwnphX /Y 7C/iFfyg9T/XADPnZdMjjFMxQFH GN0kU6/eJuzw033leunRTgziKQ6XhM3Yl DZyigJ4cIZfS1br9NOGy9NvT72n/3jmnn Sp9NrkkOKgONkbp7faMCgGyj2OX61yFy3 DzDOHtpQcAyS1qDLt4Sgsi8zXmvf1oEAWwfpeUE 6BZND9y9aHadeCzkcCL4JPdnXJSxkXWADWcNZl4bPRFrZHSQV5kgV38wOhjuCHwAnGh7lOKqMRx32fMhcavQ eE3ACQR/m4ZkxwPTNk7pxUXK/x44T26wv2qxXyeArkUSUrru0Gezf7veLuoZ0 1ung2H kuHv/q LGej8vbjB/afGcBCh 2hIZhZemLWzY67WeWwIvUUzr/2mlw9D 75T6VJyqLU0BNC9xLRVQv1no3xV23uwNugS0j/9HqHVeuNEplqYx/nuNRwCnJ7jzwPMD tD qnCu9QTuJmAm8FQLSBpPvbZIanYp96S4pF5LIH98vE8C2wVRw23LLrNmB6ybRTdjJLjssBesfzb8qn7n6GmQMnPfdb6CYGh/ NId3rl9p/4wWjqxy2aj/W4n/9QUUOzptt82v97iv7Pf/iINGEoMXjr4j4k4F0cd3TLQNIrPDs3MwPkl/A/FD8O7QPDna/e5DVyL8zL/A0KbE24WXZrjg/Gf5P HFo z972zP9L5MJbc4k W0GeL94eTfIr6oRdf6wz5GH3/H1BLBwjh9uyJawwAAMwcAABQSwECFAAUAAgICADDdTNV4fbsiWsMAADMHAAAIAAAAAAAAAAAAAAAAAAAAAAAMjAxOTUwMTExNjktNDAtUDAwMi0wMDAwMTc3NS5YTUxQSwUGAAAAAAEAAQBOAAAAuQwAAAAA";		
		String rContentFile = this.getReplaceContentFile(contentFile);
		log.info("rContentFile: {} ",rContentFile);
		byte[] contentByte = Base64.getDecoder().decode(rContentFile);
		String sContentByte = new String(contentByte);
		log.info(" sContentByte: {} "+sContentByte);
	}   
	
	public String getReplaceContentFile(String contentFile) {
		String rContentFile = contentFile.replaceAll(" ", "+");
		return rContentFile;		
	}
}
