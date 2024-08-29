package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.model.vo.Post;
import com.study.model.vo.Search;
import com.study.model.vo.View;
import com.study.model.vo.Write;

@Mapper
public interface PostMapper {
	String findPasswordByPostId(Long postId);
	void savePost(Write write);
	View findPostById(Long postId);
	List<Post> findPostList(Search search, Integer offset);
	Integer findTotalPostCount(Search search);
	void updateViewsByPostId(Long postId);
	void deletePostByIdAndPassword(Long postId, String postPassword);
}
