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
            return ResponseEntity.badRequest().body("Error: මේ User ID එක වලංගු නැත!");
        }

        User user = optionalUser.get();

        if(sellerRepository.existsByUser(user)){
            return ResponseEntity.badRequest().body("Error: මේ පරිශීලකයා දැනටමත් විකුණුම්කරුවෙක් ලෙස ලියාපදිංචි වී ඇත!");
        }

        Seller seller = new Seller();
        seller.setUser(user);
        seller.setBusinessName(request.getBusinessName());

        sellerRepository.save(seller);
        return ResponseEntity.ok("විකුණුම්කරු (Seller) සාර්ථකව ලියාපදිංචි වුණා!");
    }

    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request) {
        Optional<Seller> optionalSeller = sellerRepository.findById(request.getSellerId());
        if(optionalSeller.isEmpty()){
            return ResponseEntity.badRequest().body("Error: වැරදි Seller ID එකක්!");
        }

        Product product = new Product();
        product.setSeller(optionalSeller.get());
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock()!= null? request.getStock() : 0);

        if(request.getCategoryId()!= null) {
            Optional<Category> optCat = categoryRepository.findById(request.getCategoryId());
            optCat.ifPresent(product::setCategory);
        }

        productRepository.save(product);
        return ResponseEntity.ok("අලුත් භාණ්ඩය සාර්ථකව පද්ධතියට එකතු කළා!");
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);

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
}