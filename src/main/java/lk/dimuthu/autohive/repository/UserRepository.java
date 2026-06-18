package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Checks whether an email already exists in the database
    boolean existsByEmail(String email);

    // Retrieves a user by their email address, wrapped in an Optional to handle null cases
    Optional<User> findByEmail(String email);
}
