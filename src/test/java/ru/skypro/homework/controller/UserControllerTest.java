package ru.skypro.homework.controller;

import org.apache.catalina.User;
import org.apache.catalina.security.SecurityConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.constant.ConstantForTests.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    @WithMockUser
    void getUsersMe() throws Exception {
//        doReturn(Optional.of(USER_1)).when(userRepository).getUserByEmailIgnoreCase(anyString());
        when(userRepository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(USER_1));
//        doNothing().when(userRepository.getUserByEmailIgnoreCase());
        doReturn(USER_DTO_1).when(userMapper).userToUserDto(USER_1);
        doReturn(ResponseEntity.ok(USER_DTO_1)).when(userService).getUsersMe(AUTHENTICATION);

        mockMvc.perform(MockMvcRequestBuilders
                                .get("/users/me")
                                .with(csrf())
//                                .content(AUTHENTICATION.toString())
                )
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser
    void getUsersMeUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateUserImage() {
    }

    @Test
    void getUserImage() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void getUser() {
    }
}