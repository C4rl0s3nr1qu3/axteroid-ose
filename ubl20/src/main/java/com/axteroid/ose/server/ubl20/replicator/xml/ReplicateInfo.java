package com.axteroid.ose.server.ubl20.replicator.xml;

/**
 * User: jmacavilca
 * Date: 20/03/12
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Info", propOrder = {
        "worker",
        "origen",
        "destino",
        "estado"
})
public class ReplicateInfo {
    @XmlElement(required = true)
    protected String worker;

    @XmlElement(required = true)
    protected String origen;

    @XmlElement(required = true)
    protected String destino;

    @XmlElement(required = true)
    protected String estado;

    public ReplicateInfo() {
    }

    public ReplicateInfo(String worker, String origen, String destino, String estado) {
        this.worker = worker;
        this.origen = origen;
        this.destino = destino;
        this.estado = estado;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Info{" + worker+ ':' + origen+ ':'+estado+'}';
    }
}
