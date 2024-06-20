package com.axteroid.ose.server.ubl20.gateway.request;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.ubl20.gateway.CmdSignOnLine;
import com.axteroid.ose.server.ubl20.gateway.batch.EDocumentoCliente;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;
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
@XmlType(propOrder = {
        "parameters",
        "documentos"
})
@XmlRootElement(name = "ValidateOnLineCmd")
public class ValidateOnLineCmd extends ActionImpl {

    @XmlElement(name = "documento", nillable = true)
    @NotEmpty(groups = CmdSignOnLine.class)
    @Valid
    List<EDocumentoCliente> documentos;

    @Override
    public String getResult() {
        return null;
    }

    public ValidateOnLineCmd() {
    }

    @XmlElement(name = "parameter", nillable = true)
    @NotEmpty(message = "7027")
    List<Parameter> parameters;

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<EDocumentoCliente> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<EDocumentoCliente> documentos) {
        this.documentos = documentos;
    }

    @Override
    public String getDeclareSunat() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }

    @Override
    public String getPublish() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }
}
