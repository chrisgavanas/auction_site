package com.webapplication.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "Category")
public class Category {

    @Id
    private String categoryId;
    private String parentId;
    private List<String> subCategoryIds;
    private String description;

    public Category() {
    }

    public Category(String categoryId, String parentId, List<String> subCategoryIds, String description) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.subCategoryIds = subCategoryIds;
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getSubCategoryIds() {
        return subCategoryIds;
    }

    public void setSubCategoryIds(List<String> subCategoryIds) {
        this.subCategoryIds = subCategoryIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
