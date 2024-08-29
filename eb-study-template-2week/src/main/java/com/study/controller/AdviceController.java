package com.study.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.study.dto.Search;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AdviceController {

	/*
	* page, start_date, end_date, category_id, search_word 가져와서
	* search 객체 만들어서 model에 추가
	*/
	@ModelAttribute
	public void addAttributes(HttpServletRequest request, Model model) {
		String pageParam = request.getParameter("page");
		Integer page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;

		Search search = Search.builder()
			.startDate(request.getParameter("start_date"))
			.endDate(request.getParameter("end_date"))
			.categoryId(request.getParameter("category_id"))
			.searchWord(request.getParameter("search_word"))
			.page(page)
			.build();

		model.addAttribute("search",search);
	}
}
