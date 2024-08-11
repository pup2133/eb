package com.study.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.study.container.DI;
import com.study.service.CommentServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommentController implements Controller{

	@Override
	public View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return null;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			CommentServiceImpl cs = DI.getCommentService();

			String writer = request.getParameter("writer");
			String comment = request.getParameter("comment");
			String postId = request.getParameter("postId");

			cs.addComment(writer,comment,postId);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
