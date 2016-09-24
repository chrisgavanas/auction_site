package com.webapplication.dao;

import com.webapplication.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
	
    Category findCategoryByCategoryId(String categoryId);

    List<Category> findCategoryByParentId(String parentId);

    Category findCategoryByDescription(String description);

    @Query("{'categoryId' : { $in : ?0 } }")
    List<Category> findCategoriesByIds(List<String> categoryIds);

}
