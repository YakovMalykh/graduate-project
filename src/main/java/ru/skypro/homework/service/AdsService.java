package ru.skypro.homework.service;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.User;

public interface AdsService {
    ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAllAds();

    ResponseEntity<Void> deleteAds(Long adsPk);

    ResponseEntity<FullAdsDto> getAds(Integer adsPk);

    ResponseEntity<AdsDto> updateAds(Integer adsPk, AdsDto adsDto);

    ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Boolean authenticated, String authority, Object credentials, Object details, Object principal);
}
