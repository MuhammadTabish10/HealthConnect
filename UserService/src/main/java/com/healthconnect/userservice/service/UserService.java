package com.healthconnect.userservice.service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import org.springframework.http.ResponseEntity;

public interface UserService extends GenericService<UserDto> {
    UserDto registerUser(UserDto userDto);
    TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials);
    ResponseEntity<String> logoutUser(String refreshToken);
    TokenResponse refreshAccessToken(String refreshToken);
}
