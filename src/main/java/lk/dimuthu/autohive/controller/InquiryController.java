package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.InquiryRequest;
import lk.dimuthu.autohive.dto.request.QuoteRequest;
import lk.dimuthu.autohive.dto.response.InquiryResponse;
import lk.dimuthu.autohive.dto.response.QuoteResponse;
import lk.dimuthu.autohive.entity.*;
import lk.dimuthu.autohive.repository.*;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createInquiry(@RequestBody InquiryRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ User ID එක වලංගු නැත!");
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setUser(optionalUser.get());
        inquiry.setInquiryType(request.getInquiryType()!= null? request.getInquiryType() : "open");
        inquiry.setPartDescription(request.getPartDescription());
        inquiry.setImageUrl(request.getImageUrl());
        inquiry.setStatus("pending");

        if(request.getCategoryId()!= null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(inquiry::setCategory);
        }
        if(request.getVehicleId()!= null) {
            vehicleRepository.findById(request.getVehicleId()).ifPresent(inquiry::setVehicle);
        }

        inquiryRepository.save(inquiry);
        return ResponseEntity.ok("ඔබගේ ඉල්ලුම සාර්ථකව පද්ධතියට එක් කළා!");
    }

    @PostMapping("/quote")
    public ResponseEntity<String> submitQuote(@RequestBody QuoteRequest request) {
        Optional<Seller> optionalSeller = sellerRepository.findById(request.getSellerId());
        if(optionalSeller.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Seller ID එක වලංගු නැත!");
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(request.getInquiryId());
        if(optionalInquiry.isEmpty()){
            return ResponseEntity.badRequest().body("Error: මේ Inquiry ID එක වලංගු නැත!");
        }

        Quote quote = new Quote();
        quote.setInquiry(optionalInquiry.get());
        quote.setSeller(optionalSeller.get());
        quote.setPrice(request.getPrice());
        quote.setDeliveryTimeDays(request.getDeliveryTimeDays());

        Inquiry inquiry = optionalInquiry.get();
        inquiry.setStatus("quoting");
        inquiryRepository.save(inquiry);

        quoteRepository.save(quote);
        return ResponseEntity.ok("ඔබගේ මිල ගණන (Quote) සාර්ථකව ඉදිරිපත් කළා!");
    }

    @GetMapping("/my-inquiries/{userId}")
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(@PathVariable String userId) {
        List<Inquiry> myInquiries = inquiryRepository.findByUserId(userId);

        List<InquiryResponse> responses = myInquiries.stream().map(i -> new InquiryResponse(
                i.getId(),
                i.getUser().getId(),
                i.getUser().getName(),
                i.getVehicle()!= null? i.getVehicle().getMake() + " " + i.getVehicle().getModel() : "Not Specified",
                i.getCategory()!= null? i.getCategory().getName() : "General",
                i.getInquiryType(),
                i.getPartDescription(),
                i.getImageUrl(),
                i.getStatus(),
                i.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{inquiryId}/quotes")
    public ResponseEntity<List<QuoteResponse>> getQuotesForInquiry(@PathVariable String inquiryId) {
        List<Quote> quotes = quoteRepository.findByInquiryId(inquiryId);

        List<QuoteResponse> responses = quotes.stream().map(q -> new QuoteResponse(
                q.getId(),
                q.getInquiry().getId(),
                q.getSeller().getId(),
                q.getSeller().getBusinessName(),
                q.getPrice(),
                q.getDeliveryTimeDays(),
                q.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/open-feed")
    @PreAuthorize("hasAnyAuthority('seller', 'admin')")
    public ResponseEntity<List<InquiryResponse>> getOpenInquiriesFeed() {
        List<Inquiry> openInquiries = inquiryRepository.findByInquiryTypeAndStatus("open", "pending");

        List<InquiryResponse> responses = openInquiries.stream().map(i -> new InquiryResponse(
                i.getId(),
                i.getUser().getId(),
                i.getUser().getName(),
                i.getVehicle()!= null? i.getVehicle().getMake() + " " + i.getVehicle().getModel() : "Not Specified",
                i.getCategory()!= null? i.getCategory().getName() : "General",
                i.getInquiryType(),
                i.getPartDescription(),
                i.getImageUrl(),
                i.getStatus(),
                i.getCreatedAt()
        )).toList();

        return ResponseEntity.ok(responses);
    }
}