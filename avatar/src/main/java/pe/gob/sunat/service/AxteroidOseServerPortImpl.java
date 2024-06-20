package pe.gob.sunat.service;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@javax.jws.WebService(
                      serviceName = "axteroidOseServer",
                      portName = "AxteroidOseServerWsPort",
                      targetNamespace = "http://service.sunat.gob.pe/",
                      wsdlLocation = "http://localhost:6080/ol-ti-itcpe/billService?wsdl",
                      endpointInterface = "pe.gob.sunat.service")
                      
public class AxteroidOseServerPortImpl implements AxteroidOseServerInterface {

	private static final Logger log = LoggerFactory.getLogger(AxteroidOseServerPortImpl.class);

    public byte[] getStatusCdr(pe.gob.sunat.service.StatusCdr statusCdr) { 
        log.info("Executing operation getStatusCdr");
        System.out.println(statusCdr);
        try {
            byte[] _return = new byte[] {};
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


    public String sendPack(java.lang.String fileName, DataHandler contentFile) { 
    	log.info("Executing operation sendPack");
        System.out.println(fileName);
        System.out.println(contentFile);
        try {
        	String _return ="";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public byte[] sendBill(java.lang.String fileName, DataHandler contentFile) { 
    	log.info("Executing operation sendBill");
        System.out.println(fileName);
        System.out.println(contentFile);
        try {
            byte[] _return = new byte[] {};
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public pe.gob.sunat.service.StatusResponse getStatus(java.lang.String ticket) { 
    	log.info("Executing operation getStatus");
        System.out.println(ticket);
        try {
            pe.gob.sunat.service.StatusResponse _return = new pe.gob.sunat.service.StatusResponse();
            byte[] _returnContent = new byte[] {};
            _return.setContent(_returnContent);
            _return.setStatusCode("StatusCode-515565130");
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public String sendSummary(java.lang.String fileName, DataHandler contentFile) { 
    	log.info("Executing operation sendSummary");
        System.out.println(fileName);
        System.out.println(contentFile);
        try {
        	String _return = "";
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
