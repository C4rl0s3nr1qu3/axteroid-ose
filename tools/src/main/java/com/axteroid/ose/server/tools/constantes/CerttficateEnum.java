package com.axteroid.ose.server.tools.constantes;

public enum CerttficateEnum {
	MITSUI("Mitsui2021$","mitsui automotriz s.a. (mitsui_automotriz)","alignetsfe", "0000_mitsuiPFX20200110.jks"),
	ETEXGROUP("hSSuuAdnadjE6JLC","(pe1_pfvp_1095_sw_kpsc)_loor_000859942","hSSuuAdnadjE6JLC", "etexgroupcert.jks"),	
	BARMOX("BL20478005017","te-c9854db6-3f72-47aa-bf86-d62db477cc81","BL20478005017", "barmox.jks"),
	TECNIMOTORS("M15r3c34$J41q3n$","JOAQUIN BONILLA CHU (TECNIMOTORS)","alignetsfe", "0000_tecnimotorsPFX20210503.jks"),
	R20172606777("1234","20172606777","1234", "keyStoreSunat.jks");
		
	private String keyPassword;
	private String privateKeyAlias;
	private String keyStorePassword;
	private String certicate;
	
	private CerttficateEnum(String keyPassword, String privateKeyAlias, String keyStorePassword, String certicate){		
		this.keyPassword = keyPassword;
		this.privateKeyAlias = privateKeyAlias;
		this.keyStorePassword = keyStorePassword;
		this.certicate = certicate;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	public String getPrivateKeyAlias() {
		return privateKeyAlias;
	}

	public void setPrivateKeyAlias(String privateKeyAlias) {
		this.privateKeyAlias = privateKeyAlias;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getCerticate() {
		return certicate;
	}

	public void setCerticate(String certicate) {
		this.certicate = certicate;
	}


}
