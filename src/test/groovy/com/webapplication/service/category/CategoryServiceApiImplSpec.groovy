package com.webapplication.service.category

import com.webapplication.dao.CategoryRepository
import com.webapplication.dto.category.CategoryResponseDto
import com.webapplication.mapper.CategoryMapper
import spock.lang.Specification


class CategoryServiceApiImplSpec extends Specification {

    CategoryRepository mockCategoryRepository
    CategoryMapper mockCategoryMapper
    CategoryServiceApi categoryService

    def setup() {
        mockCategoryRepository = Mock(CategoryRepository)
        mockCategoryMapper = Mock(CategoryMapper)

        categoryService = new CategoryServiceApiImpl(categoryRepository: mockCategoryRepository, categoryMapper: mockCategoryMapper)
    }

    def "Get all categories"() {
        given:
        List<Category> categoryList = []
        List<CategoryResponseDto> categoryResponseDtoList = []

        when:
        categoryService.getCategories()

        then:
        1 * mockCategoryRepository.findAll() >> categoryList
        1 * mockCategoryMapper.categoryListToCategoryResponseDto(categoryList) >> categoryResponseDtoList

    }

}
