package com.study.service;

import java.sql.SQLException;

import com.study.dao.CommentDAO;
import com.study.dto.Comment;

public class CommentServiceImpl implements CommentService {

	private final CommentDAO commentDAO;

	public CommentServiceImpl(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

	@Override
	public void addComment(String writer, String comment, String postId) throws SQLException {
		try {
			commentDAO.addComment(postId,new Comment(writer, comment));
		} catch (SQLException e) {
			throw new SQLException("댓글 추가 실패",e);
		}
	}
}
