package Applications.repositories;

import Applications.model.Passport;
import Applications.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {
    Passport findByOwner(User owner);



}
