package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.dto.request.LoginRequest;
import lk.dimuthu.autohive.dto.request.RegisterRequest;
import lk.dimuthu.autohive.dto.response.AuthResponse;
import lk.dimuthu.autohive.entity.User;
import lk.dimuthu.autohive.repository.UserRepository;
import lk.dimuthu.autohive.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) { // Using DTO instead of Entity directly

        // Check if the email is already registered in the database
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Error: This email is already registered!");
        }

        // Extract data from the DTO and create a new User Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user); // Save the Entity to the Database

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) { // Using DTO instead of Map

        // Retrieve the user by email
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Error: User not found!");
        }

        User user = optionalUser.get();

        // Verify the provided password against the stored password hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Error: Incorrect password!");
        }

        // Generate a JWT token for the authenticated user
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Return a structured AuthResponse DTO instead of a plain Map
        AuthResponse response = new AuthResponse(token, user.getId(), user.getName(), user.getRole());

        return ResponseEntity.ok(response);
    }
}