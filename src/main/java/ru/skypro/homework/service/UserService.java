package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService extends UserDetailsService {

    ResponseEntity<ResponseWrapperUserDto> getUsers();

    ResponseEntity<UserDto> updateUser(UserDto userDto);

    /**
     * метод не дописан
     */
    ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto currentPassword);

    ResponseEntity<UserDto> getUser(Integer id);
}
