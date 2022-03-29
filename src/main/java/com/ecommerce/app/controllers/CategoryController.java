package com.ecommerce.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@Value("${file.upload.location}/category")
	private String location;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllCategory(){
		List<Category> categoryList = categoryRepository.findAll();
		return ResponseEntity.ok(categoryList);	
	}
	
	
	//http://localhost:8080/api/products/image/1
  	@GetMapping("/image/{categoryId}")
  	// Can be tested with browser. Will work fine with react / angular app.
  	public ResponseEntity<byte[]> getFile(@PathVariable String categoryId) throws IOException {
  		Optional<Category> category = categoryRepository.findById(categoryId) ;
  		Path path = Paths.get(location, category.get().getImageName());
  		byte[] imageData = Files.readAllBytes(path);
  		return ResponseEntity.ok()
  				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + category.get().getImageName() + "\"")
  				.body(imageData);
  	}
}
