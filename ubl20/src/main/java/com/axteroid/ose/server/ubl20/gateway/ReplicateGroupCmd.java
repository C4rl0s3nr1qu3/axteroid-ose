package com.axteroid.ose.server.ubl20.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.axteroid.ose.server.ubl20.replicator.xml.ReplicateGroupL1Replicacion;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "group")
public class ReplicateGroupCmd {

    @XmlElement(name = "list", required = false)
    List<ReplicateGroupL1Replicacion> list = new ArrayList<ReplicateGroupL1Replicacion>();

    public ReplicateGroupCmd() {
    }

    public List<ReplicateGroupL1Replicacion> getList() {
        return list;
    }

    public void setList(List<ReplicateGroupL1Replicacion> list) {
        this.list = list;
    }
}
