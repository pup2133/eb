package com.study.model.dto.response;

import java.util.List;

import com.study.model.vo.Comment;
import com.study.model.vo.FileMetaData;
import com.study.model.vo.View;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글을 볼 때 response 해줄 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewResponse {
	private View view;
	private List<Comment> comment;
	private List<FileMetaData> files;
}
