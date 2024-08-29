package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.dto.FileMetaData;

@Mapper
public interface FileMapper {
	FileMetaData getFile(Long fileId);
	List<FileMetaData> getFiles(Long postId);
	void saveFile(Long postId, FileMetaData file);
}
