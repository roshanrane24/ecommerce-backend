package com.ecommerce.app.dto.response;

import lombok.Data;

@Data
public class ProfileResponse {
	 
	private String id;	 
	  
	private String firstname;
		  
	private String lastname;	 
 
	private String email;	 
	 
	private String password;

	public ProfileResponse(String id, String firstname, String lastname, String email, String password) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
	}
	
	
}
