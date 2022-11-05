package ru.skypro.homework.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.constant.ConstantForTests.*;

class UserMapperTest {


    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    void setUp() {
        USER_1.setId(1L);
        USER_1.setFirstName(FIRST_NAME);
        USER_1.setLastName(LAST_NAME);
        USER_1.setEmail(EMAIL);
        USER_1.setPhone(PHONE);
        USER_1.setPassword(PASSWORD);
        USER_1.setRole(ROLE.name());

        USER_2.setId(2L);

        USER_DTO_1.setId(2);
        USER_DTO_1.setFirstName(FIRST_NAME);
        USER_DTO_1.setLastName(LAST_NAME);
        USER_DTO_1.setPhone(PHONE);
        USER_DTO_1.setEmail(EMAIL);

        REGISTER_REQ_DTO.setFirstName(FIRST_NAME);
        REGISTER_REQ_DTO.setLastName(LAST_NAME);
        REGISTER_REQ_DTO.setUsername(EMAIL);
        REGISTER_REQ_DTO.setPhone(PHONE);
        REGISTER_REQ_DTO.setPassword(PASSWORD);
        REGISTER_REQ_DTO.setRole(ROLE);


    }

    @Test
    void userToUserDto() {
        UserDto userDto = mapper.userToUserDto(USER_1);
        assertEquals(1, userDto.getId());
        assertEquals(FIRST_NAME, userDto.getFirstName());
        assertEquals(LAST_NAME, userDto.getLastName());
        assertEquals(PHONE, userDto.getPhone());
        assertEquals(EMAIL, userDto.getEmail());
    }

    @Test
    void userDtoToUser() {
        User user = mapper.userDtoToUser(USER_DTO_1);

        assertEquals(2L, user.getId());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(PHONE, user.getPhone());
        assertEquals(EMAIL, user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void registerReqDtoToUser() {
        User user = mapper.registerReqDtoToUser(REGISTER_REQ_DTO);

        assertNull(user.getId());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(PHONE, user.getPhone());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(ROLE.name(), user.getRole());
    }

    @Test
    void listUsersToListUserDto() {
        LIST_USER.add(USER_1);
        LIST_USER.add(USER_2);

        List<UserDto> result = mapper.listUsersToListUserDto(LIST_USER);

        assertEquals(UserDto.class, result.get(0).getClass());
        assertEquals(2, result.size());
    }

    @Test
    void updateUserFromUserDto() {
        USER_DTO_2.setFirstName("test");
        mapper.updateUserFromUserDto(USER_DTO_2, USER_1);

        assertEquals(1L, USER_1.getId());
        assertEquals("test", USER_1.getFirstName());// обновляли только это поле
        assertEquals(LAST_NAME, USER_1.getLastName());
        assertEquals(PHONE, USER_1.getPhone());
        assertEquals(EMAIL, USER_1.getEmail());
        assertEquals(PASSWORD, USER_1.getPassword());
        assertEquals(ROLE.name(), USER_1.getRole());

    }

    @Test
    void updatePassword() {
        NEW_PASSWORD_DTO.setCurrentPassword(PASSWORD);
        NEW_PASSWORD_DTO.setNewPassword("123456");
        assertEquals(PASSWORD,USER_1.getPassword());

        mapper.updatePassword(NEW_PASSWORD_DTO,USER_1);

        assertEquals(1L, USER_1.getId());
        assertEquals(FIRST_NAME, USER_1.getFirstName());
        assertEquals(LAST_NAME, USER_1.getLastName());
        assertEquals(PHONE, USER_1.getPhone());
        assertEquals(EMAIL, USER_1.getEmail());
        assertEquals("123456", USER_1.getPassword());// обновляли только это поле
        assertEquals(ROLE.name(), USER_1.getRole());

    }
}