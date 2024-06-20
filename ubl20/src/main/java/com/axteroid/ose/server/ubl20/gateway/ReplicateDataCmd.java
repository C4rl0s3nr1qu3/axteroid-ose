package com.axteroid.ose.server.ubl20.gateway;

import org.hibernate.validator.constraints.NotEmpty;

import com.axteroid.ose.server.ubl20.gateway.command.Command;
import com.axteroid.ose.server.ubl20.replicator.xml.ReplicateDataReplicacion;
import com.axteroid.ose.server.ubl20.replicator.xml.ReplicateInfo;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: jmacavilca
 * Date: 19/03/12
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReplicateDataCmd")
public class ReplicateDataCmd implements Command {
    @XmlElement(required = true)
    protected ReplicateInfo info;

    @XmlElement(name = "replicaciones", nillable = true)
    @NotEmpty(groups = ReplicateDataReplicacion.class)
    @Valid
    List<ReplicateDataReplicacion> replicaciones;

    public ReplicateDataCmd() {
    }

    public List<ReplicateDataReplicacion> getReplicaciones() {
        return replicaciones;
    }

    public void setReplicaciones(List<ReplicateDataReplicacion> replicaciones) {
        this.replicaciones = replicaciones;
    }

    public ReplicateInfo getInfo() {
        return info;
    }

    public void setInfo(ReplicateInfo info) {
        this.info = info;
    }
}
