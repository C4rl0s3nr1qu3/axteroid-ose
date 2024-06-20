package pe.gob.sunat.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServiceClient(name = "billService", 
	wsdlLocation = "https://e-betaose2.sunat.gob.pe/ol-ti-itemision-cpe/billService?wsdl",
	targetNamespace = "http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/") 
public class BillServiceSunat extends Service{
	private static final Logger log = LoggerFactory.getLogger(BillServiceSunat.class);
    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "billService");
    public final static QName BillServicePort = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "BillServicePort");
    static {
        URL url = null;
        try {
            url = new URL("https://e-betaose2.sunat.gob.pe/ol-ti-itemision-cpe/billService?wsdl");
        } catch (MalformedURLException e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("MalformedURLException \n"+errors);
        }catch(Throwable e) {
        	StringWriter errors = new StringWriter();				
			e.printStackTrace(new PrintWriter(errors));
			log.error("BillService_Ose \n"+errors);
        }        
        WSDL_LOCATION = url;
    }

    public BillServiceSunat(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BillServiceSunat(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BillServiceSunat() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public BillServiceSunat(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public BillServiceSunat(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public BillServiceSunat(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    @WebEndpoint(name = "BillServicePort")
    public BillServiceSunatInterface getBillServicePort() {
        return super.getPort(BillServicePort, BillServiceSunatInterface.class);
    }

    @WebEndpoint(name = "BillServicePort")
    public BillServiceSunatInterface getBillServicePort(WebServiceFeature... features) {
        return super.getPort(BillServicePort, BillServiceSunatInterface.class, features);
    }

}
