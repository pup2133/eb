package com.study.dto;

public class Category {

    private long categoryId;
    private String category;

    public long getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public Category(long categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }
}
