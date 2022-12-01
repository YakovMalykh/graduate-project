package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.exception.UserNotFoundEception;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AvatarRepository avatarRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder encoder, AvatarRepository avatarRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public boolean createUser(RegisterReqDto registerReqDto) {
        User user = userMapper.registerReqDtoToUser(registerReqDto);
        userRepository.save(user);
        log.info("user with username: " + registerReqDto.getUsername() + " is saved");
        return true;
    }


    @Override
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        log.info("получаем всех юзеров из БД");
        List<UserDto> listUserDto = userMapper.listUsersToListUserDto(userList);
        ResponseWrapperUserDto responseWrapperUserDto = new ResponseWrapperUserDto();
        responseWrapperUserDto.setCount(listUserDto.size());
        responseWrapperUserDto.setResults(listUserDto);
        log.info("конвертировали в responseWrapperUserDto и отправляем");
        return ResponseEntity.ok(responseWrapperUserDto);
    }

    /**
     * метод получения данных обратившегося пользователя
     */
    @Override
    public ResponseEntity<UserDto> getUsersMe(Authentication auth) {
        log.info("Сервис получения текущего юзера");
        User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow(() -> new UserNotFoundEception());
        UserDto userDto = userMapper.userToUserDto(user);
        log.info("конвертировали в UserDto и отправляем");
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<byte[]> getAvatarByUserId(Long id) {
        log.info("Сервис получения аватара текущего юзера");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundEception());
        Avatar avatar = avatarRepository.findAvatarByUser(user).orElse(new Avatar()); //bили добавить картинку по умолчанию
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPrewiew().length);
        return ResponseEntity.status(200).headers(headers).body(avatar.getPrewiew());
    }

    @Override
    public ResponseEntity<byte[]> updateUserImage(MultipartFile avatarFile, Authentication auth) {
        log.info("Сервис обновления аватара текущего юзера");
        User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow(() -> new UserNotFoundEception());
        Avatar avatar = avatarRepository.findAvatarByUser(user).orElse(new Avatar());
        avatar.setUser(user);
        if (!avatarFile.isEmpty()) {
            avatar.setMediaType(avatarFile.getContentType());
            try {
                avatar.setPrewiew(avatarFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        avatarRepository.save(avatar);
        return ResponseEntity.ok().body(avatar.getPrewiew());
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UserDto userDto, Authentication auth) {
        log.info("Сервис обновления юзера");
        User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow(() -> new UserNotFoundEception());
        userMapper.updateUserFromUserDto(userDto, user);
        userRepository.save(user);
        log.info("fields of user with id: " + user.getId() + " updated");
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }

    @Override
    public ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto passwordDto, Authentication auth) {
        log.info("Сервис установки пароля");
        User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow(() -> new UserNotFoundEception());
        if (passwordDto.getNewPassword().isEmpty() || !encoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
            log.info("Текущий пароль указан неверно");
            return ResponseEntity.notFound().build();
        }
        user.setPassword(encoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Пароль текущего пользователя обновлен");
        return ResponseEntity.ok(passwordDto);
    }

    @Override
    public ResponseEntity<UserDto> getUser(Integer id) {
        log.info("метод получения пользователя по Id");
        User user = userRepository.findById(id.longValue()).orElseThrow(() -> new UserNotFoundEception());
        UserDto userDto = userMapper.userToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public Optional<User> userExists(String username) {
       return userRepository.getUserByEmailIgnoreCase(username);
    }


}

