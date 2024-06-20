package com.axteroid.ose.server.tools.exception;

import javax.xml.bind.annotation.XmlElement;

public class SoBody {
	
	private SoFault soFault;

	public SoFault getSoFault() {
		return soFault;
	}
	
	@XmlElement(name = "Fault")
	public void setSoFault(SoFault soFault) {
		this.soFault = soFault;
	}
}
