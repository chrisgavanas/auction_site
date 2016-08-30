package com.webapplication.exception.category;


import com.webapplication.error.category.CategoryError;

public class CategoryHierarchyException extends Exception {

    public CategoryHierarchyException(CategoryError error) {
        super(error.getDescription());
    }

}
