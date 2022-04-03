package com.ecommerce.app.services;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.app.dto.request.ProductDetailsRequest;
import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
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
	public Stream<ProductDetailsRequest> getLatestProducts() {
		return productRepository.getLatestProducts();
	}

	@Override
	public Stream<ProductDetailsRequest> getMostVisitedProducts() {
		return productRepository.getMostVisitedProducts();
	}

	@Override
	public Product updateVisits(Product product) {
		product.setVisits(product.getVisits()+1);
		return productRepository.save(product);
	}

	@Override
	public ProductDetailsRequest getWishListProductById(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product by ID " + productId + " not found!!!!"));
		ProductDetailsRequest productsRequest = new ProductDetailsRequest(productId, product.getName(), product.getImage(), product.getPrice());
		return productsRequest;
	}
	
	@Override
    public ShoppingCartProductsRequest getShoppingCartProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product by ID " + productId + " not found!!!!"));
        ShoppingCartProductsRequest productsRequest = new ShoppingCartProductsRequest(productId, product.getName(), product.getImage(), product.getPrice());
        return productsRequest;
    }

	@Override
	public boolean stockUnavailable(List<ShoppingCartProductsRequest> itemsList) {
		 for (ShoppingCartProductsRequest item : itemsList) {
			Product p = productRepository.findById(item.get_id())
					.orElseThrow(() -> new RuntimeException("Product by ID " + item.get_id() + " not found!!!!"));
			if(p.getStock() < item.getQuantity())
				return true;
		}
		return false;
	}

	@Override
	public void reduceStock(List<ShoppingCartProductsRequest> itemsList) {
		for (ShoppingCartProductsRequest item : itemsList) {
			Product p = productRepository.findById(item.get_id())
					.orElseThrow(() -> new RuntimeException("Product by ID " + item.get_id() + " not found!!!!"));
			 p.setStock(p.getStock()-item.getQuantity());
			 productRepository.save(p);
				 
		}
	 
	}

}
