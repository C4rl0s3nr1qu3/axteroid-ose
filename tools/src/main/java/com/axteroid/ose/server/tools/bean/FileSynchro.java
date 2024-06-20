package com.axteroid.ose.server.tools.bean;

public class FileSynchro {

	private S3File UBL;
	private S3File CDR;
	private S3File CDRSunat;
	
	public S3File getUBL() {
		return UBL;
	}
	public void setUBL(S3File uBL) {
		UBL = uBL;
	}
	public S3File getCDR() {
		return CDR;
	}
	public void setCDR(S3File cDR) {
		CDR = cDR;
	}
	public S3File getCDRSunat() {
		return CDRSunat;
	}
	public void setCDRSunat(S3File cDRSunat) {
		CDRSunat = cDRSunat;
	}	
	
}
