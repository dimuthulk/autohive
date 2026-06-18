package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.CategoryRequest;
import lk.dimuthu.autohive.dto.response.CategoryResponse;
import lk.dimuthu.autohive.entity.Category;
import lk.dimuthu.autohive.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest request) {
        // Create a new Category entity
        Category category = new Category();
        category.setName(request.getName());

        // If a parent ID is provided, associate this category as a sub-category
        if (request.getParentId() != null && !request.getParentId().isEmpty()) {
            Optional<Category> parentOpt = categoryRepository.findById(request.getParentId());
            parentOpt.ifPresent(category::setParent); // Set parent category if it exists
        }

        // Save the category to the database
        categoryRepository.save(category);
        return ResponseEntity.ok("Category successfully added!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        // Retrieve all categories from the database
        List<Category> categories = categoryRepository.findAll();

        // Convert each Category entity to a CategoryResponse DTO
        List<CategoryResponse> responses = categories.stream().map(c -> new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getParent() != null ? c.getParent().getId() : null,      // Parent ID (null if no parent)
                c.getParent() != null ? c.getParent().getName() : null      // Parent name (null if no parent)
        )).toList();

        return ResponseEntity.ok(responses);
    }
}
