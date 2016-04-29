package com.webapplication.mapper;

import com.webapplication.dto.category.CategoryResponseDto;
import com.webapplication.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public List<CategoryResponseDto> categoryListToCategoryResponseDto(List<Category> categories) {
        if (categories == null)
            return null;

        return categories.stream().map(category -> new CategoryResponseDto(category.getCategoryId(), category.getDescription()))
                .collect(Collectors.toList());
    }

}
