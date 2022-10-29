package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Empty;
import net.bytebuddy.pool.TypePool;
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

    /*
     * методы по работе с объявленими
     */
    @Operation(summary = "добавляем новое объявление",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = Ads.class))),
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping()
    public ResponseEntity<CreateAds> addAds(
            @Parameter(description = "передаем заполненное объявление") @RequestBody Ads ads
    ) {
        log.info("метод добавления нового объявления");
        return ResponseEntity.ok(new CreateAds());
    }

    @Operation(summary = "получаем список всех объявлений",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAds.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping()
    public ResponseEntity<Collection<ResponseWrapperAds>> getAllAds() {
        log.info("метод получения всех объявлений");
        return ResponseEntity.ok(List.of(new ResponseWrapperAds()));
    }

    @Operation(summary = "получаем объявление (по его ID) ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = FullAds.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id) {
        log.info("метод получения объявления по его id");
        return ResponseEntity.ok(new FullAds());
    }

    @Operation(summary = "получаем объявления обращающегося пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAds.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = Empty.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/me")
    public ResponseEntity<Collection<ResponseWrapperAds>> getAdsMe(
            @Parameter(description = "true/false") @RequestParam(required = false) Boolean authenticated,
            @Parameter(description = "authorities[0].authority") @RequestParam(required = false) String authority,
            @Parameter(description = "credentials") @RequestParam(required = false) Object credentials,
            @Parameter(description = "details") @RequestParam(required = false) Object details,
            @Parameter(description = "principal") @RequestParam(required = false) Object principal
    ) {
        log.info("метод получения всех объявлений данного пользователя");
        return ResponseEntity.ok(List.of(new ResponseWrapperAds()));
    }

    @Operation(summary = "обновляем объявление по его id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = Ads.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Ads> updateAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id,
            @RequestBody Ads ads
    ) {
        log.info("метод обновления объявления");
        return ResponseEntity.ok(new Ads());
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

    /*
     * методы по работе с комментариями к объявленим
     */
    @Operation(summary = "доавляем новый комментарий к обявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsComment.class))),
                    @ApiResponse(responseCode = "201", description = "created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping("/{adsPk}/comment")
    public ResponseEntity<AdsComment> addAdsComment(
            @Parameter(description = "передаем заполненный комментарий")
            @RequestBody AdsComment adsComment,
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk
    ) {
        log.info("метод доавления нового комментария");
        return ResponseEntity.ok(new AdsComment());
    }

    @Operation(summary = "получаем список всех комментариев у данного обяъвления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment")
    public ResponseEntity<Collection<ResponseWrapperAdsComment>> getAdsComments(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk) {
        log.info("метод получения всех комментариев");
        return ResponseEntity.ok(List.of(new ResponseWrapperAdsComment()));
    }

    @Operation(summary = "получаем комментарий (по его ID) у данного обяъвления (по его первичному ключу)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsComment> getAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        log.info("метод получения одного комментария");
        return ResponseEntity.ok(new AdsComment());
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
    public ResponseEntity<AdsComment> updateAdsComment(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody AdsComment adsComment
    ) {
        log.info("метод обновления комментария");
        return ResponseEntity.ok(new AdsComment());
    }
}
