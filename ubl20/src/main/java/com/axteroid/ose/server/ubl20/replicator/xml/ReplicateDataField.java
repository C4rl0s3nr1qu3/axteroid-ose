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
@XmlType(name = "Field", propOrder = {
        "name",
        "value"
})
public class ReplicateDataField {
    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = false)
    protected Object value;

    public ReplicateDataField() {
    }

    public ReplicateDataField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Field{" + name + ':' + value + '}';
    }
}
