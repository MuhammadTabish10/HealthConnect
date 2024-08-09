package com.healthconnect.userservice.service;

import com.healthconnect.userservice.dto.UserDto;

public interface UserService {
    UserDto registerUser(UserDto userDto);
}
