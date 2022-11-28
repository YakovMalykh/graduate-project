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
    public ResponseEntity<Void> updateImage(Long adsId, MultipartFile file) {
        Optional<Ads> optionalAds = adsRepository.findById(adsId);
        if (optionalAds.isPresent()) {
            Ads ads = optionalAds.get();
            String imagesFilePath = ads.getImage();//получили путь из Объявления
            Optional<Image> optionalImage = imageRepository.findImageByFilePath(imagesFilePath);//ищем по пути саму картинку
            if (optionalImage.isPresent()) {
                Image image = optionalImage.get();
                try {
                    Image imageUpdated = adsMapper.fileToImage(file, image);
                    imageRepository.save(imageUpdated);

                    log.info("картинка " + image.getId() + " в объявлении " + ads.getId() + " обновлена");
                    return ResponseEntity.ok().build();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("картинка с полем filePath: " + imagesFilePath + " не найдена");
            return ResponseEntity.notFound().build();
        }
        log.info("Ads doesn't exists");
        return ResponseEntity.notFound().build();
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
