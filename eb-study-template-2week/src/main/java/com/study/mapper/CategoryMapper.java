package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.dto.Category;

@Mapper
public interface CategoryMapper {
	List<Category> getCategory();
}
