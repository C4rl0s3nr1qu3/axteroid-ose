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
@XmlType(name = "Entity", propOrder = {
        "filter",
        "rows"
})
public class ReplicateDataEntity {
    @XmlElement(name = "entityFilter", required = true)
    ReplicacionParamsEntityFilter filter ;
    @XmlElement(name = "rows", required = false)
    List<ReplicateDataRow> rows = new ArrayList<ReplicateDataRow>();

    public ReplicateDataEntity() {
    }

    public ReplicateDataEntity(ReplicacionParamsEntityFilter filter) {
        this.filter = filter;
    }

    public ReplicacionParamsEntityFilter getFilter() {
        return filter;
    }

    public void setFilter(ReplicacionParamsEntityFilter filter) {
        this.filter = filter;
    }

    public List<ReplicateDataRow> getRows() {
        return rows;
    }

    public void setRows(List<ReplicateDataRow> rows) {
        this.rows = rows;
    }
}
