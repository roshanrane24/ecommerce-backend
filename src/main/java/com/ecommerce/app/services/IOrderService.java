package com.ecommerce.app.services;

import java.util.List;

import com.ecommerce.app.dto.request.NewOrderRequest;
import com.ecommerce.app.dto.request.OrderRequest;
import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.User;

public interface IOrderService {
	
//	Stream<OrderRequest> getLatestOrders();
	
	Double getOrderAmount(List<ShoppingCartProductsRequest> listOfProducts);
	
	Order saveOrder(Order order);

	Order createNewOrder(OrderRequest orderRequest);

	Order getOrderById(String orderId);

	OrderRequest createNewOrderRequest(NewOrderRequest orderRequest, User user);
}
