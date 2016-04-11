package com.webapplication.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {

}
