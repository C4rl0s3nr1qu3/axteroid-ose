package com.axteroid.ose.server.tools.constantes;

public enum FileTypeEnum {
	ZIP("XML_ZIP"),
	UBL("XML_UBL"),
	CDR("XML_CDR"),
	SUNAT("SUNAT_AR");		
	public String val;
	
	FileTypeEnum(String _val) {
		val = _val;
	}
	
	public String getProperyName() {
		return val;
	}		
}
