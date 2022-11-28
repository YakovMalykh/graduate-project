package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;

public interface CommentService {

    ResponseEntity<AdsCommentDto> addCommentToDb(Integer adsPk, AdsCommentDto commentDto, String authorUsername);

    /**
     * получаем все комментарии у заданного объявления
     *
     */
    ResponseEntity<ResponseWrapperAdsCommentDto> getAllComments(Integer adsPk);

    ResponseEntity<AdsCommentDto> getAdsComment(Integer adsPk, Integer id);

    ResponseEntity<AdsCommentDto> updateAdsComment(Integer adsPk, Integer id, AdsCommentDto adsCommentDto);

    ResponseEntity<Void> deleteAdsComment(Integer adsPk, Integer id);
}
