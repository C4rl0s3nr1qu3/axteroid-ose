package com.axteroid.ose.server.avatar.bean;

public class GroupBean {
	
	private String estado;
	private String error;
	private int intCount;
	private boolean replay = true;
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getIntCount() {
		return intCount;
	}
	public void setIntCount(int intCount) {
		this.intCount = intCount;
	}
	public boolean isReplay() {
		return replay;
	}
	public void setReplay(boolean replay) {
		this.replay = replay;
	}
}
