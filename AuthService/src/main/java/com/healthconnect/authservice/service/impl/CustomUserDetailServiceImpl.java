package com.healthconnect.authservice.service.impl;

import com.healthconnect.authservice.config.CustomUserDetail;
import com.healthconnect.authservice.repository.UserRepository;
import com.healthconnect.commonmodels.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsActiveIsTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetail(user);
    }
}
