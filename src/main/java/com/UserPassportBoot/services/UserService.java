package com.UserPassportBoot.services;


import com.UserPassportBoot.DAO.UserDAO;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "users", key = "#id")
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User  not found with id: " + id));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    public Page<User> findAllUsersSortedByBirthDateDesc(Pageable pageable) {
        return userRepository.findAllByOrderByDateOfBirthDesc(pageable);
    }

    public Page<User> findAllUsersSortedByBirthDateAsc(Pageable pageable) {
        return userRepository.findAllByOrderByDateOfBirthAsc(pageable);
    }


    @Transactional
    public void delete(int id) {
userRepository.deleteById(id);
    }
}