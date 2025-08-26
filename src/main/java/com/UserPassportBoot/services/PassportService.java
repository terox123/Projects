package com.UserPassportBoot.services;


import com.UserPassportBoot.DAO.PassportDAO;
import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.repositories.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PassportService {
    private final PassportRepository passportRepository;
    private final PassportDAO passportDAO;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PassportService(PassportRepository passportRepository, PassportDAO passportDAO, JdbcTemplate jdbcTemplate) {
        this.passportRepository = passportRepository;
        this.passportDAO = passportDAO;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<Passport> allPassports(Pageable pageable) {
        return passportRepository.findAll(pageable);
    }
@Cacheable(value = "passports", key = "#id")
    public Passport showByIdOfPassport(int id) {
        Optional<Passport> found = passportRepository.findById(id);
        return found.orElse(null);
    }

public Page<Passport> findAllPassportByOwnerBirthDateAsc(Pageable pageable){
        return passportRepository.findAllOrderByOwnerBirthDateAsc(pageable);
}

    public Page<Passport> findAllPassportByOwnerBirthDateDesc(Pageable pageable){
        return passportRepository.findAllOrderByOwnerBirthDateDesc(pageable);
    }

public Page<Passport> searchPassportByStartingWith(String characters, Pageable pageable){
        return passportRepository.searchPassportByNumberStartingWith(characters, pageable);
}
@Cacheable(value = "passportByOwner", key = "#owner.id")
    public Passport findByOwner(User owner) {
        return passportRepository.findByOwner(owner).orElse(null);
    }


    @Transactional
    public void save(Passport passport) {
        passportRepository.save(passport);
    }

    @Transactional
    public void update(int id, Passport passportToUpdate) {
        passportToUpdate.setId(id);
        passportRepository.save(passportToUpdate);

    }

    @Transactional
    public void delete(int id) {
        passportRepository.deleteById(id);
    }
}
