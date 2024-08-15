package com.healthconnect.authservice.service.impl;

import com.healthconnect.authservice.constant.LogMessages;
import com.healthconnect.authservice.config.CustomUserDetail;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.healthconnect.baseservice.constant.ErrorMessages.ENTITY_NOT_FOUND_WITH_EMAIL;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailServiceImpl.class);

    private final UserRepository userRepository;

    public CustomUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info(String.format(LogMessages.LOADING_USER_BY_EMAIL_LOG, email));

        User user = userRepository.findByEmailAndIsActiveIsTrue(email)
                .orElseThrow(() -> {
                    logger.error(String.format(LogMessages.USER_NOT_FOUND_LOG, email));
                    return new UsernameNotFoundException(String.format(ENTITY_NOT_FOUND_WITH_EMAIL, User.class, email));
                });

        logger.info(String.format(LogMessages.USER_FOUND_LOG, email));
        return new CustomUserDetail(user);
    }
}
