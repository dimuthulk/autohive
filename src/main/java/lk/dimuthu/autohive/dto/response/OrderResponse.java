package lk.dimuthu.autohive.dto.response;

import java.time.LocalDateTime;

public class OrderResponse {
    private String id;
    private String inquiryId;
    private String inquiryDescription;
    private String quoteId;
    private String buyerId;
    private String buyerName;
    private String sellerId;
    private String sellerBusinessName;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;

    public OrderResponse(String id, String inquiryId, String inquiryDescription, String quoteId,
                         String buyerId, String buyerName, String sellerId, String sellerBusinessName,
                         String status, Double totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.inquiryId = inquiryId;
        this.inquiryDescription = inquiryDescription;
        this.quoteId = quoteId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerBusinessName = sellerBusinessName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getInquiryId() { return inquiryId; }
    public String getInquiryDescription() { return inquiryDescription; }
    public String getQuoteId() { return quoteId; }
    public String getBuyerId() { return buyerId; }
    public String getBuyerName() { return buyerName; }
    public String getSellerId() { return sellerId; }
    public String getSellerBusinessName() { return sellerBusinessName; }
    public String getStatus() { return status; }
    public Double getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}