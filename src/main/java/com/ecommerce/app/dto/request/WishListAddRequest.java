package com.ecommerce.app.dto.request;

import lombok.Value;

@Value
public class WishListAddRequest {
	
String productId;
String token;
}
