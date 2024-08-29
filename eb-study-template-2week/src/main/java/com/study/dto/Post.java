package com.study.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	private Long postId;
	private String postWriter;
	private String postPassword;
	private String postTitle;
	private String postContent;
	private Integer postViews;
	private String postCreatedDate;
	private String postRevisionDate;
	private Long categoryId;

	private String categoryName;
	private Integer fileCount;
	private List<FileMetaData> files;
}
