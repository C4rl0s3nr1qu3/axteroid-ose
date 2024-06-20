package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusCdr", propOrder = {
    "statusCdr"
})

public class GetStatusCdr {
	
	protected StatusCdr statusCdr;

	public StatusCdr getStatusCdr() {
		return statusCdr;
	}

	public void setStatusCdr(StatusCdr statusCdr) {
		this.statusCdr = statusCdr;
	}
	
}
