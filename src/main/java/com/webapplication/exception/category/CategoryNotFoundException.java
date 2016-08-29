package com.webapplication.exception.category;

import com.webapplication.error.category.CategoryError;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(CategoryError error) {
        super(error.getDescription());
    }
}

