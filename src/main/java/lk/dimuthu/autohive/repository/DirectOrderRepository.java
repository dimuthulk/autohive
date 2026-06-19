package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.DirectOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectOrderRepository extends JpaRepository<DirectOrder, String> {
    List<DirectOrder> findByBuyerId(String buyerId);
    List<DirectOrder> findByProductSellerId(String sellerId);
}