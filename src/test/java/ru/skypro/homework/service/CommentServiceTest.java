package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.constant.ConstantForTests.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentMapper commentMapper;


    @InjectMocks
    private CommentServiceImpl commentService;


    @BeforeEach
    void setUp() {
        AUTHOR_1.setId(1L);
        AUTHOR_1.setEmail(EMAIL);
        AUTHOR_2.setId(2L);
        ADS.setId(1L);

        TEST_COMMENT_1.setId(1L);
        TEST_COMMENT_1.setAuthor(AUTHOR_1);
        TEST_COMMENT_1.setAdsId(ADS);
        TEST_COMMENT_1.setCreatedAt(PARSE_DATE);
        TEST_COMMENT_1.setText(TEXT);

        TEST_COMMENT_2.setId(2L);
        TEST_COMMENT_2.setAuthor(AUTHOR_2);
        TEST_COMMENT_2.setAdsId(ADS);
        TEST_COMMENT_2.setCreatedAt(PARSE_DATE);
        TEST_COMMENT_2.setText(TEXT_2);

        ADS_COMMENT_DTO.setPk(1);
        ADS_COMMENT_DTO.setAuthor(1);
        ADS_COMMENT_DTO.setCreatedAt(PARSE_DATE);
        ADS_COMMENT_DTO.setText(TEXT);

        LIST_COMMENTS.add(TEST_COMMENT_1);
        LIST_COMMENTS.add(TEST_COMMENT_2);
    }


    @Test
    void addCommentToDbSuccessful() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ADS));
        when(commentMapper.adsCommentDtoToComment(any(AdsCommentDto.class))).thenReturn(TEST_COMMENT_1);
        when(userRepository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(AUTHOR_1));
        when(commentRepository.save(any(Comment.class))).thenReturn(TEST_COMMENT_1);

        ResponseEntity<AdsCommentDto> response = commentService.addCommentToDb(1, ADS_COMMENT_DTO, EMAIL);
        assertNotNull(response);
    }

    @Test
    void addCommentToDbFailed() {
        ResponseEntity<AdsCommentDto> response = commentService.addCommentToDb(1, null, EMAIL);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void addCommentToDbFailedCauseAdsIdIsNull() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<AdsCommentDto> response = commentService.addCommentToDb(1, ADS_COMMENT_DTO, EMAIL);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void getAllComments_whenCommentsExists() {
        LIST_ADS_COMMENT_DTO.add(ADS_COMMENT_DTO);
        when(commentRepository.findAllByAdsId(anyLong())).thenReturn(LIST_COMMENTS);
        when(commentMapper.listCommentsToListAdsCommentDto(LIST_COMMENTS)).thenReturn(LIST_ADS_COMMENT_DTO);

        ResponseEntity<ResponseWrapperAdsCommentDto> response = commentService.getAllComments(1);

        assertEquals(1, response.getBody().getCount());
        assertEquals(ADS_COMMENT_DTO, response.getBody().getResults().get(0));
    }

    @Test
    void getAllComments_whenCommentsListIsEmpty() {
        when(commentRepository.findAllByAdsId(anyLong())).thenReturn(EMPTY_LIST_COMMENTS);

        ResponseEntity<ResponseWrapperAdsCommentDto> response = commentService.getAllComments(1);

        assertEquals(ResponseEntity.notFound().build(), response);
    }


    @Test
    void getAdsComment_ifSuccess() {
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(TEST_COMMENT_1));
        when(commentMapper.commentToAdsCommentDto(any(Comment.class))).thenReturn(ADS_COMMENT_DTO);

        ResponseEntity<AdsCommentDto> response = commentService.getAdsComment(1, 1);
        assertEquals(1, response.getBody().getPk());
        assertEquals(1, response.getBody().getAuthor());
        assertEquals(PARSE_DATE, response.getBody().getCreatedAt());
        assertEquals(TEXT, response.getBody().getText());
    }

    @Test
    void getAdsComment_ifFail() {
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        ResponseEntity<AdsCommentDto> response = commentService.getAdsComment(1, 1);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void updateAdsComment_whenSuccess() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ADS));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(TEST_COMMENT_2));

        ResponseEntity<AdsCommentDto> response = commentService.updateAdsComment(1, 1, ADS_COMMENT_DTO);

        assertEquals(ResponseEntity.ok(ADS_COMMENT_DTO), response);
    }

    @Test
    void updateAdsComment_whenFailed() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(TEST_COMMENT_2));

        ResponseEntity<AdsCommentDto> response = commentService.updateAdsComment(1, 1, ADS_COMMENT_DTO);

        assertEquals(ResponseEntity.status(204).build(), response);
    }

    @Test
    void deleteAdsComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(TEST_COMMENT_1));
        doNothing().when(commentRepository).delete(any(Comment.class));
        commentService.deleteAdsComment(1, 1);
        verify(commentRepository, times(1)).delete(TEST_COMMENT_1);
    }

    @Test
    void deleteAdsComment_whenOptionalCommentIsEmpty() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Void> response = commentService.deleteAdsComment(1, 1);
        assertEquals(ResponseEntity.status(204).build(), response);
    }

}