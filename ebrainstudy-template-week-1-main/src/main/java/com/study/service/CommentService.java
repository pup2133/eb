package com.study.service;

import java.sql.SQLException;

public interface CommentService {
	void addComment(String writer, String comment, String postId) throws SQLException;
}
