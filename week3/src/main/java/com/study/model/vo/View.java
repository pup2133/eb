package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글을 볼 때 데이터를 담을 VO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class View {
	private Long postId;
	private String postTitle;
	private String postWriter;
	private String postContent;
	private String postCreatedDate;
	private String postRevisionDate;
	private Integer postViews;
	private String categoryName;
}
