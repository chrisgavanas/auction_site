package com.webapplication.api.category;

import com.webapplication.dto.category.CategoryResponseDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public interface CategoryApi {

    @RequestMapping(path = "/category", method = RequestMethod.GET, produces = "application/json")
    List<CategoryResponseDto> getCategories() throws Exception;

    @RequestMapping(path = "/category/{categoryId}", method = RequestMethod.GET, produces = "application/json")
    CategoryResponseDto getCategory(@PathVariable String categoryId) throws Exception;
}
