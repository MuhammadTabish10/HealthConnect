package com.healthconnect.userservice.service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.HospitalDto;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService extends GenericService<UserDto> {
    UserDto registerUser(UserDto userDto);
    TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials);
    ResponseEntity<String> logoutUser(String refreshToken);
    TokenResponse refreshAccessToken(String refreshToken);
    String requestPasswordReset(String email);
    String resetPassword(String token, String newPassword, String confirmPassword);
    Map<Long, UserDto> findAllByIds(List<Long> ids);
}
