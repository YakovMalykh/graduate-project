package ru.skypro.homework.mappers;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AdsMapper {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ImageRepository imageRepository;
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", source = "images")
    public abstract AdsDto adsToAdsDto(Ads ads);

    public Integer userToInteger(User author) {
        return Math.toIntExact(author.getId());
    }

    public String imageToString (List<Image> images)
    {
        String imageStr=new String();
        if (!images.isEmpty()){
       imageStr= images.get(0).getFilePath();}
        return imageStr;
    }
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "images", source = "image")
    public abstract Ads adsDtoToAds(AdsDto adsDto);
    public List<Image>  stringToImage (String imageStr) { //не понятно, как тут все таки надо возвращать
        Image image= imageRepository.findImageByFilePath(imageStr).orElse(new Image());
        List  <Image> images=new ArrayList<>();
                 images.add(image);
               return images;
    }
    public User integerToUser(Integer authorId) {
          User user = userRepository.findById(authorId.longValue()).get();
        return user;
    }


   // @Mapping(target = "id", source = "pk")
    public abstract Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    @Mapping(target = "pk", source = "ads.id")
    @Mapping(target = "image", source = "ads.images")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")

    public abstract FullAdsDto adsToFullAdsDto(Ads ads, User user, List<Image> images);

  //@Mapping(target = "author", source = "user")
  @Mapping(target = "author", source = "user.id")
  // @Mapping(target = "authorLastName", source = "user.lastName")
    public abstract Ads createAdsDtoUserImageToAds(CreateAdsDto createAdsDto, User user, List<Image> images);

    @Mapping(target = "id", source = "pk")// не знаю нужно ли это в данном случае
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateAdsFromAdsDto(AdsDto adsDto, @MappingTarget Ads ads);


    public abstract List<AdsDto> listAdsToListAdsDto(List<Ads> adsList);

    @Mapping(target = "filePath", expression = "java(file.getResource().getFilename())")
    @Mapping(target = "fileSize", expression = "java((int) (file.getSize()))")
    @Mapping(target = "mediaType", expression = "java(file.getContentType())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prewiew", expression = "java(file.getBytes())")
    public abstract  Image imageToFile (MultipartFile file) throws IOException;
}
