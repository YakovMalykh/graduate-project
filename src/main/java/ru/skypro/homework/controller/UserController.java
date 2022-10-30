package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

       @Operation(
            summary = "выводим всех пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperUserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperUserDto> getUsers() {
        log.info("метод вывода списка всех пользователей");
        return ResponseEntity.ok(new ResponseWrapperUserDto());
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
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        log.info("метод обновления существующего пользователя");
        return ResponseEntity.ok(new UserDto());
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
    public ResponseEntity<NewPasswordDto> setPassword(@RequestBody NewPasswordDto currentPassword) {
        log.info("метод установки пользователю нового пароля");
        return ResponseEntity.ok(new NewPasswordDto());
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
        return ResponseEntity.ok(new UserDto());
    }

}
