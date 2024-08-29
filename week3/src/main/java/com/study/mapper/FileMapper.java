package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.model.vo.FileMetaData;

@Mapper
public interface FileMapper {
	FileMetaData findFileById(Long fileId);
	List<FileMetaData> findFilesByPostId(Long postId);
	void saveFile(Long postId, FileMetaData file);
}
