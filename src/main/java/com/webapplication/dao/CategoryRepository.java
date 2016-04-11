package com.webapplication.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Category findCategoryByCategoryId(Integer categoryId);
    List<Category> findAll();
}
