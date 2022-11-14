package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.models.User;

import java.util.Optional;

public interface UserService extends UserDetailsService{
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean createUser(RegisterReqDto registerReqDto);

    ResponseEntity<ResponseWrapperUserDto> getUsers();

    ResponseEntity<UserDto> updateUser(UserDto userDto);

    /**
     * метод не дописан
     */
    ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto currentPassword);

    ResponseEntity<UserDto> getUser(Integer id);

    Optional<User> userExists(String username);
}
