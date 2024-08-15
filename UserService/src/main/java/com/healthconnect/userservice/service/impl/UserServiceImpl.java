package com.healthconnect.userservice.service.impl;

import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.UserRepository;
import com.healthconnect.userservice.repository.UserDataRepository;
import com.healthconnect.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDto> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserDataRepository userDataRepository, MappingUtils mappingUtils, UserRepository userRepository) {
        super(userDataRepository, mappingUtils, User.class, UserDto.class);
        this.userRepository = userRepository;
    }


}
