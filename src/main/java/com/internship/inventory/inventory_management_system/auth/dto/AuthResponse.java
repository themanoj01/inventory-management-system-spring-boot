package com.internship.inventory.inventory_management_system.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthResponse {
    private String token;
    private List<String> roles;
}
