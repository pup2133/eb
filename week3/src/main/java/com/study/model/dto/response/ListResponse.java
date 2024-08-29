package com.study.model.dto.response;

import java.util.List;

import com.study.model.vo.Category;
import com.study.model.vo.Page;
import com.study.model.vo.Post;
import com.study.model.vo.Search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 조회시 response 해줄 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
	private List<Post> posts;
	private Integer totalPostCount;
	private List<Category> categories;
	private Page page;
	private Search search;
}
