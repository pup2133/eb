package com.study.dto.request;

import com.study.dto.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

	private Long postId;

	@NonNull
	private Long categoryId;

	@NonNull @NotBlank @NotEmpty
	@Pattern(regexp = "^.{3,4}$")
	private String postWriter;

	@NonNull @NotBlank @NotEmpty
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$")
	private String postPassword;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,15}$")
	private String confirmPassword;

	@NonNull @NotBlank @NotEmpty
	@Pattern(regexp = "^.{4,99}$")
	private String postTitle;

	@NonNull @NotBlank @NotEmpty
	@Pattern(regexp = "^.{4,1999}$")
	private String postContent;

	public Post toEntity(){
		return Post.builder()
			.postId(getPostId())
			.postWriter(getPostWriter())
			.postPassword(getPostPassword())
			.postTitle(getPostTitle())
			.postContent(getPostContent())
			.categoryId(getCategoryId())
			.build();
	}

}
