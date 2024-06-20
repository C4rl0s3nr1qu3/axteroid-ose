package com.axteroid.ose.server.ubl20.replicator.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.axteroid.ose.server.ubl20.gateway.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class ReplicateGroupL1Replicacion implements Command {
    @XmlElement(required = false)
    Long replicacionId;
    @XmlElement(required = false)
    List<ReplicateGroupL2ReplicacionEntity> entitys = new ArrayList<ReplicateGroupL2ReplicacionEntity>();
    @XmlElement(name = "fileXml")
    String fileXml;

    @XmlElement(name = "nombreAdjunto_1")
    String nombreAdjunto_1;
    @XmlElement(name = "nombreAdjunto_2")
    String nombreAdjunto_2;
    @XmlElement(name = "nombreAdjunto_3")
    String nombreAdjunto_3;
    @XmlElement(name = "nombreAdjunto_4")
    String nombreAdjunto_4;
    @XmlElement(name = "nombreAdjunto_5")
    String nombreAdjunto_5;


    @XmlElement(name = "fileAdicionales")
    List<String> fileAdicionales = new ArrayList<String>();


    public Long getReplicacionId() {
        return replicacionId;
    }

    public void setReplicacionId(Long replicacionId) {
        this.replicacionId = replicacionId;
    }

    public List<ReplicateGroupL2ReplicacionEntity> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<ReplicateGroupL2ReplicacionEntity> entitys) {
        this.entitys = entitys;
    }

    public String getFileXml() {
        return fileXml;
    }

    public void setFileXml(String fileXml) {
        this.fileXml = fileXml;
    }



    public List<String> getFileAdicionales() {
        return fileAdicionales;
    }

    public void setFileAdicionales(List<String> fileAdicionales) {
        this.fileAdicionales = fileAdicionales;
    }

    @Override
    public String toString() {
        return "Replicacion{" +
                "replicacionId=" + replicacionId +
                ", entitys=" + entitys.size() +
                ", fileXml='" + (fileXml!=null) + '\'' +
                ", nombreAdjunto_1='" + (nombreAdjunto_1!=null) + '\'' +
                ", nombreAdjunto_2='" + (nombreAdjunto_2!=null) + '\'' +
                ", nombreAdjunto_3='" + (nombreAdjunto_3!=null) + '\'' +
                ", nombreAdjunto_4='" + (nombreAdjunto_4!=null) + '\'' +
                ", nombreAdjunto_5='" + (nombreAdjunto_5!=null) + '\'' +
                ", fileAdicionales=" + fileAdicionales.size() +
                '}';
    }

    public String getNombreAdjunto_1() {
        return nombreAdjunto_1;
    }

    public void setNombreAdjunto_1(String nombreAdjunto_1) {
        this.nombreAdjunto_1 = nombreAdjunto_1;
    }

    public String getNombreAdjunto_2() {
        return nombreAdjunto_2;
    }

    public void setNombreAdjunto_2(String nombreAdjunto_2) {
        this.nombreAdjunto_2 = nombreAdjunto_2;
    }

    public String getNombreAdjunto_3() {
        return nombreAdjunto_3;
    }

    public void setNombreAdjunto_3(String nombreAdjunto_3) {
        this.nombreAdjunto_3 = nombreAdjunto_3;
    }

    public String getNombreAdjunto_4() {
        return nombreAdjunto_4;
    }

    public void setNombreAdjunto_4(String nombreAdjunto_4) {
        this.nombreAdjunto_4 = nombreAdjunto_4;
    }

    public String getNombreAdjunto_5() {
        return nombreAdjunto_5;
    }

    public void setNombreAdjunto_5(String nombreAdjunto_5) {
        this.nombreAdjunto_5 = nombreAdjunto_5;
    }
}
