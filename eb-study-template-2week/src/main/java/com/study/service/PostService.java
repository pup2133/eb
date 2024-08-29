package com.study.service;

import org.springframework.web.multipart.MultipartFile;

import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;

public interface PostService {
	Post getPostById(Long postId);
	void modifyViews(Long postId);
	Posts getPostList(Search search);
	void removePost(Long postId, String password);
	void savePost(Post post, MultipartFile[] files);
}
