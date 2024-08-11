package com.study.dto;

import java.util.List;

public class Post {

    private long postId;
    private String writer;
    private String password;
    private String title;
    private String content;
    private int views;
    private int file;
    private List<Files> files;
    private String createdDate;
    private String revisionDate;
    private String categoryId;
    private String category;
    private List<Comment> comments;

    public long getPostId() {
        return postId;
    }

    public String getWriter() {
        return writer;
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public int getFile() {
        return file;
    }

    public List<Files> getFiles() {
        return files;
    }

    public int getViews() {
        return views;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Post(long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Post{" +
            "postId=" + postId +
            ", writer='" + writer + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", views=" + views +
            ", files=" + files +
            ", createdDate=" + createdDate +
            ", revisionDate=" + revisionDate +
            ", category='" + category + '\'' +
            ", comments=" + comments +
            '}';
    }

    // Modify
    public Post(long postId, String writer, String title, String content, int views, String createdDate,
        String revisionDate,String category, String categoryId, List<Files> files) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdDate = createdDate;
        this.revisionDate = revisionDate;
        this.category = category;
        this.categoryId = categoryId;
        this.files = files;
    }

    // modify
    public Post(long postId, String categoryId, String writer, String password, String title, String content) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    // 유효성 검증, post 저장
    public Post(String categoryId, String writer, String password, String title, String content) {
        this.categoryId = categoryId;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
    }

    // view에서 쓸 생성자
    public Post(long postId,
                String writer,
                String title,
                String content,
                int views,
                String createdDate,
                String revisionDate,
                String category,
                List<Comment> comments,
                List<Files> files) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdDate = createdDate;
        this.revisionDate = revisionDate;
        this.category = category;
        this.comments = comments;
        this.files = files;
    }

    // list에서 쓸 생성자
    public Post(long postId,
                String categoryId,
                String title,
                String writer,
                int views,
                String createdDate,
                String revisionDate,
                int file) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.title = title;
        this.writer = writer;
        this.views = views;
        this.createdDate = createdDate;
        this.revisionDate = revisionDate;
        this.file = file;
    }
}
