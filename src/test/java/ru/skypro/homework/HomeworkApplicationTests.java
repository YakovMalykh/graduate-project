package ru.skypro.homework;

import org.apache.catalina.security.SecurityConfig;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.controller.AdsController;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.UserDetailsServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.constant.ConstantForTests.AUTHOR_1;
import static ru.skypro.homework.constant.ConstantForTests.USER_DETAILS;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
@WebMvcTest
class HomeworkApplicationTests {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }


    @MockBean
    private UserService userService;
    @MockBean
    private AdsService adsService;
    @MockBean
    private ImageService imageService;
    @MockBean
    private AdsController adsController;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @SpyBean
    private AuthServiceImpl authService;

    @InjectMocks
    private AuthController authController;


    @Test
    void contextLoads() {
        Assertions.assertThat(authController).isNotNull();
    }

    @Test
    @WithMockUser
    void loginWhenSuccessful() throws Exception {

        JSONObject loginRegDto = new JSONObject();
        loginRegDto.put("username", "yakov@mail.ru");
        loginRegDto.put("password", "password");

        when(userService.userExists(anyString())).thenReturn(Optional.of(AUTHOR_1));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(USER_DETAILS);
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(authService.login(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
//                SecurityMockMvcRequestBuilders
//                        .formLogin("/login")
                        .post("/login")
                        .with(csrf())
                        .content(loginRegDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    void registerWhenSuccessful() throws Exception {

        JSONObject loginRegDto = new JSONObject();
        loginRegDto.put("username", "yakov@mail.ru");
        loginRegDto.put("password", "password");
        loginRegDto.put("firstName", "firstName");
        loginRegDto.put("phone", "phone");
        loginRegDto.put("role", "USER");

//        when(userService.userExists(anyString())).thenReturn(Optional.empty());
//        when(encoder.encode(anyString())).thenReturn("password");
//        when(userService.createUser(any(RegisterReqDto.class))).thenReturn(true);
//        when(authService.register(any(RegisterReqDto.class), any(Role.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
//                SecurityMockMvcRequestBuilders
//                        .formLogin("/login")
                                .post("/register")
                                .with(csrf())
                                .content(loginRegDto.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

    }

}
