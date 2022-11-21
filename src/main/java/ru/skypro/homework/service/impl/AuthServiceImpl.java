package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
//    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsService userDetailsService, UserService userService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.encoder = encoder;
    }

//    public AuthServiceImpl(UserDetailsManager manager) {
//        this.manager = manager;
//        this.encoder = new BCryptPasswordEncoder();
//    }

    @Override
    public boolean login(String userName, String password) {
        if (!userService.userExists(userName).isPresent()) {
            log.info("не нашел пользователя");
            return false;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        return encoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (userService.userExists(registerReqDto.getUsername()).isPresent()) {
            return false;
        }
        String encodedPassword = encoder.encode(registerReqDto.getPassword());
        registerReqDto.setPassword(encodedPassword);
        log.info("encoded password set");
        registerReqDto.setRole(role);
        userService.createUser(registerReqDto);
        return true;
    }
}
