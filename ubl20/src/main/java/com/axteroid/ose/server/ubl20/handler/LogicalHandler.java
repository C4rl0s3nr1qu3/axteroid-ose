package com.axteroid.ose.server.ubl20.handler;

/**
 * Created by IntelliJ IDEA.
 * User: kaiser
 * Date: 16/09/12
 * Time: 09:00 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;


public class LogicalHandler implements javax.xml.ws.handler.LogicalHandler<LogicalMessageContext> {


    public void close(MessageContext mc) {
    }


    public boolean handleFault(LogicalMessageContext messagecontext) {
        return true;
    }

    public boolean handleMessage(LogicalMessageContext mc) {
        HandlerUtils.printMessageContext("Service LogicalHandler", mc);
        if (Boolean.FALSE.equals(mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
                LogicalMessage msg = mc.getMessage();
                System.out.println("xxxxxxxxxxxxx "+msg.toString());
/*
                Object payload = msg.getPayload(jaxbContext);
                if (payload instanceof BillService) {
                    BillService req = (BillService) payload;
                    System.out.println("here en payload");
                }
*/

        }
        return true;
    }

}
