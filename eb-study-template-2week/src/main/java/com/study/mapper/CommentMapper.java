package com.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.dto.Comment;

@Mapper
public interface CommentMapper {
	Comment getComment(Long commentId);
	List<Comment> getCommentByPostId(Long postId);
	void saveComment(Comment comment);
}
