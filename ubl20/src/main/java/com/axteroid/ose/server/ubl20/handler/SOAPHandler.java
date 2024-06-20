package com.axteroid.ose.server.ubl20.handler;

/**
 * Created by IntelliJ IDEA.
 * User: kaiser
 * Date: 16/09/12
 * Time: 09:01 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

public class SOAPHandler implements javax.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(MessageContext mc) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext mc) {

        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext mc) {
        HandlerUtils.printMessageContext("Service SOAPHandler", mc);
        if (Boolean.FALSE.equals(mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
            SOAPMessage sm = mc.getMessage();

            try {
                SOAPHeader sh = sm.getSOAPHeader();

                // Note in real use validity checking should be done
                // (really two terms present? namespaces? etc.)
                System.out.println("handel message ... ajuaaaaaaaa");
            } catch (SOAPException e) {
                throw new ProtocolException(e);
            }
        }
        return true;
    }
}