package lk.dimuthu.autohive.dto.request;

public class LoginRequest {
    private String email;
    private String password;

    // Getters and Setters - Required for Spring to map JSON request body to this object
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
