package com.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.model.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 1. 파일 예외 처리
	 * 2. 파일 저장, 삭제, 다운로드, 검증 시 발생되는 예외
	 * @param e: 예외
	 * @return ApiResponse: 에러 message 리턴
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FileException.class)
	public ApiResponse fileException(FileException e){
		return ApiResponse.builder().message(e.getMessage()).build();
	}

	/**
	 * 1. 패스워드 예외 처리
	 * 2. 게시글 삭제, 등록, 수정 시 발생되는 예외
	 * @param e: 예외
	 * @return ApiResponse: 에러 message 리턴
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PasswordException.class)
	public ApiResponse passwordException(PasswordException e){
		return ApiResponse.builder().message(e.getMessage()).build();
	}

	/**
	 * 1. @Valid 예외 처리
	 * 2. 수정, 등록 시 발생되는 예외
	 * @param e: 예외
	 * @return ApiResponse: 에러 메세지를 파싱하여 실패한 검증의 메세지 리턴
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse methodArgumentNotValidException(MethodArgumentNotValidException e){
		String[] split = e.getMessage().split("; ");

		String prefix = "default message [";
		String suffix = "]";

		int startIndex = split[split.length-1].indexOf(prefix) + prefix.length();
		int endIndex = split[split.length-1].indexOf(suffix, startIndex);
		String message = split[split.length-1].substring(startIndex, endIndex);

		return ApiResponse.builder().message(message).build();
	}

	/**
	 * 1. 댓글 예외처리
	 * 2. 댓글 등록 후 조회 실패시 예외 처리
	 * @param e: 예외
	 * @return ApiResponse: 에러 메세지 리턴
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CommentException.class)
	public ApiResponse commentException(CommentException e){
		return ApiResponse.builder().message(e.getMessage()).build();
	}

	/**
	 * 1. 게시글 예외처리
	 * 2. 검색 조건에 맞는 게시글이 없으면 예외 처리
	 * @param e: 에러
	 * @return ApiResponse: 에러 메세지 리턴
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(PostException.class)
	public ApiResponse ListException(PostException e){
		return ApiResponse.builder().message(e.getMessage()).build();
	}
}
