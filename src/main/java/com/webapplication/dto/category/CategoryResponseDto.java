package com.webapplication.dto.category;

public class CategoryResponseDto {
    private Integer categoryId;
    private String description;

    public CategoryResponseDto(Integer categoryId, String description) {
        this.categoryId = categoryId;
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
