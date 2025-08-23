package com.UserPassportBoot.repositories;


import com.UserPassportBoot.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findAllByOrderByDateOfBirthAsc(Pageable pageable);
    Page<User> findAllByOrderByDateOfBirthDesc(Pageable pageable);
    Optional<User> findByEmail(String email);

    Optional<User> findByName(@NotBlank(message = "Name can't be empty") String name);
}
