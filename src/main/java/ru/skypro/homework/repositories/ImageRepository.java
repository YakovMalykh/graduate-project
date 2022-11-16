package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository <Image, Long> {
   Optional< Image> findImageByFilePath(String filePath);
   void deleteAllByAds_Id (Long adsId);

   List<Image> findImagesByAds (Ads ads);
}
