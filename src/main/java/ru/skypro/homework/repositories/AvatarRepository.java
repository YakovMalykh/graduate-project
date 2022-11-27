package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.User;

import java.util.Optional;

public interface AvatarRepository  extends JpaRepository <Avatar,Long>{
    Optional<Avatar> findAvatarByUser (User user);

}
