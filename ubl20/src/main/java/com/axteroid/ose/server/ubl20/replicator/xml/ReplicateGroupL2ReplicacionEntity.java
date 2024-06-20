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
public class ReplicateGroupL2ReplicacionEntity implements Command {

    @XmlElement(required = true)
    protected ReplicacionParamsEntityFilter filter;

    @XmlElement(name = "rows", required = false)
    List<ReplicateDataRow> rows = new ArrayList<ReplicateDataRow>();


    @Override
    public String toString() {
        return "GroupItem{" +
                "entityId=" + filter.getEntityId() +
                ", rows=" + rows.size() +
                '}';
    }

    public ReplicateGroupL2ReplicacionEntity() {
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
