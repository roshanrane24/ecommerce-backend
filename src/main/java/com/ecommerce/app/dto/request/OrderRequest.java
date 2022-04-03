package com.ecommerce.app.dto.request;

import java.util.Date;

import com.ecommerce.app.models.OrderStatus;

import lombok.Data;

@Data
public class OrderRequest {

	String orderId;
	
	Date orderDate;
	
	Double orderAmount;
	
	OrderStatus order_status;
}
