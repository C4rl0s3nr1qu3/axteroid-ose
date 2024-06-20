package com.axteroid.ose.server.ubl20.gateway.request;

import javax.xml.bind.annotation.*;

import com.axteroid.ose.server.tools.constantes.Constantes;

import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ConsultBatchCmd")
public class ConsultBatchCmd extends ActionImpl {

    @XmlElement(name = "parameter", nillable = true)
    private List<Parameter> parameters;

    @XmlAttribute(required = false)
    protected String output = "";

    @Override
    public String getResult() {
        return output;
    }

    public ConsultBatchCmd() {
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getDeclareSunat() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }

    @Override
    public String getPublish() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
