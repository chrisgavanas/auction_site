package com.webapplication.api.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.webapplication.dto.category.CategoryResponseDto;
import com.webapplication.service.category.CategoryServiceApi;

@Component
public class CategoryApiImpl implements CategoryApi {

    @Autowired
    private CategoryServiceApi categoryService;

    public List<CategoryResponseDto> getCategories() throws Exception {
        return categoryService.getCategories();
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
