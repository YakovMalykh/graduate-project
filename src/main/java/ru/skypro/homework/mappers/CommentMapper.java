package ru.skypro.homework.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;

import java.util.Collection;

@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    AdsCommentDto commentToAdsCommentDto(Comment comment);
//    default Integer adsToInteger(Ads adsId) {
//        return Math.toIntExact(adsId.getId());
//    }
    default Integer userToInteger(User author) {
        return Math.toIntExact(author.getId());
    }

    @Mapping(target = "id",source = "pk")
    Comment adsCommentDtoToComment(AdsCommentDto adsCommentDto);
    default User integerToUser(Integer authorId) {
        // иду в UserRepository и по authorId достаю нужного User
        return new User();
    }

//    default Ads intagerToAds(Integer adsId) {
//        // здесь нужно идти в репозиторий AdsRepository и по adsId получекнному
//        // из контроллера доставвать Ads
//        return new Ads();
//    }


}
