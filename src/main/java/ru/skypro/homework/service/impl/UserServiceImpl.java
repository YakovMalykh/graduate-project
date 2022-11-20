package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.encoder = encoder;
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
            responseWrapperUserDto.setResult(listUserDto);
            log.info("конвертировали в responseWrapperUserDto и отправляем");
            return ResponseEntity.ok(responseWrapperUserDto);
        } else {
            log.info("юзеров не найдено");
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<UserDto> updateUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userDto.getId().longValue());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userMapper.updateUserFromUserDto(userDto, user);
            userRepository.save(user);
            log.info("fields of user with id: " + user.getId() + " updated");
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(204).build();
        }
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
        //мне кажется здесь не нужен маппер
      //  userMapper.updatePassword(passwordDto, user);
        userRepository.save(user);
        log.info("Пароль текущего пользователя обновлен");
        //а мы должны открыто возвращать пароль или закодировано?
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