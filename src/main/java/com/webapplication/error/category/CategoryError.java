package com.webapplication.error.category;


public enum CategoryError {
    CATEGORY_NOT_FOUND("Category not found.");

    private final String description;

    CategoryError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}