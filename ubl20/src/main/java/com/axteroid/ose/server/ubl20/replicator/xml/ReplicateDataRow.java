package com.axteroid.ose.server.ubl20.replicator.xml;

/**
 * User: jmacavilca
 * Date: 20/03/12
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Row", propOrder = {
        "fields"
})
public class ReplicateDataRow {

    @XmlElement(name = "fields")
    protected List<ReplicateDataField> fields = new ArrayList<ReplicateDataField>();

    public ReplicateDataRow() {
    }

    public ReplicateDataRow(List<ReplicateDataField> fields) {
        this.fields = fields;
    }


    public List<ReplicateDataField> getFields() {
        return fields;
    }

    public void setFields(List<ReplicateDataField> fields) {
        this.fields = fields;
    }
}
