package com.webapplication.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.webapplication.dto.category.CategoryResponseDto;
import com.webapplication.entity.Category;

@Component
public class CategoryMapper {

    public List<CategoryResponseDto> categoryListToCategoryResponseDto(List<Category> categories) {
        if (categories == null)
            return null;

        return categories.stream().map(category -> {
            return new CategoryResponseDto(category.getCategoryId(), category.getDescription());
        }).collect(Collectors.toList());
    }

}
