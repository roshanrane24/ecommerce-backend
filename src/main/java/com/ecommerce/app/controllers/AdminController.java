package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.app.dto.request.AddNewProduct;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.ERole;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.OrderStatus;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.models.User;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IOrderService;
import com.ecommerce.app.services.IProductService;
import com.ecommerce.app.services.IRoleService;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*") /* http://localhost:3000/ */ 
				  /*Incase if our frontend is running on 3000 port*/ 
				  /* "*" is used receving requests from all ports*/
public class AdminController {

	@Autowired
	IProductService productService;
	
	@Autowired 
	IOrderService orderService;

	@Autowired
	IUserService userService;

	@Autowired
	IRoleService roleService;

	@Autowired
	JwtUtils jwtUtils;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-product")
	public ResponseEntity<?> addNewProducts(@RequestBody AddNewProduct newProduct) {
		try {
			Product product = productService.saveProductToDb(newProduct);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Product adding Failed!!!"));
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-image/{productId}")
	public ResponseEntity<?> addImageToProduct(@PathVariable String productId, @RequestBody MultipartFile image) {
		try {
			Product p = productService.addImage(productId, image);

			return ResponseEntity.ok(p);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Image adding Failed!!!"));
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/make-admin")
	public ResponseEntity<?> makeAdmin(@RequestBody String email) {
		User user = userService.getByEmail(email);
		user.getRoles().add(roleService.getByName(ERole.ROLE_ADMIN));
		return ResponseEntity.ok(userService.saveUser(user));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/change-order-status/{orderId}")
	public ResponseEntity<?> changeOrderStatus(@PathVariable String orderId){
		Order order;
		try {
			order = orderService.getOrderById(orderId);
			order.setOrderStatus(OrderStatus.INTRANSIT);
			orderService.saveOrder(order);
			return ResponseEntity.ok(new MessageResponse("Order Status Changed Successfully!!!"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Status Change Failed!!!"));
		}
		
	}
}