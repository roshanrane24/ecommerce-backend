package com.ecommerce.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.models.Product;
import com.ecommerce.app.payload.request.LatestProductsRequest;
import com.ecommerce.app.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("http://localhost:3000")
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Value("${file.upload.location}")
	private String location;
	
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestProducts(){

        List<LatestProductsRequest> products;
        try(Stream<LatestProductsRequest> stream = productRepository.getLatestProducts()) {
            products = stream.limit(7).collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }
    
  //http://localhost:8080/api/products/image/1
  	@GetMapping("/image/{productId}")
  	// Can be tested with browser. Will work fine with react / angular app.
  	public ResponseEntity<byte[]> getFile(@PathVariable String productId) throws IOException {
  		Optional<Product> product = productRepository.findById(productId);
  		Path path = Paths.get(location, product.get().getImage());
  		byte[] imageData = Files.readAllBytes(path);
  		return ResponseEntity.ok()
  				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + product.get().getImage() + "\"")
  				.body(imageData);
  	}

}
