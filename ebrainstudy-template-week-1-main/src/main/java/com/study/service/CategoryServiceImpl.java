package com.study.service;

import java.util.List;

import com.study.dao.CategoryDAO;
import com.study.dto.Category;

public class CategoryServiceImpl implements CategoryService{

	private final CategoryDAO categoryDAO;

	public CategoryServiceImpl(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	// 카테고리
	@Override
	public List<Category> getCategory() {
		List<Category> list;
		try {
			list = categoryDAO.getCategory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}
}
