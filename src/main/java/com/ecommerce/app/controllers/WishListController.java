package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.WishListAddRequest;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.User;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IProductService;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/wish-list")
@CrossOrigin("*")
public class WishListController {

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IProductService productService;

	
	
	// for displaying wishlist
	// @GetMapping("/{token}")

	// for adding to wishlist
	@PostMapping("/add")
	public ResponseEntity<?> addToWishList(@RequestBody WishListAddRequest wishList) {
		String email = jwtUtils.getUserNameFromJwtToken(wishList.getToken());
		User user = userService.getByEmail(email);
		user.getWishList().add(productService.getWishListProductById(wishList.getProductId()));
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Product added to wishlist successfully!"));
	}

}
