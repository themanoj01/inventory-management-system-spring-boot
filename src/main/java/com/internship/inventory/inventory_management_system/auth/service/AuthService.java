package com.internship.inventory.inventory_management_system.auth.service;

import com.internship.inventory.inventory_management_system.auth.config.TokenProvider;
import com.internship.inventory.inventory_management_system.auth.dto.AuthResponse;
import com.internship.inventory.inventory_management_system.auth.dto.UserLoginDto;
import com.internship.inventory.inventory_management_system.model.User;
import com.internship.inventory.inventory_management_system.model.Role;
import com.internship.inventory.inventory_management_system.repository.RoleRepository;
import com.internship.inventory.inventory_management_system.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, TokenProvider tokenProvider, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }
    public AuthResponse generateToken(UserLoginDto request) {
        if(request.getEmail() == null || request.getPassword() == null){
            throw new BadCredentialsException("Invalid email or password");
        }
        final User user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            throw new BadCredentialsException("User not found");
        }
        final Set<Role> roles = user.getRoles();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = tokenProvider.generateToken(authentication);

        return  AuthResponse.builder()
                .token(token)
                .roles(roles.stream().map(role -> role.getRoleName().toUpperCase())
                        .collect(Collectors.toList()))
                .build();

    }

}

