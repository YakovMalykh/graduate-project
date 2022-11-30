package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CreateAdsDto {
    @NotBlank(message = "Описание не должно быть пустым")
    @Pattern( regexp = ".{8,30}",message = "Описание должно быть больше 8 символов и не больше 30, ограничения фронта")
    private String description;
    @Max(2147483647)
    @NotNull(message = "Цена не должна быть нулевой")
    @Positive(message = "Цена не должна быть отрицательной")
    private Integer price;
    @NotBlank(message = "Титул не должен быть пустым")
    @Pattern( regexp = ".{8,30}",message = "Титул должен быть больше 8 символов и не больше 30, ограничения фронта")
    private String title;
}
