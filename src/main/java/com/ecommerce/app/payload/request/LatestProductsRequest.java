package com.ecommerce.app.payload.request;

import lombok.Value;

@Value
public class LatestProductsRequest {

	    String _id, name, image;
	    Double price;

}
