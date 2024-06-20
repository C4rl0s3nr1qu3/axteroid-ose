package com.axteroid.ose.server.builder.build;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.sunat.service.SendSunatClientCxf;
import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.tools.constantes.TipoTaskEnum;

public class DocumentReviewBuild {
	private static final Logger log = LoggerFactory.getLogger(DocumentReviewBuild.class);
	
	public void getBuilding(String ticket) {
		String [] status = ticket.split(Constantes.OSE_SPLIT);
		if(status[0].equals(TipoTaskEnum.SEND_CDR.getDescripcion())) {	
			SendCDRBuild sendCDRBuild = new SendCDRBuild();
			sendCDRBuild.getBuilding(status[1]);
			return;
		}
		
		if(status[0].equals(TipoTaskEnum.GET_CDR.getDescripcion())) {	
			this.getCDR(status[1]);
			return;
		}
		
		if(status[0].equals(TipoTaskEnum.UPDATE_CPE.getDescripcion())) {	
			UpdateCpeBuild updateCpeBuild = new UpdateCpeBuild();
			updateCpeBuild.getBuilding(status[1]);
			return;
		}
		
		if(status[0].equals(TipoTaskEnum.SEND_UBL.getDescripcion())) {	
			SendUBLBuild sendUBLBuild = new SendUBLBuild();
			sendUBLBuild.getBuilding(status[1]);
			return;
		}
		
	}	
		
	private void getCDR(String id) {
		try {
			Documento tb = new Documento();
			tb.setId(Long.parseLong(id));
			OseDBCRUDLocal oseDBCRUDLocal = 
					(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");						
			Documento tbComprobante = oseDBCRUDLocal.buscarTbComprobanteXID(tb);	
			if(!(tbComprobante.getId()!=null))
				return;
						
			SendSunatClientCxf oseSUNAT_Client = new SendSunatClientCxf();
			oseSUNAT_Client.getStatus(tbComprobante);
			if(!(tbComprobante.getMensajeSunat()!=null) || (tbComprobante.getMensajeSunat().length == 0))
				return;
			tbComprobante.setUserModi(Constantes.AVATAR_USER);		
			oseDBCRUDLocal.updateTbComprobanteCDR(tbComprobante);
									
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getCDR Exception \n"+errors);
		}
	}	
	
}
