package com.study.dto;

import com.study.dto.response.CommentResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	private Long CommentId;
	private String commentWriter;
	private String commentContent;
	private String commentCreatedDate;
	private Long postId;

	public CommentResponseDto toResponseDto() {
		return CommentResponseDto.builder()
			.commentWriter(getCommentWriter())
			.commentContent(getCommentContent())
			.commentCreatedDate(getCommentCreatedDate())
			.build();
	}
}