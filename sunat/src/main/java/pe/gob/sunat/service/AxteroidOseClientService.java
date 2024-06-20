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
public class AxteroidOseClientService extends Service{
	   public final static URL WSDL_LOCATION;

	    public final static QName SERVICE = new QName("http://service.sunat.gob.pe/", "axteroidOseServer");
	    public final static QName AxteroidOseServerPort = new QName("http://service.sunat.gob.pe/", "AxteroidOseServerPort");
	    static {
	        URL url = null;
	        try {
	            url = new URL("http://localhost:6080/ol-ti-itcpe/billService?wsdl");
	        } catch (MalformedURLException e) {
	            java.util.logging.Logger.getLogger(AxteroidOseClientService.class.getName())
	                .log(java.util.logging.Level.INFO, 
	                     "Can not initialize the default wsdl from {0}", "http://localhost:6080/ol-ti-itcpe/billService?wsdl");
	        }
	        WSDL_LOCATION = url;
	    }

   public AxteroidOseClientService(URL wsdlLocation) {
       super(wsdlLocation, SERVICE);
   }

   public AxteroidOseClientService(URL wsdlLocation, QName serviceName) {
       super(wsdlLocation, serviceName);
   }

   public AxteroidOseClientService() {
       super(WSDL_LOCATION, SERVICE);
   }
   
   public AxteroidOseClientService(WebServiceFeature ... features) {
       super(WSDL_LOCATION, SERVICE, features);
   }

   public AxteroidOseClientService(URL wsdlLocation, WebServiceFeature ... features) {
       super(wsdlLocation, SERVICE, features);
   }

   public AxteroidOseClientService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
       super(wsdlLocation, serviceName, features);
   }    

   @WebEndpoint(name = "AxteroidOseServerPort")
   public AxteroidOseClient getAxteroidOseServerPort() {
       return super.getPort(AxteroidOseServerPort, AxteroidOseClient.class);
   }

   @WebEndpoint(name = "AxteroidOseServerPort")
   public AxteroidOseClient getAxteroidOseServerPort(WebServiceFeature... features) {
       return super.getPort(AxteroidOseServerPort, AxteroidOseClient.class, features);
   }
}
