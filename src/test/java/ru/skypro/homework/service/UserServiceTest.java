package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.constant.ConstantForTests.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository repository;
    @Mock
    UserMapper mapper;
    @Mock
    PasswordEncoder encoder;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        USER_1.setId(1L);
        USER_1.setFirstName(FIRST_NAME);
        USER_1.setLastName(LAST_NAME);
        USER_1.setEmail(EMAIL);
        USER_1.setPhone(PHONE);

        USER_DTO_1.setId(1);
        USER_DTO_1.setFirstName("test first name");
        USER_DTO_1.setPhone("+7 222 222 11 00");

        USER_DTO_2.setId(1);
        USER_DTO_2.setFirstName(FIRST_NAME);
        USER_DTO_2.setLastName(LAST_NAME);
        USER_DTO_2.setEmail(EMAIL);
        USER_DTO_2.setPhone(PHONE);

    }
    @Test
    void createUser_whenSuccessful() {
        when(mapper.registerReqDtoToUser(REGISTER_REQ_DTO)).thenReturn(USER_1);
        when(repository.save(any(User.class))).thenReturn(USER_1);

        boolean response = userService.createUser(REGISTER_REQ_DTO);
        assertTrue(response);
    }

    @Test
    void getUsers_whenSuccessful() {
        LIST_USER.add(USER_1);
        LIST_USER.add(USER_2);

        LIST_USER_DTO.add(USER_DTO_1);
        LIST_USER_DTO.add(USER_DTO_2);

        when(repository.findAll()).thenReturn(LIST_USER);
        when(mapper.listUsersToListUserDto(LIST_USER)).thenReturn(LIST_USER_DTO);

        ResponseEntity<ResponseWrapperUserDto> response = userService.getUsers();

        assertEquals(2, response.getBody().getCount());
        assertEquals(USER_DTO_1, response.getBody().getResults().get(0));
    }

    @Test
    void getUsers_whenListUserEmpty() {
        LIST_USER.clear();
        when(repository.findAll()).thenReturn(LIST_USER);

        ResponseEntity<ResponseWrapperUserDto> response = userService.getUsers();

        assertEquals(ResponseEntity.notFound().build(), response);
    }
    @Test
    void getUsersMe_whenSuccessful() {
        when(repository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(USER_1));
        when(mapper.userToUserDto(USER_1)).thenReturn(USER_DTO_2);

        ResponseEntity<UserDto> response = userService.getUsersMe(AUTHENTICATION);

        assertEquals(ResponseEntity.ok(USER_DTO_2),response);
    }
    @Test
    void getUsersMe_whenUserIsNotFoundShouldThrowNoSuchElementExseption() {
        when(repository.getUserByEmailIgnoreCase(anyString())).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, ()->userService.getUsersMe(AUTHENTICATION));
    }

    @Test
    void updateUser() {
        when(repository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(USER_1));
        doNothing().when(mapper).updateUserFromUserDto(any(UserDto.class), any(User.class));

        ResponseEntity<UserDto> response = userService.updateUser(USER_DTO_1, AUTHENTICATION);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void updateUser_whenUserDoesntExists() {
        when(repository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userService.updateUser(USER_DTO_1, AUTHENTICATION);

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void setPassword_whenSuccessful() {
        USER_1.setPassword(ENCODED_PASSWORD);
        NEW_PASSWORD_DTO.setCurrentPassword(PASSWORD);
        NEW_PASSWORD_DTO.setNewPassword(NEW_PASSWORD);

        when(repository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(USER_1));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(encoder.encode(anyString())).thenReturn(ENCODED_NEW_PASSWORD);

        assertEquals(USER_1.getPassword(),ENCODED_PASSWORD);
        ResponseEntity<NewPasswordDto> response = userService.setPassword(NEW_PASSWORD_DTO, AUTHENTICATION);

        assertEquals(USER_1.getPassword(),ENCODED_NEW_PASSWORD);
        assertEquals(ResponseEntity.ok(NEW_PASSWORD_DTO),response);
    }
    @Test
    void setPassword_whenCurrentPasswordDidntMatch() {
        USER_1.setPassword(ENCODED_PASSWORD);
        NEW_PASSWORD_DTO.setCurrentPassword(PASSWORD);
        NEW_PASSWORD_DTO.setNewPassword(NEW_PASSWORD);

        when(repository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(USER_1));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);// текущий пароль не совпал с переданным в DTO-шке

        ResponseEntity<NewPasswordDto> response = userService.setPassword(NEW_PASSWORD_DTO, AUTHENTICATION);

        assertEquals(USER_1.getPassword(),ENCODED_PASSWORD);// текущий пароль остался прежним
        assertEquals(ResponseEntity.notFound().build(),response);
    }

    @Test
    void getUser_successfully() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(USER_1));
        when(mapper.userToUserDto(any(User.class))).thenReturn(USER_DTO_1);

        ResponseEntity<UserDto> response = userService.getUser(1);
        assertEquals(ResponseEntity.ok(USER_DTO_1), response);
    }

    @Test
    void getUser_whenUserDontExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userService.getUser(1);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

}