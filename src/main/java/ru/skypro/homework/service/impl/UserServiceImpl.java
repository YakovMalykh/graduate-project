package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
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

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            List<UserDto> listUserDto = userMapper.listUsersToListUserDto(userList);
            ResponseWrapperUserDto responseWrapperUserDto = new ResponseWrapperUserDto();
            responseWrapperUserDto.setCount(listUserDto.size());
            responseWrapperUserDto.setResult(listUserDto);
            return ResponseEntity.ok(responseWrapperUserDto);
        } else {
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

    /**
     * метод не дописан
     *
     */
    @Override
    public ResponseEntity<NewPasswordDto> setPassword(NewPasswordDto passwordDto) {
        // нам нужен пользователь, чтобы проверить текущий пароль тому что лежит в БД
        // и если совпадает - обновлять пароль
        Optional<User> optionalUser = userRepository.findById(1L);
        if (true) {
            User user = optionalUser.get();
            userMapper.updatePassword(passwordDto, user);
            userRepository.save(user);
            return ResponseEntity.ok(passwordDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UserDto> getUser(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id.longValue());
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
}
