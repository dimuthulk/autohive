package lk.dimuthu.autohive.dto.response;

public class AuthResponse {
    private String token;
    private String userId;
    private String name;
    private String role;

    // Constructor to initialize all fields at once
    public AuthResponse(String token, String userId, String name, String role) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    // Getter and Setter for JWT token
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    // Getter and Setter for user ID
    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    // Getter and Setter for user ID
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    // Getter and Setter for user's role
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
}
