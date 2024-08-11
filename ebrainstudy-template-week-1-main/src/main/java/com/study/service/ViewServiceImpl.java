package com.study.service;

import com.study.dao.PostDAO;
import com.study.dto.Post;

public class ViewServiceImpl implements ViewService{

	private final PostDAO postDAO;

	public ViewServiceImpl(PostDAO postDAO) {
		this.postDAO = postDAO;
	}

	// 조회수 추가
	@Override
	public void addViews(String postId) {
		postDAO.updateViews(postId);
	}

	// 조건에 맞는 글 삭제
	@Override
	public boolean deletePost(String postId, String password) {
		try {
			return postDAO.deletePost(postId, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// id값에 맞는 post 가져오기
	@Override
	public Post getPostById(String id) {
		try {
			return postDAO.getPostById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
