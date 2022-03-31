package com.ecommerce.app.services;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.app.dto.request.ProductsRequest;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImplementation implements IProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Product getProductById(String productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product by ID " + productId + " not found!!!!"));
	}

	@Override
	public Stream<ProductsRequest> getLatestProducts() {
		return productRepository.getLatestProducts();
	}

	@Override
	public Stream<ProductsRequest> getMostVisitedProducts() {
		return productRepository.getMostVisitedProducts();
	}

	@Override
	public Product updateVisits(Product product) {
		product.setVisits(product.getVisits()+1);
		return productRepository.save(product);
	}

	@Override
	public ProductsRequest getWishListProductById(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product by ID " + productId + " not found!!!!"));
		ProductsRequest productsRequest = new ProductsRequest(productId, product.getName(), product.getImage(), product.getPrice());
		return productsRequest;
	}

}
