package com.webapplication.api.category;

import com.webapplication.dto.category.CategoryResponseDto;

import com.webapplication.service.category.CategoryServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class CategoryApiImpl implements CategoryApi {

    @Autowired
    private CategoryServiceApi categoryService;

    @Override
    public List<CategoryResponseDto> getCategories() throws Exception {

    	
       

        return categoryService.getCategories();
    }

    @Override
    public CategoryResponseDto getCategory(@PathVariable String categoryId) throws Exception {
    	return categoryService.getCategory(categoryId);
    }
    
    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
