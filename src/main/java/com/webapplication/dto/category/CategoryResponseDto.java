package com.webapplication.dto.category;

import java.util.List;

public class CategoryResponseDto {

    private String categoryId;
    private String description;
    private String parentId;
    private List<CategoryResponseDto> subCategories;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<CategoryResponseDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryResponseDto> subCategories) {
        this.subCategories = subCategories;
    }

}
