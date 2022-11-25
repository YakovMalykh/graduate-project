package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.UserRepository;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CommentMapper {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AdsRepository adsRepository;

    @Mapping(target = "pk", source = "id")
    public abstract AdsCommentDto commentToAdsCommentDto(Comment comment);

    public Integer userToInteger(User author) {
        return Math.toIntExact(author.getId());
    }

    @Mapping(target = "adsId", source = "pk")
    public abstract Comment adsCommentDtoToComment(AdsCommentDto adsCommentDto);

    public User integerToUser(Integer authorId) {
        // не знаю стоит ли проверять на Null возвращаемое значение, т.к. по логике не могу
        // представить ситуации, когда у объявления нет автора и его нет в БД
        return userRepository.findById(authorId.longValue()).get();
    }

    public Ads inregerToAds(Integer adsPk) {
        return adsRepository.findById(adsPk.longValue()).get();
    }

    public abstract List<AdsCommentDto> listCommentsToListAdsCommentDto(List<Comment> commentsList);

    @Mapping(target = "adsId", source = "pk")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateCommentFromAdsCommentDto(AdsCommentDto adsCommentDto, @MappingTarget Comment comment);

}
