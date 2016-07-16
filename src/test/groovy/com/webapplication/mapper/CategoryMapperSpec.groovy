package com.webapplication.mapper

import com.webapplication.dto.category.CategoryResponseDto
import com.webapplication.entity.Category
import spock.lang.Specification


class CategoryMapperSpec extends Specification {

    CategoryMapper categoryMapper

    def setup() {
        categoryMapper = new CategoryMapper()
    }

    def "Convert a Category list to a CategoryListResponseDto list with null data"() {
        given:
        List<Category> categories = null

        when:
        List<CategoryResponseDto> categoryResponseDtoList = categoryMapper.categoryListToCategoryResponseDto(categories)

        then:
        categoryResponseDtoList == null
    }

    def "Convert a Category list to a CategoryListResponseDto list"() {
        given:
        List<Category> categories = [
                new Category(categoryId: 1, description: 'A description'),
                new Category(categoryId: 2, description: 'Another description')
        ]

        when:
        List<CategoryResponseDto> categoryResponseDtoList = categoryMapper.categoryListToCategoryResponseDto(categories)

        then:
        with(categoryResponseDtoList[0]) {
            categoryId == categories[0].categoryId
            description == categories[0].description
        }
        with(categoryResponseDtoList[1]) {
            categoryId == categories[1].categoryId
            description == categories[1].description
        }
    }
}
