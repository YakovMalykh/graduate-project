package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.ImageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.io.IOException;



@Slf4j

@RestController
@RequestMapping(value="/ads")
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
    @PostMapping(value = "/", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)//consumes = {"multipart/mixed"},produces="applcation/json" )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("!hasRole('ROLE_ANONYMOUS')")

    // @Parameter(description = "передаем заполненное объявление")@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdsDto> addAds(
            @Valid  @RequestPart("properties") @Parameter(schema=@Schema(type = "string", format="binary"))CreateAdsDto createAdsDto , @RequestPart("image") MultipartFile imageList

    ) {

        log.info("метод добавления нового объявления");
           try {
            return adsService.addAdsToDb(createAdsDto, imageList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PatchMapping(value ="/{adsPk}/images/{id}", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> updateImage(
            @PathVariable Integer adsPk,
            @PathVariable Integer id,
            @RequestBody MultipartFile file
    ) {
        return imageService.updateImage(adsPk.longValue(), id.longValue(), file);
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
            @Parameter(description = "true/false", required = false) @RequestParam(required = false) Boolean authenticated,
            @Parameter(description = "authorities[0].authority", required = false) @RequestParam(required = false) String authority,
            @Parameter(description = "credentials", required = false) @RequestParam(required = false) Object credentials,
            @Parameter(description = "details", required = false) @RequestParam(required = false) Object details,
            @Parameter(description = "principal", required = false) @RequestParam(required = false) Object principal
    ) {

        log.info("метод получения всех объявлений данного пользователя");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         return adsService.getAdsMe(auth);
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
        return adsService.updateAds(id, adsDto);
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
        return adsService.deleteAds(Long.valueOf(id));
    }

    @Operation(summary = "доавляем новый комментарий к обявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = AdsCommentDto.class))),
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

        return commentService.addCommentToDb(adsPk, adsCommentDto);
    }

    @Operation(summary = "получаем список всех комментариев у данного обяъвления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            })
    @GetMapping("/{adsPk}/comment")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAdsComments(
            @Parameter(description = "передаем первичный ключ обявления")
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
    @GetMapping("/{adsPk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "передаем первичный ключ обявления")
            @PathVariable Integer adsPk,
            @Parameter(description = "передаем ID комментария")
            @PathVariable Integer id) {
        return commentService.getAdsComment(adsPk, id);
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
        return commentService.deleteAdsComment(adsPk, id);
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
        return commentService.updateAdsComment(adsPk, id, adsCommentDto);
    }
}
