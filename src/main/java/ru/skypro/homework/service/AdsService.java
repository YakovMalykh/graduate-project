package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Image;

import java.io.IOException;
import java.util.List;

public interface AdsService {
    ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto, MultipartFile images) throws IOException;

    ResponseEntity<ResponseWrapperAdsDto> getAllAds();

    ResponseEntity<Void> deleteAds(Long adsPk);

    ResponseEntity<FullAdsDto> getAds(Integer adsPk);

    ResponseEntity<AdsDto> updateAds(Integer adsPk, AdsDto adsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication auth);

    /**
     * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
     * @param tittle часть заоголовка
     */
    ResponseEntity<ResponseWrapperAdsDto> getAdsByTittle(String tittle);
}
