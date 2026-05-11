package com.kushagra.nexcart.controller;

import com.kushagra.nexcart.dto.request.LoginUserRequest;
import com.kushagra.nexcart.dto.request.RegisterRequest;
import com.kushagra.nexcart.dto.response.UserLoginResponse;
import com.kushagra.nexcart.dto.response.UserRegisterResponse;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.service.JwtService;
import com.kushagra.nexcart.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class UserAuthController {
    private final JwtService jwtService;

    private final UserAuthService userAuthService;

    public UserAuthController(JwtService jwtService, UserAuthService userAuthService) {
        this.jwtService = jwtService;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        User registeredUser = userAuthService.register(registerRequest);

        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setEmail(registeredUser.getEmail());
        userRegisterResponse.setFirstName(registeredUser.getFirstName());
        userRegisterResponse.setLastName(registeredUser.getLastName());
        userRegisterResponse.setPhoneNumber(registeredUser.getPhoneNumber());
        return new ResponseEntity<>(userRegisterResponse,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(@RequestBody LoginUserRequest loginUserDto) {
        User authenticatedUser = userAuthService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setExpiresIn(jwtService.getExpirationTime());
        userLoginResponse.setToken(jwtToken);
        return ResponseEntity.ok(userLoginResponse);
    }
}
