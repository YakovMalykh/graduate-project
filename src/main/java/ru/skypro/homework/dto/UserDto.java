package ru.skypro.homework.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String phone;
    private Integer id;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}" ,
            flags = Pattern.Flag. CASE_INSENSITIVE ,message = "Email должен быть корректным адресом электронной почты")
    private String email;
    private String image;
}
