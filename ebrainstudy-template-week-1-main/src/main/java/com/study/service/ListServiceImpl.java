package com.study.service;

import java.util.ArrayList;
import java.util.List;

import com.study.dao.PostDAO;
import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;

public class ListServiceImpl implements ListService {

	private final PostDAO postDAO;

	public ListServiceImpl(PostDAO postDAO) {
		this.postDAO = postDAO;
	}

	// 80자 넘어가면 이후 ...로 표기
	private List<Post> parserTitle(List<Post> post) {
		List<Post> p = new ArrayList<>();
		for (Post post1 : post) {
			String title = post1.getTitle();
			if (title.length() >= 80) {
				title = title.substring(0, 40);
				title += "...";
				post1.setTitle(title);
			}
			p.add(post1);
		}
		return p;
	}

	// 페이징
	@Override
	public Posts getList(String page) {
		int offset = ((Integer.parseInt(page) - 1) * 10);
		Posts posts = postDAO.getList(offset);
		List<Post> list = parserTitle(posts.getList());
		return new Posts(list, posts.getCount());
	}

	// 검색
	@Override
	public Posts getSearchPost(Search search) {
		int offset = ((Integer.parseInt(search.getPage()) - 1) * 10);
		Posts posts = postDAO.getSearchPost(search, offset);
		List<Post> list = parserTitle(posts.getList());
		return new Posts(list, posts.getCount());
	}
}
