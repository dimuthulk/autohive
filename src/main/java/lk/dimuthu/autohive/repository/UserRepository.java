package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // ඊමේල් එකක් දැනටමත් database එකේ තියෙනවද කියලා බලන්න මේක උදව් වෙනවා
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
