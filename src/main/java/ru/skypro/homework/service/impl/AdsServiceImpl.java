package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final AdsMapper adsMapper;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.adsMapper = adsMapper;
    }


    @Override
    public ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto) {
        if (createAdsDto != null) {
            Ads ads = adsMapper.createAdsDtoToAds(createAdsDto);
        //    User user = new User();
        //    user.setId(3L);
           // ads.setAuthor(user);
            Ads savedAds = adsRepository.save(ads);
            AdsDto adsDto = adsMapper.adsToAdsDto(savedAds);
            log.info("new ad saved to DB! Id: " + ads.getId() + ", author: " + ads.getAuthor());
            return ResponseEntity.ok(adsDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }


    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResult(adsDtoList);
            return ResponseEntity.ok(responseWrapperAdsDto);
        }
        return ResponseEntity.notFound().build();
    }


    @Override
    public ResponseEntity<Void> deleteAds(Long adsPk) {
        if (adsPk != null) {
            adsRepository.deleteById(adsPk);
            log.info(" ad with Id: " + adsPk + " deleted");
        }
        log.info("not found ad");
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<FullAdsDto> getAds(Integer adsPk) {
        Optional<Ads> optionalAds = adsRepository.findById(adsPk.longValue());

        if (optionalAds.isPresent()) {
            Optional<User> optionslUser = userRepository.findById(adsPk.longValue());
            FullAdsDto fullAdsDto = adsMapper.adsToFullAdsDto(optionalAds.get(), optionslUser.get());

            return ResponseEntity.ok(fullAdsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AdsDto> updateAds(Integer adsPk, Ads ads) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Boolean authenticated, String authority, Object credentials, Object details, Object principal) {
        return null;
    }
}