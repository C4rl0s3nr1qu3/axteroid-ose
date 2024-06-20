package com.axteroid.ose.server.ubl20.gateway;

import java.util.List;

import org.slf4j.Logger;
//import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.ubl20.gateway.command.Command;


/**
 * User: RAC                  * Date: 23/03/12
 */
public class SignAction implements Command {
	//private static final Logger log = Logger.getLogger(SignAction.class);
	private static final Logger log = LoggerFactory.getLogger(SignAction.class);
    private List documentos;

    //private Ejecucion ejecucion;

    public String get(String value) {
        //return ejecucion.get(value);
        return null;
    }

    public String getVersion() {
        //return ejecucion.getVersion();
        return null;
    }

    public String getTipoAccion() {
        //return ejecucion.getAccion();
        return null;
    }

/*    public Ejecucion getEjecucion() {
        return ejecucion;
    }

    public void setEjecucion(Ejecucion ejecucion) {
        this.ejecucion = ejecucion;
    }*/

    public boolean isSunatOnline() {
        return "SunatOnLineCmd".equals(getTipoAccion()) || "SunatOnLineSummaryCmd".equals(getTipoAccion());

    }

    public boolean isSignOnline() {
    	log.info("logbiz - isSignOnline - getTipoAccion():"+getTipoAccion());
        return "ValidateOnLineCmd".equals(getTipoAccion()) ||
                "ValidateOnLineSummaryCmd".equals(getTipoAccion()) ||
                "SignOnLineCmd".equals(getTipoAccion()) ||
                "SignOnLineSummaryCmd".equals(getTipoAccion()) || 
                "SignOnLineDespatchCmd".equals(getTipoAccion()) || 
                "SignOnLineRetentionCmd".equals(getTipoAccion()) || 
                "SignOnLinePerceptionCmd".equals(getTipoAccion()) || 
                "SignOnLineReversionCmd".equals(getTipoAccion());
    }

    public boolean isSignBatch() {
        return "ValidateBatchCmd".equals(getTipoAccion()) ||
                "SignBatchCmd".equals(getTipoAccion());
    }

    public Long getIdTicket() {
        //return ejecucion.getId();
        return null;
    }

    public List getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List documentos) {
        this.documentos = documentos;
    }

    public int getTotalDocumentos() {
        if (documentos == null) return 0;
        return documentos.size();
    }

    public boolean isSummary() {
        return "SignOnLineSummaryCmd".equals(getTipoAccion()) ||
                "SignOneSummaryCmd".equals(getTipoAccion()) ||
                "SunatOnLineSummaryCmd".equals(getTipoAccion()) ||
                "ValidateOnLineSummaryCmd".equals(getTipoAccion());
    }
    
    public boolean isReversion() {
    	return "SignOnLineReversionCmd".equals(getTipoAccion());
    }
    
    public boolean isDespatch() {
        return "SignOnLineDespatchCmd".equals(getTipoAccion());
    }
    
    public boolean isRetention() {
        return "SignOnLineRetentionCmd".equals(getTipoAccion());
    }
    
    public boolean isPerception() {
    	return "SignOnLinePerceptionCmd".equals(getTipoAccion());
    }    

    public boolean isDocument() {
/*    	if(UblConstantes.CODIGOS_BOLETA_FACTURA.contains(ejecucion.get(Parameter.TIPO_ARCHIVO)) 
    			|| UblConstantes.CODIGOS_NOTA_CREDITO_DEBITO.contains(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	 return false;
    }
    
    public boolean isResumenDocument() {
/*    	if(UblConstantes.CODIGOS_RESUMEN.contains(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	return false;
    }    

    public boolean isReversionDocument() {
/*    	if(Constantes.REVERSION.equals(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	return false;
    } 
    
    public boolean isSignOne() {
        return "SignOneCmd".equals(getTipoAccion()) ||
                "SignOneSummaryCmd".equals(getTipoAccion());
    }
    
    public boolean isGuiaDocument() {
 /*   	if(UblConstantes.CODIGOS_GUIAS.contains(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	return false;
    }
    
    public boolean isRetencionDocument() {
/*    	if(Constantes.RETENCION.equals(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	return false;
    }
    
    public boolean isPercepcionDocument() {
/*    	if(Constantes.PERCEPCION.equals(ejecucion.get(Parameter.TIPO_ARCHIVO))) {
    		return true;
    	} else {
    		return false;
    	}*/
    	return false;
    }
}
