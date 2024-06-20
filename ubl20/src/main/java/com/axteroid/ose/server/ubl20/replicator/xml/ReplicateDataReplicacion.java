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
@XmlType(name = "Replicacion", propOrder = {
        "id",
        "entitys"
})
public class ReplicateDataReplicacion {
    @XmlElement(required = true)
    protected Long id;
    @XmlElement(name = "entitys", required = false)
    List<ReplicateDataEntity> entitys = new ArrayList<ReplicateDataEntity>();


    public ReplicateDataReplicacion() {
    }

    public ReplicateDataReplicacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReplicateDataEntity> getEntitys() {
        return entitys;
    }

    public void setEntities(List<ReplicateDataEntity> entitys) {
        this.entitys = entitys;
    }
}
