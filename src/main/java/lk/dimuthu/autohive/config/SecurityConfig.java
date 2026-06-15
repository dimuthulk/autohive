package lk.dimuthu.autohive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Password එක Hash කරන්න පාවිච්චි කරන Encoder එක
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // අපි හදන API වලට බාධාවක් නැතුව Request එන්න දෙන එක (Default security අක්‍රීය කිරීම)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // API වලට CSRF disable කිරීම
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // දැනට සියලුම Requests වලට අවසර දීම

        return http.build();
    }
}
