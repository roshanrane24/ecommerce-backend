package com.ecommerce.app.dto.request;

import lombok.Value;

@Value
public class ShoppingCartProductsRequest {

	    String _id, name, image;
	    Double price;
	    Integer quantity=1; 
}
