package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Slf4j
@Service
@Transactional
public class AdsServiceImpl implements AdsService {
    @Value("$(image.dir.path)")
    private String imageDir;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AdsMapper adsMapper;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, ImageRepository imageRepository, AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.adsMapper = adsMapper;
    }


    @Override //метод пока не отрабатывает, т.к. нет автора
    public ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto, MultipartFile file) throws IOException{
        if (createAdsDto != null) {
           Ads savedAds = adsRepository.save(adsMapper.createAdsDtoToAds(createAdsDto));
           AdsDto adsDto = adsMapper.adsToAdsDto(savedAds);

            Path filePath = Path.of(imageDir, savedAds.getId()+ "." + getExtensions(file.getOriginalFilename()));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }
            Image image=new Image();
            image.setAds(savedAds);
            image.setFilePath(filePath.toString());
            image.setFileSize(file.getSize());
            image.setMediaType(file.getContentType());
            image.setPrewiew(file.getBytes());
            image.setPrewiew(generatePreview(filePath));
            imageRepository.save(image);

            log.info("new ad saved to DB! Id: " + savedAds.getId() + ", author: " + savedAds.getAuthor());
            return ResponseEntity.ok(adsDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }

    private byte[] generatePreview(Path filePath) throws IOException {
        log.info("metod generatePreview started");
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage prewiew = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = prewiew.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(prewiew, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtensions(String fileName) {
        log.info("metod getExtensions started");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
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
            Optional<User> optionslUser = userRepository.findById(optionalAds.get().getAuthor().getId());
            FullAdsDto fullAdsDto = adsMapper.adsToFullAdsDto(optionalAds.get(), optionslUser.get());
            return ResponseEntity.ok(fullAdsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AdsDto> updateAds(Integer adsPk, AdsDto adsDto) {
          Optional<Ads> optionalAds = adsRepository.findById(adsPk.longValue());

        if (optionalAds.isPresent()) {
            Ads ads = optionalAds.get();
            adsMapper.updateAdsFromAdsDto(adsDto, ads);
            adsRepository.save(ads);
            log.info("success, ads with id: " + adsPk + "has been updated");
            return ResponseEntity.ok(adsDto);
        } else {
            log.info("Ads doesn't exists");
            return ResponseEntity.status(204).build();
        }
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Boolean authenticated, String authority, Object credentials, Object details, Object principal) {
        List<Ads> adsList = adsRepository.findAllByAuthor_Id(3L); //потом заменим на автора из контекста
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResult(adsDtoList);
            return ResponseEntity.ok(responseWrapperAdsDto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
     * @param tittle часть заоголовка
     * @return
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsByTittle(String tittle) {
        List<Ads> adsList = adsRepository.findByTitleContainingIgnoreCaseOrderByPrice(tittle);
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResult(adsDtoList);
            return ResponseEntity.ok(responseWrapperAdsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

