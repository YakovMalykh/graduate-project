package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.User;

import java.util.List;

public interface AdsRepository  extends JpaRepository <Ads, Long> {
     List<Ads> findAllByAuthor (User user);
     /**
      * метод ищет обявления по частичному совпадению заголовка(tittle) и возвращает отсротированный по цене список
      */
     List<Ads> findByTitleContainingIgnoreCaseOrderByPrice(String tittle);
}
