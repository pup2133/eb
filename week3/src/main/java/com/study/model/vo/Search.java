package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 검색 조건 데이터를 담을 VO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {
	private String startDate;
	private String endDate;
	private String categoryId;
	private String searchWord;
	private Integer page;
}
