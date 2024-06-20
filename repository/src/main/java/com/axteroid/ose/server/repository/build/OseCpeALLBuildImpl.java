package com.axteroid.ose.server.repository.build;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.jpa.model.Documento;
import com.axteroid.ose.server.repository.rules.OseCpeALLCRUDLocal;
import com.axteroid.ose.server.repository.rules.impl.OseCpeALLCRUDImpl;
import com.axteroid.ose.server.tools.constantes.TipoDocumentoSUNATEnum;
import com.axteroid.ose.server.tools.edocu.EResumenDocumento;
import com.axteroid.ose.server.tools.edocu.EResumenDocumentoItem;
import com.axteroid.ose.server.tools.edocu.EReversionDocumento;
import com.axteroid.ose.server.tools.edocu.EReversionDocumentoItem;


public class OseCpeALLBuildImpl {
	private static final Logger log = LoggerFactory.getLogger(OseCpeALLBuildImpl.class);
	
	public void getDocument_CPEALL(Documento tbComprobante){
		log.info("getDocument_CPEALL "+tbComprobante.getId());
		this.grabarOseCPEALL(tbComprobante);					
	}	
		
	public void getDocument_SummaryResumen_ALL(Documento tbComprobante, EResumenDocumento eDocumento){		
		//log.info("getDocument_SummaryResumen "+tbComprobante.getId());
		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.RESUMEN_DIARIO.getCodigo()))
			this.getDocument_SummaryResumen_RC_ALL(tbComprobante, eDocumento);
		if(tbComprobante.getTipoDocumento().equals(TipoDocumentoSUNATEnum.COMUNICACION_BAJAS.getCodigo()))
			this.getDocument_SummaryResumen_RA_ALL(tbComprobante, eDocumento);
	}
	
	private void getDocument_SummaryResumen_RC_ALL(Documento tbComprobante, EResumenDocumento eDocumento){
		//log.info("getDocument_SummaryResumen_RC "+tbComprobante.getId());
		for(EResumenDocumentoItem rdi : eDocumento.getItems()){			 
			this.grabarOseCPEALL_RC_RA(rdi, tbComprobante);								
		}
	}
	
	private void getDocument_SummaryResumen_RA_ALL(Documento tbComprobante, EResumenDocumento eDocumento){
		//log.info("getDocument_SummaryResumen_RA "+tbComprobante.getId());
		for(EResumenDocumentoItem rdi : eDocumento.getItems()){
			this.grabarOseCPEALL_RC_RA(rdi, tbComprobante);				
		}
	}

	public void getDocument_SummaryResumen_RR_ALL(Documento tbComprobante, EReversionDocumento eDocumento){
		//log.info("getDocument_SummaryResumen_RR "+tbComprobante.getId());
		for(EReversionDocumentoItem rdi : eDocumento.getItems()){	
			this.grabarOseCPEALL_RR(rdi, tbComprobante);							
		}
	}

	private void grabarOseCPEALL(Documento tbComprobante) {
		BuildDocumentCPEALL buildDocumentCPEALL = new BuildDocumentCPEALL();
		Document document = buildDocumentCPEALL.appendDocumentCPEALL(tbComprobante);	
		OseCpeALLCRUDLocal OseCpeALLCRUDLocal = new OseCpeALLCRUDImpl();
		OseCpeALLCRUDLocal.grabarOseCPEALL(document);		
	}	
	
	private void grabarOseCPEALL_RC_RA(EResumenDocumentoItem rdi, Documento tbComprobante) {
		BuildDocumentCPEALL buildDocumentCPEALL = new BuildDocumentCPEALL();
		Document document = buildDocumentCPEALL.appendDocumentCPEALL_RC_RA(rdi, tbComprobante);	
		OseCpeALLCRUDLocal OseCpeALLCRUDLocal = new OseCpeALLCRUDImpl();
		OseCpeALLCRUDLocal.grabarOseCPEALL(document);		
	}
	
	private void grabarOseCPEALL_RR(EReversionDocumentoItem rdi, Documento tbComprobante) {
		BuildDocumentCPEALL buildDocumentCPEALL = new BuildDocumentCPEALL();
		Document document = buildDocumentCPEALL.appendDocumentCPEALL_RR(rdi, tbComprobante);	
		OseCpeALLCRUDLocal OseCpeALLCRUDLocal = new OseCpeALLCRUDImpl();
		OseCpeALLCRUDLocal.grabarOseCPEALL(document);		
	}
}
