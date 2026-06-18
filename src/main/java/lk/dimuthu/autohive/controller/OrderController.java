package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.OrderRequest;
import lk.dimuthu.autohive.dto.request.OrderStatusUpdateRequest;
import lk.dimuthu.autohive.dto.response.OrderResponse;
import lk.dimuthu.autohive.entity.*;
import lk.dimuthu.autohive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for handling order-related operations including placing orders,
 * updating order status, and retrieving order history.
 * All endpoints are prefixed with "/api/v1/orders".
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    // Repository dependencies for database operations
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    /**
     * Places a new order based on a selected quote.
     *
     * @param request The order request containing quote ID and buyer ID
     * @return ResponseEntity with success message including total amount, or error message if validation fails
     */
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        // Validate that the quote exists
        Optional<Quote> optionalQuote = quoteRepository.findById(request.getQuoteId());
        if(optionalQuote.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Invalid Quote ID!");
        }
        Quote selectedQuote = optionalQuote.get();

        // Validate that the buyer exists
        Optional<User> optionalBuyer = userRepository.findById(request.getBuyerId());
        if(optionalBuyer.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Invalid Buyer ID!");
        }

        // Create and populate the new order
        Order newOrder = new Order();
        newOrder.setInquiry(selectedQuote.getInquiry());
        newOrder.setQuote(selectedQuote);
        newOrder.setSeller(selectedQuote.getSeller());
        newOrder.setBuyer(optionalBuyer.get());
        newOrder.setTotalAmount(selectedQuote.getPrice());
        newOrder.setStatus("pending"); // Initial status for new orders

        orderRepository.save(newOrder);

        // Update the associated inquiry status to "ordered"
        Inquiry inquiry = selectedQuote.getInquiry();
        inquiry.setStatus("ordered");
        inquiryRepository.save(inquiry);

        return ResponseEntity.ok("Your order has been successfully placed! (Total: Rs." + selectedQuote.getPrice() + ")");
    }

    /**
     * Updates the status of an existing order. Only the seller who owns the order
     * can update its status.
     *
     * @param orderId The ID of the order to update
     * @param request The status update request containing seller ID and new status
     * @return ResponseEntity with success message or error message if validation fails
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderStatusUpdateRequest request) {

        // Validate that the order exists
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Invalid Order ID!");
        }

        Order order = optionalOrder.get();

        // Verify that the requesting seller owns this order (authorization check)
        if(!order.getSeller().getId().equals(request.getSellerId())){
            return ResponseEntity.status(403).body("Error: You are not authorized to update this order!");
        }

        // Validate that the new status is allowed (only "shipped" or "delivered")
        String newStatus = request.getStatus();
        if(!"shipped".equals(newStatus) && !"delivered".equals(newStatus)) {
            return ResponseEntity.badRequest().body("Error: Invalid status. Please use 'shipped' or 'delivered' only.");
        }

        // Update and save the order status
        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok("Order status successfully updated to '" + newStatus + "'!");
    }

    /**
     * Retrieves all orders placed by a specific buyer.
     *
     * @param buyerId The ID of the buyer whose orders to fetch
     * @return ResponseEntity containing a list of order responses with full details
     */
    @GetMapping("/my-orders/{buyerId}")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@PathVariable String buyerId) {
        // Fetch all orders for the given buyer ID
        List<Order> myOrders = orderRepository.findByBuyerId(buyerId);

        // Convert each order entity to a response DTO with all necessary information
        List<OrderResponse> responses = myOrders.stream().map(o -> new OrderResponse(
                o.getId(),                                    // Order ID
                o.getInquiry().getId(),                       // Inquiry ID
                o.getInquiry().getPartDescription(),          // Part description from the inquiry
                o.getQuote().getId(),                         // Quote ID
                o.getBuyer().getId(),                         // Buyer ID
                o.getBuyer().getName(),                       // Buyer name
                o.getSeller().getId(),                        // Seller ID
                o.getSeller().getBusinessName(),              // Seller business name
                o.getStatus(),                                // Order status (pending/shipped/delivered)
                o.getTotalAmount(),                           // Total order amount
                o.getCreatedAt()                              // Order creation timestamp
        )).toList();

        return ResponseEntity.ok(responses);
    }
}