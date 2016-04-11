package com.webapplication.service.category;

import java.util.List;

import com.webapplication.dto.category.CategoryResponseDto;

public interface CategoryServiceApi {

    List<CategoryResponseDto> getCategories() throws Exception;

}
