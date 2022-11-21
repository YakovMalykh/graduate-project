package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;

    public ImageServiceImpl(ImageRepository imageRepository, AdsRepository adsRepository, AdsMapper adsMapper) {
        this.imageRepository = imageRepository;
        this.adsRepository = adsRepository;
        this.adsMapper = adsMapper;
    }

    @Override
    public ResponseEntity<Image> updateImage(Long adsId, Long imageId, MultipartFile file) {
        Optional<Ads> optionalAds = adsRepository.findById(adsId);
        if (!optionalAds.isEmpty()) {
            Ads ads = optionalAds.get();
            try {
                Image image = adsMapper.imageToFile(file);
                image.setId(imageId);
                image.setAds(ads);
                ads.getImages().set(0, image);
                adsRepository.save(ads);
                imageRepository.save(image);
                log.info("success, image has been updated");
                return ResponseEntity.ok(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            log.info("Ads doesn't exists");
            return ResponseEntity.status(204).build();
        }
    }

    @Override
    public ResponseEntity<Image> getImageById(Long imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            log.info("получили картинку из БД " + image.getId() + " " + image.getFilePath());
            return ResponseEntity.ok(image);
        } else {
            log.info("картинки не найдено");
            return ResponseEntity.notFound().build();
        }
    }
}
