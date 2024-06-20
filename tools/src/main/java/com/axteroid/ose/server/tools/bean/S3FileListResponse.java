package com.axteroid.ose.server.tools.bean;

import java.util.List;

public class S3FileListResponse {
	private List<S3File> s3Filelist;

	public List<S3File> getS3Filelist() {
		return s3Filelist;
	}

	public void setS3Filelist(List<S3File> s3Filelist) {
		this.s3Filelist = s3Filelist;
	}
	
}
