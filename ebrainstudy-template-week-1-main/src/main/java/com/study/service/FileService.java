package com.study.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface FileService {
	boolean saveFile(long postId, HttpServletRequest request);
	void fileDownload(HttpServletResponse response, String fileId);
}
