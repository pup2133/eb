package com.study.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.study.model.vo.Modify;

@Mapper
public interface ModifyMapper {
	Modify findPostById (Long postId);
	void deleteFileById(String fileId);
	void modifyPost(Modify modify);
}
