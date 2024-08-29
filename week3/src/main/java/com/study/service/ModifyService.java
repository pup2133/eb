package com.study.service;

import org.springframework.web.multipart.MultipartFile;

import com.study.model.vo.Modify;

public interface ModifyService {
	Modify findPostById(Long postId);
	void modifyPost(Modify modify, MultipartFile[] files);
}
