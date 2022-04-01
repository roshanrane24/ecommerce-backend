package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.AddAddressRequest;
import com.ecommerce.app.dto.request.ChangeAddressRequest;
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
    @GetMapping("/displayprofile/{token}")
   	public ResponseEntity<?> displayProfile(@PathVariable String token){
   	String email = jwtUtils.getUserNameFromJwtToken(token);
   	User user = userService.getByEmail(email);
   	return new ResponseEntity<>(userService.getProfile(user), HttpStatus.OK);
   	}
    
    //Display List of Addresses
    @GetMapping("/address/{token}")
	public ResponseEntity<?> displayWishList(@PathVariable String token){
	String email = jwtUtils.getUserNameFromJwtToken(token);
	User user = userService.getByEmail(email);
	return new ResponseEntity<>(user.getAddresses(), HttpStatus.OK);
	}
    
    //Edit Profile Details
    
    //Add Address
    @PostMapping("/address/add")
    public ResponseEntity<?> addAddress(@RequestBody AddAddressRequest addAddress){
    	String email = jwtUtils.getUserNameFromJwtToken(addAddress.getToken());
		User user = userService.getByEmail(email);
		Address address = new Address(AddressType.valueOf(addAddress.getTypeOfAddress()), addAddress.getCountry(),addAddress.getState(),addAddress.getFullName(),addAddress.getMobileNumber(),addAddress.getPincode(),addAddress.getLine1(),addAddress.getLine2(),addAddress.getLandmark(),addAddress.getTownCity());
	 
		user.getAddresses().put(address.getId() ,address);
		if(user.getDefaultAddress()== null)
			user.setDefaultAddress(address);
		
		userService.saveUser(user);
	 
		return ResponseEntity.ok(new MessageResponse("Address added successfully ."));
    }
    
    //Delete Address
    @PostMapping("/address/delete")
    public ResponseEntity<?> deleteAddress(@RequestBody ChangeAddressRequest deleteAddress){
    	String email = jwtUtils.getUserNameFromJwtToken(deleteAddress.getToken());
		User user = userService.getByEmail(email);
		 
		 
		if(user.getDefaultAddress().getId().equals(deleteAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("Default Address can not be remove!! first change the Default Address then Delete"));
		
		user.getAddresses().remove(deleteAddress.getAddressId());
		userService.saveUser(user);
	 
		return ResponseEntity.ok(new MessageResponse("Address removed successfully ."));
    }
    
    //Select Default Address
    @PostMapping("/address/change-default")
    public ResponseEntity<?> changeDefaultAddress(@RequestBody ChangeAddressRequest changeAddress){
    	String email = jwtUtils.getUserNameFromJwtToken(changeAddress.getToken());
		User user = userService.getByEmail(email);
		 
		if(!user.getAddresses().containsKey(changeAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("Address not found."));
		if(user.getDefaultAddress().getId().equals(changeAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("Already Default address exist"));
		user.setDefaultAddress(user.getAddresses().get(changeAddress.getAddressId()));
		 userService.saveUser(user);
		 return ResponseEntity.ok(new MessageResponse("Default Address changed successfully."));
    }
}