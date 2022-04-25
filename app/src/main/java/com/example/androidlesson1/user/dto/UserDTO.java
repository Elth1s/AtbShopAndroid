package com.example.androidlesson1.user.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String email;
    private String firstName;
    private String secondName;
    private String photo;
    private String phone;
}
