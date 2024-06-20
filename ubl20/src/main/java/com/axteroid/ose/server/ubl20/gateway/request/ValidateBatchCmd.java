package com.axteroid.ose.server.ubl20.gateway.request;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.ubl20.gateway.CmdSignBatch;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(name = "ValidateBatchCmd")
public class ValidateBatchCmd extends ActionImpl {

    @XmlElement(name = "parameters", nillable = true)
    @NotEmpty(message = "7027")
    @Valid
    protected List<Parameter> parameters;

    public ValidateBatchCmd() {
    }

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

    public Class getInterfaceValidation(){
        return CmdSignBatch.class;
    }

    @Override
    public String getDeclareSunat() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }

    @Override
    public String getPublish() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }

    @Override
    public String getDeclareDirectSunat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
