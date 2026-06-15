package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

}

