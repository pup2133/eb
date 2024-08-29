package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 데이터를 담을 VO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	private Long categoryId;
	private String categoryName;
}
