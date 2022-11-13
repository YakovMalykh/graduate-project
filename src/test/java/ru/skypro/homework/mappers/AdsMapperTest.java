package ru.skypro.homework.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.skypro.homework.constant.ConstantForTests.*;
import static ru.skypro.homework.constant.ConstantForTests.LIST_COMMENTS;

@ExtendWith(MockitoExtension.class)
public class AdsMapperTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ImageRepository imageRepository;
    @Mock
    AdsRepository adsRepository;
    @InjectMocks
    AdsMapper mapper = Mappers.getMapper(AdsMapper.class);

    @BeforeEach
    void setUp() {
        AUTHOR_1.setId(1L);

        AUTHOR_2.setId(2L);
        AUTHOR_2.setEmail(EMAIL);
        AUTHOR_2.setPhone(PHONE);
        AUTHOR_2.setLastName(LAST_NAME);
        AUTHOR_2.setFirstName(FIRST_NAME);


        IMAGE.setFilePath(IMAGE_STR);
        LIST_IMAGES.add(IMAGE);

        // CREATE_ADS_DTO.setPk(1);
        //CREATE_ADS_DTO.setImage(IMAGE_STR);
        CREATE_ADS_DTO.setTitle(TITLE);
        CREATE_ADS_DTO.setDescription(DESCRIPTION);
        CREATE_ADS_DTO.setPrice(PRICE);

        ADS_DTO.setPk(1);
        ADS_DTO.setAuthor(2);
        ADS_DTO.setImage(IMAGE_STR);
        ADS_DTO.setTitle(TITLE);
        ADS_DTO.setPrice(PRICE);

        TEST_ADS_1.setId(1l);
        TEST_ADS_1.setAuthor(AUTHOR_2);
        TEST_ADS_1.setImages(LIST_IMAGES);
        TEST_ADS_1.setTitle(TITLE);
        TEST_ADS_1.setDescription(DESCRIPTION_2);
        TEST_ADS_1.setPrice(PRICE);

        TEST_ADS_2.setId(2L);
        TEST_ADS_2.setAuthor(AUTHOR_2);
        TEST_ADS_2.setImages(LIST_IMAGES);
        TEST_ADS_2.setTitle(TITLE_2);
        TEST_ADS_2.setDescription(DESCRIPTION_2);
        TEST_ADS_2.setPrice(PRICE_2);

        LIST_ADS.add(TEST_ADS_1);
        LIST_ADS.add(TEST_ADS_2);
        LIST_COMMENTS.add(TEST_COMMENT_1);
        LIST_COMMENTS.add(TEST_COMMENT_2);
    }

    @Test
    void imageToString_whenSuccess() {
        String result = mapper.imageToString(LIST_IMAGES);
        assertEquals(IMAGE_STR, result);
    }

    @Test
    void stringToImage_whenSuccess() {
        when(imageRepository.findImageByFilePath(anyString())).thenReturn(IMAGE);
        List<Image> result = mapper.stringToImage(IMAGE_STR);
        assertEquals(IMAGE_STR, result.get(0).getFilePath());
    }

    @Test
    void adsToAdsDto_whenMaps_thenCorrect() {
        //   when(imageRepository.findImageByFilePath(anyString())).thenReturn(IMAGE);

        AdsDto adsDto = mapper.adsToAdsDto(TEST_ADS_2);
        assertEquals(2, adsDto.getPk());
        assertEquals(2, adsDto.getAuthor());
        assertEquals(PRICE_2, adsDto.getPrice());
        assertEquals(TITLE_2, adsDto.getTitle());
        assertEquals(IMAGE_STR, adsDto.getImage());
    }

    @Test
    void adsDtoToAds_whenMaps_thenCorrect() {
        when(imageRepository.findImageByFilePath(anyString())).thenReturn(IMAGE);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(AUTHOR_2));

        Ads ads = mapper.adsDtoToAds(ADS_DTO);

        assertEquals(1, ads.getId());
        assertEquals(AUTHOR_2, ads.getAuthor());
        assertEquals(LIST_IMAGES, ads.getImages());
        assertEquals(TITLE, ads.getTitle());
        assertEquals(PRICE, ads.getPrice());
    }

    @Test
    void createdAdsDtoToAds_whenMaps_thenCorrect() {

        Ads ads = mapper.createAdsDtoToAds(CREATE_ADS_DTO);
        assertEquals(TITLE, ads.getTitle());
        assertEquals(PRICE, ads.getPrice());
        assertEquals(DESCRIPTION, ads.getDescription());
    }

    @Test
    void adsToFullAdsDto_whenMaps_thenCorrect() {
        FullAdsDto fullAdsDto = mapper.adsToFullAdsDto(TEST_ADS_2, AUTHOR_2);
        assertEquals(2, fullAdsDto.getPk());
        assertEquals(DESCRIPTION_2, fullAdsDto.getDescription());
        assertEquals(PRICE_2, fullAdsDto.getPrice());
        assertEquals(TITLE_2, fullAdsDto.getTitle());
        assertEquals(AUTHOR_2.getLastName(), fullAdsDto.getAuthorLastName());
        assertEquals(IMAGE_STR, fullAdsDto.getImage());
    }

    @Test
    void listAdsToListAdsDto() {
        List<AdsDto> result = mapper.listAdsToListAdsDto(LIST_ADS);
        assertEquals(ADS_DTO, result.get(0));
     }
  /*  @Test
    void updateAdsFromAdsDto() {
        ADS_DTO.setImage(null);
        ADS_DTO.setPrice(null);

   //     when(userRepository.findById(anyLong())).thenReturn(Optional.of(AUTHOR_2));
   //     when(imageRepository.findImageByFilePath(anyString())).thenReturn((IMAGE));

        mapper.updateAdsFromAdsDto(ADS_DTO, TEST_ADS_2);

     //   assertEquals(AUTHOR_2, TEST_ADS_2.getAuthor());
     //   assertEquals(1, TEST_ADS_2.getId());
        assertEquals(ADS_DTO.getImage(), TEST_ADS_2.getImages());//не меняли поле
        assertEquals(TITLE, TEST_ADS_2.getTitle());
        assertEquals(PRICE_2, TEST_ADS_2.getPrice());//не меняли поле
    }
*/
}
