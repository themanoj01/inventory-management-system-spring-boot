package com.internship.inventory.inventory_management_system.auth.controller;

import com.internship.inventory.inventory_management_system.auth.dto.AuthResponse;
import com.internship.inventory.inventory_management_system.auth.dto.RegisterDto;
import com.internship.inventory.inventory_management_system.auth.dto.UserLoginDto;
import com.internship.inventory.inventory_management_system.auth.service.AuthService;
import com.internship.inventory.inventory_management_system.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDto dto) {
        AuthResponse response = authService.generateToken(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }
}
