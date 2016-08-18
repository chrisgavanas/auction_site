package com.webapplication.exception;


import com.webapplication.error.category.CategoryError;

public class CategoryHierarchyException extends Exception {

    public CategoryHierarchyException(CategoryError error) {
        super(error.getDescription());
    }

}
