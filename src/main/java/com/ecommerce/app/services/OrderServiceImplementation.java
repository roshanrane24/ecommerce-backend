package com.ecommerce.app.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.app.dto.request.NewOrderRequest;
import com.ecommerce.app.dto.request.OrderRequest;
import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.OrderStatus;
import com.ecommerce.app.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImplementation implements IOrderService {

	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public Stream<OrderRequest> getLatestOrders() {
		
		return orderRepository.getLatestOrders();
	}
	
	@Override
	public Double getOrderAmount(List<ShoppingCartProductsRequest> listOfProducts) {
		Double amount=0.0;
		for(ShoppingCartProductsRequest product : listOfProducts) {
			amount+=product.getSubTotal();
		}
		return amount;
	}

	@Override
	public Order createNewOrder(NewOrderRequest orderRequest) {
		Order newOrder = new Order(getOrderAmount(orderRequest.getItemsList()),orderRequest.getItemsList(), Instant.now(), OrderStatus.PAYMENT_PENDING, orderRequest.getDeliveryAddress());
		return newOrder;
	}

	@Override
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

}
