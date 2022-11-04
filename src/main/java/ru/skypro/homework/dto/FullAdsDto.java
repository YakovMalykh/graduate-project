package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class FullAdsDto {
    private Integer pk;
    private String image;
    private String description;
    private Integer price;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String email;
    private String phone;

 }
