package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // පාරිභෝගිකයාගේ ID එකෙන් ඔහුගේ සියලුම orders හොයාගන්න
    List<Order> findByBuyerId(String buyerId);
}
