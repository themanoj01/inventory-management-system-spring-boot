package com.internship.inventory.inventory_management_system.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class RegisterDto {
    private String name;
    private String email;
    private String username;
    private String password;
    private Set<String> roles;
}
