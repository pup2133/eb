package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글을 조회할 때 데이터를 담을 VO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	private Long postId;
	private String categoryName;
	private String postTitle;
	private String postWriter;
	private String postCreatedDate;
	private String postRevisionDate;
	private Integer postViews;
	private Integer fileCount;
}
