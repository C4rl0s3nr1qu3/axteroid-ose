package com.axteroid.ose.server.ubl20.gateway.request;


import javax.xml.bind.annotation.XmlTransient;

import com.axteroid.ose.server.ubl20.gateway.command.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: RAC
 * Date: 16/02/12
 */
public abstract class ActionImpl implements Command {

    @XmlTransient
    protected Long idTicket;

    @XmlTransient
    protected Map<String, String> parametros=new HashMap<String, String>();

    public String getDeclareDirectSunat(){
        return null;
    }

    public abstract String getDeclareSunat();

    public abstract String getPublish();

    public abstract String getResult();

    public String get(String field) {
        return parametros.get(field);
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public abstract List<Parameter> getParameters();

    public Map<String, String> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, String> parametros) {
        this.parametros = parametros;
    }
}
