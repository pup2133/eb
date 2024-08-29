package com.study.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
