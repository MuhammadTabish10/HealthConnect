package com.healthconnect.userservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.userservice.constant.ApiEndpoints;
import com.healthconnect.userservice.dto.UserDto;
import com.healthconnect.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiEndpoints.USERS)
public class UserController extends GenericController<UserDto> {

    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        super(userService);
    }

    @PostMapping(ApiEndpoints.REGISTER)
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUser = userService.registerUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
