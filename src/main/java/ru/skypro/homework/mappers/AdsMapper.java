package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AdsMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "pk", source = "id")
    public abstract AdsDto adsToAdsDto(Ads ads);

    public Integer userToInteger(User author) {
        return Math.toIntExact(author.getId());
    }

    @Mapping(target = "id", source = "pk")
    public abstract Ads adsDtoToAds(AdsDto adsDto);

    public User integerToUser(Integer authorId) {
        // не знаю стоит ли проверять на Null возвращаемое значение, т.к. по логике не могу
        // представить ситуации, когда у объявления нет автора и его нет в БД
        //например сбой программы или там автора забанили или случайно удалили,А обновление осталось, поидеи надо перестраховаться
        // может сделать какого ниь автора Неизвестин и возвращать его в таких случаях?
        User user = userRepository.findById(authorId.longValue()).get();
        return user;
    }

    @Mapping(target = "id", source = "pk")
    public abstract Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    @Mapping(target = "pk", source = "ads.id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    public abstract FullAdsDto adsToFullAdsDto(Ads ads, User user);

    @Mapping(target = "id", source = "pk")// не знаю нужно ли это в данном случае
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateAdsFromAdsDto(AdsDto adsDto, @MappingTarget Ads ads);

    public abstract List<AdsDto> listAdsToListAdsDto(List<Ads> adsList);
}
