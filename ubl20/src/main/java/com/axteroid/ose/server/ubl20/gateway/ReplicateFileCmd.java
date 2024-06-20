package com.axteroid.ose.server.ubl20.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.axteroid.ose.server.ubl20.gateway.command.Command;
import com.axteroid.ose.server.ubl20.replicator.xml.ReplicateInfo;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReplicateFileCmd")
public class ReplicateFileCmd implements Command {
    @XmlElement(required = true)
    protected ReplicateInfo info;

    @XmlElement(required = true)
    protected String fileName;

    public ReplicateFileCmd() {
    }

    public ReplicateInfo getInfo() {
        return info;
    }

    public void setInfo(ReplicateInfo info) {
        this.info = info;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ReplicateFileCmd{" +
                "info=" + info +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
