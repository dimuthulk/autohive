package lk.dimuthu.autohive.controller;
import lk.dimuthu.autohive.entity.Inquiry;
import lk.dimuthu.autohive.entity.Quote;
import lk.dimuthu.autohive.repository.InquiryRepository;
import lk.dimuthu.autohive.repository.QuoteRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    // 1. පාරිභෝගිකයෙකු නව ඉල්ලුමක් (Inquiry) දැමීම
    @PostMapping("/create")
    public ResponseEntity<String> createInquiry(@RequestBody Inquiry inquiry) {

        // අදාළ පාරිභෝගිකයා පද්ධතියේ ඉන්නවද බලනවා
        if(!userRepository.existsById(inquiry.getUserId())){
            return ResponseEntity.badRequest().body("Error: මේ User ID එක වලංගු නැත!");
        }

        inquiry.setStatus("pending");
        inquiryRepository.save(inquiry);
        return ResponseEntity.ok("ඔබගේ ඉල්ලුම සාර්ථකව පද්ධතියට එක් කළා. විකුණුම්කරුවන් ඉක්මනින් මිල ගණන් ලබා දේවි!");
    }

    // 2. විකුණුම්කරුවෙකු අදාළ Inquiry එකකට මිලක් (Quote) ඉදිරිපත් කිරීම
    @PostMapping("/quote")
    public ResponseEntity<String> submitQuote(@RequestBody Quote quote) {

        if(!sellerRepository.existsById(quote.getSellerId())){
            return ResponseEntity.badRequest().body("Error: මේ Seller ID එක වලංගු නැත!");
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(quote.getInquiryId());
        if(optionalInquiry.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Inquiry ID එක වලංගු නැත!");
        }

        // පළමු වතාවට මිලක් ආපු ගමන්, අදාළ Inquiry එකේ status එක 'quoting' විදියට වෙනස් කරනවා
        Inquiry inquiry = optionalInquiry.get();
        inquiry.setStatus("quoting");
        inquiryRepository.save(inquiry);

        quoteRepository.save(quote);
        return ResponseEntity.ok("ඔබගේ මිල ගණන (Quote) සාර්ථකව ඉදිරිපත් කළා!");
    }

    // 3. පාරිභෝගිකයෙකුට තමන්ගේ ඉල්ලීම් (Inquiries) බලාගැනීමේ API එක
    @GetMapping("/my-inquiries/{userId}")
    public ResponseEntity<List<Inquiry>> getMyInquiries(@PathVariable String userId) {
        List<Inquiry> myInquiries = inquiryRepository.findByUserId(userId);
        return ResponseEntity.ok(myInquiries);
    }

    // 4. යම්කිසි Inquiry එකකට විකුණුම්කරුවන්ගෙන් ලැබුණු Quotes (මිල ගණන්) බලාගැනීමේ API එක
    @GetMapping("/{inquiryId}/quotes")
    public ResponseEntity<List<Quote>> getQuotesForInquiry(@PathVariable String inquiryId) {
        List<Quote> quotes = quoteRepository.findByInquiryId(inquiryId);
        return ResponseEntity.ok(quotes);
    }

    // 5. විකුණුම්කරුවන්ට සියලුම විවෘත ඉල්ලුම් (Open Inquiries) බලාගැනීමේ API එක
    @GetMapping("/open-feed")
    @PreAuthorize("hasAnyAuthority('seller', 'admin')") // මේකෙන් Seller ට විතරක් මේක සීමා කරනවා
    public ResponseEntity<List<Inquiry>> getOpenInquiriesFeed() {
        List<Inquiry> openInquiries = inquiryRepository.findByInquiryTypeAndStatus("open", "pending");
        return ResponseEntity.ok(openInquiries);
    }
}