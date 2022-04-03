package com.ecommerce.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.app.models.Order;

public interface OrderRepository extends MongoRepository<Order,String> {

//    @Query(value="{}",
//            sort="{order_date : -1}",
//            fields = "{ _id: 1 , order_date: 1, order_amount: 1, order_status:1}")
//    Stream<OrderRequest> getLatestOrders();
	
	Order findByRazorpayOrderId(String orderId);
}
