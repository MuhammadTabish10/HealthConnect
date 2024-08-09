package com.healthconnect.userservice.controller;

import com.healthconnect.userservice.constant.ApiEndpoints;
import com.healthconnect.userservice.dto.UserDto;
import com.healthconnect.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(ApiEndpoints.USERS)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiEndpoints.REGISTER)
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUser = userService.registerUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
