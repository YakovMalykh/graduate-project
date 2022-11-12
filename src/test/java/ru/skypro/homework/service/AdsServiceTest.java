package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.AdsCommentDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsCommentDto;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.service.impl.AdsServiceImpl;

import javax.validation.constraints.Null;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.skypro.homework.constant.ConstantForTests.*;


@ExtendWith(MockitoExtension.class)
public class AdsServiceTest {
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private AdsMapper adsMapper;

    @InjectMocks
    private AdsServiceImpl adsService;


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

     //   CREATE_ADS_DTO.setPk(1);
     //   CREATE_ADS_DTO.setImage(IMAGE_STR);
        CREATE_ADS_DTO.setTitle(TITLE);
        CREATE_ADS_DTO.setDescription(DESCRIPTION);
        CREATE_ADS_DTO.setPrice(PRICE);

        ADS_DTO.setPk(1);
        ADS_DTO.setAuthor(2);
        ADS_DTO.setImage(IMAGE_STR);
        ADS_DTO.setTitle(TITLE);
        ADS_DTO.setPrice(PRICE);


        TEST_ADS_2.setId(2L);
        TEST_ADS_2.setAuthor(AUTHOR_2);
        TEST_ADS_2.setImages(LIST_IMAGES);
        TEST_ADS_2.setTitle(TITLE_2);
        TEST_ADS_2.setDescription(DESCRIPTION_2);
        TEST_ADS_2.setPrice(PRICE_2);

        LIST_COMMENTS.add(TEST_COMMENT_1);
        LIST_COMMENTS.add(TEST_COMMENT_2);
    }

    @Test
    void addToDbSuccessful() {
        when(adsMapper.createAdsDtoToAds(any(CreateAdsDto.class))).thenReturn(TEST_ADS_2);
        when(adsRepository.save(any(Ads.class))).thenReturn(TEST_ADS_2);
        ResponseEntity<AdsDto> response = adsService.addAdsToDb( CREATE_ADS_DTO, LIST_IMAGES);
        assertNotNull(response);
    }

    @Test
    void addToDbFailed() {
        ResponseEntity<AdsDto> response = adsService.addAdsToDb( null, null);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

}
