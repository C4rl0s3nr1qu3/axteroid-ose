//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.15 at 06:45:22 PM COT 
//


package com.axteroid.ose.server.ubl20.gateway.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "EmisorEstadoCmd")
public class EmisorEstadoCmd extends  ActionImpl  {

    public EmisorEstadoCmd() {
    }

    public EmisorEstadoCmd(String idEmisor,String contenido) {
        parameters= new ArrayList<Parameter>();
        parameters.add(new Parameter(Parameter.ID_EMISOR,idEmisor));
        parameters.add(new Parameter(Parameter.CONTENIDO,contenido));
    }

    @XmlElement(name = "parameter",nillable = true)
    List<Parameter> parameters;

    @Override
    public String getResult() {
        return null;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
    public String getDeclareSunat() {
        return null;
    }
    public String getPublish() {
        return null;
    }

}
