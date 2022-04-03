package com.ecommerce.app.services;

import java.util.List;

import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Order;
import com.ecommerce.app.models.User;

public interface IOrderService {
	
//	Stream<OrderRequest> getLatestOrders();
	
	Double getOrderAmount(List<ShoppingCartProductsRequest> listOfProducts);
	
	
	Order saveOrder(Order order,String razorpayId);

	Order createNewOrder( User user);

	Order getOrderById(String orderId);
}
