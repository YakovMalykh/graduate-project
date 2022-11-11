package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    /**
     * @param username  - используем email в качестве username
     */
    Optional<User> getUserByEmail(String username);
}
