package com.axteroid.ose.server.builder.build;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.builder.sendsummary.SendSummaryProcessGo;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.rulesejb.rules.impl.OseDBCRUDImpl;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class SendUBLBuild {
	//private static final Logger log = Logger.getLogger(SendUBLBuild.class);
	private static final Logger log = LoggerFactory.getLogger(SendUBLBuild.class);
	private OseDBCRUDLocal oseDBCRUDLocal = new OseDBCRUDImpl();
	
	public void getBuilding(String id){
		try {
			Documento tb = new Documento();
			tb.setId(Long.parseLong(id));
			Documento documento = oseDBCRUDLocal.buscarTbComprobanteXID(tb);	
			if(!(documento.getId()!=null))
				return;				
//			TbContent tbc = tbComprobante.getIdContent();
//			TbContent tbContent = oseDBCRUDLocal.buscarTbContentID(tbc);
//			if(!(tbContent.getId()!=null))
//				return;	
			this.buildComprobante(documento);												
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getBuilding \n"+errors);
		}
	}	
	
	public void buildComprobante(Documento documento) {		
		log.info("buildComprobante: "+documento.toString()+" | "+documento.getEstado());
		try {
			if(documento.getTipoDocumento().equals(Constantes.SUNAT_COMUNICACION_BAJAS) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_RESUMEN_DIARIO) ||
					documento.getTipoDocumento().equals(Constantes.SUNAT_REVERSION)) {	
				SendSummaryProcessGo sendSummaryProcessGo = new SendSummaryProcessGo();
				sendSummaryProcessGo.procesarComprobante(documento);
			}
//			if(documento.getTipoDocumento().equals(ConstantesOse.SUNAT_PACK) ) {	
//				SendPackProcess procesarSendPack = new SendPackProcess();
//				procesarSendPack.procesarComprobante(tbComprobante, tbContent);
//			}
			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("buildComprobante Exception \n"+errors);
		}
	}	
	
//	public void getBuildingLT(String id) {
//		try {
//			TbContent tbc= new TbContent();
//			tbc.setId(Long.parseLong(id));				
//			TbContent tbContent = oseDBCRUDLocal.buscarTbContentID(tbc);
//			if(!(tbContent.getId()!=null))
//				return;	
//			List<Documento> listTBC = oseDBCRUDLocal.buscarTbComprobanteXContentID(tbContent);			
//			this.buildComprobanteLT(listTBC,tbContent);												
//		}catch(Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("getBuilding \n"+errors);
//		}
//	}		
//	
//	public void buildComprobanteLT(List<Documento> listTBC,TbContent tbContent) {		
//		log.info("buildComprobanteLT: "+tbContent.getFilename()+" | "+listTBC.size() );
//		try {
//			SendPackProcess procesarSendPack = new SendPackProcess();
//			procesarSendPack.procesarComprobanteGet(listTBC, tbContent);
//			
//		}catch(Exception e) {
//			StringWriter errors = new StringWriter();				
//			e.printStackTrace(new PrintWriter(errors));
//			log.error("buildComprobanteLT Exception \n"+errors);
//		}
//	}	
		
}
