package com.example.androidlesson1.account.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private String name;
    private String surname;
    private String photo;
}
