package com.ecommerce.app.dto.request;

import lombok.Data;

@Data
public class TransactionRequest {
	private String transactionId;
	private String orderId;
}
