package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.List;

public interface AdsService {
    ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto, List<MultipartFile> images,Authentication authentication);

    ResponseEntity<ResponseWrapperAdsDto> getAllAds();

    ResponseEntity<Void> deleteAds(Long adsPk);

    ResponseEntity<FullAdsDto> getAds(Integer adsPk);

    ResponseEntity<AdsDto> updateAds(Integer adsPk, CreateAdsDto createAdsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication auth);

    /**
     * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
     * @param tittle часть заоголовка
     */
    ResponseEntity<ResponseWrapperAdsDto> getAdsByTittle(String tittle);
}
