package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.app.dto.request.AddNewProduct;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.ERole;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.User;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IProductService;
import com.ecommerce.app.services.IRoleService;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	IProductService productService;

	@Autowired
	IUserService userService;

	@Autowired
	IRoleService roleService;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/addProduct")
	public ResponseEntity<?> addNewProducts(@RequestHeader String authorization,
			@RequestBody AddNewProduct newProduct) {

		User userAdmin = jwtUtils.getUserFromRequestHeader(authorization);
		if (!userAdmin.getRoles().contains(roleService.getByName(ERole.ROLE_ADMIN)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse("Admin Credentials Required!!!"));

		try {
			Product product = productService.saveProductToDb(newProduct);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Product adding Failed!!!"));
		}
	}

	@PostMapping("/addImage/{productId}")
	public ResponseEntity<?> addImageToProduct(@RequestHeader String authorization, @PathVariable String productId,
			@RequestBody MultipartFile image) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String adminEmail = jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = userService.getByEmail(adminEmail);
		if (!userAdmin.getRoles().contains(roleService.getByName(ERole.ROLE_ADMIN)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse("Admin Credentials Required!!!"));

		try {
			Product p = productService.addImage(productId, image);

			return ResponseEntity.ok(p);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Image adding Failed!!!"));
		}
	}

	@PostMapping("/makeAdmin")
	public ResponseEntity<?> makeAdmin(@RequestHeader String authorization, @RequestBody String email) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String adminEmail = jwtUtils.getUserNameFromJwtToken(token);
		User userAdmin = userService.getByEmail(adminEmail);
		if (!userAdmin.getRoles().contains(roleService.getByName(ERole.ROLE_ADMIN)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse("Admin Credentials Required!!!"));
		User user = userService.getByEmail(email);
		user.getRoles().add(roleService.getByName(ERole.ROLE_ADMIN));
		return ResponseEntity.ok(userService.saveUser(user));
	}
}