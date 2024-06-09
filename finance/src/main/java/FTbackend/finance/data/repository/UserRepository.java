package FTbackend.finance.data.repository;


import org.springframework.stereotype.Repository;
import FTbackend.finance.data.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


}