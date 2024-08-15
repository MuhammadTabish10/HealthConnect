package com.healthconnect.authservice.service.impl;

import com.healthconnect.authservice.constant.LogMessages;
import com.healthconnect.authservice.config.CustomUserDetail;
import com.healthconnect.authservice.dto.LoginCredentials;
import com.healthconnect.authservice.dto.TokenResponse;
import com.healthconnect.authservice.repository.UserAuthRepository;
import com.healthconnect.authservice.service.AuthService;
import com.healthconnect.authservice.service.JwtService;
import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.exception.EntityNotFoundException;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.commonmodels.model.Role;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.RoleRepository;
import com.healthconnect.commonmodels.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class AuthServiceImpl extends GenericServiceImpl<User, UserDto> implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private static final String BEARER = "Bearer";
    private static final String ROLE_CLIENT = "ROLE_CLIENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MappingUtils mappingUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserAuthRepository userAuthRepository, UserRepository userRepository, RoleRepository roleRepository, MappingUtils mappingUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        super(userAuthRepository, mappingUtils, User.class, UserDto.class);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        logger.info(String.format(LogMessages.REGISTERING_USER_LOG, userDto.getEmail()));

        User user = mappingUtils.mapToEntity(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(ROLE_CLIENT)
                .orElseThrow(() -> {
                    logger.error(String.format(LogMessages.ROLE_NOT_FOUND_LOG, ROLE_CLIENT));
                    return new EntityNotFoundException(
                            String.format(ErrorMessages.ENTITY_NOT_FOUND_BY_NAME, Role.class.getSimpleName(), ROLE_CLIENT));
                });

        user.setRoles(Collections.singleton(role));
        User savedUser = userRepository.save(user);

        logger.info(String.format(LogMessages.USER_REGISTERED_SUCCESS_LOG, userDto.getEmail()));
        return mappingUtils.mapToDto(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials) {
        logger.info(String.format(LogMessages.AUTHENTICATING_USER_LOG, loginCredentials.getEmail()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword())
        );

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetail);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(jwtService.getExpirationTime() / 1000);

        logger.info(String.format(LogMessages.AUTHENTICATION_SUCCESS_LOG, loginCredentials.getEmail()));
        return new TokenResponse(token, expiresAt, BEARER, now);
    }
}
