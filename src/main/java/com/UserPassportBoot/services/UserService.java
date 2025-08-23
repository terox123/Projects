package com.UserPassportBoot.services;


import com.UserPassportBoot.DAO.UserDAO;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.repositories.UserRepository;
import com.UserPassportBoot.security.UserDetailsClass;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
private final UserDAO userDAO;
        private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserService(UserRepository userRepository, UserDAO userDAO, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
        this.jdbcTemplate = jdbcTemplate;
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
        userRepository.save(user);
    }

    @Transactional
    public void update(int id, User updatedUser ) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

public Page<User> findAllUsersSortedByBirthDateDesc(Pageable pageable){
        return userRepository.findAllByOrderByDateOfBirthDesc(pageable);
}

    public Page<User> findAllUsersSortedByBirthDateAsc(Pageable pageable) {
        return userRepository.findAllByOrderByDateOfBirthAsc(pageable);
    }

    @Transactional
    public void delete(int id) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(id) from java_users where id = ?", Integer.class, id);
        userRepository.deleteById(id);
        userDAO.setIDForDelete(maxId);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
Optional<User> user = userRepository.findByName(username);
if(user.isEmpty()){
    throw new UsernameNotFoundException("UserName not found");
}

return new UserDetailsClass(user.get());

    }
}

