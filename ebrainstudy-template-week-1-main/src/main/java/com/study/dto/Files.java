package com.study.dto;

public class Files {

	private long fileId;
	private String fileName;
	private String storeName;
	private String path;
	private long postId;

	public Files(long fileId, String fileName, String storeName, String path) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.storeName = storeName;
		this.path = path;
	}

	//파일 저장때
	public Files(String fileName, String storeName, String path, long postId) {
		this.fileName = fileName;
		this.storeName = storeName;
		this.path = path;
		this.postId = postId;
	}

	public long getFileId() {
		return fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getStoreName() {
		return storeName;
	}

	public String getPath() {
		return path;
	}

	public long getPostId() {
		return postId;
	}

}
