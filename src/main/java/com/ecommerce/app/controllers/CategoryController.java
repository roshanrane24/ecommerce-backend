package com.ecommerce.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.models.Category;
import com.ecommerce.app.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("http://localhost:3000")
public class CategoryController{
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllCategory(){
		
		List<Category> categoryList = categoryRepository.findAll();
		
		return ResponseEntity.ok(categoryList);	
	}
}
