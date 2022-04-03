package com.ecommerce.app.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.ecommerce.app.dto.request.ShoppingCartProductsRequest;

import lombok.Data;

@Data
@Document(collection ="orders") 
public class Order {
	
		@Id  // Specify the MongoDB document’s primary key _id using the @Id annotation
		private String id;
		
		@Field(value = "order_amount")
		private Double orderAmount;
		
		@Field(value = "transaction_id")
		private String transactionId;
		
		@Field(value = "order_items_list")
		List<ShoppingCartProductsRequest> itemList=new ArrayList<>();
		
		@CreatedDate
		@Field(value = "order_date")
		private Instant orderDate;
		
		@NotBlank
		@Field(value = "order_status")
		private OrderStatus orderStatus;
		
		@Field(value = "delivery_address")
		private Address deliveryAddress;

		public Order(Double orderAmount, List<ShoppingCartProductsRequest> itemList,
				Instant orderDate, @NotBlank OrderStatus orderStatus, Address deliveryAddress) {
			super();
			this.orderAmount = orderAmount;
			this.itemList = itemList;
			this.orderDate = orderDate;
			this.orderStatus = orderStatus;
			this.deliveryAddress = deliveryAddress;
		}
		
		
}
