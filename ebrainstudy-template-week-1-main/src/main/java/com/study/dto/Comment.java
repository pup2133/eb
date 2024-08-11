package com.study.dto;

public class Comment {

	private long commentId;
	private String writer;
	private String content;
	private String createdDate;

	public Comment(String writer, String content) {
		this.writer = writer;
		this.content = content;
	}

	public Comment(long commentId, String writer, String content, String createdDate) {
		this.commentId = commentId;
		this.writer = writer;
		this.content = content;
		this.createdDate = createdDate;
	}

	public long getCommentId() {
		return commentId;
	}

	public String getWriter() {
		return writer;
	}

	public String getContent() {
		return content;
	}

	public String getCreatedDate() {
		return createdDate;
	}
}