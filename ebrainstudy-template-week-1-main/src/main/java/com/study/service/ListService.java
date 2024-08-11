package com.study.service;

import com.study.dto.Posts;
import com.study.dto.Search;

public interface ListService {
	Posts getList(String page);
	Posts getSearchPost(Search search);
}
