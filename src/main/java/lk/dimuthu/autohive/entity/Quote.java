package lk.dimuthu.autohive.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "inquiry_id", length = 36, nullable = false)
    private String inquiryId;

    @Column(name = "seller_id", length = 36, nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private Double price;

    @Column(name = "delivery_time_days")
    private Integer deliveryTimeDays;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInquiryId() { return inquiryId; }
    public void setInquiryId(String inquiryId) { this.inquiryId = inquiryId; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDeliveryTimeDays() { return deliveryTimeDays; }
    public void setDeliveryTimeDays(Integer deliveryTimeDays) { this.deliveryTimeDays = deliveryTimeDays; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
