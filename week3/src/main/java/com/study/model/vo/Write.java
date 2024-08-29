package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글을 등록할 때 데이터를 담을 VO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Write {
	private Long postId;
	private Long categoryId;
	private String postTitle;
	private String postWriter;
	private String postPassword;
	private String confirmPassword;
	private String postContent;
}
