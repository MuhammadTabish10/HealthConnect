package com.healthconnect.authservice.controller;

import com.healthconnect.authservice.constant.ApiEndpoints;
import com.healthconnect.authservice.dto.LoginCredentials;
import com.healthconnect.authservice.dto.TokenResponse;
import com.healthconnect.authservice.service.AuthService;
import com.healthconnect.commonmodels.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.USERS)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<TokenResponse> loginUserAndReturnAuthenticationToken(@Valid @RequestBody LoginCredentials loginCredentials) {
        TokenResponse tokenResponse = authService.loginUserAndReturnToken(loginCredentials);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(ApiEndpoints.REGISTER)
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = authService.registerUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
