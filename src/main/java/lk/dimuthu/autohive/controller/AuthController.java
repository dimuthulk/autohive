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
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) { // මෙතන Entity එක වෙනුවට DTO එක

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Error: මේ ඊමේල් එක දැනටමත් ලියාපදිංචි කර ඇත!");
        }

        // DTO එකෙන් එන දත්ත අරගෙන සැබෑ User Entity එක හදනවා
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user); // Entity එක Database එකට Save කරනවා

        return ResponseEntity.ok("User කෙනෙක් සාර්ථකව ලියාපදිංචි වුණා!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) { // Map එක වෙනුවට DTO එක

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Error: පරිශීලකයා සොයාගත නොහැක!");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Error: මුරපදය වැරදියි!");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Map එකක් යවනවා වෙනුවට අපි හදපු ලස්සන AuthResponse DTO එක යවනවා
        AuthResponse response = new AuthResponse(token, user.getId(), user.getName(), user.getRole());

        return ResponseEntity.ok(response);
    }
}