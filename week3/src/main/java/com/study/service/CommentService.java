package com.study.service;

import java.util.List;

import com.study.model.dto.response.CommentResponse;
import com.study.model.vo.Comment;

public interface CommentService {
	List<Comment> findCommentByPostId(Long postId);
	CommentResponse saveComment(Comment Comment);
}
