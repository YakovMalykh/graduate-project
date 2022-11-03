package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.service.CommentService;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public ResponseEntity<AdsCommentDto> addCommentToDb(Integer adsPk, AdsCommentDto adsCommentDto) {
        if (adsCommentDto != null) {
            Comment comment = commentMapper.adsCommentDtoToComment(adsCommentDto);
            comment.setId(adsPk.longValue());
            commentRepository.save(comment);

            return null;
        }
        return null;
    }

    /**
     * получаем все комментарии у заданного объявления
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
