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
import java.util.List;
import java.util.Optional;

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
//            comment.setAdsId(); здесь нужно идти в AdsRepository и полученный Ads прописать в коммент
            Comment savedComment = commentRepository.save(comment);
            log.info("new comment saved to DB! Id: " + savedComment.getId() + ", author: " + savedComment.getAuthor());
            return ResponseEntity.ok(adsCommentDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }

    /**
     * получаем все комментарии у заданного объявления
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllComments(Integer adsPk) {
        List<Comment> commentList = commentRepository.findAllByAdsId(adsPk.longValue());
        if (!commentList.isEmpty()) {
            List<AdsCommentDto> adsCommentDtoList = commentMapper.listCommentsToListAdsCommentDto(commentList);

            ResponseWrapperAdsCommentDto responseWrapperAdsCommentDto = new ResponseWrapperAdsCommentDto();

            responseWrapperAdsCommentDto.setCount(adsCommentDtoList.size());
            responseWrapperAdsCommentDto.setResult(adsCommentDtoList);

            return ResponseEntity.ok(responseWrapperAdsCommentDto);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<AdsCommentDto> getAdsComment(Integer adsPk, Integer id) {
        Optional<Comment> optionalComment = commentRepository.findById(id.longValue());
        if (optionalComment.isPresent()) {
            AdsCommentDto adsCommentDto = commentMapper.commentToAdsCommentDto(optionalComment.get());
            return ResponseEntity.ok(adsCommentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
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
