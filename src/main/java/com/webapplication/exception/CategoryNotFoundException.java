package com.webapplication.exception;

import com.webapplication.error.category.CategoryError;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(CategoryError error) {
        super(error.getDescription());
    }
}

