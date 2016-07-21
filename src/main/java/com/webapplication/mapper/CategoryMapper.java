package com.webapplication.mapper;

import com.webapplication.dto.category.CategoryResponseDto;
import com.webapplication.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponseDto categoryToCategoryResponseDto(Category category) {
        if (category == null)
            return null;

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setCategoryId(category.getCategoryId());
        categoryResponseDto.setDescription(category.getDescription());

        return categoryResponseDto;
    }

    public List<CategoryResponseDto> categoryListToCategoryResponseDto(List<Category> categories) {
        if (categories == null)
            return null;

        return categories.stream().map(this::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }

}
