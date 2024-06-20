package com.axteroid.ose.server.ubl20.gateway.request;

import org.hibernate.validator.constraints.NotEmpty;
import javax.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {
        "parameters"
})
@XmlRootElement(name = "ReplicateXmlCmd")
public class ReplicateXmlCmd extends ActionImpl {
    @XmlAttribute(name = "declare-sunat", required = false)
    protected String declareSunat = "0";
    @XmlAttribute(name = "declare-direct-sunat", required = false)
    protected String declareDirectSunat = "0";
    @XmlAttribute(required = false)
    protected String publish = "0";
    @XmlAttribute(required = false)
    protected String output = "";

    public ReplicateXmlCmd() {
    }

    @XmlElement(name = "parameter", nillable = true)
    @NotEmpty(message = "7027")
    List<Parameter> parameters;

    @Override
    public String getResult() {
        return output;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getDeclareSunat() {
        return declareSunat;
    }

    public void setDeclareSunat(String declareSunat) {
        this.declareSunat = declareSunat;
    }

    public String getPublish() {
        return publish;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getDeclareDirectSunat() {
        return declareDirectSunat;
    }

    public void setDeclareDirectSunat(String declareDirectSunat) {
        this.declareDirectSunat = declareDirectSunat;
    }
}
