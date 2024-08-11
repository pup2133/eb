package com.study.service;

import com.study.dto.Post;

import jakarta.servlet.http.HttpServletRequest;

public interface WriterService {
	long savePost(Post post, HttpServletRequest request);
}
