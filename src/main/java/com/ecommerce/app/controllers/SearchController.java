package com.ecommerce.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.ProductDetailsRequest;
import com.ecommerce.app.services.IProductService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin("*")
public class SearchController {
	
	@Autowired
	IProductService productService;
	
	@GetMapping("/sub-category")
	public ResponseEntity<?> getProductsBySubCategory(@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "pageNumber") int pageNumber) {
		String key = keyword.strip();
		Pageable pageable = PageRequest.of(pageNumber - 1, 5);
		Page<ProductDetailsRequest> products; 
		if (key.length() == 0) {
			//products = productService.getAllByQ(pageable);
			return ResponseEntity.ok("");
		} else {
			 products = productService.getAllBySubCategory(key, pageable);
		}
			List<ProductDetailsRequest> p = products.getContent();
			int page = products.getNumber() + 1;
			int pages = products.getTotalPages();
			Map<String, Object> result = Map.of("page", page, "pages", pages, "products", p);
			return ResponseEntity.ok(result);

	}
	
	@GetMapping("/category")
	public ResponseEntity<?> getProductsByCategory(@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "pageNumber") int pageNumber) {
		String key = keyword.strip();
		Pageable pageable = PageRequest.of(pageNumber - 1, 5);
		Page<ProductDetailsRequest> products; 
		if (key.length() == 0) {
			//products = productService.getAllByQ(pageable);
			return ResponseEntity.ok("");
		} else {
			 products = productService.getAllByCategory(key, pageable);
		}
			List<ProductDetailsRequest> p = products.getContent();
			int page = products.getNumber() + 1;
			int pages = products.getTotalPages();
			Map<String, Object> result = Map.of("page", page, "pages", pages, "products", p);
			return ResponseEntity.ok(result);

	}
	
	@GetMapping
	public ResponseEntity<?> getProductsByQ(@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "pageNumber") int pageNumber) {
		String key = keyword.strip();
		Pageable pageable = PageRequest.of(pageNumber - 1, 5);
		Page<ProductDetailsRequest> products; 
		if (key.length() == 0) {
//			products = productService.getAllByQ(pageable);
			return ResponseEntity.ok("");
		} else {
			 products = productService.getAllByQ(key, pageable);
		}
			List<ProductDetailsRequest> p = products.getContent();
			int page = products.getNumber() + 1;
			int pages = products.getTotalPages();
			Map<String, Object> result = Map.of("page", page, "pages", pages, "products", p);
			return ResponseEntity.ok(result); 

	}
}
