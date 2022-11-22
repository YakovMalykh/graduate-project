package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.User;

import java.util.Optional;

public interface UserService {

    boolean createUser(RegisterReqDto registerReqDto);

    ResponseEntity<ResponseWrapperUserDto> getUsers();

    ResponseEntity<UserDto> updateUser(CreateUserDto createUserDto, Authentication auth);

    /**
     * метод не дописан
     */
    ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto currentPassword, Authentication auth);

    ResponseEntity<UserDto> getUser(Integer id);

    Optional<User> userExists(String username);

    ResponseEntity<UserDto> getUsersMe(Authentication auth);
}