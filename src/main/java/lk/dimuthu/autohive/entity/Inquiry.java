package lk.dimuthu.autohive.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inquiries")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "inquiry_type")
    private String inquiryType = "open"; // 'open' හෝ 'direct'

    @Column(name = "part_description", nullable = false, columnDefinition = "TEXT")
    private String partDescription;

    private String status = "pending"; // 'pending', 'quoting', 'ordered', 'closed'

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters and Setters (අනිවාර්යයෙන්ම මේවා දාන්න)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getInquiryType() { return inquiryType; }
    public void setInquiryType(String inquiryType) { this.inquiryType = inquiryType; }

    public String getPartDescription() { return partDescription; }
    public void setPartDescription(String partDescription) { this.partDescription = partDescription; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}
}
