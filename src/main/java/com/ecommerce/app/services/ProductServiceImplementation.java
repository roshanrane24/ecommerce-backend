package com.ecommerce.app.services;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.app.dto.request.AddNewProduct;
import com.ecommerce.app.dto.request.ProductDetailsRequest;
import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;
import com.ecommerce.app.models.Product;
import com.ecommerce.app.repository.ProductRepository;


@Service
@Transactional
public class ProductServiceImplementation implements IProductService {

	@Value("${file.upload.location}/products")
	private String location;
	
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
    public ShoppingCartProductsRequest getShoppingCartProductById(String productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product by ID " + productId + " not found!!!!"));
        ShoppingCartProductsRequest productsRequest = new ShoppingCartProductsRequest(productId, product.getName(), product.getImage(), product.getPrice(), quantity);
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
	
	@Override
	public Product saveProductToDb(AddNewProduct newProduct){

		Product p = new Product();

//		image.transferTo(new File(location, image.getOriginalFilename()));

//		p.setImage(image.getOriginalFilename());
		p.setName(newProduct.getName());
		p.setDescription(newProduct.getDescription());
		p.setSubCategoryName(newProduct.getSubCategoryName());
		p.setPrice(newProduct.getPrice());
		p.setStock(newProduct.getStock());
		p.setAdditionalDetails(newProduct.getAdditionalDetails());
		p.setProduct_added_date(Instant.now());

		return productRepository.save(p);
	}
	
	@Override
	public Product addImage(String productId, MultipartFile image) throws IllegalStateException, IOException {
		Product p = productRepository.findById(productId).get();

		image.transferTo(new File(location, productId+".jpeg"));

		p.setImage(productId+".jpeg");
		
		return productRepository.save(p);
	}

}
