package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    @Operation(summary = "добавляем новое объявление",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping("/")
    public ResponseEntity<CreateAdsDto> addAds(
            @Parameter(description = "передаем заполненное объявление") @RequestBody AdsDto adsDto
    ) {
        log.info("метод добавления нового объявления");
        return ResponseEntity.ok(new CreateAdsDto());
    }

    @Operation(summary = "получаем список всех объявлений",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/")
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        log.info("метод получения всех объявлений");
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    @Operation(summary = "получаем объявление (по его ID) ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = FullAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id) {
        log.info("метод получения объявления по его id");
        return ResponseEntity.ok(new FullAdsDto());
    }

    @Operation(summary = "получаем объявления обращающегося пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = Empty.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(
            @Parameter(description = "true/false") @RequestParam(required = false) Boolean authenticated,
            @Parameter(description = "authorities[0].authority") @RequestParam(required = false) String authority,
            @Parameter(description = "credentials") @RequestParam(required = false) Object credentials,
            @Parameter(description = "details") @RequestParam(required = false) Object details,
            @Parameter(description = "principal") @RequestParam(required = false) Object principal
    ) {
        log.info("метод получения всех объявлений данного пользователя");
        return ResponseEntity.ok(new ResponseWrapperAdsDto());
    }

    @Operation(summary = "обновляем объявление по его id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id,
            @RequestBody AdsDto adsDto
    ) {
        log.info("метод обновления объявления");
        return ResponseEntity.ok(new AdsDto());
    }

    @Operation(summary = "удаляем объявление (по его ID) ",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id) {
        log.info("метод удаления объявления");
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "доавляем новый комментарий к обявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsCommentDto.class))),
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping("/{adsPk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsComment(
            @Parameter(description = "передаем заполненный комментарий")
            @RequestBody AdsCommentDto adsCommentDto,
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk
    ) {
        log.info("метод доавления нового комментария");
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "получаем список всех комментариев у данного обяъвления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment")
    public ResponseEntity<Collection<ResponseWrapperAdsCommentDto>> getAdsComments(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk) {
        log.info("метод получения всех комментариев");
        return ResponseEntity.ok(List.of(new ResponseWrapperAdsCommentDto()));
    }

    @Operation(summary = "получаем комментарий (по его ID) у данного обяъвления (по его первичному ключу)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        log.info("метод получения одного комментария");
        return ResponseEntity.ok(new AdsCommentDto());
    }

    @Operation(summary = "удаляем комментарий (по его ID) у данного обяъвления (по его первичному ключу)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @DeleteMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        log.info("метод удаления комментария");
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "обновляем существующий комментарий",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        log.info("метод обновления комментария");
        return ResponseEntity.ok(new AdsCommentDto());
    }
}
