package com.axteroid.ose.server.avatar.worker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.avatar.build.GetStatusBuild;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoAvatarPropertiesEnum;
import com.axteroid.ose.server.tools.util.DateUtil;
import com.axteroid.ose.server.tools.util.DirUtil;
import com.axteroid.ose.server.tools.util.LogDateUtil;

public class DetalleDocumentosWorker {
    static Logger log = LoggerFactory.getLogger(DetalleDocumentosWorker.class);

    public void worker(){    	
    	Map<String, String> mapa = DirUtil.getMapRobotProperties();					
		
		if((mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA.getCodigo()) != null) &&
				(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		
			log.info("ACTUALIZAR_IBM_PG_MONGO_FACTURA: " + mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA.getCodigo()));
			int proceso = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_PROCESO.getCodigo()));
			int year = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_YEAR.getCodigo()));
			int mesINI = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_MES_INI.getCodigo()));
			int mesFIN = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_MES_FIN.getCodigo()));
			int diaINI = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_DIA_INI.getCodigo()));
			int diaFIN = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_MONGO_FACTURA_DIA_FIN.getCodigo()));
			Date fecDBInicio = DateUtil.stringToDateYYYY_MM_DD(year+"-"+mesINI+"-"+diaINI);
			Date fecDBFin = DateUtil.stringToDateYYYY_MM_DD(year+"-"+mesFIN+"-"+diaFIN);
			String td = "in ('RA','RC','RR')"; 
			this.revisar_FACTURACION(proceso, fecDBInicio, fecDBFin, td);
		}		
		
		if((mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE.getCodigo())!=null) &&
				(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE.getCodigo()).trim().equals(Constantes.CONTENT_TRUE))) {		
			log.info("ACTUALIZAR_IBM_PG_FACTURA_CPE: " + mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE.getCodigo()));
			int proceso = Integer.parseInt(mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_PROCESO.getCodigo()));
			//log.info("ACTUALIZAR_IBM_PG_FACTURA_CPE_PROCESO: " + mapa.get(TipoFileRobotProperEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_PROCESO.getCodigo()));
			String fechaIniDB = mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_INI.getCodigo());
			//log.info("ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_INI: " + mapa.get(TipoFileRobotProperEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_INI.getCodigo()));
			String fechaFinDB = mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_FIN.getCodigo());
			//log.info("ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_FIN: " + mapa.get(TipoFileRobotProperEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_FECHA_FIN.getCodigo()));
			String td = mapa.get(TipoAvatarPropertiesEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_TD.getCodigo());
			//log.info("ACTUALIZAR_IBM_PG_MONGO_FECHA_MES_FIN: " + mapa.get(TipoFileRobotProperEnum.ACTUALIZAR_IBM_PG_FACTURA_CPE_TD.getCodigo()));
			Date fecDBInicio = DateUtil.stringToDateYYYY_MM_DD(fechaIniDB); 
			Date fecDBFin = DateUtil.stringToDateYYYY_MM_DD(fechaFinDB);
			this.actualizaIBM_PG_FACTURA_CPE(proceso, fecDBInicio, fecDBFin, td);
		}			
    }
    
    private void revisar_FACTURACION(int status, Date fecDBInicio, Date fecDBFin, String td){
    	try {
    		log.info("revisar_FACTURACION ");    				   		   			   		   		
			//int k=0;
			Date fecDBAct = fecDBInicio;
			while(LogDateUtil.esFechaMayor(fecDBFin,fecDBAct)) {
				//k++;
				String strFecDBAct = DateUtil.dateToStringYYYY_MM_DD(fecDBAct);
		    	String [] fecDB = strFecDBAct.split("-");
		    	int year = Integer.parseInt(fecDB[0]);
		    	int mes = Integer.parseInt(fecDB[1]);
		    	int dia = Integer.parseInt(fecDB[2]);
				String fechaDetalle = " extract(year from FECHA_CREA::timestamp) = "+year
						+ " and extract(month from FECHA_CREA::timestamp) = "+mes
						+ " and extract(day from FECHA_CREA::timestamp) = "+dia;
				GetStatusBuild getStatusBuild = new GetStatusBuild();
				List<String> listDetalleResumen = getStatusBuild.getListDetalleResumenTDSSShort(fechaDetalle, td);
	    		
				fecDBAct = DateUtil.addDays(fecDBAct, 1);				
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
    					getStatusBuild.revisarFacturaOseTDSSShort(fecha, td, listDetalleResumen);	 
    			}	    		
	    	}	
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception \n"+errors);
		}	  
    }
    
    private void actualizaIBM_PG_FACTURA_CPE(int status, Date fecDBInicio, Date fecDBFin, String td){
    	try {
    		log.info("actualizaIBM_PG_FACTURA_CPE ");		   		   		
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
    				GetStatusBuild getStatusBuild = new GetStatusBuild();
    				if(status==6)
    					getStatusBuild.updateFacturaOseTD(year, mes, dia ,hora, td);	    
    				if(status==7)
    					getStatusBuild.updateFacturaOseTD_SSShort(fecha, td);	 
//    				if(status==8)	    				
//    					getStatusBuild.updateFacturaOseTD_OLD(status, year, mes, dia, hora, td);
//    				if(status==9)	    				
//    					getStatusBuild.updateFacturaOseTD_OLD_SS(year, mes, dia, hora, td);
    				if(status==17) {
    					getStatusBuild.updateFacturaOseTD_SSMIN(year, mes, dia ,hora, td, " between 0 and 29 ");	
    					getStatusBuild.updateFacturaOseTD_SSMIN(year, mes, dia ,hora, td, " between 30 and 59 ");	
    				}
    			}	    		
	    	}	
    	} catch (Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("actualizaIBM_PG_MONGO_FECHA Exception \n"+errors);
		}	 	  
    } 
    
}
