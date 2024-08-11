package com.study.controller;

import java.io.IOException;
import java.util.List;

import com.study.container.DI;
import com.study.dto.Category;
import com.study.dto.Post;
import com.study.service.CategoryServiceImpl;
import com.study.util.Validation;
import com.study.service.WriterServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig
public class WriteController implements Controller {

	@Override
	public View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryServiceImpl cs = DI.getCategoryService();
		List<Category> category = cs.getCategory();

		request.setAttribute("category", category);
		return new View("/WEB-INF/views/write.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WriterServiceImpl ws = DI.getWriterService();
		Validation v = Validation.getINSTANCE();

		Post post = new Post(
			request.getParameter("category"),
			request.getParameter("writer"),
			request.getParameter("password"),
			request.getParameter("title"),
			request.getParameter("content")
		);

		boolean result = v.validate(post, request.getParameter("confirmPassword"));

		if (result) {
			ws.savePost(post, request);
		}

	}
}
