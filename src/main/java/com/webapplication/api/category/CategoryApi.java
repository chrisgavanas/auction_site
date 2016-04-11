package com.webapplication.api.category;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.dto.category.CategoryResponseDto;

@RestController
@RequestMapping(path = "/api")
public interface CategoryApi {

    @RequestMapping(path = "/category", method = RequestMethod.GET, produces = "application/json")
    List<CategoryResponseDto> getCategories() throws Exception;

}
