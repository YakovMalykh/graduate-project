package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.CommentRepository;
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
import java.util.ArrayList;
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
    private final CommentRepository commentRepository;
    private final AdsMapper adsMapper;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, ImageRepository imageRepository, CommentServiceImpl commentService, CommentRepository commentRepository, AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.adsMapper = adsMapper;
    }

    @Override
    public ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto, List<MultipartFile> filesList) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("метод addAdsToD");
        if (createAdsDto != null) {
            Ads ad = adsMapper.createAdsDtoToAds(createAdsDto);
            User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow();

            List<Image> images = new ArrayList<>();
            for (MultipartFile file : filesList) {
                Path filePath = saveFileIntoFolder(imageDir, ad, file);
                Image image = saveImageIntoDb(filePath, ad, file);
                images.add(image);
            }

            ad.setAuthor(user);
            ad.setImages(images);

            Ads savedAd = adsRepository.save(ad);
            AdsDto adsDto = adsMapper.adsToAdsDto(savedAd);

            log.info("new ad is saved to DB! Id: " + savedAd.getId() + ", author: " + savedAd.getAuthor());
            return ResponseEntity.ok(adsDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }

    private Path saveFileIntoFolder(String imageDir, Ads ad, MultipartFile file) throws IOException {
        // вместо ad.getId() м. прописать tittle, на момент вызоыва метода Id еще null
        Path filePath = Path.of(imageDir, ad.getTitle() + "_" + file.getOriginalFilename() + "." + getExtensions(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        return filePath;
    }

    /**
     * этот метод следовало бы размещать в ImageService
     */
    private Image saveImageIntoDb(Path filePath, Ads savedAd, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setAds(savedAd);
        image.setFilePath(filePath.toString());
        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());
        image.setPrewiew(file.getBytes()); // это получается лишнее
//        image.setPrewiew(generatePreview(filePath));
        return imageRepository.save(image);
    }

    /**
     * нужен ли он нам???
     */
    private byte[] generatePreview(Path filePath) throws IOException {
        log.info("method generatePreview started");
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
        log.info("method getExtensions started");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResults(adsDtoList);
            log.info("получили все объявления");
        } else {
            log.info("объявлений не найдено");
        }
        return ResponseEntity.ok(responseWrapperAdsDto);
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.getUserByEmailIgnoreCase(username).orElseThrow();
        List<Ads> adsList = adsRepository.findAllByAuthor_Id(user.getId()); //потом заменим на автора из контекста
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResults(adsDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapperAdsDto);

        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<FullAdsDto> getAds(Integer adsPk) {
        Optional<Ads> optionalAds = adsRepository.findById(adsPk.longValue());

        if (optionalAds.isPresent()) {

            Optional<User> optionslUser = userRepository.findById(optionalAds.get().getAuthor().getId());
            //FullAdsDto fullAdsDto = adsMapper.adsToFullAdsDto(optionalAds.get(), optionslUser.get());
            List<Image> images = imageRepository.findImagesByAds(optionalAds.get());
            FullAdsDto fullAdsDto = adsMapper.adsToFullAdsDto(optionalAds.get(), optionslUser.get(), images);

            return ResponseEntity.ok(fullAdsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteAds(Long adsPk) {
        Optional<Ads> optionalAds = adsRepository.findById(adsPk);
        if (optionalAds.isPresent()) {
            removeFileFromFolder(optionalAds.get());
            imageRepository.deleteAllByAds(optionalAds.get());
            commentRepository.deleteAllByAdsId(optionalAds.get());
            adsRepository.deleteById(adsPk);
            log.info(" ad with Id: " + adsPk + "is deleted");
            return ResponseEntity.ok().build();
        } else {
            log.info("not found ad");
            return ResponseEntity.status(204).build();
        }
    }


    private void removeFileFromFolder(Ads ad) {
        imageRepository.findImagesByAds(ad).forEach(e -> {
                    String filePath = e.getFilePath();
                    Path path = Path.of(filePath);
                    try {
                        log.info("файл " + filePath + " удален");
                        Files.deleteIfExists(path);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
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


    /**
     * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
     *
     * @param tittle часть заоголовка
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsByTittle(String tittle) {
        List<Ads> adsList = adsRepository.findByTitleContainingIgnoreCaseOrderByPrice(tittle);
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResults(adsDtoList);
            return ResponseEntity.ok(responseWrapperAdsDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

