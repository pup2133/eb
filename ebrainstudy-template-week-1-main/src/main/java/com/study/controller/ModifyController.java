package com.study.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.study.container.DI;
import com.study.dto.Category;
import com.study.dto.Post;
import com.study.service.CategoryServiceImpl;
import com.study.service.ModifyServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ModifyController implements Controller {

	@Override
	public View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String postId = request.getParameter("post");

		ModifyServiceImpl ms = DI.getModifyService();
		CategoryServiceImpl cs =DI.getCategoryService();

		List<Category> category = cs.getCategory();
		Post post = ms.modifyPostView(new Post(Long.parseLong(postId)));

		int fileCount = 0;
		if(post.getFiles()!=null) fileCount = post.getFiles().size();

		request.setAttribute("post",post);
		request.setAttribute("category",category);
		request.setAttribute("fileCount",fileCount);

		return new View("/WEB-INF/views/modify.jsp");

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ModifyServiceImpl ms = DI.getModifyService();
			ms.modifyPost(request);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (SQLException e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(e.getMessage());
		} catch (IllegalStateException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write(e.getMessage());		}
	}
}
