package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundEception extends NotFoundException {
    public UserNotFoundEception (){super("Пользователь не найден");}
}
