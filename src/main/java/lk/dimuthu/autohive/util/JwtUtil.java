package lk.dimuthu.autohive.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    // Token එක සෑදීම සඳහා භාවිතා කරන රහස් කේතය (Secret Key). මෙයට අකුරු/ඉලක්කම් 32කට වඩා දිග එකක් දෙන්න.
    private final String SECRET_STRING = "AutoHiveSecretKeySuperSecureAndVeryLong2021051";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // පැයක් යනකම් Token එක වලංගු වේ (3,600,000 Milliseconds)
    private final long jwtExpirationMs = 3600000;

    // 1. Email එක සහ Role එක පදනම් කරගෙන අලුත් Token එකක් සෑදීම
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // 2. Token එකෙන් Email එක ලබා ගැනීම
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 3. Token එක වලංගු ද නැද්ද යන්න පරීක්ෂා කිරීම
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 4. Token එකෙන් Role එක ලබා ගැනීම
    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}
