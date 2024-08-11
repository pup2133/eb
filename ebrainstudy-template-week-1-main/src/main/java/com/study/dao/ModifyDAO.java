package com.study.dao;

import com.study.dto.Post;

public interface ModifyDAO {
	String getPassword(long id);
	int deleteFile(String fileId);
	int updatePost(Post post);
	Post modifyPostView(Post post);
}
