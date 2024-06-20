package pe.gob.sunat.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "axteroidOseServer", 
wsdlLocation = "http://localhost:6080/ol-ti-itcpe/billService?wsdl",
targetNamespace = "http://service.sunat.gob.pe/") 
public class AxteroidOseServerService extends Service{
	public final static URL WSDL_LOCATION;
	public final static QName SERVICE = new QName("http://service.sunat.gob.pe/", "axteroidOseServer");
	public final static QName AxteroidOseServerWsPort = new QName("http://service.sunat.gob.pe/", "AxteroidOseServerWsPort");
	static {
		URL url = null;
	    try {
	    	url = new URL("http://localhost:6080/ol-ti-itcpe/billService?wsdl");
	    } catch (MalformedURLException e) {
	    	java.util.logging.Logger.getLogger(AxteroidOseServerService.class.getName())
	        	.log(java.util.logging.Level.INFO, 
	             "Can not initialize the default wsdl from {0}", "http://localhost:6080/ol-ti-itcpe/billService?wsdl");
	    }
	    WSDL_LOCATION = url;
	}

   public AxteroidOseServerService(URL wsdlLocation) {
       super(wsdlLocation, SERVICE);
   }

   public AxteroidOseServerService(URL wsdlLocation, QName serviceName) {
       super(wsdlLocation, serviceName);
   }

   public AxteroidOseServerService() {
       super(WSDL_LOCATION, SERVICE);
   }
   
   public AxteroidOseServerService(WebServiceFeature ... features) {
       super(WSDL_LOCATION, SERVICE, features);
   }

   public AxteroidOseServerService(URL wsdlLocation, WebServiceFeature ... features) {
       super(wsdlLocation, SERVICE, features);
   }

   public AxteroidOseServerService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
       super(wsdlLocation, serviceName, features);
   }    

   @WebEndpoint(name = "AxteroidOseServerWsPort")
   public AxteroidOseServerInterface getOseServerPort() {
       return super.getPort(AxteroidOseServerWsPort, AxteroidOseServerInterface.class);
   }

   @WebEndpoint(name = "AxteroidOseServerPort")
   public AxteroidOseServerInterface getOseServerPort(WebServiceFeature... features) {
       return super.getPort(AxteroidOseServerWsPort, AxteroidOseServerInterface.class, features);
   }
}
