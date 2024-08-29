package com.study.model.dto.response;

import java.util.List;

import com.study.model.vo.Category;
import com.study.model.vo.FileMetaData;
import com.study.model.vo.Modify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 수정시 response 해줄 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyResponse {
	private Modify post;
	private List<Category> categories;
	private List<FileMetaData> files;
}
