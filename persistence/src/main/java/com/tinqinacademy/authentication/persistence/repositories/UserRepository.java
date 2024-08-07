package com.tinqinacademy.authentication.persistence.repositories;

import com.tinqinacademy.authentication.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsernameIgnoreCase(String username);
    Boolean existsByUsernameIgnoreCase(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isVerified = TRUE WHERE u.email = ?1")
    void confirmEmail(String email);
}
