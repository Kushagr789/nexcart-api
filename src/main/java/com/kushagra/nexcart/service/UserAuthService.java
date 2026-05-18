package com.kushagra.nexcart.service;

import com.kushagra.nexcart.dto.request.LoginUserRequest;
import com.kushagra.nexcart.dto.request.RegisterRequest;
import com.kushagra.nexcart.dto.response.UserLoginResponse;
import com.kushagra.nexcart.dto.response.UserRegisterResponse;
import com.kushagra.nexcart.entity.Role;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.RoleName;
import com.kushagra.nexcart.exception.BadRequestException;
import com.kushagra.nexcart.repository.RoleRepository;
import com.kushagra.nexcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserAuthService(
            UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            RoleRepository roleRepository, JwtService jwtService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public UserRegisterResponse register(RegisterRequest request) {

        if (request.getRole() == RoleName.ROLE_ADMIN) {
            throw new RuntimeException("Admin registration is not allowed");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already already exists"
            );
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRoles(Set.of(role));

        User savedUser =userRepository.save(user);

        UserRegisterResponse response =
                new UserRegisterResponse();
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setPhoneNumber(savedUser.getPhoneNumber());

        return response;
    }

    public UserLoginResponse authenticate(LoginUserRequest request) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

            User authenticatedUser =
                    (User) authentication.getPrincipal();

            String jwtToken =
                    jwtService.generateToken(authenticatedUser);

            UserLoginResponse response =
                    new UserLoginResponse();

            response.setToken(jwtToken);

            response.setExpiresIn(
                    jwtService.getExpirationTime()
            );

            return response;

        } catch (BadCredentialsException ex) {

            throw new BadRequestException(
                    "Invalid email or password"
            );
        }

    }
}
