package com.axteroid.ose.server.tools.xml;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.axteroid.ose.server.tools.bean.ArResponse;

public class UblGre {
	public static ArResponse readStatusFromAR(byte[] gre) {		
		byte[] zip = Base64.decodeBase64(gre);
		byte[] xml = UblResponseSunat.extractXMLFromCDR(zip);		
		String xmlString = new String(xml,Charset.forName("UTF-8"));
		//System.out.println("xmlString: \n"+xmlString);
		//Map<String,String> fields = new HashMap<String, String>();
		ArResponse arResponse = new ArResponse();
		String respCode = UblResponseSunat.readValueFromXML(xmlString, "ResponseCode");
		if(respCode!=null) 
			arResponse.setResponseCode(respCode);
			//fields.put("responseCode", respCode);
		String desc = UblResponseSunat.readValueFromXML(xmlString, "Description");
		if(desc!=null) 
			arResponse.setDescription(desc);
			//fields.put("description", desc);	
		List<String> note = UblResponseSunat.readResponseCodeARNote(xml);
		arResponse.setNote(note);
		return arResponse;
	}	
	
	
}
