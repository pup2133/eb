package com.study.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.study.model.vo.FileMetaData;

public interface FileService {
	FileMetaData findFileById(Long fileId);
	List<FileMetaData> findFilesByPostId(Long postId);
	void deleteFileFromStore(String storeName);
	void saveFilesByPostId(Long postId, MultipartFile[] files);
	void transferFileData(InputStream inputStream, OutputStream outputStream);
}
