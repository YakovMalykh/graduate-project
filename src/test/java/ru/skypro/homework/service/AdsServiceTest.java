package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.service.impl.AdsServiceImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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

        TEST_ADS_1.setId(1L);
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

        LIST_ADS_DTO.add(ADS_DTO);
        LIST_ADS.add(TEST_ADS_1);
        LIST_ADS.add(TEST_ADS_2);
    }

    @Test
    void getAllAds_whenAdsExists() {
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        when(adsMapper.listAdsToListAdsDto(LIST_ADS)).thenReturn(LIST_ADS_DTO);

        ResponseEntity<ResponseWrapperAdsDto> response = adsService.getAllAds();

        assertEquals(1, response.getBody().getCount());
        assertEquals(LIST_ADS_DTO, response.getBody().getResults());
    }

    /*   @Test
  void addToDbSuccessful() {
        when(adsMapper.createAdsDtoToAds(any(CreateAdsDto.class))).thenReturn(TEST_ADS_2);
        when(adsRepository.save(any(Ads.class))).thenReturn(TEST_ADS_2);

        ResponseEntity<AdsDto> response = adsService.addAdsToDb( CREATE_ADS_DTO, LIST_IMAGES);
        assertNotNull(response);
    }

    /*   @Test
    void addToDbFailed() {
        ResponseEntity<AdsDto> response = adsService.addAdsToDb( null, null);
        assertEquals(ResponseEntity.notFound().build(), response);
    }
*/
}
