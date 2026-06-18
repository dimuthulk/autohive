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

/**
 * Security Configuration class that sets up authentication and authorization
 * for the application using JWT (JSON Web Token) based authentication.
 *
 * This configuration:
 * 1. Defines password encoding strategy (BCrypt)
 * 2. Configures which endpoints are public vs protected
 * 3. Adds JWT authentication filter to the security chain
 */
@Configuration
@EnableMethodSecurity  // Enables method-level security annotations like @PreAuthorize
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Creates a password encoder bean for hashing user passwords.
     * BCrypt is used for secure password storage with built-in salting.
     *
     * @return PasswordEncoder instance using BCrypt hashing algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain that intercepts all HTTP requests.
     *
     * Security rules:
     * - CSRF protection is disabled (stateless REST API)
     * - Public endpoints (auth) are accessible without authentication
     * - All other endpoints require a valid JWT token
     * - JWT authentication filter is added before the default authentication
     *
     * @param http HttpSecurity object for configuring web security
     * @return SecurityFilterChain configured with the security rules
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints: Login and Register - anyone can access without a token
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Protected endpoints: All other APIs (Inquiries, Orders, etc.) require valid JWT token
                        .anyRequest().authenticated()
                )
                // Add our custom JWT filter to the Spring Security filter chain
                // This filter intercepts requests and validates JWT tokens
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}