package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.entity.Product;
import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.repository.ProductRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 1. Seller කෙනෙක් Register කිරීමේ API එක
    @PostMapping("/seller")
    public ResponseEntity<String> registerSeller(@RequestBody Seller seller) {

        // අදාළ User ID එක Database එකේ තියෙනවද කියලා බලනවා
        if(!userRepository.existsById(seller.getUserId())){
            return ResponseEntity.badRequest().body("Error: මේ User ID එකට අදාළ පරිශීලකයෙක් පද්ධතියේ නැත!");
        }

        // එක User කෙනෙක්ට හදන්න පුළුවන් එක කඩයයි
        if(sellerRepository.existsByUserId(seller.getUserId())){
            return ResponseEntity.badRequest().body("Error: මේ පරිශීලකයා දැනටමත් විකුණුම්කරුවෙක් ලෙස ලියාපදිංචි වී ඇත!");
        }

        sellerRepository.save(seller);
        return ResponseEntity.ok("විකුණුම්කරු (Seller) සාර්ථකව ලියාපදිංචි වුණා!");
    }

    // 2. අලුත් Product එකක් Add කිරීමේ API එක
    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {

        // අදාළ Seller පද්ධතියේ ඉන්නවද කියලා බලනවා
        if(!sellerRepository.existsById(product.getSellerId())){
            return ResponseEntity.badRequest().body("Error: වැරදි Seller ID එකක්!");
        }

        productRepository.save(product);
        return ResponseEntity.ok("අලුත් භාණ්ඩය සාර්ථකව පද්ධතියට එකතු කළා!");
    }
}
