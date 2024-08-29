package com.study.dto.request;

import com.study.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
	private Long CommentId;
	private String commentWriter;
	private String commentContent;
	private String commentCreatedDate;
	private Long postId;

	public Comment toEntity(){
		return Comment.builder()
			.postId(getPostId())
			.commentWriter(getCommentWriter())
			.commentContent(getCommentContent())
			.build();
	}
}
