package com.study.service;

import com.study.dto.Post;

public interface ViewService {
	void addViews(String postId);
	boolean deletePost(String postId, String password);
	Post getPostById(String id);
}
