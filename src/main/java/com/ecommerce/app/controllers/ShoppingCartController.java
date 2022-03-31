package com.ecommerce.app.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shopping-cart")
@CrossOrigin("*")
public class ShoppingCartController {
	
//    @GetMapping("/add-to-cart/{userId}/{productId}")
//	public ResponseEntity<?> getProductById(@PathVariable String productId) {
//    	Product product = productService.getProductById(productId);
//    	return new ResponseEntity<>(productService.updateVisits(product), HttpStatus.OK);
//	}
    


}
