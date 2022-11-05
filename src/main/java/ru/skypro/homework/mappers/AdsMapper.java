package ru.skypro.homework.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.User;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsMapper {
    @Mapping(target = "pk", source = "id")
    AdsDto adsToAdsDto(Ads ads);

    default Integer userToInteger(User author) {
        return Math.toIntExact(author.getId());
    }

    @Mapping(target = "id", source = "pk")
    Ads adsDtoToAds(AdsDto adsDto);

    default User integerToUser(Integer authorId) {
        // иду в UserRepository и по authorId достаю нужного User
        return new User();
    }

    @Mapping(target = "id", source = "pk")
    Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    @Mapping(target = "pk", source = "ads.id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    FullAdsDto adsToFullAdsDto(Ads ads, User user);


      List<AdsDto> listAdsToListAdsDto(List<Ads> adsList);
}
