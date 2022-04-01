package com.ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.services.IUserService;

@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin("*")
public class UserController {
    
    @Autowired
    IUserService userService;
    
    //Display Profile Details
    
    
    
    //Display List of Addresses
    
    //Edit Profile Details
    
    //Add Address
    
    //Delete Address
    
    //Select Default Address

}