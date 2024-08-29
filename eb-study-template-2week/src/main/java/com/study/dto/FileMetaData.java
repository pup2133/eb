package com.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaData {
	private Long fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private Long fileSize;
	private Long postId;
}
