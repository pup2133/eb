package com.study.model.dto.request;

import com.study.model.vo.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 게시글에 등록한 댓글의 정보를 담는 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
	private String commentWriter;
	private String commentContent;
	private Long postId;

	public Comment toVo(){
		return Comment.builder()
			.commentWriter(getCommentWriter())
			.commentContent(getCommentContent())
			.postId(getPostId())
			.build();
	}
}
