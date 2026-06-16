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

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        Optional<Quote> optionalQuote = quoteRepository.findById(request.getQuoteId());
        if(optionalQuote.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Quote ID එක වලංගු නැත!");
        }
        Quote selectedQuote = optionalQuote.get();

        Optional<User> optionalBuyer = userRepository.findById(request.getBuyerId());
        if(optionalBuyer.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Buyer ID එක වලංගු නැත!");
        }

        Order newOrder = new Order();
        newOrder.setInquiry(selectedQuote.getInquiry());
        newOrder.setQuote(selectedQuote);
        newOrder.setSeller(selectedQuote.getSeller());
        newOrder.setBuyer(optionalBuyer.get());
        newOrder.setTotalAmount(selectedQuote.getPrice());
        newOrder.setStatus("pending");

        orderRepository.save(newOrder);

        Inquiry inquiry = selectedQuote.getInquiry();
        inquiry.setStatus("ordered");
        inquiryRepository.save(inquiry);

        return ResponseEntity.ok("ඔබගේ ඇණවුම සාර්ථකව සම්පූර්ණ විය! (Total: Rs." + selectedQuote.getPrice() + ")");
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderStatusUpdateRequest request) {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Order ID එක වලංගු නැත!");
        }

        Order order = optionalOrder.get();

        if(!order.getSeller().getId().equals(request.getSellerId())){
            return ResponseEntity.status(403).body("Error: ඔබට මෙම ඇණවුම වෙනස් කිරීමට අවසර නැත!");
        }

        String newStatus = request.getStatus();
        if(!"shipped".equals(newStatus) &&!"delivered".equals(newStatus)) {
            return ResponseEntity.badRequest().body("Error: වලංගු නොවන තත්ත්වයකි. කරුණාකර 'shipped' හෝ 'delivered' පමණක් භාවිතා කරන්න.");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok("ඇණවුමේ තත්ත්වය '" + newStatus + "' ලෙස සාර්ථකව යාවත්කාලීන විය!");
    }

    @GetMapping("/my-orders/{buyerId}")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@PathVariable String buyerId) {
        List<Order> myOrders = orderRepository.findByBuyerId(buyerId);

        List<OrderResponse> responses = myOrders.stream().map(o -> new OrderResponse(
                o.getId(),
                o.getInquiry().getId(),
                o.getInquiry().getPartDescription(),
                o.getQuote().getId(),
                o.getBuyer().getId(),
                o.getBuyer().getName(),
                o.getSeller().getId(),
                o.getSeller().getBusinessName(),
                o.getStatus(),
                o.getTotalAmount(),
                o.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(responses);
    }
}