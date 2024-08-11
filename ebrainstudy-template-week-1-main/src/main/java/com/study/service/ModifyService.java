package com.study.service;

import java.sql.SQLException;

import com.study.dto.Post;

import jakarta.servlet.http.HttpServletRequest;

public interface ModifyService {
	void modifyPost(HttpServletRequest request) throws SQLException, IllegalStateException;
	Post modifyPostView(Post post);
}
