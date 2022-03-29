package com.ecommerce.app.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ecommerce.app.models.Product;
import com.ecommerce.app.payload.request.LatestProductsRequest;

public interface ProductRepository extends MongoRepository<Product,String>{
	
    @Query(value="{}",
            sort="{product_added_date : -1}",
            fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Stream<LatestProductsRequest> getLatestProducts();
    
    Optional<Product> findById(String id);

}
