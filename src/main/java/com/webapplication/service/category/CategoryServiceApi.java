package com.webapplication.service.category;

import com.webapplication.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryServiceApi {

    List<CategoryResponseDto> getCategories() throws Exception;
    
    CategoryResponseDto getCategory(String categoryId) throws Exception;
}
