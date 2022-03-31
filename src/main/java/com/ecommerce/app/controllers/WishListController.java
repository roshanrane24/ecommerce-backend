package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.ProductRequest;
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
	@GetMapping("/{token}")
	public ResponseEntity<?> displayWishList(@PathVariable String token){
	String email = jwtUtils.getUserNameFromJwtToken(token);
	User user = userService.getByEmail(email);
	return new ResponseEntity<>(user.getWishList(), HttpStatus.OK);
	}

	// for adding to wishlist
	@PostMapping("/add")
	public ResponseEntity<?> addToWishList(@RequestBody ProductRequest wishList) {
		String email = jwtUtils.getUserNameFromJwtToken(wishList.getToken());
		User user = userService.getByEmail(email);
		if(user.getWishList().contains(productService.getWishListProductById(wishList.getProductId())))
			return ResponseEntity.ok(new MessageResponse("Product already added to Wish List"));
		user.getWishList().add(productService.getWishListProductById(wishList.getProductId()));
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Product added to wishlist successfully!"));
	}
	
	// for adding to wishlist
	@DeleteMapping("/remove")
	public ResponseEntity<?> removeFromWishList(@RequestBody ProductRequest wishList) {
		String email = jwtUtils.getUserNameFromJwtToken(wishList.getToken());
		User user = userService.getByEmail(email);
		if(user.getWishList().isEmpty())
			return ResponseEntity.ok(new MessageResponse("Wishlist is Empty"));
		if(!user.getWishList().contains(productService.getWishListProductById(wishList.getProductId())))
			return ResponseEntity.ok(new MessageResponse("Product does not exist!"));
		user.getWishList().remove(productService.getWishListProductById(wishList.getProductId()));
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Product removed from wishlist successfully!"));
	}

}
