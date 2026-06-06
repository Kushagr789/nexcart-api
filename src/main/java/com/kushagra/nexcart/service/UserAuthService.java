package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.LoginUserRequest;
import com.kushagra.nexcart.dto.request.RegisterRequest;
import com.kushagra.nexcart.dto.response.UserLoginResponse;
import com.kushagra.nexcart.dto.response.UserRegisterResponse;
import com.kushagra.nexcart.entity.User;

public interface UserAuthService {

    User getAuthenticatedUser();

    UserRegisterResponse register(
            RegisterRequest request
    );

    UserLoginResponse authenticate(
            LoginUserRequest request
    );

    void validateSeller(User user);

    void validateAdmin(User user);
}