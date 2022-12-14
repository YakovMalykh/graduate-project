package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.User;
import java.util.Optional;

public interface UserService {
    ResponseEntity<byte[]> getAvatarByUserId(Long id);
    boolean createUser(RegisterReqDto registerReqDto);

    ResponseEntity<ResponseWrapperUserDto> getUsers();


    ResponseEntity<UserDto> updateUser(UserDto createUserDto, Authentication auth);

    /**
     * метод не дописан
     */
    ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto currentPassword, Authentication auth);

    ResponseEntity<UserDto> getUser(Integer id);

    Optional<User> userExists(String username);

    /**
     * метод получения данных обратившегося пользователя
     */
    ResponseEntity<UserDto> getUsersMe(Authentication auth);

    ResponseEntity<byte[]> updateUserImage(MultipartFile avatarFile, Authentication auth) ;
}