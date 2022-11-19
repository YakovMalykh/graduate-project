package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateAdsDto {
    //  private Integer pk;
    @NotNull
    private String description;
    //  private String image;
    @NotNull
    @Positive
    private Integer price;
    @NotBlank
    private String title;
}
