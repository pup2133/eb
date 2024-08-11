package com.study.dao;

import java.sql.SQLException;

import com.study.dto.Comment;

public interface CommentDAO {
	void addComment(String postId, Comment comment) throws SQLException;
}
