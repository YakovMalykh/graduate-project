package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.UserService;
import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

 //   @PreAuthorize("hasAuthority('ADMIN')")
//здесь прописываем авторити вместо ролей, т.к. у насх прописываются авторити у юзера
    // все что без приставки ROLE_ являетися авторити
    @Operation(
            summary = "выводим профиль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperUserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUsersMe(Authentication auth) {
        log.info("метод вывода текущего пользователя");
        return userService.getUsersMe(auth);
    }

    @Operation(
            summary = "обновляем существующего пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, Authentication auth) {
        log.info("метод обновления существующего пользователя");
        return userService.updateUser(userDto, auth);
    }
    @PatchMapping(value ="/me/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateUserImage(@RequestPart ("image") MultipartFile avatarFile, Authentication auth) {
        log.info("метод обновления аватара");
        log.info(auth.getName());
        try {
            return userService.updateUserImage(avatarFile, auth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value ="/me/image", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> getUserImage( Authentication auth ) {
        log.info("метод получения аватара");
             return userService.getUsersMeImage(auth);
    }
    @Operation(
            summary = "устанавливаем пользователю новый пароль",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = NewPasswordDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestBody NewPasswordDto passwordDto, Authentication auth) {
        log.info("метод установки пользователю нового пароля");
        // метод сервиса еще не дописан
        //если мы передаем аутентификацию в контроллер, то она автоматом берется из контекста?
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.setPassword(passwordDto, auth);
    }


    @Operation(
            summary = "получаем пользователя по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        log.info("метод получения пользователя по ID");
        return userService.getUser(id);
    }

}