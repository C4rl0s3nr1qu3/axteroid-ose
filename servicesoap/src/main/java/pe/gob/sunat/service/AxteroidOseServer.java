package pe.gob.sunat.service;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService
public interface AxteroidOseServer {

    @WebMethod(action = "urn:sendBill")
    @RequestWrapper(localName = "sendBill", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendBill")
    @ResponseWrapper(localName = "sendBillResponse", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendBillResponse")
    @WebResult(name = "applicationResponse", targetNamespace = "")
	public byte[] sendBill(
        @WebParam(name = "fileName", targetNamespace = "")
        java.lang.String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile
    ) throws SOAPException;	
	
    @WebMethod(action = "urn:sendSummary")
    @RequestWrapper(localName = "sendSummary", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendSummary")
    @ResponseWrapper(localName = "sendSummaryResponse", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendSummaryResponse")
    @WebResult(name = "ticket", targetNamespace = "")
    public java.lang.String sendSummary(
            @WebParam(name = "fileName", targetNamespace = "")
            java.lang.String fileName,
            @WebParam(name = "contentFile", targetNamespace = "")
            DataHandler contentFile
    ) throws SOAPException;
    
    @WebMethod(action = "urn:getStatus")
    @RequestWrapper(localName = "getStatus", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.GetStatus")
    @ResponseWrapper(localName = "getStatusResponse", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.GetStatusResponse")
    @WebResult(name = "status", targetNamespace = "")
    public StatusResponse getStatus(
            @WebParam(name = "ticket", targetNamespace = "")
            java.lang.String ticket
    ) throws SOAPException;
    
    @WebMethod(action = "urn:getStatusCdr")
    @RequestWrapper(localName = "getStatusCdr", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.GetStatusCdr")
    @ResponseWrapper(localName = "getStatusCdrResponse", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.GetStatusCdrResponse")
    @WebResult(name = "document", targetNamespace = "")
    public byte[] getStatusCdr(
            @WebParam(name = "statusCdr", targetNamespace = "")
            StatusCdr statusCdr
    ) throws SOAPException;
 
    @WebMethod(action = "urn:sendPack")
    @RequestWrapper(localName = "sendPack", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendPack")
    @ResponseWrapper(localName = "sendPackResponse", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.service.SendPackResponse")
    @WebResult(name = "ticket", targetNamespace = "")
    public java.lang.String sendPack(
            @WebParam(name = "fileName", targetNamespace = "")
            java.lang.String fileName,
            @WebParam(name = "contentFile", targetNamespace = "")
            DataHandler contentFile
    ) throws SOAPException;
}
