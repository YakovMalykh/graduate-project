package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.ImageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ads")
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class AdsController {
    private final AdsService adsService;
    private final CommentService commentService;
    private final ImageService imageService;

    @Operation(summary = "добавляем новое объявление",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PreAuthorize("isAuthenticated()")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AdsDto> addAds(
//         @Valid    @Parameter(schema=@Schema(type = "string", format="binary"))
            @RequestPart("properties") CreateAdsDto createAdsDto, @RequestPart("image") List<MultipartFile> imageList
    ) {
        log.info("метод добавления нового объявления");
        return adsService.addAdsToDb(createAdsDto, imageList);
    }

    @Operation(summary = "получаем список всех объявлений",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = ResponseWrapperAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        log.info("метод получения всех объявлений");
        return adsService.getAllAds();
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
        return adsService.getAds(id);
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
            Authentication authentication
    ) {
        log.info("метод получения всех объявлений данного пользователя");
        return adsService.getAdsMe(authentication);
    }

    // USER может править только свои объявления, ADMIN может править объявления других пользователей

    @PreAuthorize("@adsServiceImpl.getAds(#id).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')")
    @Operation(summary = "обновляем объявление по его id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsDto.class))),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{id}")
//    @Transactional // если поставить здесь эту аннотацию и использовать @PostAuthorize.. => вызывается метод, выполнятеся метод сервиса => в БД вносятся изменения
    // => затем возвращается ответ в контроллер и здесь у нас не проходит проверка прописанная в @PostAuthorize, то в этом случае все изменения внесенные в БД откатятся?
    public ResponseEntity<AdsDto> updateAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id,
            @RequestBody CreateAdsDto createAdsDto
    ) {
        log.info("метод обновления объявления");
        return adsService.updateAds(id, createAdsDto);
    }

    @PreAuthorize("@adsServiceImpl.getAds(#id).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')")
    @Operation(description = "редактирование картинки объявления")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(
            @PathVariable Integer id,
            @RequestPart(value = "image") MultipartFile file
    ) {
        return imageService.updateImage(id.longValue(), file);
    }

    // USER может удалять только свои объявления, ADMIN может удалять объявления других пользователей
    @PreAuthorize("@adsServiceImpl.getAds(#id).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')")
    @Operation(summary = "удаляем объявление (по его ID) ",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAds(
            @Parameter(description = "передаем ID объявления") @PathVariable Integer id
    ) {

        log.info("метод удаления объявления");
        return adsService.deleteAds(Long.valueOf(id));
    }

    @Operation(summary = "добавляем новый комментарий к объявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsCommentDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @PostMapping(value = "/{adsPk}/comments")
    public ResponseEntity<AdsCommentDto> addAdsComment(
            @Parameter(description = "передаем текст комментария")
            @NotNull
            @RequestBody AdsCommentDto adsCommentDto,
            @Parameter(description = "передаем первичный ключ объявления")
            @PathVariable Integer adsPk,
            Authentication authentication
    ) {
        log.info("create comment method");
        String authorUsername = authentication.getName();
        return commentService.addCommentToDb(adsPk, adsCommentDto, authorUsername);
    }

    @Operation(summary = "получаем список всех комментариев у данного обяъвления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comments")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAdsComments(
            @Parameter(description = "передаем первичный ключ объявления")
            @PathVariable Integer adsPk) {
        return commentService.getAllComments(adsPk);
    }

    @Operation(summary = "получаем комментарий (по его ID) у данного обяъвления (по его первичному ключу)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        return commentService.getAdsComment(adsPk, id);
    }

    //    @PreAuthorize("@adsServiceImpl.getAds(#adsPk).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')") - к этому варианту можно вернуться,
    //    когда разберемся с картинку у объявления
    @PreAuthorize("@userServiceImpl.getUser(@commentServiceImpl.getAdsComment(#adsPk,#id).body.author).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')")
    @Operation(summary = "удаляем комментарий (по его ID) у данного обяъвления (по его первичному ключу)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @DeleteMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        return commentService.deleteAdsComment(adsPk, id);
    }

    //    @PreAuthorize("@adsServiceImpl.getAds(#adsPk).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')") - к этому варианту можно вернуться,
    //    когда разберемся с картинку у объявления
    @PreAuthorize("@userServiceImpl.getUser(@commentServiceImpl.getAdsComment(#adsPk,#id).body.author).body.email.equals(authentication.principal.username) or hasAuthority('ADMIN')")
    @Operation(summary = "обновляем существующий комментарий",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PatchMapping("/{adsPk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        return commentService.updateAdsComment(adsPk, id, adsCommentDto);
    }
}
