package com.study.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 데이터를 담을 VO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {
	private Long fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private Long postId;
}
