package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.dto.Post;
import com.study.dto.Search;

@Mapper
public interface PostMapper {
	void savePost(Post post);
	Post getPostById(Long postId);
	List<Post> getPostList(Search search, Integer offset);
	Integer getPostAllCount(Search search);
	void modifyViews(Long postId);
	void removePost(Long postId, String password);
}
