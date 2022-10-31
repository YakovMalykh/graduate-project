package ru.skypro.homework.interfacies;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.models.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


class CommentMapperTest {
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String DATE = "10-12-2000 10:11:11";

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
    private final LocalDateTime parse = LocalDateTime.parse(DATE, DateTimeFormatter.ofPattern(DATE_FORMAT));

    @Test
    void commentToAdsCommentDto_whenMaps_thenCorrect() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(1L);
        comment.setAdsId(1L);
        comment.setCreatedAt(parse);
        comment.setText("text");

        AdsCommentDto adsCommentDto = mapper.commentToAdsCommentDto(comment);

        assertEquals(1,adsCommentDto.getAuthor());
        assertEquals(parse,adsCommentDto.getCreatedAt());
        assertEquals("text",adsCommentDto.getText());
    }

    @Test
    void adsCommentDtoToComment_whenMaps_thenCorrect() {
        AdsCommentDto adsCommentDto = new AdsCommentDto();
        adsCommentDto.setCreatedAt(parse);
        adsCommentDto.setText("text");
        adsCommentDto.setAuthor(1);

        Comment comment = mapper.adsCommentDtoToComment(adsCommentDto);

        assertEquals("text",comment.getText());
        assertEquals(parse,comment.getCreatedAt());
        assertEquals(1L,comment.getAuthor());

    }
}