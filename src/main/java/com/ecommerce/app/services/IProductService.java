package com.ecommerce.app.services;

import java.util.stream.Stream;

import com.ecommerce.app.dto.request.ProductsRequest;
import com.ecommerce.app.models.Product;

public interface IProductService {
	
	Product getProductById(String productId);

	Stream<ProductsRequest> getLatestProducts();
	
	Stream<ProductsRequest> getMostVisitedProducts();

	Product updateVisits(Product product);
	
	ProductsRequest getWishListProductById(String productId);
}
