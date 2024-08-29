package com.study.util;

import org.springframework.stereotype.Service;

import com.study.model.vo.Page;

@Service
public class Pagination {

	public Page pagination(Integer totalPost, Integer page){
		Integer totalPages = (totalPost + 10 - 1) / 10;
		Integer currentPage = page > 0 ? page : 1;
		Integer startPage = ((currentPage - 1) / 10) * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, totalPages);
		Integer prevRange = startPage - 10;
		Integer nextRange = endPage + 1;

		return Page.builder()
			.totalPages(totalPages)
			.currentPage(currentPage)
			.startPage(startPage)
			.endPage(endPage)
			.prevRange(prevRange)
			.nextRange(nextRange)
			.build();
	}
}
