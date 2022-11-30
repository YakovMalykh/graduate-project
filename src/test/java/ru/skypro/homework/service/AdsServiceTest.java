package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.impl.AdsServiceImpl;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.constant.ConstantForTests.*;


@ExtendWith(MockitoExtension.class)
public class AdsServiceTest {
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private AdsMapper adsMapper;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

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
        IMAGE.setId(1L);

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

        ADS_DTO_2.setPk(2);
        ADS_DTO_2.setAuthor(2);
        ADS_DTO_2.setImage(IMAGE_2_STR);
        ADS_DTO_2.setPrice(PRICE_2);
        ADS_DTO_2.setTitle(TITLE_2);

        TEST_ADS_1.setId(1L);
        TEST_ADS_1.setAuthor(AUTHOR_2);
        TEST_ADS_1.setTitle(TITLE);
        TEST_ADS_1.setDescription(DESCRIPTION_2);
        TEST_ADS_1.setPrice(PRICE);

        TEST_ADS_2.setId(2L);
        TEST_ADS_2.setAuthor(AUTHOR_2);
        TEST_ADS_2.setTitle(TITLE_2);
        TEST_ADS_2.setDescription(DESCRIPTION_2);
        TEST_ADS_2.setPrice(PRICE_2);
        TEST_ADS_2.setImage(IMAGE_2_STR);

        LIST_ADS_DTO.add(ADS_DTO);
        LIST_ADS.add(TEST_ADS_1);
        LIST_ADS.add(TEST_ADS_2);


        LIST_OF_FILES.add(FILE_1);
        LIST_OF_FILES.add(FILE_2);
    }

    @Test
    void getAllAds_whenAdsExists() {
        when(adsRepository.findAll()).thenReturn(LIST_ADS);
        when(adsMapper.listAdsToListAdsDto(LIST_ADS)).thenReturn(LIST_ADS_DTO);

        ResponseEntity<ResponseWrapperAdsDto> response = adsService.getAllAds();

        assertEquals(2, response.getBody().getCount());
        assertEquals(LIST_ADS_DTO, response.getBody().getResults());
    }

    @Test
    void addToDbSuccessful() {
        when(adsMapper.createAdsDtoToAds(any(CreateAdsDto.class))).thenReturn(TEST_ADS_2);
        when(userRepository.getUserByEmailIgnoreCase(anyString())).thenReturn(Optional.of(AUTHOR_1));
        when(imageRepository.save(any(Image.class))).thenReturn(IMAGE);

        when(adsRepository.save(any(Ads.class))).thenReturn(TEST_ADS_2);
        when(adsMapper.adsToAdsDto(any(Ads.class))).thenReturn(ADS_DTO_2);

        ResponseEntity<AdsDto> response = adsService.addAdsToDb(CREATE_ADS_DTO, LIST_OF_FILES,AUTHENTICATION);

        assertEquals(ResponseEntity.ok(ADS_DTO_2),response);

    }

    /*   @Test
    void addToDbFailed() {
        ResponseEntity<AdsDto> response = adsService.addAdsToDb( null, null);
        assertEquals(ResponseEntity.notFound().build(), response);
    }
*/
}
