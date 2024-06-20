package com.axteroid.ose.server.ws;

import static org.junit.Assert.*;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pe.gob.sunat.service.AxteroidOseServerWs;
import pe.gob.sunat.service.AxteroidOseServerWsImpl;
import pe.gob.sunat.service.StatusResponse;


public class OseJUnit4 {
	private static Endpoint endpoint;	
	
	private static AxteroidOseServerWsImpl oseServerWsImpl = new AxteroidOseServerWsImpl();
	
	@BeforeClass
    public static void beforeClass() throws Exception {		
        endpoint = Endpoint.publish("http://localhost:7071/ol-ti-itcpe/billService", oseServerWsImpl);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        endpoint.stop();
    }

    @Test
    public void testAddNoMockSetup() throws Exception {
        URL wsdlUrl = new URL("http://localhost:7071/ol-ti-itcpe/billService?wsdl");
        QName serviceName = new QName("http://service.sunat.gob.pe/", "oseServerWsImpl");
        Service service = Service.create(wsdlUrl, serviceName);

        AxteroidOseServerWs port = service.getPort(AxteroidOseServerWs.class);
        StatusResponse result = port.getStatus("100");
        assertEquals(0, result);
    }
}
