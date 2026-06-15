package lk.dimuthu.autohive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Password එක Hash කරන්න පාවිච්චි කරන Encoder එක
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // අපි හදන API වලට බාධාවක් නැතුව Request එන්න දෙන එක (Default security අක්‍රීය කිරීම)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Login සහ Register API වලට ඕනෑම කෙනෙක්ට Token එකක් නැතුව එන්න පුළුවන්
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // ඒ ඇරෙන්න අනිත් හැම API එකකටම (Inquiries, Orders වගේ) Token එක අනිවාර්යයි
                        .anyRequest().authenticated()
                )
                // අපේ Filter එක Spring Security ක්‍රියාවලියට එකතු කරනවා
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
