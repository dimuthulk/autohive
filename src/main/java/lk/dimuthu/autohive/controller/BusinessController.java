package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.ProductRequest;
import lk.dimuthu.autohive.dto.request.SellerRequest;
import lk.dimuthu.autohive.dto.response.ProductResponse;
import lk.dimuthu.autohive.entity.Category;
import lk.dimuthu.autohive.entity.Product;
import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.entity.User;
import lk.dimuthu.autohive.repository.CategoryRepository;
import lk.dimuthu.autohive.repository.ProductRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/seller")
    public ResponseEntity<String> registerSeller(@RequestBody SellerRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Invalid User ID!");
        }

        User user = optionalUser.get();

        if(sellerRepository.existsByUser(user)){
            return ResponseEntity.badRequest().body("Error: This user is already registered as a seller!");
        }

        // 1. Seller Profile එක හදනවා
        Seller seller = new Seller();
        seller.setUser(user);
        seller.setBusinessName(request.getBusinessName());
        sellerRepository.save(seller);

        // 2. මෙන්න අලුතින් එකතු කරන කෑල්ල: User ගේ Role එක Update කිරීම
        user.setRole("SELLER");
        userRepository.save(user);

        return ResponseEntity.ok("Seller registered successfully!");
    }

    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request) {
        // Validate that the seller exists
        Optional<Seller> optionalSeller = sellerRepository.findById(request.getSellerId());
        if(optionalSeller.isEmpty()){
            return ResponseEntity.badRequest().body("Error: Invalid Seller ID!");
        }

        // Create and populate the product entity
        Product product = new Product();
        product.setSeller(optionalSeller.get());
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock()!= null? request.getStock() : 0);

        // අලුතින් එකතු කළ කොටස
        product.setImageUrl(request.getImageUrl());

        // Associate category if category ID is provided and exists
        if(request.getCategoryId()!= null) {
            Optional<Category> optCat = categoryRepository.findById(request.getCategoryId());
            optCat.ifPresent(product::setCategory);
        }

        productRepository.save(product);
        return ResponseEntity.ok("New product added successfully!");
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        // Find all products containing the keyword in their name (case-insensitive)
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);

        // Convert each product entity to a response DTO with all necessary information
        List<ProductResponse> responses = products.stream().map(p -> new ProductResponse(
                p.getId(),
                p.getSeller().getId(),
                p.getSeller().getBusinessName(),
                p.getCategory()!= null? p.getCategory().getId() : null,
                p.getCategory()!= null? p.getCategory().getName() : null,
                p.getName(),
                p.getBrand(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl()
        )).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/seller/user/{userId}")
    public ResponseEntity<?> getSellerProfile(@PathVariable String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: User not found!");
        }

        User user = optionalUser.get();

        // SellerRepository එකේ මේ User ට අදාළ Seller කෙනෙක් ඉන්නවද බලනවා
        // (මචං ඔයාගේ SellerRepository එකේ Optional<Seller> findByUser(User user); කියලා method එකක් නැත්නම් ඒක add කරන්න)
        Optional<Seller> optionalSeller = sellerRepository.findByUser(user);

        if (optionalSeller.isEmpty()) {
            return ResponseEntity.status(404).body("Seller profile not found");
        }

        Seller seller = optionalSeller.get();

        // StackOverflow (Infinite recursion) අවුල් එන එක නවත්තන්න කෙලින්ම Map එකක් යවනවා
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("sellerId", seller.getId());
        response.put("businessName", seller.getBusinessName());

        return ResponseEntity.ok(response);
    }
}