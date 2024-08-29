package com.study.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.study.dto.Post;

@Mapper
public interface ModifyMapper {
	String getPassword(Long id);
	Post modifyPostView(Long postId);
	void removeFile(String fileId);
	void modifyPost(Post post);
}
