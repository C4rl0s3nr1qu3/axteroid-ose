package com.axteroid.ose.server.ubl20.gateway.request;

import com.axteroid.ose.server.tools.constantes.Constantes;
import com.axteroid.ose.server.ubl20.gateway.batch.EResumenCliente;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "parameters",
        "documentos"
})
@XmlRootElement(name = "ValidateOnLineSummaryCmd")
public class ValidateOnLineSummaryCmd extends  ActionImpl  {

    @XmlElement(name = "documento",nillable = true)
    List<EResumenCliente> documentos;

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

    public ValidateOnLineSummaryCmd() {
    }

    public List<EResumenCliente> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<EResumenCliente> documentos) {
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

    @Override
    public String getDeclareDirectSunat() {
        return Constantes.SUNAT_ESTADOCP_NO_EXISTE;
    }
}
