package lk.dimuthu.autohive.controller;


import lk.dimuthu.autohive.dto.request.RatingRequest;
import lk.dimuthu.autohive.entity.Order;
import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.entity.SellerRating;
import lk.dimuthu.autohive.repository.OrderRepository;
import lk.dimuthu.autohive.repository.SellerRatingRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    @Autowired
    private SellerRatingRepository ratingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('customer')") // Restricted to customers only
    public ResponseEntity<String> addRating(@RequestBody RatingRequest request) {

        // 1. Check if the associated order exists
        Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());
        if(optionalOrder.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Order not found!");
        }
        Order order = optionalOrder.get();

        // 2. Verify that the order is in "delivered" status (ratings allowed only after delivery)
        if(!"delivered".equals(order.getStatus())) {
            return ResponseEntity.badRequest().body("Error: You can only rate after the order has been delivered!");
        }

        // 3. Check if a rating already exists for this order (preventing duplicate ratings)
        if(ratingRepository.existsByOrder(order)) {
            return ResponseEntity.badRequest().body("Error: You have already submitted a rating for this order!");
        }

        // 4. Save the new rating
        SellerRating rating = new SellerRating();
        rating.setOrder(order);
        rating.setBuyer(order.getBuyer());
        rating.setSeller(order.getSeller());
        rating.setRatingValue(request.getRatingValue());
        rating.setReview(request.getReview());
        ratingRepository.save(rating);

        // 5. Calculate the seller's new average rating and update the Seller table
        Double newAvgRating = ratingRepository.getAverageRatingForSeller(order.getSeller().getId());
        Seller seller = order.getSeller();
        seller.setRating(newAvgRating);
        sellerRepository.save(seller);

        return ResponseEntity.ok("Your rating has been successfully recorded!");
    }
}

