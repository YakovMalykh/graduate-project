package ru.skypro.homework.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


class CommentMapperTest {

    //нужно мокать репозиторий
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String DATE = "10-12-2000 10:11:11";
    CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
    private final LocalDateTime parse = LocalDateTime.parse(DATE, DateTimeFormatter.ofPattern(DATE_FORMAT));
    private final Ads ads = new Ads();
    private final User author = new User();

    @Test
    void commentToAdsCommentDto_whenMaps_thenCorrect() {
        author.setId(2L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(author);
        comment.setCreatedAt(parse);
        comment.setText("text");

        AdsCommentDto adsCommentDto = mapper.commentToAdsCommentDto(comment);
        assertEquals(1,adsCommentDto.getPk());
        assertEquals(2,adsCommentDto.getAuthor());
        assertEquals(parse,adsCommentDto.getCreatedAt());
        assertEquals("text",adsCommentDto.getText());
    }

    @Test
    void adsCommentDtoToComment_whenMaps_thenCorrect() {
        AdsCommentDto adsCommentDto = new AdsCommentDto();
        adsCommentDto.setPk(2);
        adsCommentDto.setAuthor(1);
        adsCommentDto.setCreatedAt(parse);
        adsCommentDto.setText("text");

        Comment comment = mapper.adsCommentDtoToComment(adsCommentDto);

        assertEquals(2L,comment.getId());
//        assertEquals(1, comment.getAuthor());//сначала нужно прописать логику в маппере по обращению к нужному репозиторию
        assertEquals(parse, comment.getCreatedAt());
        assertEquals("text",comment.getText());

    }
}