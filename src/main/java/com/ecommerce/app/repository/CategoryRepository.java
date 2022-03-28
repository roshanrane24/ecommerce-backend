package com.ecommerce.app.repository;

import java.util.List;
 

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.app.models.Category;

public interface CategoryRepository extends MongoRepository<Category, String>{
	 List<Category> findAll();
}
