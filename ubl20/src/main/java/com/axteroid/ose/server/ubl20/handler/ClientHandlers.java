package com.axteroid.ose.server.ubl20.handler;

/**
 * Created by IntelliJ IDEA.
 * User: kaiser
 * Date: 16/09/12
 * Time: 09:02 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;


public class ClientHandlers {

    public static class LogicalHandler implements
            javax.xml.ws.handler.LogicalHandler<LogicalMessageContext> {


        public void close(MessageContext mc) {
        }


        public boolean handleFault(LogicalMessageContext messagecontext) {
            return true;
        }


        public boolean handleMessage(LogicalMessageContext mc) {
            LogicalMessage msg = mc.getMessage();
            HandlerUtils.printMessageContext("Client LogicalHandler", mc);

            if (Boolean.TRUE.equals(mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {

                    System.out.println("xxxxxxxxxxxxx "+msg.toString());
/*
                    Object payload = msg.getPayload(jaxbContext);
                    if (payload instanceof BillService) {
                        BillService req = (BillService) payload;
                        System.out.println("here en clientehandler");
                    }
*/

            }
            return true;
        }

    }

    public static class SOAPHandler implements
            javax.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {


        public Set<QName> getHeaders() {
            return null;
        }


        public void close(MessageContext mc) {
        }


        public boolean handleFault(SOAPMessageContext mc) {
            return true;
        }


        public boolean handleMessage(SOAPMessageContext mc) {
            if (Boolean.TRUE.equals(mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
                HandlerUtils.printMessageContext("Client SOAPHandler", mc);
                SOAPMessage sm = mc.getMessage();

                try {
                    SOAPFactory sf = SOAPFactory.newInstance();
                    SOAPHeader sh = sm.getSOAPHeader();
                    if (sh == null) {
                        sh = sm.getSOAPPart().getEnvelope().addHeader();
                    }

                    Name twoTermName = sf.createName("TwoTerms", "samp", "http://www.example.org");
                    SOAPHeaderElement shElement = sh.addHeaderElement(twoTermName);
                    SOAPElement firstTerm = shElement.addChildElement("term");
                    firstTerm.addTextNode("Apple");
                    shElement.addChildElement(firstTerm);
                    SOAPElement secondTerm = shElement.addChildElement("term");
                    secondTerm.addTextNode("Orange");
                    shElement.addChildElement(secondTerm);
                } catch (SOAPException e) {
                    throw new ProtocolException(e);
                }
            }

            return true;
        }
    }
}