package com.study.dto;

public class Search {

	private String startDate;
	private String endDate;
	private String categoryId;
	private String searchWord;
	private String page;

	public Search(String startDate, String endDate, String categoryId, String searchWord, String page) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
		this.searchWord = searchWord;
		this.page = page;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public String getPage() {
		return page;
	}
}
