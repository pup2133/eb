package com.study.service;

import com.study.container.DI;
import com.study.dao.PostDAO;
import com.study.dto.Post;

import jakarta.servlet.http.HttpServletRequest;

public class WriterServiceImpl implements WriterService{

	private final PostDAO postDAO;

	public WriterServiceImpl(PostDAO postDAO) {
		this.postDAO = postDAO;
	}

	// 게시글 저장
	@Override
	public long savePost(Post post, HttpServletRequest request){
		try {
			long postId = postDAO.savePost(post);
			FileServiceImpl fs = DI.getFileService();
			fs.saveFile(postId, request);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return 0;
	}
}
