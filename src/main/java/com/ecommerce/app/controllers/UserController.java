package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.dto.request.AddAddressRequest;
import com.ecommerce.app.dto.request.ChangeAddressRequest;
import com.ecommerce.app.dto.request.UserDetailsUpdateRequest;
import com.ecommerce.app.dto.response.MessageResponse;
import com.ecommerce.app.models.Address;
import com.ecommerce.app.models.AddressType;
import com.ecommerce.app.models.User;
import com.ecommerce.app.security.jwt.JwtUtils;
import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/user-details")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	IUserService userService;

	@Autowired
	PasswordEncoder encoder;

	// Display Profile Details
	@GetMapping("/display")
	public ResponseEntity<?> displayProfile(@RequestHeader String authorization) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		return new ResponseEntity<>(userService.getProfile(user), HttpStatus.OK);
	}

	// Display List of Addresses
	@GetMapping("/address/display")
	public ResponseEntity<?> displayWishList(@RequestHeader String authorization) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		return new ResponseEntity<>(user.getAddresses(), HttpStatus.OK);
	}

	// Edit Profile Details
	@PostMapping("/edit")
	public ResponseEntity<?> editUserDetails(@RequestHeader String authorization,
			@RequestBody UserDetailsUpdateRequest userDetails) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		user.setFirstname(userDetails.getFirstname());
		user.setLastname(userDetails.getLastname());
		user.setPassword(encoder.encode(userDetails.getPassword()));
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("User details updated successfully"));
	}

	// Add Address
	@PostMapping("/address/add")
	public ResponseEntity<?> addAddress(@RequestHeader String authorization,
			@RequestBody AddAddressRequest addAddress) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		Address address = new Address(AddressType.valueOf(addAddress.getTypeOfAddress()), addAddress.getCountry(),
				addAddress.getState(), addAddress.getFullName(), addAddress.getMobileNumber(), addAddress.getPincode(),
				addAddress.getLine1(), addAddress.getLine2(), addAddress.getLandmark(), addAddress.getTownCity());
		if (user.getAddresses().containsKey(address.getId()))
			return ResponseEntity.ok(new MessageResponse("Address already present"));
		user.getAddresses().put(address.getId(), address);
		if (user.getDefaultAddress() == null)
			user.setDefaultAddress(address);
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Address added successfully"));
	}

	// Delete Address
	@DeleteMapping("/address/delete")
	public ResponseEntity<?> deleteAddress(@RequestHeader String authorization,
			@RequestBody ChangeAddressRequest deleteAddress) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		if (!user.getAddresses().containsKey(deleteAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("Address not found."));
		if (user.getDefaultAddress().getId().equals(deleteAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse(
					"Default Address can not be remove!! first change the Default Address then Delete"));
		user.getAddresses().remove(deleteAddress.getAddressId());
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Address removed successfully"));
	}

	// Select Default Address
	@PostMapping("/address/change-default")
	public ResponseEntity<?> changeDefaultAddress(@RequestHeader String authorization,
			@RequestBody ChangeAddressRequest changeAddress) {
		String token = jwtUtils.getTokenFromHeader(authorization);
		String email = jwtUtils.getUserNameFromJwtToken(token);
		User user = userService.getByEmail(email);
		if (!user.getAddresses().containsKey(changeAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("Address not found."));
		if (user.getDefaultAddress().getId().equals(changeAddress.getAddressId()))
			return ResponseEntity.ok(new MessageResponse("This address is already set to default"));
		user.setDefaultAddress(user.getAddresses().get(changeAddress.getAddressId()));
		userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("Default Address changed successfully"));
	}
}