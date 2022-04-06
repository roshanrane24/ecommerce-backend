package com.ecommerce.app.repository;

import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ecommerce.app.dto.request.ProductDetailsRequest;
import com.ecommerce.app.models.Product;

public interface ProductRepository extends MongoRepository<Product,String>{
	
	//findById are inherited methods no need to write here
	
    @Query(value="{}",
            sort="{product_added_date : -1}",
            fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Stream<ProductDetailsRequest> getLatestProducts();
    
    @Query(value="{}",
            sort="{visits : -1}",
            fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Stream<ProductDetailsRequest> getMostVisitedProducts();
    
    
    
    @Query(value = "{name: {$regex: ?0, $options: 'i'}}",
    		sort="{product_added_date : -1}",
    		 fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Page<ProductDetailsRequest> findAllByQ(String query, Pageable pageable);

    @Query(value = "{subCategory: {$regex: ?0, $options: 'i'}}",
    		sort="{product_added_date : -1}",
    		 fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Page<ProductDetailsRequest> findAllBySubCategory(String query, Pageable pageable);
    
    @Query(value="{}",
            sort="{product_added_date : -1}",
            fields = "{ _id: 1 , name: 1, image: 1, price:1 }")
    Page<ProductDetailsRequest> getAllByQ(Pageable pageable);
}
