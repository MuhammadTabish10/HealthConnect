package com.healthconnect.authservice.service.impl;

import com.healthconnect.authservice.config.CustomUserDetail;
import com.healthconnect.authservice.dto.LoginCredentials;
import com.healthconnect.authservice.dto.TokenResponse;
import com.healthconnect.authservice.repository.UserRepository;
import com.healthconnect.authservice.service.AuthService;
import com.healthconnect.authservice.service.JwtService;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.commonmodels.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl extends GenericServiceImpl<User, UserDto> implements AuthService {

    private static final String BEARER = "Bearer";

    private final UserRepository userRepository;
    private final MappingUtils mappingUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, MappingUtils mappingUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        super(userRepository, mappingUtils, User.class, UserDto.class);
        this.userRepository = userRepository;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        User user = mappingUtils.mapToEntity(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return mappingUtils.mapToDto(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword())
        );
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetail);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(jwtService.getExpirationTime() / 1000);

        return new TokenResponse(token, expiresAt, BEARER, now);
    }
}
