package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글을 조회할 때 데이터를 담을 VO
 */
@Getter
@Builder
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

	public Post copyWith(String newPostTitle) {
		return Post.builder()
			.postId(getPostId())
			.categoryName(getCategoryName())
			.postTitle(newPostTitle)
			.postWriter(getPostWriter())
			.postCreatedDate(getPostCreatedDate())
			.postRevisionDate(getPostRevisionDate())
			.postViews(getPostViews())
			.fileCount(getFileCount())
			.build();
	}
}
