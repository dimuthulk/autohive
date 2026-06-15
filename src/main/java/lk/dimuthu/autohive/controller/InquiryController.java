package lk.dimuthu.autohive.controller;
import lk.dimuthu.autohive.entity.Inquiry;
import lk.dimuthu.autohive.entity.Quote;
import lk.dimuthu.autohive.repository.InquiryRepository;
import lk.dimuthu.autohive.repository.QuoteRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}