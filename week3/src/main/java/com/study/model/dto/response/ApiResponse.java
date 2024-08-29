package com.study.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * API 요청에 대한 response 위한 DTO
 * object: response 내용
 * message: response 메세지
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	private Object object;
	private String message;
}
