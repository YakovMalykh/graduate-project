package ru.skypro.homework.exception;

import org.webjars.NotFoundException;

public class UserNotFoundEception extends NotFoundException {
    public UserNotFoundEception (){super("Пользователь не найден");}
}
