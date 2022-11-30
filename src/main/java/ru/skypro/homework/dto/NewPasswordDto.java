package ru.skypro.homework.dto;

import lombok.Data;
import ru.skypro.homework.components.validation.ValidPassword;

@Data
public class NewPasswordDto {
    private String currentPassword;
    @ValidPassword
    private String newPassword;
}
