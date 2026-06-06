package com.kushagra.nexcart.service.impl;

import com.kushagra.nexcart.dto.request.LoginUserRequest;
import com.kushagra.nexcart.dto.request.RegisterRequest;
import com.kushagra.nexcart.dto.response.UserLoginResponse;
import com.kushagra.nexcart.dto.response.UserRegisterResponse;
import com.kushagra.nexcart.entity.Role;
import com.kushagra.nexcart.entity.User;
import com.kushagra.nexcart.enums.RoleName;
import com.kushagra.nexcart.exception.BadRequestException;
import com.kushagra.nexcart.exception.ResourceNotFoundException;
import com.kushagra.nexcart.repository.RoleRepository;
import com.kushagra.nexcart.repository.UserRepository;
import com.kushagra.nexcart.service.JwtService;
import com.kushagra.nexcart.service.UserAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl
        implements UserAuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public UserRegisterResponse register(
            RegisterRequest request
    ) {

        if (request.getRole() == RoleName.ROLE_ADMIN) {

            throw new BadRequestException(
                    "Admin registration is not allowed"
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        Role role = roleRepository.findByName(
                request.getRole()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Role not found"
                ));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRoles(Set.of(role));

        User savedUser =
                userRepository.save(user);

        return UserRegisterResponse.builder()
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .build();
    }

    @Override
    public UserLoginResponse authenticate(
            LoginUserRequest request
    ) {

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
                    jwtService.generateToken(
                            authenticatedUser
                    );

            return UserLoginResponse.builder()
                    .token(jwtToken)
                    .expiresIn(
                            jwtService.getExpirationTime()
                    )
                    .build();

        } catch (BadCredentialsException ex) {

            throw new BadRequestException(
                    "Invalid email or password"
            );
        }
    }

    @Override
    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }

    @Override
    public void validateSeller(User user) {

        boolean isSeller = user.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName()
                                == RoleName.ROLE_SELLER
                );

        if (!isSeller) {

            throw new BadRequestException(
                    "Only sellers are allowed"
            );
        }
    }

    @Override
    public void validateAdmin(User user) {

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getName()
                                == RoleName.ROLE_ADMIN
                );

        if (!isAdmin) {

            throw new BadRequestException(
                    "Only admins are allowed"
            );
        }
    }
}