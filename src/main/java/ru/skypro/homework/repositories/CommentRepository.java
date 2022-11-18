package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ads;
import ru.skypro.homework.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByAdsId(Long adsId);

    void deleteAllByAdsId(Ads adsId);

    List<Comment> findAllByAdsId(Ads adsId);

}
