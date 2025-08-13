package Applications.services;

import Applications.DAO.PassportDAO;
import Applications.model.Passport;
import Applications.model.User;
import Applications.repositories.PassportRepository;
import Applications.util.PassportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<Passport> allPassports() {
        return passportRepository.findAll();
    }

    public Passport showByIdOfPassport(int id) {
        Optional<Passport> found = passportRepository.findById(id);
        return found.orElse(null);
    }

    public Passport findByOwner(User owner) {
        return passportRepository.findByOwner(owner);
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
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(id) from passport where id = ?", Integer.class, id);
        passportRepository.deleteById(id);
        passportDAO.setIDAfterDelete(maxId);
    }
}
