package lk.dimuthu.autohive.controller;

import lk.dimuthu.autohive.entity.User;
import lk.dimuthu.autohive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // අලුතින් එකතු කරපු Password Encoder එක
    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
