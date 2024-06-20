

package com.axteroid.ose.server.tools.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoEnvelope {
	@XmlElement(name = "Body")
	private SoBody soBody;

	public SoBody getSoBody() {
		return soBody;
	}

	public void setSoBody(SoBody soBody) {
		this.soBody = soBody;
	}


	
}
