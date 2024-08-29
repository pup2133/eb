package com.study.model.dto.request;

import com.study.model.vo.Write;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자가 게시글에 등록할 정보를 담는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteRequest {
	@NotNull(message="카테고리를 입력해 주세요")
	private Long categoryId;

	@NotNull(message = "작성자를 입력해 주세요")
	@NotBlank(message = "작성자를 입력해 주세요")
	@NotEmpty(message = "작성자를 입력해 주세요")
	@Pattern(regexp = "^.{3,4}$", message = "작성자는 3글자 이상 5글자 미만입니다")
	private String postWriter;

	@NotNull(message = "비밀번호를 입력해 주세요")
	@NotBlank (message = "비밀번호를 입력해 주세요")
	@NotEmpty(message = "비밀번호를 입력해 주세요")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$",
			 message = "비밀번호는 영어,숫자,특수문자를 1개 이상 포함한 4자 이상 16자 미만입니다")
	private String postPassword;

	@NotNull(message = "확인용 비밀번호를 입력해 주세요")
	@NotBlank (message = "확인용 비밀번호를 입력해 주세요")
	@NotEmpty(message = "확인용 비밀번호를 입력해 주세요")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$",
			 message = "확인용 비밀번호는 영어,숫자,특수문자를 1개 이상 포함한 4자 이상 16자 미만입니다")
	private String confirmPassword;

	@NotNull(message = "제목을 입력해 주세요")
	@NotBlank(message = "제목을 입력해 주세요")
	@NotEmpty(message = "제목을 입력해 주세요")
	@Pattern(regexp = "^.{4,99}$", message = "제목은 4자 이상 100자 미만 입니다")
	private String postTitle;

	@NotNull(message = "내용을 입력해 주세요")
	@NotBlank(message = "내용을 입력해 주세요")
	@NotEmpty(message = "내용을 입력해 주세요")
	@Pattern(regexp = "^.{4,1999}$", message = "내용은 4자 이상 2000자 미만 입니다")
	private String postContent;

	public Write toVo(){
		return Write.builder()
			.categoryId(getCategoryId())
			.postWriter(getPostWriter())
			.postPassword(getPostPassword())
			.confirmPassword(getConfirmPassword())
			.postTitle(getPostTitle())
			.postContent(getPostContent())
			.build();
	}

}
