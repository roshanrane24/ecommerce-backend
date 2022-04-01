package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.AddAddressRequest;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.Address;
import com.ecommerce.app.models.AddressType;
import com.ecommerce.app.models.User;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin("*")
public class UserController {
    
	@Autowired
	private JwtUtils jwtUtils;
	
    @Autowired
    IUserService userService;
    
    //Display Profile Details
    
    
    //Display List of Addresses
    
    //Edit Profile Details
    
    //Add Address
    @PostMapping("/address/add")
    public ResponseEntity<?> addAddress(@RequestBody AddAddressRequest addAddress){
    	String email = jwtUtils.getUserNameFromJwtToken(addAddress.getToken());
		User user = userService.getByEmail(email);
		Address address = new Address(AddressType.valueOf(addAddress.getTypeOfAddress()), addAddress.getCountry(),addAddress.getState(),addAddress.getFullName(),addAddress.getMobileNumber(),addAddress.getPincode(),addAddress.getLine1(),addAddress.getLine2(),addAddress.getLandmark(),addAddress.getTownCity());
		System.out.println("Aman");
		user.getAddresses().put(address.getId(), address);
		System.out.println("123");
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Address added successfully ."));
    }
    
    //Delete Address
    
    //Select Default Address

}