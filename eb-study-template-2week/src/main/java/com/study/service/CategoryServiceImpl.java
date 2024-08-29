package com.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.dto.Category;
import com.study.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	/* 카테고리 전체 조회 */
	@Override
	public List<Category> getCategory() {
		return categoryMapper.getCategory();
	}
}
