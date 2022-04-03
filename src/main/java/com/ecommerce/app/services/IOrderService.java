package com.ecommerce.app.services;

import java.util.List;
import java.util.stream.Stream;

import com.ecommerce.app.dto.request.NewOrderRequest;
import com.ecommerce.app.dto.request.OrderRequest;
import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Order;

public interface IOrderService {
	
	Stream<OrderRequest> getLatestOrders();
	
	Double getOrderAmount(List<ShoppingCartProductsRequest> listOfProducts);
	
	Order createNewOrder(NewOrderRequest orderRequest);
	
	Order saveOrder(Order order);

}
