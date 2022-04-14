package com.ecommerce.app.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ecommerce.app.models.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query(value="{}",
            sort="{order_date : -1}")
    Stream<Order> getAllOrders();

	Optional<Order> findByRazorpayOrderId(String orderId);
}
