package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // අලුතින් එකතු කරන එක: භාණ්ඩයේ නමේ අඩංගු වචනයක් (keyword) හරහා සෙවීමට (Capital/Simple අකුරු නොසලකා)
    List<Product> findByNameContainingIgnoreCase(String name);
}

