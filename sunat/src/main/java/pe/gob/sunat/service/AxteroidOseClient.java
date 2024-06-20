package pe.gob.sunat.service;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
//import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://service.sunat.gob.pe/", name = "AxteroidOseServer")
//@XmlSeeAlso({ObjectFactory.class})
public interface AxteroidOseClient {

    @WebMethod
    @RequestWrapper(localName = "getStatusCdr", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.GetStatusCdr")
    @ResponseWrapper(localName = "getStatusCdrResponse", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.GetStatusCdrResponse")
    @WebResult(name = "return", targetNamespace = "")
    public byte[] getStatusCdr(
        @WebParam(name = "statusCdr", targetNamespace = "")
        pe.gob.sunat.service.StatusCdr statusCdr
    ) throws SOAPException;

    @WebMethod
    @RequestWrapper(localName = "sendPack", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendPack")
    @ResponseWrapper(localName = "sendPackResponse", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendPackResponse")
    @WebResult(name = "ticket", targetNamespace = "")
    public String sendPack(
        @WebParam(name = "fileName", targetNamespace = "")
        java.lang.String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile
    ) throws SOAPException;

    @WebMethod
    @RequestWrapper(localName = "sendBill", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendBill")
    @ResponseWrapper(localName = "sendBillResponse", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendBillResponse")
    @WebResult(name = "applicationResponse", targetNamespace = "")
    public byte[] sendBill(
        @WebParam(name = "fileName", targetNamespace = "")
        java.lang.String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile
    ) throws SOAPException;

    @WebMethod
    @RequestWrapper(localName = "getStatus", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.GetStatus")
    @ResponseWrapper(localName = "getStatusResponse", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.GetStatusResponse")
    @WebResult(name = "return", targetNamespace = "")
    public pe.gob.sunat.service.StatusResponse getStatus(
        @WebParam(name = "ticket", targetNamespace = "")
        java.lang.String ticket
    )throws SOAPException;

    @WebMethod
    @RequestWrapper(localName = "sendSummary", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendSummary")
    @ResponseWrapper(localName = "sendSummaryResponse", targetNamespace = "http://service.sunat.gob.pe/", className = "pe.gob.sunat.service.SendSummaryResponse")
    @WebResult(name = "ticket", targetNamespace = "")
    public String sendSummary(
        @WebParam(name = "fileName", targetNamespace = "")
        java.lang.String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile
    )throws SOAPException;
}
