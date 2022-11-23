package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.models.Image;

public interface ImageService {
  ResponseEntity<Void> updateImage(Long adsId, MultipartFile file);

  ResponseEntity<Image> getImageById(Long imageId);
}
