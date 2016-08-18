package com.webapplication.mapper;

import com.webapplication.dao.CategoryRepository;
import com.webapplication.dto.category.CategoryResponseDto;
import com.webapplication.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponseDto categoryToCategoryResponseDto(Category category) {
        if (category == null)
            return null;

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setCategoryId(category.getCategoryId());
        categoryResponseDto.setDescription(category.getDescription());
        categoryResponseDto.setParentId(category.getParentId());
        List<Category> subCategories = categoryRepository.findCategoryByParentId(category.getCategoryId());
        List<CategoryResponseDto> subCategoriesDto = categoryListToCategoryResponseDto(subCategories);
        categoryResponseDto.setSubCategories(subCategoriesDto);

        return categoryResponseDto;
    }

    public List<CategoryResponseDto> categoryListToCategoryResponseDto(List<Category> categories) {
        if (categories == null)
            return null;

        return categories.stream().map(this::categoryToCategoryResponseDto)
                .collect(Collectors.toList());
    }

    public List<String> categoryListToCategoryIds(List<Category> categories) {
        return categories.stream().map(Category::getCategoryId).collect(Collectors.toList());
    }

}
