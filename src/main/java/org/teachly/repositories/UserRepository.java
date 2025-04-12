package org.teachly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teachly.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Найти определенного пользователя по его username (уникальное поле)
    Optional<User> findByUsername(String username);

    // Проверить, что пользователь с данным username существует
    boolean existsByUsername(String username);
}
