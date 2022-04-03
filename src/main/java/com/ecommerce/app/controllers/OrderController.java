package com.ecommerce.app.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.config.RazorPayClientConfig;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.dto.response.OrderResponse;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.User;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IOrderService;
import com.ecommerce.app.services.IProductService;
import com.ecommerce.app.services.IUserService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

	private RazorpayClient client;

	private RazorPayClientConfig razorPayClientConfig;

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

	@Autowired
	public OrderController(RazorPayClientConfig razorpayClientConfig) throws RazorpayException {
		this.razorPayClientConfig = razorpayClientConfig;
		this.client = new RazorpayClient(razorpayClientConfig.getKey(), razorpayClientConfig.getSecret());
	}
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
		if (user.getShoppingCart().isEmpty() || user.getBillingAddress() == null || user.getShippingAddress() == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Cannot create order !!!"));
		Order newOrder = orderService.createNewOrder(user);
		if (productService.stockUnavailable(newOrder.getItemList()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Cannot order as stock is unavailable"));

		// productService.reduceStock(newOrder.getItemList());

//		orderService.saveOrder(newOrder);
//		Order savedOrder = orderRepository.findById(newOrder.getId())
//				.orElseThrow(() -> new RuntimeException("Order not found."));
		
		OrderResponse razorPay = null;
        try {
            // The transaction amount is expressed in the currency subunit, such
            // as paise (in case of INR)
            String amountInPaise = convertRupeeToPaise(newOrder.getOrderAmount().toString());
            // Create an order in RazorPay and get the order id
            System.out.println("1");
            com.razorpay.Order order = createRazorPayOrder(amountInPaise);
            System.out.println("2");
            razorPay = getOrderResponse((String)order.get("id"));
            newOrder.setRazorpayOrderId((String)order.get("id"));
            // Save order in the database
            
            user.getOrders().add(orderService.saveOrder(newOrder,razorPay.getRazorpayOrderId()));
    		user.setShoppingCart(new ArrayList<>());
    		userService.saveUser(user);
        } catch (RazorpayException e) {
            
            return ResponseEntity.ok("Did't work!!");
        }
        return ResponseEntity.ok(razorPay);

	}

    private OrderResponse getOrderResponse(String orderId) {
        OrderResponse razorPay = new OrderResponse();
      
        razorPay.setRazorpayOrderId(orderId);
        
        return razorPay;
    }
	private com.razorpay.Order createRazorPayOrder(String amount) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", "txn_123456");
        return client.Orders.create(options);
    }
 
    private String convertRupeeToPaise(String paise) {
        BigDecimal b = new BigDecimal(paise);
        BigDecimal value = b.multiply(new BigDecimal("100"));
        return value.setScale(0, RoundingMode.UP).toString();
    }

//	@PostMapping("/reduce-stock")
//	public ResponseEntity<?> reduceStock(@RequestBody TransactionRequest transactionRequest) {
//
//		Order order = orderService.getOrderById(transactionRequest.getOrderId());
//
//		productService.reduceStock(order.getItemList());
//
//		order.setOrderStatus(OrderStatus.PLACED);
//		order.setPaymentStatus(PaymentStatus.SUCCESS);
//
//		orderService.saveOrder(order);
//		return ResponseEntity.ok(new MessageResponse("Stock Reduced Successfully"));
//	}

}
