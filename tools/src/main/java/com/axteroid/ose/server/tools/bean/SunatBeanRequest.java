package com.axteroid.ose.server.tools.bean;

public class SunatBeanRequest {
	protected byte[] content;
    protected String filename;
    
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

    
}
