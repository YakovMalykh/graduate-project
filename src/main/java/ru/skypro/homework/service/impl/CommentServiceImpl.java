package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.CommentService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, AdsRepository adsRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<AdsCommentDto> addCommentToDb(Integer adsPk, AdsCommentDto commentDto, String authorUsername) {
        Optional<Ads> adsOptional = adsRepository.findById(adsPk.longValue());
        Optional<User> optionalUser = userRepository.getUserByEmailIgnoreCase(authorUsername);

        if (adsOptional.isPresent() && optionalUser.isPresent()) {
            User author = optionalUser.get();
            Ads ad = adsOptional.get();
            AdsCommentDto adsCommentDto = fillingFieldsOfAdsCommentDto(commentDto, ad, author);
            Comment comment = commentMapper.adsCommentDtoToComment(adsCommentDto);
            Comment savedComment = commentRepository.save(comment);
            log.info("new comment saved to DB! Id: " + savedComment.getId() + ", author: " + savedComment.getAuthor());
            return ResponseEntity.ok(adsCommentDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }

    private AdsCommentDto fillingFieldsOfAdsCommentDto(AdsCommentDto adsCommentDto, Ads ad, User author) {
        adsCommentDto.setPk(ad.getId().intValue());
        adsCommentDto.setAuthor(author.getId().intValue());
        adsCommentDto.setText(adsCommentDto.getText());
        adsCommentDto.setCreatedAt(LocalDateTime.now());

        log.info("adsCommentDto has been filled ");
        return adsCommentDto;
    }

    /**
     * получаем все комментарии у заданного объявления
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllComments(Integer adsPk) {
        Optional<Ads> adsOptional = adsRepository.findById(adsPk.longValue());
        if (adsOptional.isPresent()) {
            List<Comment> commentList = commentRepository.findAllByAdsId(adsOptional.get());
            if (!commentList.isEmpty()) {
                List<AdsCommentDto> adsCommentDtoList = commentMapper.listCommentsToListAdsCommentDto(commentList);

                ResponseWrapperAdsCommentDto responseWrapperAdsCommentDto = new ResponseWrapperAdsCommentDto();

                responseWrapperAdsCommentDto.setCount(adsCommentDtoList.size());
                responseWrapperAdsCommentDto.setResults(adsCommentDtoList);
                log.info("list of comments had been converted into ResponseWrapperAdsCommentDTO");
                return ResponseEntity.ok(responseWrapperAdsCommentDto);
            }
        }
        log.info("Any comment doesn't exist");
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<AdsCommentDto> getAdsComment(Integer adsPk, Integer id) {
        Optional<Comment> optionalComment = commentRepository.findById(id.longValue());
        if (optionalComment.isPresent()) {
            AdsCommentDto adsCommentDto = commentMapper.commentToAdsCommentDto(optionalComment.get());
            log.info("comment with id:" + id + " has been retrieved and converted into AdsCommentDto");
            return ResponseEntity.ok(adsCommentDto);
        } else {
            log.info("comment doesn't exist");
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AdsCommentDto> updateAdsComment(Integer adsPk, Integer id, AdsCommentDto adsCommentDto) {

        Optional<Ads> optionalAds = adsRepository.findById(adsPk.longValue());

        Optional<Comment> optionalComment = commentRepository.findById(id.longValue());

        if (optionalAds.isPresent() && optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            commentMapper.updateCommentFromAdsCommentDto(adsCommentDto, comment);
            commentRepository.save(comment);
            log.info("success, comment with id: " + id + "has been updated");
            return ResponseEntity.ok(adsCommentDto);
        } else {
            log.info("Ads or Comment doesn't exists");
            return ResponseEntity.status(204).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteAdsComment(Integer adsPk, Integer id) {
        Optional<Comment> commentOptional = commentRepository.findById(id.longValue());
        if (commentOptional.isPresent()) {
            log.info("comment id: " + id + "has been deleted");
            commentRepository.delete(commentOptional.get());
        } else {
            log.info("comment doesn't exist");
            return ResponseEntity.status(204).build();// if comment has been deleted already;}
        }
        return null;
    }
}
