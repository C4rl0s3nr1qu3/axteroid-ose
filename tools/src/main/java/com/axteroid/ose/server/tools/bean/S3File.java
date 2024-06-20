package com.axteroid.ose.server.tools.bean;

import javax.validation.constraints.NotNull;

public class S3File {

	@NotNull
	private String filePath;
	@NotNull
	private byte[] fileContent;
	@NotNull	
	private String fileMD5;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileMD5() {
		return fileMD5;
	}
	public void setFileMD5(String fileMD5) {
		this.fileMD5 = fileMD5;
	}
	
	
	
}
