package lk.dimuthu.autohive.repository;

import lk.dimuthu.autohive.entity.Order;
import lk.dimuthu.autohive.entity.SellerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRating, String> {

    // Check if a rating already exists for a given order (Only one rating allowed per order)
    boolean existsByOrder(Order order);

    // Calculate and retrieve the average rating value for a specific seller
    @Query("SELECT AVG(r.ratingValue) FROM SellerRating r WHERE r.seller.id = :sellerId")
    Double getAverageRatingForSeller(@Param("sellerId") String sellerId);
}