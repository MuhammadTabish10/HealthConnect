package com.healthconnect.userservice.service.impl;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.constant.LogMessages;
import com.healthconnect.baseservice.exception.*;
import com.healthconnect.baseservice.service.impl.GenericServiceImpl;
import com.healthconnect.baseservice.util.MappingUtils;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.commonmodels.model.User;
import com.healthconnect.commonmodels.repository.UserRepository;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.TokenResponse;
import com.healthconnect.userservice.repository.UserDataRepository;
import com.healthconnect.userservice.service.KeycloakAdminService;
import com.healthconnect.userservice.service.UserService;
import com.healthconnect.userservice.util.UserUtils;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

import static com.healthconnect.userservice.constant.LogMessages.*;
import static com.healthconnect.userservice.util.UserUtils.extractUserIdFromResponse;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDto> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${app.token-url}")
    private String tokenUrl;

    @Value("${app.logout-url}")
    private String logoutUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserUtils userUtils;
    private final MappingUtils mappingUtils;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserDataRepository userDataRepository, MappingUtils mappingUtils,
                           UserRepository userRepository, KeycloakAdminService keycloakAdminService,
                           UserUtils userUtils, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        super(userDataRepository, mappingUtils, User.class, UserDto.class);
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.userUtils = userUtils;
        this.mappingUtils = mappingUtils;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        logger.info(REGISTERING_USER_LOG, userDto.getEmail());

        Response keycloakResponse = createUserInKeycloak(userDto);

        String keycloakUserId = extractUserIdFromResponse(keycloakResponse);
        User user = mappingUtils.mapToEntity(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setKeycloakUserId(keycloakUserId);
        User savedUser = userRepository.save(user);

        logger.info(USER_REGISTERED_SUCCESS_LOG, userDto.getEmail());
        return mappingUtils.mapToDto(savedUser, UserDto.class);
    }

    @Override
    public TokenResponse loginUserAndReturnToken(LoginCredentials loginCredentials) {
        logger.info(LOG_MSG_AUTHENTICATING_USER, loginCredentials.getEmail());

        MultiValueMap<String, String> body = userUtils.createLoginRequestBody(loginCredentials);
        HttpHeaders headers = userUtils.createFormUrlEncodedHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<HashMap<String, Object>> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
                    });

            return Optional.ofNullable(response.getBody())
                    .map(userUtils::buildTokenResponse)
                    .orElseThrow(() -> {
                        logger.error(LOG_MSG_FAILED_TO_RETRIEVE_TOKEN, loginCredentials.getEmail());
                        return new RuntimeException(ERROR_MSG_FAILED_TO_RETRIEVE_TOKEN);
                    });

        } catch (Exception ex) {
            logger.error(LOG_MSG_USER_AUTHENTICATION_FAILED, loginCredentials.getEmail(), ex.getMessage(), ex);
            throw new RuntimeException(ERROR_MSG_AUTHENTICATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<String> logoutUser(String refreshToken) {
        MultiValueMap<String, String> body = userUtils.createLogoutRequestBody(refreshToken);
        HttpHeaders headers = userUtils.createFormUrlEncodedHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    logoutUrl, HttpMethod.POST, request, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info(LOG_MSG_USER_LOGGED_OUT_SUCCESS, refreshToken);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn(LOG_MSG_UNEXPECTED_LOGOUT_RESPONSE, refreshToken, response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(ERROR_MSG_UNEXPECTED_RESPONSE + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            String errorDescription = userUtils.extractErrorDescription(e.getResponseBodyAsString());
            logger.error(LOG_MSG_LOGOUT_ERROR, refreshToken, errorDescription);
            return ResponseEntity.status(e.getStatusCode()).body(errorDescription);
        } catch (Exception e) {
            logger.error(LOG_MSG_UNEXPECTED_LOGOUT_ERROR, refreshToken, e.getMessage(), e);
            return ResponseEntity.status(500).body(ERROR_MSG_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
        }
    }

    @Override
    public TokenResponse refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = userUtils.createRefreshTokenRequestBody(refreshToken);
        HttpHeaders headers = userUtils.createFormUrlEncodedHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<HashMap<String, Object>> response = restTemplate.exchange(
                    tokenUrl, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
                    });

            return Optional.ofNullable(response.getBody())
                    .map(userUtils::buildTokenResponse)
                    .orElseThrow(() -> {
                        logger.error(LOG_MSG_FAILED_TO_RETRIEVE_TOKEN_FROM_REFRESH_TOKEN, refreshToken);
                        return new RuntimeException(ERROR_MSG_FAILED_TO_RETRIEVE_TOKEN);
                    });

        } catch (HttpClientErrorException ex) {
            logger.error(LOG_MSG_ERROR_GENERATING_ACCESS_TOKEN, refreshToken, ex.getMessage(), ex);
            throw new JwtTokenInvalidException(userUtils.extractErrorDescription(ex.getResponseBodyAsString()));
        }
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        logger.info(LogMessages.ENTITY_UPDATE_START, UserDto.class.getSimpleName(), id);
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error(LogMessages.ENTITY_FETCH_ERROR, UserDto.class.getSimpleName(), id);
                        return new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, User.class.getSimpleName(), id));
                    });

            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setAge(userDto.getAge());
            existingUser.setGender(userDto.getGender());
            existingUser.setAddress(userDto.getAddress());
            existingUser.setCity(userDto.getCity());

            User updatedUser = userRepository.save(existingUser);

            UserRepresentation keycloakUser = keycloakAdminService.getUserById(updatedUser.getKeycloakUserId());
            keycloakUser.setFirstName(userDto.getFirstName());
            keycloakUser.setLastName(userDto.getLastName());

            keycloakAdminService.updateUser(updatedUser.getKeycloakUserId(), keycloakUser);

            logger.info(LogMessages.ENTITY_UPDATE_SUCCESS, UserDto.class.getSimpleName(), id);
            return mappingUtils.mapToDto(updatedUser, UserDto.class);

        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_UPDATE_ERROR, UserDto.class.getSimpleName(), id, e.getMessage(), e);
            throw new EntityUpdateException(String.format(ErrorMessages.ENTITY_UPDATE_FAILED, UserDto.class.getSimpleName(), id), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info(LogMessages.ENTITY_DELETE_START, UserDto.class.getSimpleName(), id);

        try {
            int updateCount = userRepository.setStatusWhereId(id, false);

            if (updateCount == 0) {
                logger.error(LogMessages.ENTITY_FETCH_ERROR, UserDto.class.getSimpleName(), id);
                throw new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, UserDto.class.getSimpleName(), id));
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND_AT_ID, User.class.getSimpleName(), id)));

            userUtils.enableOrDisableUser(realm, user.getKeycloakUserId(), false);
            logger.info(LogMessages.ENTITY_DELETE_SUCCESS, UserDto.class.getSimpleName(), id);

        } catch (Exception e) {
            logger.error(LogMessages.ENTITY_DELETE_ERROR, UserDto.class.getSimpleName(), id, e.getMessage(), e);
            throw new EntityDeleteException(String.format(ErrorMessages.ENTITY_DELETE_FAILED, UserDto.class.getSimpleName(), id), e);
        }
    }

    private Response createUserInKeycloak(UserDto userDto) {
        try (Response response = keycloakAdminService.addUser(
                realm,
                userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPassword())) {

            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                logger.error(KEYCLOAK_USER_CREATION_FAILED, response.getStatus());
                throw new EntitySaveException(FAILED_TO_CREATE_USER_IN_KEYCLOAK_STATUS_LOG + response.getStatus());
            }

            return response;

        } catch (Exception e) {
            logger.error(KEYCLOAK_USER_CREATION_ERROR, userDto.getEmail(), e);
            throw new EntitySaveException(FAILED_TO_CREATE_USER_IN_KEYCLOAK_LOG, e);
        }
    }
}
