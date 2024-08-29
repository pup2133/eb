package com.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.model.vo.Category;
import com.study.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	/**
	 * 1. 전체 카테고리 조회
	 * @return List<Category>: 전체 카테고리 목록
	 */
	@Override
	public List<Category> getCategories() {
		return categoryMapper.getCategories();
	}
}
