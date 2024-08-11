package com.study.dao;

import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;

public interface PostDAO {
	Post getPostById(String postId);
	Posts getList(int offset);
	boolean deletePost(String postId, String password);
	void updateViews(String postId);
	long savePost(Post post);
	Posts getSearchPost(Search search, int offset);
}
