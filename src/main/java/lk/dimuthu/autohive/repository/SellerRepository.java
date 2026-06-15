package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
    boolean existsByUserId(String userId); // එක User කෙනෙක්ට කඩවල් දෙකක් හදන එක නවත්තන්න මේක ඕනේ
}
