package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.LoginUserRequest;
import com.kushagra.nexcart.dto.request.RegisterRequest;
import com.kushagra.nexcart.dto.response.UserLoginResponse;
import com.kushagra.nexcart.dto.response.UserRegisterResponse;
import com.kushagra.nexcart.service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return new ResponseEntity<>(
                userAuthService.register(registerRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @Valid @RequestBody LoginUserRequest loginUserRequest
    ) {
        return ResponseEntity.ok(
                userAuthService.authenticate(loginUserRequest)
        );
    }
}
