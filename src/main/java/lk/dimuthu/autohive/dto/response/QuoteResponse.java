package lk.dimuthu.autohive.dto.response;

import java.time.LocalDateTime;

public class QuoteResponse {
    private String id;
    private String inquiryId;
    private String sellerId;
    private String sellerBusinessName;
    private Double price;
    private Integer deliveryTimeDays;
    private LocalDateTime createdAt;

    public QuoteResponse(String id, String inquiryId, String sellerId, String sellerBusinessName,
                         Double price, Integer deliveryTimeDays, LocalDateTime createdAt) {
        this.id = id;
        this.inquiryId = inquiryId;
        this.sellerId = sellerId;
        this.sellerBusinessName = sellerBusinessName;
        this.price = price;
        this.deliveryTimeDays = deliveryTimeDays;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getInquiryId() { return inquiryId; }
    public String getSellerId() { return sellerId; }
    public String getSellerBusinessName() { return sellerBusinessName; }
    public Double getPrice() { return price; }
    public Integer getDeliveryTimeDays() { return deliveryTimeDays; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}