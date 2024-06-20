package com.axteroid.ose.server.ubl20.replicator.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Params")
public class ReplicacionParams {
    @XmlElement(name = "entity", required = true)
    List<ReplicacionParamsEntityFilter> entityFilters = new ArrayList<ReplicacionParamsEntityFilter>();

    @XmlElement
    String fileXml;    // la ruta esta en el campo del registroa replicar

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

    @XmlElement(required = false)
    List<String> fileAdicionales = new ArrayList<String>();  // no esta relacionado al registro a replicar

    public ReplicacionParams clone() {
        try {
            ReplicacionParams clone = (ReplicacionParams) super.clone();
            clone.setEntityFilters(new ArrayList<ReplicacionParamsEntityFilter>(entityFilters));
             clone.setFileAdicionales(new ArrayList<String>(fileAdicionales));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public ReplicacionParams addEntityFilter(ReplicacionParamsEntityFilter entityFilter) {
        entityFilters.add(entityFilter);
        return this;
    }


    public List<ReplicacionParamsEntityFilter> getEntityFilters() {
        return entityFilters;
    }

    public void setEntityFilters(List<ReplicacionParamsEntityFilter> entityFilters) {
        this.entityFilters = entityFilters;
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

    public boolean existenAdjuntos() {
        return (nombreAdjunto_1 != null || nombreAdjunto_2 != null || nombreAdjunto_3 != null || nombreAdjunto_4 != null || nombreAdjunto_5 != null);
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
