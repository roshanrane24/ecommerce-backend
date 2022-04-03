package com.ecommerce.app.dto.request;

import java.util.List;

import com.ecommerce.app.models.Address;

import lombok.Data;

@Data
public class NewOrderRequest {
	
	List<ShoppingCartProductsRequest> itemsList;

	Address deliveryAddress;
}
