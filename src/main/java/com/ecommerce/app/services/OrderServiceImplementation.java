package com.ecommerce.app.services;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.User;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.UserRepository;

@Service
@Transactional
public class OrderServiceImplementation implements IOrderService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
//	@Override
//	public Stream<OrderRequest> getLatestOrders() {
//		
//		return orderRepository.getLatestOrders();
//	}
//	
	@Override
	public Double getOrderAmount(List<ShoppingCartProductsRequest> listOfProducts) {
		Double amount=0.0;
		for(ShoppingCartProductsRequest product : listOfProducts) {
			amount+=product.getSubTotal();
		}
		return amount;
	}

	@Override
	public Order createNewOrder(User user) {
		Order newOrder = new Order(getOrderAmount(user.getShoppingCart()),user.getShoppingCart(), user.getShippingAddress() , user.getBillingAddress());
		return newOrder;
	}

	@Override
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(String orderId) {
		 
		return orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Order Id Not Found !!"));
	}

}
