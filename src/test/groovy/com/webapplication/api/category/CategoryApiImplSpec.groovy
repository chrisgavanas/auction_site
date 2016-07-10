package com.webapplication.api.category

import com.webapplication.dto.category.CategoryResponseDto
import com.webapplication.service.category.CategoryServiceApi
import spock.lang.Specification

class CategoryApiImplSpec extends Specification {

    CategoryApiImpl categoryApi
    CategoryServiceApi mockCategoryService

    def setup() {
        mockCategoryService = Mock(CategoryServiceApi)


        categoryApi = new CategoryApiImpl(categoryService: mockCategoryService)
    }

    def "Successfully get a list of the existing categories"() {
        when:
        categoryApi.getCategories()

        then:
        1 * mockCategoryService.getCategories() >> new LinkedList<CategoryResponseDto>()
        0 * _
    }

}
