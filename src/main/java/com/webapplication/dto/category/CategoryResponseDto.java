package com.webapplication.dto.category;

public class CategoryResponseDto {
    private String categoryId;
    private String description;

    public CategoryResponseDto() {
    }

    public CategoryResponseDto(String categoryId, String description) {
        this.categoryId = categoryId;
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
