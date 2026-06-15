package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.entity.Product;
import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.repository.ProductRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.badRequest().body("Error: There is no user in the system with this User ID!");
        }

        // එක User කෙනෙක්ට හදන්න පුළුවන් එක කඩයයි
        if(sellerRepository.existsByUserId(seller.getUserId())){
            return ResponseEntity.badRequest().body("Error: This user is already registered as a seller!");
        }

        sellerRepository.save(seller);
        return ResponseEntity.ok("Seller successfully registered!");
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

    // 3. පාරිභෝගිකයින්ට භාණ්ඩ සෙවීම සඳහා (Product Search API)
    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return ResponseEntity.ok(products);
    }
}
