package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 데이터를 담을 VO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	private Long CommentId;
	private String commentWriter;
	private String commentContent;
	private String commentCreatedDate;
	private Long postId;
}