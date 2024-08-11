package com.study.util;

import com.study.dto.Post;

public class Validation {

	private static Validation INSTANCE = new Validation();

	public static Validation getINSTANCE() {
		return INSTANCE;
	}

	// 카테고리 유효성
	private boolean validateCategoryId(Post post) {
		return post.getCategoryId() != null && !post.getCategoryId().isEmpty();
	}

	// 작성자 유효성
	private boolean validateWriter(Post post) {
		return post.getWriter() != null
			&& !post.getWriter().isEmpty()
			&& post.getWriter().matches("^.{3,4}$");
	}

	// 패스워드 유효성
	private boolean validatePassword(Post post, String password) {
		return post.getPassword() != null
			&& !post.getPassword().isEmpty()
			&& password.equals(post.getPassword())
			&& post.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$");
	}

	// 패스워드 유효성
	private boolean validatePassword(Post post) {
		return post.getPassword() != null
			&& !post.getPassword().isEmpty()
			&& post.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$");
	}

	// 제목 유효성
	private boolean validateTitle(Post post) {
		return post.getTitle() != null
			&& !post.getTitle().isEmpty()
			&& post.getTitle().matches("^.{4,99}$");
	}

	// 내용 유효성
	private boolean validateContent(Post post) {
		return post.getContent() != null
			&& !post.getContent().isEmpty()
			&& post.getContent().matches("^.{4,1999}$");
	}

	// write 쓸 거
	public boolean validate(Post post, String password) {
		return validateCategoryId(post) &&
			validateWriter(post) &&
			validatePassword(post, password) &&
			validateTitle(post) &&
			validateContent(post);
	}

	// modify 쓸 거
	public boolean validate(Post post) {
		return validateCategoryId(post) &&
			validateWriter(post) &&
			validatePassword(post) &&
			validateTitle(post) &&
			validateContent(post);
	}

}
