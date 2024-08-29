package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.model.dto.response.CommentResponse;
import com.study.model.vo.Comment;

@Mapper
public interface CommentMapper {
	CommentResponse findComment(Long commentId);
	List<Comment> findCommentByPostId(Long postId);
	void saveComment(Comment comment);
}
