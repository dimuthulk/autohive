package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
    // කලින් තිබුණු existsByUserId වෙනුවට මේක දාන්න
    boolean existsByUser(User user);
}