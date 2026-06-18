package lk.dimuthu.autohive.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.dimuthu.autohive.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT Authentication Filter that intercepts every HTTP request and validates
 * JWT tokens present in the Authorization header.
 *
 * This filter:
 * 1. Extracts the JWT token from the Authorization header
 * 2. Validates the token and extracts user information (email and role)
 * 3. Sets up Spring Security authentication context with user details and authorities
 *
 * The filter runs once per request (extends OncePerRequestFilter)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // Utility class for JWT operations (generation, validation, extraction)

    /**
     * Core filter method that processes each HTTP request.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header from the request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Check if the header exists and starts with "Bearer " (standard JWT format)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Extract token by removing "Bearer " prefix
            try {
                email = jwtUtil.getEmailFromToken(token);  // Extract email/username from token
            } catch (Exception e) {
                System.out.println("Invalid Token!");  // Log invalid token error
            }
        }

        // If email is extracted and user is not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate the JWT token
            if (jwtUtil.validateToken(token)) {

                // NEW: Extract the role from the token and set it in Spring Security
                String role = jwtUtil.getRoleFromToken(token);
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));  // Convert role to Spring Security authority

                // Create authentication token with user email and authorities
                // Previously used empty ArrayList (new ArrayList<>()), now passing actual authorities
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, authorities  // email as principal, null credentials, authorities for role-based access
                );
                // Add request details to the authentication token (IP, session, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication in the security context (user is now logged in)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain to the next filter or the controller
        filterChain.doFilter(request, response);
    }
}