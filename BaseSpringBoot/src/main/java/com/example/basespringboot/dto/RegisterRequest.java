package com.example.basespringboot.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userAdmin;
    private String passwordAdmin;
    private String userName;
    private String password;
    private String role;
}
