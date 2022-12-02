package ru.skypro.homework.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Validated
@Data
public class RegisterReqDto {
    @NotBlank
   private String firstName;
    @NotBlank
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}" ,
            flags = Pattern.Flag. CASE_INSENSITIVE ,message = "Email должен быть корректным адресом электронной почты")
    private String username;// here email is used
    private String phone;
    private String password;
    private Role role;
}
