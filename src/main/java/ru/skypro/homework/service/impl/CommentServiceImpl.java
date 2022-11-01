package ru.skypro.homework.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.service.CommentService;

import javax.transaction.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Override
    public ResponseEntity<AdsCommentDto> addCommentToDb(Integer adsPk, AdsCommentDto adsCommentDto) {
        return null;
    }

    /**
     * получаем все комментарии у заданного объявления
     *
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllComments(Integer adsPk) {
        return null;
    }

    @Override
    public ResponseEntity<AdsCommentDto> getAdsComment(Integer adsPk, Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<AdsCommentDto> updateAdsComment(Integer adsPk, Integer id, AdsCommentDto adsCommentDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteAdsComment(Integer adsPk, Integer id) {
        return null;
    }
}
