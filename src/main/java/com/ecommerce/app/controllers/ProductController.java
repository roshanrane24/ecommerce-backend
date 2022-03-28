package com.ecommerce.app.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.payload.request.LatestProductsRequest;
import com.ecommerce.app.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("http://localhost:3000")
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestProducts(){

        List<LatestProductsRequest> products;
        try(Stream<LatestProductsRequest> stream = productRepository.getLatestProducts()) {
            products = stream.limit(7).collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }

}
