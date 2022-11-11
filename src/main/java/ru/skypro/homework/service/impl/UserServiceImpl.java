package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.getUserByEmail(username);
        if (!userByEmail.isPresent()) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        User user = userByEmail.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    /**
     * из коллекции ролей получить коллекцию GrantedAuthority
     *
     */
    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        // т.к. у нас только по одной роли у каждого юзера, пока метод выглядит так
        Collection<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole()));
        return roles;
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
