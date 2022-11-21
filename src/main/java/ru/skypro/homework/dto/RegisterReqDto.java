package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class RegisterReqDto {
    private String firstName;
    private String lastName;
    private String username;// here email is used
    private String phone;
    private String password;
    private Role role;
}
