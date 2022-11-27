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
import ru.skypro.homework.service.FileService;

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
    private final FileService fileService;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, ImageRepository imageRepository, CommentServiceImpl commentService, CommentRepository commentRepository, AdsMapper adsMapper, FileService fileService) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.adsMapper = adsMapper;
        this.fileService = fileService;
    }

    @Override
    public ResponseEntity<AdsDto> addAdsToDb(CreateAdsDto createAdsDto, List<MultipartFile> filesList) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("метод addAdsToD");
        if (createAdsDto != null) {
            Ads ad = adsMapper.createAdsDtoToAds(createAdsDto);
            User user = userRepository.getUserByEmailIgnoreCase(auth.getName()).orElseThrow();

            List<Image> images = new ArrayList<>();
            for (MultipartFile file : filesList) {
                Image image = saveImageIntoDb(ad, file);
                images.add(image);
            }

            ad.setAuthor(user);
            ad.setImages(images);
            ad.setImage(String.format("/images/%s", images.get(0).getId().toString()));

            Ads savedAd = adsRepository.save(ad);
            AdsDto adsDto = adsMapper.adsToAdsDto(savedAd);
            log.info(adsDto.toString());

            log.info("new ad is saved to DB! Id: " + savedAd.getId() + ", author: " + savedAd.getAuthor());
            return ResponseEntity.ok(adsDto);
        }
        log.info("something wrong with saving");
        return ResponseEntity.notFound().build();
    }

    /**
     * нужен ли нам этот метод? зачем нам файл сохранять в папку?
     */
    private Path saveFileIntoFolder(String imageDir, Ads ad, MultipartFile file) throws IOException {
        // вместо ad.getId() м. прописать tittle, на момент вызоыва метода Id еще null
        Path filePath = Path.of(imageDir, ad.getTitle() + "_" + fileService.getFileName(file.getOriginalFilename()) + "." + fileService.getExtensions(file.getOriginalFilename()));
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
    private Image saveImageIntoDb(Ads ad, MultipartFile file) {
        Image image = new Image();
        image.setAds(ad);
        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());

        try {
            image.setPrewiew(file.getBytes());
        } catch (IOException e) {
            log.info("файла похоже нет");
            e.printStackTrace();
        }

        Image saved = imageRepository.save(image);
        Long id = saved.getId();//получаю Id, чтобы прописать корректный путь к ней

        image.setFilePath(String.format("/images/%s", id));//прописываю корректный путь

        return imageRepository.save(image);//снова сохраняю
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
        responseWrapperAdsDto.setCount(adsDtoList.size());
        responseWrapperAdsDto.setResults(adsDtoList);
        log.info("получили все объявления " + responseWrapperAdsDto.toString());
        return ResponseEntity.ok(responseWrapperAdsDto);
    }

    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.getUserByEmailIgnoreCase(username).orElseThrow();
        List<Ads> adsList = adsRepository.findAllByAuthor(user);
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        if (!adsList.isEmpty()) {
            List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
            responseWrapperAdsDto.setCount(adsDtoList.size());
            responseWrapperAdsDto.setResults(adsDtoList);
            log.info("получили объявления обратившегося пользователя" + responseWrapperAdsDto.toString());//удалить позже...
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapperAdsDto);

        } else {
            log.info("У пользователя " + username + " еще нет объявлений");
            // заполняю responseWrapper пустым AdsDto иначе фронт не отображает страницу юзера, у которого нет объявлений
            // в консоли фронта ошибка Uncaught TypeError: Cannot read properties of undefined (reading 'length') или
            // ругается на обращение к null, хотя в Swagger требование что мы должны вернуть 404 ошибку
            ArrayList<AdsDto> defaultListEmptyAdsDto = new ArrayList<>();
            responseWrapperAdsDto.setCount(0);
            responseWrapperAdsDto.setResults(defaultListEmptyAdsDto);
            return ResponseEntity.ok(responseWrapperAdsDto);
        }
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
    public ResponseEntity<AdsDto> updateAds(Integer adsPk, CreateAdsDto createAdsDto) {
        Optional<Ads> optionalAds = adsRepository.findById(adsPk.longValue());

        if (optionalAds.isPresent()) {
            Ads ads = optionalAds.get();
            adsMapper.updateAdsFromCreateAdsDto(createAdsDto, ads);// обновляем поля объявления
            Ads savedAndUpdatedAd = adsRepository.save(ads);// сохраняем обновленное объявл-е
            AdsDto adsDto = adsMapper.adsToAdsDto(savedAndUpdatedAd);// конвертируем объявление в AdsDto, чтобы вернуть фронту
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

