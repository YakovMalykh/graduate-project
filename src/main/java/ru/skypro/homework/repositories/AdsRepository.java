package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ads;

import java.util.List;

public interface AdsRepository  extends JpaRepository <Ads, Long> {
     List<Ads> findAllByAuthor_Id(Long autorId);
}
