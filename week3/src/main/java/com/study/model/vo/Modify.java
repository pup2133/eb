package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 수정 때 데이터를 담을 VO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Modify {
	private Long postId;
	private Long categoryId;
	private String postTitle;
	private String postWriter;
	private String postPassword;
	private String postContent;
	private String postCreatedDate;
	private String postRevisionDate;
	private Integer postViews;
	private String[] removeFiles;
}
