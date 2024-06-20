package com.axteroid.ose.server.ubl20.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.axteroid.ose.server.ubl20.gateway.command.Command;

import java.util.List;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReplicateReverseCmd")
public class ReplicateReverseCmd implements Command {
    transient public static String FASE_REQUEST = "REQUEST";
    transient public static String FASE_CONFIRM = "CONFIRM";
    @XmlElement(required = true)
    protected String destino;
    @XmlElement(required = true)
    protected String fase;
    @XmlElement(name = "replicacionIdList", nillable = true)
    List<Long> replicacionIdList;
    @XmlElement(name = "fileNameList", nillable = true)
    List<String> fileNameList;

    public ReplicateReverseCmd() {
    }

    public ReplicateReverseCmd(String destino, String fase) {
        this.destino = destino;
        this.fase = fase;
    }

    public boolean isFaseRequest() {
        return FASE_REQUEST.equals(fase)  ;
    }
    public boolean isFaseConfirm() {
        return FASE_REQUEST.equals(fase)  ;
    }
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public List<Long> getReplicacionIdList() {
        return replicacionIdList;
    }

    public void setReplicacionIdList(List<Long> replicacionIdList) {
        this.replicacionIdList = replicacionIdList;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
}
