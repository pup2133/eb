package com.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.mapper.CommentMapper;
import com.study.model.dto.response.CommentResponse;
import com.study.model.vo.Comment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private final CommentMapper commentMapper;

	/**
	 * 1. 댓글 등록 후 댓글의 ID로 조회
	 * @param comment: 작성자, 작성 내용
	 * @return CommentResponse: 작성자, 작성 내용, 작성 시간
	 */
	@Override
	public CommentResponse saveComment(Comment comment) {
		commentMapper.saveComment(comment);
		return commentMapper.findComment(comment.getCommentId());
	}

	/**
	 * 1. 게시글 ID로 댓글 조회
	 * @param postId: 게시글의 ID
	 * @return List<Comment>: 게시글에 등록 된 댓글들 리턴
	 */
	@Override
	public List<Comment> findCommentByPostId(Long postId) {
		return commentMapper.findCommentByPostId(postId);
	}


}
