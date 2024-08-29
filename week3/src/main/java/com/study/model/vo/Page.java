package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 페이징 데이터를 담을 VO
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Page {
	private Integer totalPages;
	private Integer currentPage;
	private Integer startPage;
	private Integer endPage;
	private Integer prevRange;
	private Integer nextRange;
}
