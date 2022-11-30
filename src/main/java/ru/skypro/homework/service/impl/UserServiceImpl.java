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
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

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
    private final AdsMapper adsMapper;

    private final AvatarRepository avatarRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder encoder, AdsMapper adsMapper, AvatarRepository avatarRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.adsMapper = adsMapper;
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
        if (!userList.isEmpty()) {
            List<UserDto> listUserDto = userMapper.listUsersToListUserDto(userList);
            ResponseWrapperUserDto responseWrapperUserDto = new ResponseWrapperUserDto();
            responseWrapperUserDto.setCount(listUserDto.size());
            responseWrapperUserDto.setResults(listUserDto);
            log.info("конвертировали в responseWrapperUserDto и отправляем");
            return ResponseEntity.ok(responseWrapperUserDto);
        } else {
            log.info("юзеров не найдено");
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<UserDto> getUsersMe(Authentication auth) {
        log.info("Сервис получения текущего юзера");
        Optional<User> optionalUser = Optional.of(userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow());
        if (optionalUser.isEmpty()) {
            log.info("Текущего пользователя не в БД");
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userMapper.userToUserDto(optionalUser.get());
        log.info("конвертировали в UserDto и отправляем");
        return ResponseEntity.ok(userDto);


    }

    @Override
    public ResponseEntity<byte[]> getUsersMeImage(Authentication auth) {
        log.info("Сервис получения аватара текущего юзера");
        log.info(auth.getName());
        Optional<User> optionalUser = userRepository.getUserByEmailIgnoreCase(auth.getName());
        Avatar avatar = avatarRepository.findAvatarByUser(optionalUser.get()).orElse(new Avatar()); //bили добавить картинку по умолчанию
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPrewiew().length);
        return ResponseEntity.status(200).headers(headers).body(avatar.getPrewiew());
    }
    @Override
    public ResponseEntity<byte[]> getUsersIdImage(Long id) {
        log.info("Сервис получения аватара текущего юзера");
        Optional<User> optionalUser = Optional.of(userRepository.findById(id).orElseThrow());
        Avatar avatar = avatarRepository.findAvatarByUser(optionalUser.get()).orElse(new Avatar()); //bили добавить картинку по умолчанию
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPrewiew().length);
        return ResponseEntity.status(200).headers(headers).body(avatar.getPrewiew());
    }
    @Override
    public ResponseEntity<byte[]> updateUserImage(MultipartFile avatarFile, Authentication auth) throws IOException {
        log.info("Сервис обновления аватара текущего юзера");
        Optional<User> optionalUser = userRepository.getUserByEmailIgnoreCase(auth.getName());
        if (optionalUser.isEmpty() || avatarFile.isEmpty()) {
            log.info("Текущего пользователя нет в БД или не передана картинка");
            return ResponseEntity.notFound().build();
        }
        Avatar avatar = avatarRepository.findAvatarByUser(optionalUser.get()).orElse(new Avatar());
        avatar.setUser(optionalUser.get());
        // avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setPrewiew(avatarFile.getBytes());
        avatarRepository.save(avatar);
        return ResponseEntity.ok().body(avatar.getPrewiew());
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UserDto userDto, Authentication auth) {
        log.info("Сервис обновления юзера");
        Optional<User> optionalUser = userRepository.getUserByEmailIgnoreCase(auth.getName());
        if (optionalUser.isEmpty()) {
            log.info("Текущего пользователя не в БД");
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        userMapper.updateUserFromUserDto(userDto, user);
        userRepository.save(user);
        log.info("fields of user with id: " + user.getId() + " updated");
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }

    @Override
    public ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto passwordDto, Authentication auth) {
        log.info("Сервис установки пароля");
        Optional<User> optionalUser = userRepository.getUserByEmailIgnoreCase(auth.getName());
        if (optionalUser.isEmpty()) {
            log.info("Текущего пользователя не в БД");
            return ResponseEntity.notFound().build();
        }
        log.info(passwordDto.getCurrentPassword());
        log.info(optionalUser.get().getPassword());
        if (passwordDto.getNewPassword().isEmpty() || !encoder.matches(passwordDto.getCurrentPassword(), optionalUser.get().getPassword())) {
            log.info("Текущий пароль указан неверно");
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        user.setPassword(encoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Пароль текущего пользователя обновлен");
        return ResponseEntity.ok(passwordDto);
    }

    @Override
    public ResponseEntity<UserDto> getUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id.longValue());
        log.info("получаем юзера из БД");
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = userMapper.userToUserDto(user);
            log.info("retrieved user by id: " + id);
            return ResponseEntity.ok(userDto);
        } else {
            log.info("user with id: " + id + " doesn't exist");
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Optional<User> userExists(String username) {
        return userRepository.getUserByEmailIgnoreCase(username);
    }


}

