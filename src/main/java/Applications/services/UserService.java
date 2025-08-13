package Applications.services;

import Applications.DAO.UserDAO;
import Applications.model.User;
import Applications.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
private final UserDAO userDAO;
        private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserService(UserRepository userRepository, UserDAO userDAO, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

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

    @Transactional
    public void delete(int id) {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(id) from java_users where id = ?", Integer.class, id);
        userRepository.deleteById(id);
        userDAO.setIDForDelete(maxId);
    }
}
