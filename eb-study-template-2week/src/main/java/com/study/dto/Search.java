package com.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Search {
	private String startDate;
	private String endDate;
	private String categoryId;
	private String searchWord;
	private Integer page;
}
