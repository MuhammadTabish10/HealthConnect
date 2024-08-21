package com.healthconnect.userservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.userservice.constant.ApiEndpoints;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import com.healthconnect.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.USERS)
public class UserController extends GenericController<UserDto> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<TokenResponse> loginUserAndReturnAuthenticationToken(@Valid @RequestBody LoginCredentials loginCredentials) {
        TokenResponse tokenResponse = userService.loginUserAndReturnToken(loginCredentials);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(ApiEndpoints.REFRESH_TOKEN)
    public ResponseEntity<TokenResponse> getAccessTokenByRefreshToken(@RequestParam String refreshToken) {
        TokenResponse tokenResponse = userService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(ApiEndpoints.LOGOUT)
    public ResponseEntity<String> logoutUser(@RequestParam String refreshToken) {
        return userService.logoutUser(refreshToken);
    }

    @Override
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}
