package com.healthconnect.userservice.service.impl;

import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.userservice.dto.UserDto;
import com.healthconnect.userservice.model.User;
import com.healthconnect.userservice.repository.UserRepository;
import com.healthconnect.userservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MappingUtils mappingUtils;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MappingUtils mappingUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        User user = mappingUtils.mapToEntity(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return mappingUtils.mapToDto(savedUser, UserDto.class);
    }

}
