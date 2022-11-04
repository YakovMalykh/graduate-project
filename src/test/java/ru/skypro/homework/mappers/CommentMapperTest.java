package ru.skypro.homework.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.models.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.constant.ConstantForTests.*;


class CommentMapperTest {

    //нужно мокать репозиторий
    CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @BeforeEach
    void setUp() {
        AUTHOR_1.setId(1L);
        AUTHOR_2.setId(2L);

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
    void commentToAdsCommentDto_whenMaps_thenCorrect() {

        AdsCommentDto adsCommentDto = mapper.commentToAdsCommentDto(TEST_COMMENT_2);
        assertEquals(2, adsCommentDto.getPk());
        assertEquals(2, adsCommentDto.getAuthor());
        assertEquals(PARSE_DATE, adsCommentDto.getCreatedAt());
        assertEquals("text 2", adsCommentDto.getText());
    }

    @Test
    void adsCommentDtoToComment_whenMaps_thenCorrect() {

        Comment comment = mapper.adsCommentDtoToComment(ADS_COMMENT_DTO);

        assertEquals(1L, comment.getId());
//        assertEquals(1, comment.getAuthor());//сначала нужно прописать логику в маппере по обращению к UserRepository
        assertEquals(PARSE_DATE, comment.getCreatedAt());
        assertEquals("text", comment.getText());

    }

    @Test
    void userToInteger_whenSuccess() {
        AUTHOR_2.setId(2L);
        Integer result = mapper.userToInteger(AUTHOR_2);
        assertEquals(2, result.longValue());
    }

    @Test
    void integerToUser() {
        // прописать когда будет готов UserRepository, нужно мокать
    }

    @Test
    void listCommentsToListAdsCommentDto() {

        List<AdsCommentDto> result = mapper.listCommentsToListAdsCommentDto(LIST_COMMENTS);

        assertEquals(AdsCommentDto.class, result.get(0).getClass());
    }

    @Test
    void updateCommentFromAdsCommentDto() {
        ADS_COMMENT_DTO.setAuthor(null);
        ADS_COMMENT_DTO.setCreatedAt(null);
        ADS_COMMENT_DTO.setText("checking patch method");

        mapper.updateCommentFromAdsCommentDto(ADS_COMMENT_DTO, TEST_COMMENT_2);

        assertEquals(1,TEST_COMMENT_2.getId());
        assertEquals(AUTHOR_2,TEST_COMMENT_2.getAuthor());
        assertEquals(PARSE_DATE, TEST_COMMENT_2.getCreatedAt());
        assertEquals("checking patch method",TEST_COMMENT_2.getText());
    }
}