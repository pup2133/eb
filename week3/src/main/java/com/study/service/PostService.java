package com.study.service;

import org.springframework.web.multipart.MultipartFile;

import com.study.model.dto.response.ListResponse;
import com.study.model.vo.Search;
import com.study.model.vo.View;
import com.study.model.vo.Write;

public interface PostService {
	View findPostById(Long postId);
	void updateViewsByPostId(Long postId);
	ListResponse findPostList(Search search);
	void deletePostByIdAndPassword(Long postId, String postPassword);
	void savePost(Write write, MultipartFile[] files);
	String findPasswordByPostId(Long postId);
}
