package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.entity.User;
import lk.dimuthu.autohive.repository.UserRepository;
import lk.dimuthu.autohive.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // අලුතින් එකතු කරපු Password Encoder එක
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        // ඊමේල් එක කලින්ම පාවිච්චි කරලා තියෙනවද කියලා පරීක්ෂා කිරීම
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("Error: This email is already registered!");
        }

        // Password එක Hash කරලා ආයෙත් User Object එකටම සෙට් කිරීම
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        // පරිශීලකයාව Database එකේ Save කිරීම
        userRepository.save(user);

        return ResponseEntity.ok("A user has successfully registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        // 1. ඊමේල් එක තියෙනවද බලනවා
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Error: පරිශීලකයා සොයාගත නොහැක!");
        }

        User user = optionalUser.get();

        // 2. පාස්වර්ඩ් එක ගැලපෙනවද බලනවා
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Error: මුරපදය වැරදියි!");
        }

        // 3. හැමදේම හරි නම් JWT Token එක හදනවා
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // 4. Token එකයි, User ගේ විස්තරයි JSON එකක් විදියට Response එකට යවනවා
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}
