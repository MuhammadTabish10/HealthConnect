package com.healthconnect.authservice.service;

import com.healthconnect.authservice.dto.LoginCredentials;
import com.healthconnect.authservice.dto.TokenResponse;
import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.UserDto;

public interface AuthService extends GenericService<UserDto> {
    UserDto registerUser(UserDto userDto);
    TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials);
}
