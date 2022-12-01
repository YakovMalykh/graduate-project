package ru.skypro.homework.dto;

import lombok.Data;
import ru.skypro.homework.components.ValidPassword;

@Data
public class NewPasswordDto {
    private String currentPassword;
    @ValidPassword
    private String newPassword;
}
