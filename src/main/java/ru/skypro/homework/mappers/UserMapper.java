package ru.skypro.homework.mappers;

import org.mapstruct.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "image",expression = "java(\"/users/me/image/\"+user.getId())")  //+ "java(user.getId)")
    UserDto userToUserDto(User user);

     User userDtoToUser (UserDto userDto);

    @Mapping(target = "email", source = "username")
    User registerReqDtoToUser(RegisterReqDto registerReqDto);

    List<UserDto> listUsersToListUserDto(List<User> usersList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "password",source = "newPassword")
    void updatePassword(NewPasswordDto newPasswordDto, @MappingTarget User user);
}