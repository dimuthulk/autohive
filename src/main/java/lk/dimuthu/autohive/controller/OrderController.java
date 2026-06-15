package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.entity.Inquiry;
import lk.dimuthu.autohive.entity.Order;
import lk.dimuthu.autohive.entity.Quote;
import lk.dimuthu.autohive.repository.InquiryRepository;
import lk.dimuthu.autohive.repository.OrderRepository;
import lk.dimuthu.autohive.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody Map<String, String> payload) {

        String quoteId = payload.get("quoteId");
        String buyerId = payload.get("buyerId");

        // 1. අදාළ Quote එක Database එකෙන් සොයා ගැනීම
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if(optionalQuote.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Quote ID එක වලංගු නැත!");
        }
        Quote selectedQuote = optionalQuote.get();

        // 2. අලුත් ඇණවුමක් (Order) හැදීම
        Order newOrder = new Order();
        newOrder.setInquiryId(selectedQuote.getInquiryId()); // අදාළ Inquiry එකේ ID එක
        newOrder.setQuoteId(selectedQuote.getId());
        newOrder.setSellerId(selectedQuote.getSellerId()); // භාණ්ඩය විකුණන කෙනා
        newOrder.setBuyerId(buyerId); // භාණ්ඩය මිලදී ගන්නා පාරිභෝගිකයා
        newOrder.setTotalAmount(selectedQuote.getPrice()); // Quote එකේ තිබුණු මිල
        newOrder.setStatus("pending");

        orderRepository.save(newOrder);

        // 3. අදාළ Inquiry එකේ තත්ත්වය 'ordered' ලෙස වෙනස් කිරීම (එතකොට වෙන අය ආයෙත් quotes දාන එක නවතිනවා)
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(selectedQuote.getInquiryId());
        if(optionalInquiry.isPresent()){
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setStatus("ordered");
            inquiryRepository.save(inquiry);
        }

        return ResponseEntity.ok("ඔබගේ ඇණවුම සාර්ථකව සම්පූර්ණ විය! (Total: Rs." + selectedQuote.getPrice() + ")");
    }

    // 2. විකුණුම්කරු (Seller) විසින් ඇණවුමේ තත්ත්වය (Status) වෙනස් කිරීමේ API එක
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, String> payload) {

        String sellerId = payload.get("sellerId");
        String newStatus = payload.get("status"); // 'shipped' හෝ 'delivered'

        // 1. Order එක Database එකේ තියෙනවද බලනවා
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Order ID එක වලංගු නැත!");
        }

        Order order = optionalOrder.get();

        // 2. මේ Order එක වෙනස් කරන්නේ ඇත්තටම අදාළ විකුණුම්කරුද කියලා බලනවා (ආරක්ෂාව සඳහා)
        if(!order.getSellerId().equals(sellerId)){
            return ResponseEntity.status(403).body("Error: ඔබට මෙම ඇණවුම වෙනස් කිරීමට අවසර නැත!");
        }

        // 3. එවන Status එක නිවැරදිද කියලා බලනවා
        if(!"shipped".equals(newStatus) &&!"delivered".equals(newStatus)) {
            return ResponseEntity.badRequest().body("Error: වලංගු නොවන තත්ත්වයකි. කරුණාකර 'shipped' හෝ 'delivered' පමණක් භාවිතා කරන්න.");
        }

        // 4. අලුත් Status එක Save කරනවා
        order.setStatus(newStatus);
        orderRepository.save(order);

        return ResponseEntity.ok("ඇණවුමේ තත්ත්වය '" + newStatus + "' ලෙස සාර්ථකව යාවත්කාලීන විය!");
    }

    // 3. පාරිභෝගිකයෙකුට තමන්ගේ ඇණවුම් (Orders) බලාගැනීමේ API එක
    @GetMapping("/my-orders/{buyerId}")
    public ResponseEntity<List<Order>> getMyOrders(@PathVariable String buyerId) {
        List<Order> myOrders = orderRepository.findByBuyerId(buyerId);
        return ResponseEntity.ok(myOrders);
    }
}