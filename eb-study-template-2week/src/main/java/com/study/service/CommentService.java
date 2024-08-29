package com.study.service;

import java.util.List;

import com.study.dto.Comment;

public interface CommentService {
	List<Comment> getCommentByPostId(Long postId);
	Comment saveComment(Comment comment);
}
