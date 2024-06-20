package com.axteroid.ose.server.builder.sendsummary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.rulesejb.rules.OseDBCRUDLocal;
import com.axteroid.ose.server.tools.constantes.Constantes;

public class SendSummaryContent {
	private static final Logger log = LoggerFactory.getLogger(SendSummaryContent.class);
	
	public void getContentSendSummary(String idContent) {		
		try {
			Documento documento = new Documento();
			documento.setId(Long.parseLong(idContent));
			documento.setErrorComprobante(Constantes.CONTENT_PROCESO.charAt(0));

			OseDBCRUDLocal oseDBCRUDLocal = 
				(OseDBCRUDLocal) InitialContext.doLookup(Constantes.OSE_doLookup+"OseDBCRUDImpl"+Constantes.OSE_CDEJB_RULES+"OseDBCRUDLocal");						

			List<Documento> listDocumento = oseDBCRUDLocal.buscarTbComprobanteALL(documento);
			if(documento.getErrorComprobante().equals(Constantes.CONTENT_FALSE.charAt(0)) || 
					documento.getErrorComprobante().equals(Constantes.CONTENT_ERROR_DB.charAt(0))) 
				return;	
			
			for(Documento doc: listDocumento) {	
				SendSummaryProcessGo procesarSendSummaryGo = new SendSummaryProcessGo();	
				procesarSendSummaryGo.procesarComprobanteGet(doc);
			}
			
		}catch(Exception e) {
			StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("getContentSendSummary Exception \n"+errors);
		}
	}
	
}
