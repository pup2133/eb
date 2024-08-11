package com.study.dto;

import java.util.List;

public class Posts {

	private List<Post> list;
	private int count;

	public Posts(List<Post> list, int count) {
		this.list = list;
		this.count = count;
	}

	public List<Post> getList() {
		return list;
	}

	public int getCount() {
		return count;
	}

}
