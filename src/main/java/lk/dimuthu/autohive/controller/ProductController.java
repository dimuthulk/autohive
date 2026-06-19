package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.ProductRequest;
import lk.dimuthu.autohive.dto.response.ProductResponse;
import lk.dimuthu.autohive.entity.Category;
import lk.dimuthu.autohive.entity.Product;
import lk.dimuthu.autohive.entity.Seller;
import lk.dimuthu.autohive.repository.CategoryRepository;
import lk.dimuthu.autohive.repository.ProductRepository;
import lk.dimuthu.autohive.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. අලුත් නිෂ්පාදනයක් එකතු කිරීම
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request) {
        Optional<Seller> optionalSeller = sellerRepository.findById(request.getSellerId());
        if (optionalSeller.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Invalid Seller ID!");
        }

        Product product = new Product();
        product.setSeller(optionalSeller.get());
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock() != null ? request.getStock() : 0);
        product.setImageUrl(request.getImageUrl());

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(product::setCategory);
        }

        productRepository.save(product);
        return ResponseEntity.ok("Product added successfully!");
    }

    // 2. සියලුම නිෂ්පාදන ලබා ගැනීම (Catalog එක සඳහා)
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductResponse> responses = products.stream().map(p -> new ProductResponse(
                p.getId(),
                p.getSeller().getId(),
                p.getSeller().getBusinessName(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getName(),
                p.getBrand(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl()
        )).toList();

        return ResponseEntity.ok(responses);
    }

    // 3. නම හරහා නිෂ්පාදන සෙවීම (Search)
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);

        List<ProductResponse> responses = products.stream().map(p -> new ProductResponse(
                p.getId(),
                p.getSeller().getId(),
                p.getSeller().getBusinessName(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getName(),
                p.getBrand(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl()
        )).toList();

        return ResponseEntity.ok(responses);
    }
}