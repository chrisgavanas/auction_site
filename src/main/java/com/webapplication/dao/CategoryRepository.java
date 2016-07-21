package com.webapplication.dao;

import com.webapplication.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Category findCategoryByCategoryId(@Param("categoryId") String categoryId);

    @Query("{'categoryId' : { $in : ?0 } }")
    List<Category> findCategoriesByIds(List<String> categoryIds);

}
