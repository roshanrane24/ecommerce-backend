package com.ecommerce.app.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
@Data
public class ChangeAddressRequest {
	@NotBlank
	private String token; 
	@NotBlank
	private Integer addressId;
}
