package com.study.service;

import org.springframework.web.multipart.MultipartFile;

import com.study.dto.Post;

public interface ModifyService {
	Post modifyPostView(Long postId);
	void modifyPost(Post Post, MultipartFile[] files, String[] removeFiles);
}
