package com.healthconnect.userservice.controller;

import com.healthconnect.baseservice.controller.GenericController;
import com.healthconnect.commonmodels.dto.UserDto;
import com.healthconnect.userservice.constant.ApiEndpoints;
import com.healthconnect.userservice.dto.LoginCredentials;
import com.healthconnect.userservice.dto.PasswordReset;
import com.healthconnect.userservice.dto.PasswordResetRequest;
import com.healthconnect.userservice.dto.TokenResponse;
import com.healthconnect.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.USERS)
public class UserController extends GenericController<UserDto> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<TokenResponse> loginUserAndReturnAuthenticationToken(@Valid @RequestBody LoginCredentials loginCredentials) {
        TokenResponse tokenResponse = userService.loginUserAndReturnToken(loginCredentials);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(ApiEndpoints.REGISTER)
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto dto) {
        UserDto userDto = userService.registerUser(dto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(ApiEndpoints.REFRESH_TOKEN)
    public ResponseEntity<TokenResponse> getAccessTokenByRefreshToken(@RequestParam String refreshToken) {
        TokenResponse tokenResponse = userService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping(ApiEndpoints.LOGOUT)
    public ResponseEntity<String> logoutUser(@RequestParam String refreshToken) {
        return userService.logoutUser(refreshToken);
    }

    @Override
    @PutMapping(ApiEndpoints.ID)
    public ResponseEntity<UserDto> update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto user = userService.update(userDto,userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(ApiEndpoints.ID)
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(ApiEndpoints.USERS_BY_IDS)
    public ResponseEntity<Map<Long, UserDto>> getAllUsersByIds(@PathVariable(name = "user-ids") List<Long> ids) {
        Map<Long, UserDto> userMap = userService.findAllByIds(ids);
        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }


    @PostMapping(ApiEndpoints.FORGOT_PASSWORD)
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        String responseMessage = userService.requestPasswordReset(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PostMapping(ApiEndpoints.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordReset reset) {
        String responseMessage = userService.resetPassword(reset.getToken(), reset.getNewPassword(), reset.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @Override
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}
