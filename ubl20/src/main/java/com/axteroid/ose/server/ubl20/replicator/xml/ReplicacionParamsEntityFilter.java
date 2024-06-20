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
@XmlRootElement(name = "EntityFilter")
public class ReplicacionParamsEntityFilter {
    @XmlElement(required = true)
    String entityId;

    @XmlElement(name = "filterField")
    List<ReplicateDataField> filter = new ArrayList<ReplicateDataField>();

    public ReplicacionParamsEntityFilter() {
    }

    public ReplicacionParamsEntityFilter(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public ReplicacionParamsEntityFilter clone() throws CloneNotSupportedException {
        ReplicacionParamsEntityFilter clone = new ReplicacionParamsEntityFilter();
        clone.entityId = entityId;
        clone.filter = new ArrayList<ReplicateDataField>(filter);
        return clone;
    }

    public ReplicacionParamsEntityFilter addField(String name, Object value) {
        filter.add(new  ReplicateDataField(name, value));
        return this;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public List<ReplicateDataField> getFilter() {
        return filter;
    }

    public void setFilter(List<ReplicateDataField> filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "ReplicacionParamsEntityFilter{" +
                "entityId='" + entityId + '\'' +
                ", filter=" + filter +
                '}';
    }
}
