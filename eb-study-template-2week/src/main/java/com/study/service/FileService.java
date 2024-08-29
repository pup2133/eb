package com.study.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.study.dto.FileMetaData;

public interface FileService {
	FileMetaData getFile(Long fileId);
	List<FileMetaData> getFiles(Long postId);
	void removeFileFromStore(String storeName);
	void saveFile(Long postId, MultipartFile[] file);
	void transferData(InputStream inputStream, OutputStream outputStream);
}
