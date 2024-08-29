package com.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.dto.Comment;
import com.study.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private final CommentMapper commentMapper;

	/*
	* addComment()가 성공적으로 추가되면 1, 아니면 0
	* getComment()로 Comment를 가져오고
	* com==null&&insertResult==0 이면 SQLException
	*/
	//modelmapper
	@Override
	public Comment saveComment(Comment comment) {
		commentMapper.saveComment(comment);
		return commentMapper.getComment(comment.getCommentId());
	}

	/* postId를 값을 가진 포스트의 댓글 조회*/
	@Override
	public List<Comment> getCommentByPostId(Long postId) {
		return commentMapper.getCommentByPostId(postId);
	}


}
