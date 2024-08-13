package com.healthconnect.userservice.service;

import com.healthconnect.baseservice.service.GenericService;
import com.healthconnect.commonmodels.dto.UserDto;

public interface UserService extends GenericService<UserDto> {
    UserDto registerUser(UserDto userDto);
}
