package ru.skypro.homework.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class AdsCommentDto {
    private Integer pk;
    private Integer author;
    private LocalDateTime createdAt;
    @Length(min=8, max=200,message = "Комментарий должен быть больше 8 символов и не больше 200, ограничения фронта")
    private String text;
}
