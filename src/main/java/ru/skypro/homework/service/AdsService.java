package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.*;

public interface AdsService {
    ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAllAds();

    ResponseEntity<Void> deleteAds(Long adsPk);

    ResponseEntity<FullAdsDto> getAds(Integer adsPk);

    ResponseEntity<AdsDto> updateAds(Integer adsPk, AdsDto adsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Boolean authenticated, String authority, Object credentials, Object details, Object principal);

    /**
     * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
     * @param tittle часть заоголовка
     */
    ResponseEntity<ResponseWrapperAdsDto> getAdsByTittle(String tittle);
}
