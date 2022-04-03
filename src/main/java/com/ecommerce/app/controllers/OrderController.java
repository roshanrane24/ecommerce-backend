package com.ecommerce.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.TransactionRequest;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.OrderStatus;
import com.ecommerce.app.models.PaymentStatus;
import com.ecommerce.app.models.User;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IOrderService;
import com.ecommerce.app.services.IProductService;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

	@Autowired
	IOrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private IUserService userService;

	@Autowired
	private IProductService productService;

//	//For Admin
//	@GetMapping("/admin/display")
//	public ResponseEntity<?> getLatestOrders(){
//		//Need to create check for admin users
//        List<OrderRequest> orders;
//        try(Stream<OrderRequest> stream = orderService.getLatestOrders()) {
//            orders = stream.collect(Collectors.toList());
//        }
//        return ResponseEntity.ok(orders);
//    }

	// For User
	@GetMapping("/display")
	public ResponseEntity<?> getLatestOrders(@RequestHeader String authorization) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		List<Order> orders;
		try (Stream<Order> stream = user.getOrders().stream()
				.sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))) {
			orders = stream.collect(Collectors.toList());
		}
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/create")
	public ResponseEntity<?> createOrder(@RequestHeader String authorization) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		if(user.getShoppingCart().isEmpty() || user.getBillingAddress() == null || user.getShippingAddress() == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Cannot create order !!!" ));
		Order newOrder = orderService.createNewOrder(user);
		if (productService.stockUnavailable(newOrder.getItemList()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Cannot order as stock is unavailable"));

		// productService.reduceStock(newOrder.getItemList());

//		orderService.saveOrder(newOrder);
//		Order savedOrder = orderRepository.findById(newOrder.getId())
//				.orElseThrow(() -> new RuntimeException("Order not found."));
		user.getOrders().add(orderService.saveOrder(newOrder));
		user.setShoppingCart(new ArrayList<>());
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Order Created Successfully"));
	}

	@PostMapping("/reduce-stock")
	public ResponseEntity<?> reduceStock(@RequestBody TransactionRequest transactionRequest){
		
		 Order order = orderService.getOrderById(transactionRequest.getOrderId());
		  
		 productService.reduceStock(order.getItemList());
		 
		 order.setOrderStatus(OrderStatus.PLACED);
		 order.setPaymentStatus(PaymentStatus.SUCCESS);
		 
		 orderService.saveOrder(order);
		return ResponseEntity.ok(new MessageResponse("Stock Reduced Successfully"));
	}

}
