package com.axteroid.ose.server.tools.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZipFile {

	@NotNull
	@Size(min = 5,max = 256)
	private String fileName;
	
	@NotNull
	@Size(min = 5,max = 5000000)	
	private byte[] fileContent;
	
	private String userName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
		
	
	
}
