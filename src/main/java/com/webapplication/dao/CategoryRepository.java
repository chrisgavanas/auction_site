package com.webapplication.dao;

import com.webapplication.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Category findCategoryByCategoryId(Integer categoryId);

    List<Category> findAll();

    @Query("select c from Category c where c.categoryId in ?1")
    List<Category> findAll(List<Integer> categoryIds);
}
