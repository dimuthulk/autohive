package lk.dimuthu.autohive.dto.response;

import java.time.LocalDateTime;

public class UnifiedOrderResponse {
    private String id;
    private String name; // Product නම හෝ Inquiry නම
    private Double totalAmount;
    private String status;
    private String type; // "DIRECT" හෝ "INQUIRY"
    private LocalDateTime createdAt;
    private String sellerBusinessName;

    public UnifiedOrderResponse(String id, String name, Double totalAmount, String status, String type, LocalDateTime createdAt, String sellerBusinessName) {
        this.id = id;
        this.name = name;
        this.totalAmount = totalAmount;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
        this.sellerBusinessName = sellerBusinessName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSellerBusinessName() {
        return sellerBusinessName;
    }
}