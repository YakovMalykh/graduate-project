package ru.skypro.homework.interfacies;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "pk", source = "adsId")
    AdsCommentDto commentToAdsCommentDto(Comment comment);
    @Mapping(target = "adsId",source = "pk")
    Comment adsCommentDtoToComment(AdsCommentDto adsCommentDto);

}
