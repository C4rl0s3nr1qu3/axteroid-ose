package com.axteroid.ose.server.tools.bean;

public class ApiChileResponse {

	private String detail;
	private String irs_status;
	private String irs_status_detail;
	private String non_field_errors;
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getIrs_status() {
		return irs_status;
	}
	public void setIrs_status(String irs_status) {
		this.irs_status = irs_status;
	}
	public String getIrs_status_detail() {
		return irs_status_detail;
	}
	public void setIrs_status_detail(String irs_status_detail) {
		this.irs_status_detail = irs_status_detail;
	}
	public String getNon_field_errors() {
		return non_field_errors;
	}
	public void setNon_field_errors(String non_field_errors) {
		this.non_field_errors = non_field_errors;
	}
	
	@Override
	public String toString() {
		return "ApiChileResponse [detail=" + detail + ", irs_status=" + irs_status + ", irs_status_detail="
				+ irs_status_detail + ", non_field_errors=" + non_field_errors + "]";
	}


}
