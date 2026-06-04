package com.server.app.marketplace.repositories;

import com.server.app.marketplace.common.enums.UserRole;
import com.server.app.marketplace.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    long countByRole(UserRole role);
}