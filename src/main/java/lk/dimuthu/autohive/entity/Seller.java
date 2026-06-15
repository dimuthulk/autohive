package lk.dimuthu.autohive.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", length = 36, unique = true, nullable = false)
    private String userId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0.00")
    private Double rating = 0.00;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
